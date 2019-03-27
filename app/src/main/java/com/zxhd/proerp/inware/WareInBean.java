package com.zxhd.proerp.inware;

public class WareInBean {
    private String employeeName;
    private String judges;
    private String list;
    private String list_type;
    private String lists;
    private String remark;
    private String repo_time;
    private String respository_name;
    private int gteid;
    private int id;
    private int pici;
    private int repo_name;//创建人id
    private int respository_id;
    private int returnState;
    private int shenghe;
    private int state;
    private double totalPrice;

    public WareInBean(String employeeName, String judges, String list, String list_type, String lists, String remark, String repo_time, String respository_name, int gteid, int id, int pici, int repo_name, int respository_id, int returnState, int shenghe, int state, double totalPrice) {
        this.employeeName = employeeName;
        this.judges = judges;
        this.list = list;
        this.list_type = list_type;
        this.lists = lists;
        this.remark = remark;
        this.repo_time = repo_time;
        this.respository_name = respository_name;
        this.gteid = gteid;
        this.id = id;
        this.pici = pici;
        this.repo_name = repo_name;
        this.respository_id = respository_id;
        this.returnState = returnState;
        this.shenghe = shenghe;
        this.state = state;
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "WareInBean{" +
                "employeeName='" + employeeName + '\'' +
                ", judges='" + judges + '\'' +
                ", list='" + list + '\'' +
                ", list_type='" + list_type + '\'' +
                ", lists='" + lists + '\'' +
                ", remark='" + remark + '\'' +
                ", repo_time='" + repo_time + '\'' +
                ", respository_name='" + respository_name + '\'' +
                ", gteid=" + gteid +
                ", id=" + id +
                ", pici=" + pici +
                ", repo_name=" + repo_name +
                ", respository_id=" + respository_id +
                ", returnState=" + returnState +
                ", shenghe=" + shenghe +
                ", state=" + state +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getJudges() {
        return judges;
    }

    public void setJudges(String judges) {
        this.judges = judges;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getList_type() {
        return list_type;
    }

    public void setList_type(String list_type) {
        this.list_type = list_type;
    }

    public String getLists() {
        return lists;
    }

    public void setLists(String lists) {
        this.lists = lists;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRepo_time() {
        return repo_time;
    }

    public void setRepo_time(String repo_time) {
        this.repo_time = repo_time;
    }

    public String getRespository_name() {
        return respository_name;
    }

    public void setRespository_name(String respository_name) {
        this.respository_name = respository_name;
    }

    public int getGteid() {
        return gteid;
    }

    public void setGteid(int gteid) {
        this.gteid = gteid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPici() {
        return pici;
    }

    public void setPici(int pici) {
        this.pici = pici;
    }

    public int getRepo_name() {
        return repo_name;
    }

    public void setRepo_name(int repo_name) {
        this.repo_name = repo_name;
    }

    public int getRespository_id() {
        return respository_id;
    }

    public void setRespository_id(int respository_id) {
        this.respository_id = respository_id;
    }

    public int getReturnState() {
        return returnState;
    }

    public void setReturnState(int returnState) {
        this.returnState = returnState;
    }

    public int getShenghe() {
        return shenghe;
    }

    public void setShenghe(int shenghe) {
        this.shenghe = shenghe;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
