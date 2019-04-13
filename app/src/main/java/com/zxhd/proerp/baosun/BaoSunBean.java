package com.zxhd.proerp.baosun;

public class BaoSunBean {
//            "goodsid":114,
//                    "colorNum":"蓝",
//                    "colorPicture":"a.png",
//                    "color_spec":26,
//                    "goodsname":"拉菲草",
//                    "pici":2,
//                    "area_id":57,
//                    "list":"rk2019032615002",
//                    "area_number":"cw2019030240002",
//                    "metering_abbreviation":"kg",
//                    "gteid":232,
//                    "price":94.1,
//                    "judge":3,
//                    "id":2,
//                    "goodsnumber":"6901234567282",
//                    "goodsspec":"2",
//                    "twotypename":"产品c类",
//                    "sums":2000,
//                    "status":0,
//                    "metering_name":"千克"

    private int goodsid;
    private int color_spec;
    private int pici;
    private int area_id;
    private int gteid;
    private int judge;
    private int id;
    private int status;
    private int meteringId;
    private String colorNum;
    private String colorPicture;
    private String goodsname;
    private String list;
    private String area_number;
    private String metering_name;
    private String metering_abbreviation;
    private String goodsnumber;
    private String goodsspec;
    private String twotypename;
    private double price;
    private double sums;

    public BaoSunBean(int goodsid, int color_spec, int pici, int area_id, int gteid, int judge, int id, int status, String colorNum, String colorPicture, String goodsname, String list, String area_number, String metering_name, String metering_abbreviation, String goodsnumber, String goodsspec, String twotypename, double price, double sums,int meteringId) {
        this.goodsid = goodsid;
        this.color_spec = color_spec;
        this.pici = pici;
        this.area_id = area_id;
        this.gteid = gteid;
        this.judge = judge;
        this.id = id;
        this.status = status;
        this.meteringId = meteringId;
        this.colorNum = colorNum;
        this.colorPicture = colorPicture;
        this.goodsname = goodsname;
        this.list = list;
        this.area_number = area_number;
        this.metering_name = metering_name;
        this.metering_abbreviation = metering_abbreviation;
        this.goodsnumber = goodsnumber;
        this.goodsspec = goodsspec;
        this.twotypename = twotypename;
        this.price = price;
        this.sums = sums;
    }

    @Override
    public String toString() {
        return "BaoSunBean{" +
                "goodsid=" + goodsid +
                ", color_spec=" + color_spec +
                ", pici=" + pici +
                ", area_id=" + area_id +
                ", gteid=" + gteid +
                ", judge=" + judge +
                ", id=" + id +
                ", status=" + status +
                ", meteringId=" + meteringId +
                ", colorNum='" + colorNum + '\'' +
                ", colorPicture='" + colorPicture + '\'' +
                ", goodsname='" + goodsname + '\'' +
                ", list='" + list + '\'' +
                ", area_number='" + area_number + '\'' +
                ", metering_name='" + metering_name + '\'' +
                ", metering_abbreviation='" + metering_abbreviation + '\'' +
                ", goodsnumber='" + goodsnumber + '\'' +
                ", goodsspec='" + goodsspec + '\'' +
                ", twotypename='" + twotypename + '\'' +
                ", price=" + price +
                ", sums=" + sums +
                '}';
    }

    public int getMeteringId() {
        return meteringId;
    }

    public void setMeteringId(int meteringId) {
        this.meteringId = meteringId;
    }

    public int getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(int goodsid) {
        this.goodsid = goodsid;
    }

    public int getColor_spec() {
        return color_spec;
    }

    public void setColor_spec(int color_spec) {
        this.color_spec = color_spec;
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

    public int getGteid() {
        return gteid;
    }

    public void setGteid(int gteid) {
        this.gteid = gteid;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getColorNum() {
        return colorNum;
    }

    public void setColorNum(String colorNum) {
        this.colorNum = colorNum;
    }

    public String getColorPicture() {
        return colorPicture;
    }

    public void setColorPicture(String colorPicture) {
        this.colorPicture = colorPicture;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getArea_number() {
        return area_number;
    }

    public void setArea_number(String area_number) {
        this.area_number = area_number;
    }

    public String getMetering_name() {
        return metering_name;
    }

    public void setMetering_name(String metering_name) {
        this.metering_name = metering_name;
    }

    public String getMetering_abbreviation() {
        return metering_abbreviation;
    }

    public void setMetering_abbreviation(String metering_abbreviation) {
        this.metering_abbreviation = metering_abbreviation;
    }

    public String getGoodsnumber() {
        return goodsnumber;
    }

    public void setGoodsnumber(String goodsnumber) {
        this.goodsnumber = goodsnumber;
    }

    public String getGoodsspec() {
        return goodsspec;
    }

    public void setGoodsspec(String goodsspec) {
        this.goodsspec = goodsspec;
    }

    public String getTwotypename() {
        return twotypename;
    }

    public void setTwotypename(String twotypename) {
        this.twotypename = twotypename;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSums() {
        return sums;
    }

    public void setSums(double sums) {
        this.sums = sums;
    }
}
