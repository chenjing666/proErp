package com.zxhd.proerp.cont;

public class Api {
    public static final String BASE_URL = "http://192.168.0.36:8088/lfcPro/";
//    public static final String BASE_URL = "http://101.132.46.8:8088/lfcPro/";
    /**
     * 登录
     */
    public static final String LOGIN = BASE_URL + "login/doLogin";
    /**
     * 查询出库需求列表
     * 只查询未处理完成的，之前完成的不查
     */
    public static final String OUT_WARE_LIST = BASE_URL + "outwarehouseController/findAllOutwarehouse";
//    public static final String OUT_WARE_LIST = BASE_URL + "outwarehouseController/findAllOutwarehouseApp";
    /**
     * 查询出库需求列表(领料)
     */
    public static final String OUT_WARE_LIST_LINGLIAO = BASE_URL + "outwarehouseController/findAllOutwarehouseApp";
    /**
     * 出库需求列表详情
     */
    public static final String OUT_WARE_LIST_DETAILS = BASE_URL + "outwarehouseController/selAllOutwarehouseInfo";
    /**
     * 查询产品所在地堆
     */
    public static final String OUT_WARE_LIST_DETAILS_GOODSDIDUI = BASE_URL + "downRespositoryArea/selAllArea";
    /**
     * 根据产品和地堆编码查询交集
     */
    public static final String OUT_WARE_LIST_DETAILS_QUERYGOODS = BASE_URL + "outwarehouseController/findAreaOutGoods";
    /**
     * 下架
     */
    public static final String GOODS_DIDUI_DOWM = BASE_URL + "downRespositoryArea/DownRespositoryAreaApp";
    /**
     * 出库
     */
    public static final String GOODS_WARE_OUT = BASE_URL + "outdepotController/addOutdepotApp";
    /**
     * 确定结束出库
     */
    public static final String GOODS_WARE_OUT_OVER = BASE_URL + "outwarehouseController/overOutApp";
    /**
     * 到货列表
     */
    public static final String GOODS_WARE_IN_ARRIVAL = BASE_URL + "QydnInwarehouseController/queryInwarehouse";
    /**
     * 查询到货单详情
     */
    public static final String GOODS_WARE_IN_ARRIVAL_DETAILS = BASE_URL + "QydnInwarehouseController/queryallInwarehouse";
    /**
     * 推荐上架地堆
     */
    public static final String GOODS_WARE_IN_ARRIVAL_DETAILS_DIDUI_RECOMEMEND = BASE_URL + "upRespositoryArea/selAllTuiArea";
    /**
     * //地堆上架
     */
    public static final String GOODS_WARE_IN_UPGOODS = BASE_URL + "upRespositoryArea/upRespositoryAreaApp";
    /**
     * 查看货物    area_number编号
     */
    public static final String DIDUI_GOODS_DETAILS = BASE_URL + "qydnRespositoryArea/findAllApp";
    /**
     * 报损
     */
    public static final String GOODS_BAOSUN = BASE_URL + "reportLossController/addReportLossApp";
    /**
     * 移位
     */
    public static final String GOODS_YIWEI = BASE_URL + "qydnRespositoryArea/moveAreaApp";

}
