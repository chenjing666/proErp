package com.zxhd.proerp.cont;

public class Api {
    public static final String BASE_URL = "http://192.168.0.36:8088/lfcPro/";
//    public static final String BASE_URL = "http://101.132.46.8:8088/lfcPro/";
    public static final String LOGIN = BASE_URL + "login/doLogin";//登录
    public static final String OUT_WARE_LIST = BASE_URL + "outwarehouseController/findAllOutwarehouse";//查询出库需求列表
    public static final String OUT_WARE_LIST_DETAILS = BASE_URL + "outwarehouseController/selAllOutwarehouseInfo";//出库需求列表详情
    public static final String OUT_WARE_LIST_DETAILS_GOODSDIDUI = BASE_URL + "downRespositoryArea/selAllArea";//查询产品所在地堆
    public static final String OUT_WARE_LIST_DETAILS_QUERYGOODS = BASE_URL + "outwarehouseController/findAreaOutGoods";//根据产品和地堆编码查询交集
    public static final String GOODS_DIDUI_DOWM = BASE_URL + "downRespositoryArea/DownRespositoryAreaApp";//下架

}
