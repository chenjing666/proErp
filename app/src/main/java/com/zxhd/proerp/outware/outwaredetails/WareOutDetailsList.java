package com.zxhd.proerp.outware.outwaredetails;

public class WareOutDetailsList {
    private int id;
    private String goodsnumber;//货物条码
    private String goodsname;//名称
    private int judge;//大分类（产品，成品，原材料）
    private String twotypename;//最子分类  采用：大分类（最子分类）显示
    private String goodsspec;//规格
    private String colorNum;//颜色编码
    private String metering_name;//单位中文
    private String metering_abbreviation;//单位英文
    private int outnumber;//需出库
    private int downnumber;//已下架
    private int haveout;//已出库
    private int goodsid;//
    private int color_spec;//
    private int cha;//还需出库数量

    public WareOutDetailsList(int id, String goodsnumber, String goodsname, int judge, String twotypename, String goodsspec, String colorNum, String metering_name, String metering_abbreviation, int outnumber, int downnumber, int haveout, int goodsid, int color_spec, int cha) {
        this.id = id;
        this.goodsnumber = goodsnumber;
        this.goodsname = goodsname;
        this.judge = judge;
        this.twotypename = twotypename;
        this.goodsspec = goodsspec;
        this.colorNum = colorNum;
        this.metering_name = metering_name;
        this.metering_abbreviation = metering_abbreviation;
        this.outnumber = outnumber;
        this.downnumber = downnumber;
        this.haveout = haveout;
        this.goodsid = goodsid;
        this.color_spec = color_spec;
        this.cha = cha;
    }


    @Override
    public String toString() {
        return "WareOutDetailsList{" +
                "id=" + id +
                ", goodsnumber='" + goodsnumber + '\'' +
                ", goodsname='" + goodsname + '\'' +
                ", judge=" + judge +
                ", twotypename='" + twotypename + '\'' +
                ", goodsspec='" + goodsspec + '\'' +
                ", colorNum='" + colorNum + '\'' +
                ", metering_name='" + metering_name + '\'' +
                ", metering_abbreviation='" + metering_abbreviation + '\'' +
                ", outnumber=" + outnumber +
                ", downnumber=" + downnumber +
                ", haveout=" + haveout +
                ", goodsid=" + goodsid +
                ", color_spec=" + color_spec +
                ", cha=" + cha +
                '}';
    }

    public int getCha() {
        return cha;
    }

    public void setCha(int cha) {
        this.cha = cha;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodsnumber() {
        return goodsnumber;
    }

    public void setGoodsnumber(String goodsnumber) {
        this.goodsnumber = goodsnumber;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public int getJudge() {
        return judge;
    }

    public void setJudge(int judge) {
        this.judge = judge;
    }

    public String getTwotypename() {
        return twotypename;
    }

    public void setTwotypename(String twotypename) {
        this.twotypename = twotypename;
    }

    public String getGoodsspec() {
        return goodsspec;
    }

    public void setGoodsspec(String goodsspec) {
        this.goodsspec = goodsspec;
    }

    public String getColorNum() {
        return colorNum;
    }

    public void setColorNum(String colorNum) {
        this.colorNum = colorNum;
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

    public int getOutnumber() {
        return outnumber;
    }

    public void setOutnumber(int outnumber) {
        this.outnumber = outnumber;
    }

    public int getDownnumber() {
        return downnumber;
    }

    public void setDownnumber(int downnumber) {
        this.downnumber = downnumber;
    }

    public int getHaveout() {
        return haveout;
    }

    public void setHaveout(int haveout) {
        this.haveout = haveout;
    }
}
