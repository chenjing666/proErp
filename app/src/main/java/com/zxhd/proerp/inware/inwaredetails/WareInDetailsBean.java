package com.zxhd.proerp.inware.inwaredetails;

public class WareInDetailsBean {
    //    cha: 0.4
//    cha2: 0.5
//    colorNum: "bm002"
//    colorPicture: "b.png"
//    color_spec: 0
//    endproduct: 9
//    ep_name: "原材料666"
//    ep_number: "666"
//    ep_spec: "666/盒"
//    ep_unit: 1
//    goodsid: 9
//    gteid: 230
//    haveReturn: 1820
//    id: 166
//    in: 1820.1
//    inWarehouse_id: 505
//    judge: 1
//    metering_abbreviation: "kg"
//    metering_name: "千克"
//    pici: 2
//    price: 40.5
//    quantity: 1820.5
//    repo_name: "拉菲草a"
//    respository_id: 1
//    status: 0
//    sums: 0
//    supplier_Name: "供应商a"
//    twotypename: "麻绳"
    private double cha;
    private double cha2;
    private double haveReturn;
    private double in;
    private double price;
    private double quantity;
    private double sums;
    private String colorNum;
    private String colorPicture;
    private String ep_name;
    private String ep_number;
    private String ep_spec;
    private String metering_abbreviation;
    private String metering_name;
    private String repo_name;
    private String supplier_Name;
    private String twotypename;
    private int color_spec;
    private int endproduct;
    private int ep_unit;
    private int goodsid;
    private int gteid;
    private int id;
    private int inWarehouse_id;
    private int judge;
    private int pici;
    private int respository_id;
    private int status;

    public WareInDetailsBean(double cha, double cha2, double haveReturn, double in, double price, double quantity, double sums, String colorNum, String colorPicture, String ep_name, String ep_number, String ep_spec, String metering_abbreviation, String metering_name, String repo_name, String supplier_Name, String twotypename, int color_spec, int endproduct, int ep_unit, int goodsid, int gteid, int id, int inWarehouse_id, int judge, int pici, int respository_id, int status) {
        this.cha = cha;
        this.cha2 = cha2;
        this.haveReturn = haveReturn;
        this.in = in;
        this.price = price;
        this.quantity = quantity;
        this.sums = sums;
        this.colorNum = colorNum;
        this.colorPicture = colorPicture;
        this.ep_name = ep_name;
        this.ep_number = ep_number;
        this.ep_spec = ep_spec;
        this.metering_abbreviation = metering_abbreviation;
        this.metering_name = metering_name;
        this.repo_name = repo_name;
        this.supplier_Name = supplier_Name;
        this.twotypename = twotypename;
        this.color_spec = color_spec;
        this.endproduct = endproduct;
        this.ep_unit = ep_unit;
        this.goodsid = goodsid;
        this.gteid = gteid;
        this.id = id;
        this.inWarehouse_id = inWarehouse_id;
        this.judge = judge;
        this.pici = pici;
        this.respository_id = respository_id;
        this.status = status;
    }

    @Override
    public String toString() {
        return "WareInDetailsBean{" +
                "cha=" + cha +
                ", cha2=" + cha2 +
                ", haveReturn=" + haveReturn +
                ", in=" + in +
                ", price=" + price +
                ", quantity=" + quantity +
                ", sums=" + sums +
                ", colorNum='" + colorNum + '\'' +
                ", colorPicture='" + colorPicture + '\'' +
                ", ep_name='" + ep_name + '\'' +
                ", ep_number='" + ep_number + '\'' +
                ", ep_spec='" + ep_spec + '\'' +
                ", metering_abbreviation='" + metering_abbreviation + '\'' +
                ", metering_name='" + metering_name + '\'' +
                ", repo_name='" + repo_name + '\'' +
                ", supplier_Name='" + supplier_Name + '\'' +
                ", twotypename='" + twotypename + '\'' +
                ", color_spec=" + color_spec +
                ", endproduct=" + endproduct +
                ", ep_unit=" + ep_unit +
                ", goodsid=" + goodsid +
                ", gteid=" + gteid +
                ", id=" + id +
                ", inWarehouse_id=" + inWarehouse_id +
                ", judge=" + judge +
                ", pici=" + pici +
                ", respository_id=" + respository_id +
                ", status=" + status +
                '}';
    }

    public int getColor_spec() {
        return color_spec;
    }

    public void setColor_spec(int color_spec) {
        this.color_spec = color_spec;
    }

    public int getEndproduct() {
        return endproduct;
    }

    public void setEndproduct(int endproduct) {
        this.endproduct = endproduct;
    }

    public int getEp_unit() {
        return ep_unit;
    }

    public void setEp_unit(int ep_unit) {
        this.ep_unit = ep_unit;
    }

    public int getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(int goodsid) {
        this.goodsid = goodsid;
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

    public int getInWarehouse_id() {
        return inWarehouse_id;
    }

    public void setInWarehouse_id(int inWarehouse_id) {
        this.inWarehouse_id = inWarehouse_id;
    }

    public int getJudge() {
        return judge;
    }

    public void setJudge(int judge) {
        this.judge = judge;
    }

    public int getPici() {
        return pici;
    }

    public void setPici(int pici) {
        this.pici = pici;
    }

    public int getRespository_id() {
        return respository_id;
    }

    public void setRespository_id(int respository_id) {
        this.respository_id = respository_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getCha() {
        return cha;
    }

    public void setCha(double cha) {
        this.cha = cha;
    }

    public double getCha2() {
        return cha2;
    }

    public void setCha2(double cha2) {
        this.cha2 = cha2;
    }

    public double getHaveReturn() {
        return haveReturn;
    }

    public void setHaveReturn(double haveReturn) {
        this.haveReturn = haveReturn;
    }

    public double getIn() {
        return in;
    }

    public void setIn(double in) {
        this.in = in;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getSums() {
        return sums;
    }

    public void setSums(double sums) {
        this.sums = sums;
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

    public String getEp_name() {
        return ep_name;
    }

    public void setEp_name(String ep_name) {
        this.ep_name = ep_name;
    }

    public String getEp_number() {
        return ep_number;
    }

    public void setEp_number(String ep_number) {
        this.ep_number = ep_number;
    }

    public String getEp_spec() {
        return ep_spec;
    }

    public void setEp_spec(String ep_spec) {
        this.ep_spec = ep_spec;
    }

    public String getMetering_abbreviation() {
        return metering_abbreviation;
    }

    public void setMetering_abbreviation(String metering_abbreviation) {
        this.metering_abbreviation = metering_abbreviation;
    }

    public String getMetering_name() {
        return metering_name;
    }

    public void setMetering_name(String metering_name) {
        this.metering_name = metering_name;
    }

    public String getRepo_name() {
        return repo_name;
    }

    public void setRepo_name(String repo_name) {
        this.repo_name = repo_name;
    }

    public String getSupplier_Name() {
        return supplier_Name;
    }

    public void setSupplier_Name(String supplier_Name) {
        this.supplier_Name = supplier_Name;
    }

    public String getTwotypename() {
        return twotypename;
    }

    public void setTwotypename(String twotypename) {
        this.twotypename = twotypename;
    }
}
