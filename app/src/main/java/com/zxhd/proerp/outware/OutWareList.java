package com.zxhd.proerp.outware;

/**
 * 出库单，也是领料单
 */
public class OutWareList {
    private String ll_num;//出库需求单号
    private String ll_num_lingliao;//领料单号,(来源单号)
    private String ll_time;//创建时间
    private String ll_user_create;//创建人
    private String ll_ware;//仓库
    private int ll_id;//
    private int ll_type;//出库类型
    private int ll_status;//出库状态
    private String ll_remark;//备注
    private String ll_other;//备注
    private int respository_id;//仓库id
    private int state;//仓库id

    public OutWareList(String ll_num, String ll_num_lingliao, String ll_time, String ll_user_create, String ll_ware, int ll_id, int ll_type, int ll_status, String ll_remark, String ll_other, int respository_id, int state) {
        this.ll_num = ll_num;
        this.ll_num_lingliao = ll_num_lingliao;
        this.ll_time = ll_time;
        this.ll_user_create = ll_user_create;
        this.ll_ware = ll_ware;
        this.ll_id = ll_id;
        this.ll_type = ll_type;
        this.ll_status = ll_status;
        this.ll_remark = ll_remark;
        this.ll_other = ll_other;
        this.respository_id = respository_id;
        this.state = state;
    }

    @Override
    public String toString() {
        return "OutWareList{" +
                "ll_num='" + ll_num + '\'' +
                ", ll_num_lingliao='" + ll_num_lingliao + '\'' +
                ", ll_time='" + ll_time + '\'' +
                ", ll_user_create='" + ll_user_create + '\'' +
                ", ll_ware='" + ll_ware + '\'' +
                ", ll_id=" + ll_id +
                ", ll_type=" + ll_type +
                ", ll_status=" + ll_status +
                ", ll_remark='" + ll_remark + '\'' +
                ", ll_other='" + ll_other + '\'' +
                ", respository_id=" + respository_id +
                ", state=" + state +
                '}';
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getRespository_id() {
        return respository_id;
    }

    public void setRespository_id(int respository_id) {
        this.respository_id = respository_id;
    }

    public String getLl_other() {
        return ll_other;
    }

    public void setLl_other(String ll_other) {
        this.ll_other = ll_other;
    }

    public int getLl_type() {
        return ll_type;
    }

    public void setLl_type(int ll_type) {
        this.ll_type = ll_type;
    }

    public String getLl_num_lingliao() {
        return ll_num_lingliao;
    }

    public void setLl_num_lingliao(String ll_num_lingliao) {
        this.ll_num_lingliao = ll_num_lingliao;
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


}
