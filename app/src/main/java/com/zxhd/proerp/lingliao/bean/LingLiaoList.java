package com.zxhd.proerp.lingliao.bean;

public class LingLiaoList {
    private String ll_num;
    private String ll_time;
    private String ll_user_create;
    private String ll_ware;
    private int ll_id;
    private int ll_status;
    private String ll_remark;

    public LingLiaoList(String ll_num, String ll_time, String ll_user_create, String ll_ware, int ll_id, int ll_status, String ll_remark) {
        this.ll_num = ll_num;
        this.ll_time = ll_time;
        this.ll_user_create = ll_user_create;
        this.ll_ware = ll_ware;
        this.ll_id = ll_id;
        this.ll_status = ll_status;
        this.ll_remark = ll_remark;
    }

    public String getLl_num() {
        return ll_num;
    }

    public void setLl_num(String ll_num) {
        this.ll_num = ll_num;
    }

    public String getLl_time() {
        return ll_time;
    }

    public void setLl_time(String ll_time) {
        this.ll_time = ll_time;
    }

    public String getLl_user_create() {
        return ll_user_create;
    }

    public void setLl_user_create(String ll_user_create) {
        this.ll_user_create = ll_user_create;
    }

    public String getLl_ware() {
        return ll_ware;
    }

    public void setLl_ware(String ll_ware) {
        this.ll_ware = ll_ware;
    }

    public int getLl_id() {
        return ll_id;
    }

    public void setLl_id(int ll_id) {
        this.ll_id = ll_id;
    }

    public int getLl_status() {
        return ll_status;
    }

    public void setLl_status(int ll_status) {
        this.ll_status = ll_status;
    }

    public String getLl_remark() {
        return ll_remark;
    }

    public void setLl_remark(String ll_remark) {
        this.ll_remark = ll_remark;
    }

    @Override
    public String toString() {
        return "LingLiaoList{" +
                "ll_num='" + ll_num + '\'' +
                ", ll_time='" + ll_time + '\'' +
                ", ll_user_create='" + ll_user_create + '\'' +
                ", ll_ware='" + ll_ware + '\'' +
                ", ll_id=" + ll_id +
                ", ll_status=" + ll_status +
                ", ll_remark='" + ll_remark + '\'' +
                '}';
    }
}
