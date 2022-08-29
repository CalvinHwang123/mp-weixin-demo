package com.xianlu.mp.util;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.core.util.EruptSpringUtil;
import xyz.erupt.jpa.dao.EruptDao;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author 一只闲鹿
 */
public class ModelUtil {

    /**
     * 验证字段唯一性
     * @param t 对象
     * @param fieldList 需要校验的属性集合
     */
    public static <T> void validateUnique(T t, List<String> fieldList) {
        doValidateUnique(t, fieldList, new ArrayList<>());
    }

    /**
     * 验证字段唯一性
     * @param t 对象
     * @param fieldList 需要校验的属性集合
     * @param detailFieldList 需要校验的详情属性集合
     */
    public static <T> void validateUnique(T t, List<String> fieldList, List<String> detailFieldList) {
        doValidateUnique(t, fieldList, detailFieldList);
    }

    /**
     * 验证字段唯一性
     * @param t 对象
     * @param fieldList 需要校验的属性集合
     * @param detailFieldList 需要校验的详情属性集合
     */
    private static <T> void doValidateUnique(T t, List<String> fieldList, List<String> detailFieldList) {
        Class<?> clazz = t.getClass();
        String idExpr = "";
        Object id = getIdField(t);
        EruptDao eruptDao = EruptSpringUtil.getBean(EruptDao.class);
        if (!ObjectUtils.isEmpty(id)) {
            idExpr = " and id != " + id + " ";
        }
        // 获取注解上的表名
        Table annotation = clazz.getAnnotation(Table.class);
        String tableName = annotation.name();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field: declaredFields) {
            if (fieldList.contains(field.getName())) {
                EruptField eruptField = field.getDeclaredAnnotation(EruptField.class);
                // 跳过不包含 @EruptField 注解的属性（若有）
                if (ObjectUtils.isEmpty(eruptField)) {
                    continue;
                }
                View[] views = eruptField.views();
                // 属性中文名
                String name = views[0].title();
                try {
                    field.setAccessible(true);
                    // 属性值
                    Object value = field.get(t);
                    String expr = " 1 = 1 and "
                            + field.getName()
                            + " = '"
                            + value
                            + "' "
                            + idExpr;
                    // 这里使用 JPA 查询 eruptDao.queryEntityList 会报错，因为 calzz 类可能使用 @OneToMany，而详情表数据有的还未保存
                    // 会报错：org.hibernate.PersistentObjectException: detached entity passed to persist: xxx
//                List<?> list = eruptDao.queryEntityList(clazz, expr);
                    String sql = " select count(*) count from " + tableName + " where " + expr;
                    Map<String, Object> map = eruptDao.getJdbcTemplate().queryForMap(sql);
                    if (!CollectionUtils.isEmpty(map) && Integer.valueOf(map.get("count").toString()) > 0) {
                        throw new RuntimeException("操作失败：字段【" + name
                                + "】值【"
                                + value
                                + "】已存在");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException("系统异常：" + e.getMessage());
                } finally {
                    field.setAccessible(false);
                }

            }
            // 详情属性字段
            if (detailFieldList.contains(field.getName())) {
                // 一对多注解
                OneToMany oneToMany = field.getDeclaredAnnotation(OneToMany.class);
                // 多对多注解
                ManyToMany manyToMany = field.getDeclaredAnnotation(ManyToMany.class);
                if (ObjectUtils.isEmpty(oneToMany) && ObjectUtils.isEmpty(manyToMany)) {
                    continue;
                }
                EruptField eruptField = field.getDeclaredAnnotation(EruptField.class);
                View[] views = eruptField.views();
                // 详情属性中文名
                String detailName = views[0].title();
                // getGenericType() 才能获取 List<T> 中的 T 类型
                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                Type[] types = parameterizedType.getActualTypeArguments();
                if (!ObjectUtils.isEmpty(types)) {
                    try {
                        Set<Object> uniqueValueSet = new HashSet<>();
                        // 一对多
                        if (!ObjectUtils.isEmpty(oneToMany)) {
                            String typeName = types[0].getTypeName();
                            String uniqueFieldName = getValidateUniqueFieldName(typeName);
                            if (!ObjectUtils.isEmpty(uniqueFieldName)) {
                                field.setAccessible(true);
                                Object value = field.get(t);
                                if (!ObjectUtils.isEmpty(value) && value instanceof Collection) {
                                    Collection detailCollection = (Collection) value;
                                    for (Object detail : detailCollection) {
                                        Class<?> aClass = detail.getClass();
                                        try {
                                            if (!ObjectUtils.isEmpty(oneToMany)) {
                                                Field uniqueClassField = aClass.getDeclaredField(uniqueFieldName);
                                                uniqueClassField.setAccessible(true);
                                                try {
                                                    Object manyToOneValue = uniqueClassField.get(detail);
                                                    Object manyToOneIdValue = getIdField(manyToOneValue);
                                                    uniqueValueSet.add(manyToOneIdValue);
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                } finally {
                                                    uniqueClassField.setAccessible(false);
                                                }
                                            }
                                        } catch (NoSuchFieldException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (uniqueValueSet.size() < detailCollection.size()) {
                                        throw new RuntimeException("操作失败：" + detailName + "不能重复");
                                    }
                                }
                            }
                        }
                        // 多对多
                        if (!ObjectUtils.isEmpty(manyToMany)) {
                            field.setAccessible(true);
                            Object value = field.get(t);
                            if (!ObjectUtils.isEmpty(value) && value instanceof Collection) {
                                Collection detailCollection = (Collection) value;
                                for (Object detail : detailCollection) {
                                    Object idValue = getIdField(detail);
                                    uniqueValueSet.add(idValue);
                                }
                                if (uniqueValueSet.size() < detailCollection.size()) {
                                    throw new RuntimeException("操作失败：" + detailName + "不能重复");
                                }
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException("系统异常：" + e.getMessage());
                    } finally {
                        field.setAccessible(false);
                    }
                }
            }
        }
    }

    private static <T> Object getIdField(T t) {
        Class<?> clazz = t.getClass();
        Field idField;
        Object id = null;
        try {
            idField = clazz.getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            // 从父类获取 id（extends BaseModel）
            try {
                idField = clazz.getSuperclass().getDeclaredField("id");
            } catch (NoSuchFieldException ex) {
                // 从父类的父类获取 id（extends HyperModel）
                try {
                    idField = clazz.getSuperclass().getSuperclass().getDeclaredField("id");
                } catch (NoSuchFieldException ex2) {
                    ex2.printStackTrace();
                    throw new RuntimeException("系统异常：" + ex2.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("系统异常：" + e.getMessage());
        }
        try {
            idField.setAccessible(true);
            id = idField.get(t);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            idField.setAccessible(false);
        }
        return id;
    }

    private static String getValidateUniqueFieldName(String typeName) {
        Class<?> clazz;
        try {
            clazz = Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("系统异常：" + e.getMessage());
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field: declaredFields) {
            // 如果 field 有 @ManyToOne 注解
            if (!ObjectUtils.isEmpty(field.getAnnotation(ManyToOne.class))) {
                return field.getName();
            }
        }
        // 字段找不到返回 null
        return null;
    }

}
