package com.hyht.amap_historical_building;

/**
 * 接口URL
 */
public class Constant {
    //public final static String BASE_IP = "http://39.98.192.41";
    public final static String BASE_IP = "http://101.66.252.10";

    public final static String BASE_URL = BASE_IP+":8085/";
    private final static String TB_BASE_URL = BASE_URL+"hba/tb/";
    public final static String TB_ADD = TB_BASE_URL+"insert";
    public final static String TB_UPDATE = TB_BASE_URL+"updateById";
    public final static String TB_GET = TB_BASE_URL+"findByBasicId";
    public final static String TB_GETAll = TB_BASE_URL+"findAll";
    public final static String TB_GETAllLike = TB_BASE_URL+"findAllLike";
    public final static String TB_DELETE = TB_BASE_URL+"deleteById";
    public final static String TB_fuzzy_select = TB_BASE_URL+"findByCondition";

    private final static String TD_BASE_URL = BASE_URL+"hba/td/";
    public final static String TD_ADD = TD_BASE_URL+"insert";
    public final static String TD_UPDATE = TD_BASE_URL+"updateById";
    public final static String TD_GET = TD_BASE_URL+"findAllByBasicId";
    public final static String TD_FIND = TD_BASE_URL+"findByDrawId";
    public final static String TD_GETAll = TD_BASE_URL+"findAll";
    public final static String TD_GETAllLike = TD_BASE_URL+"findAllLike";
    public final static String TD_DELETE = TD_BASE_URL+"deleteById";

    private final static String TI_BASE_URL = BASE_URL+"hba/ti/";
    public final static String TI_ADD = TI_BASE_URL+"insert";
    public final static String TI_UPDATE = TI_BASE_URL+"updateById";
    public final static String TI_GET = TI_BASE_URL+"findAllByBasicId";
    public final static String TI_FIND = TI_BASE_URL+"findByImageId";
    public final static String TI_GETAll = TI_BASE_URL+"findAll";
    public final static String TI_GETAllLike = TI_BASE_URL+"findAllLike";
    public final static String TI_DELETE = TI_BASE_URL+"deleteById";

    public final static String TTD_BASE_URL = BASE_IP+":8083/";
    public final static String TTD_GET = TTD_BASE_URL+"hba/ttd/findAllByBasicId";
    public final static String TTD_PREVIEW = TTD_BASE_URL+"/3DImages/";
    public final static String TTD_GET_Panorama_Sketch = TTD_BASE_URL+"index?id=";


}

