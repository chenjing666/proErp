package com.zxhd.proerp.outware.diduigoods;

public class DiDuiGoodsList {
    //"gteid": 223,
//        "goodsid": 14,
//        "price": 0.16,
//        "repo_name": "拉菲草a",
//        "judge": 1,
//        "id": 82,
//        "pici": 1,
//        "area_id": 44,
//        "list": "RK201903251407001",
//        "district_number": "QY001",
//        "area_number": "QY001D0002",
//        "sums": 100.0
    private int goodsid;
    private double price;
    private String repo_name;
    private int judge;
    private int id;
    private int gteid;
    private int pici;
    private int area_id;
    private int sums;
    private String list;
    private String district_number;
    private String area_number;

    public DiDuiGoodsList(int goodsid, double price, String repo_name, int judge, int id, int pici, int area_id, int sums, String list, String district_number, String area_number,int gteid) {
        this.goodsid = goodsid;
        this.price = price;
        this.repo_name = repo_name;
        this.judge = judge;
        this.id = id;
        this.pici = pici;
        this.area_id = area_id;
        this.sums = sums;
        this.list = list;
        this.district_number = district_number;
        this.area_number = area_number;
        this.gteid = gteid;
    }

    @Override
    public String toString() {
        return "DiDuiGoodsList{" +
                "goodsid=" + goodsid +
                ", price=" + price +
                ", repo_name='" + repo_name + '\'' +
                ", judge=" + judge +
                ", id=" + id +
                ", pici=" + pici +
                ", area_id=" + area_id +
                ", sums=" + sums +
                ", list='" + list + '\'' +
                ", district_number='" + district_number + '\'' +
                ", area_number='" + area_number + '\'' +
                ", gteid='" + gteid + '\'' +
                '}';
    }

    public int getGteid() {
        return gteid;
    }

    public void setGteid(int gteid) {
        this.gteid = gteid;
    }

    public int getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(int goodsid) {
        this.goodsid = goodsid;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRepo_name() {
        return repo_name;
    }

    public void setRepo_name(String repo_name) {
        this.repo_name = repo_name;
    }

    public int getJudge() {
        return judge;
    }

    public void setJudge(int judge) {
        this.judge = judge;
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

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public int getSums() {
        return sums;
    }

    public void setSums(int sums) {
        this.sums = sums;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getDistrict_number() {
        return district_number;
    }

    public void setDistrict_number(String district_number) {
        this.district_number = district_number;
    }

    public String getArea_number() {
        return area_number;
    }

    public void setArea_number(String area_number) {
        this.area_number = area_number;
    }
}
