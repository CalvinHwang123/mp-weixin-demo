package com.xianlu.mp.enums;

/**
 * 公众号接口响应
 */
public enum MpBaseResp {

    INVALID_SESSION(200003, "TOKEN过期，请到菜单【登录TOKEN】重新生成二维码并完成扫码"),
    FREQ_CONTROL(200013, "频率控制，腾讯爸爸叫你手速慢点");

    /**
     * 响应码
     */
    private int ret;

    /**
     * 响应信息
     */
    private String errMsg;

    MpBaseResp(int ret, String errMsg) {
        this.ret = ret;
        this.errMsg = errMsg;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
