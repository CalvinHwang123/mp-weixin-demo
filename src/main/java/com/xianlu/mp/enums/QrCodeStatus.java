package com.xianlu.mp.enums;

public enum QrCodeStatus {

    UNSCAN(-1, "未扫码"),
    SUCCESS(0, "已成功"), // 即：已登录
    CONFIRMED(1, "已确认"),
    EXPIRED(3, "已过期"),
    SCANNED(4, "已扫码");

    private int status;
    private String name;

    QrCodeStatus(int status, String name){
        this.status = status;
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
