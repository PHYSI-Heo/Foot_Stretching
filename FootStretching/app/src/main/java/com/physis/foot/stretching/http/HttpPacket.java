package com.physis.foot.stretching.http;

public class HttpPacket {

    public static final String BASEURL = "http://192.168.1.12:3000";
//    public static final String BASEURL = "http://192.168.219.105:3000";

    /*
            API
     */
    public static final String MANAGER_LOGIN_URL = BASEURL + "/manager/auth/login";
    public static final String REGISTER_USER_URL = BASEURL + "/user/register";
    public static final String GET_MY_PATTERNs_URL = BASEURL + "/pattern/hospital/list";
    public static final String SET_MY_PATTERNs_URL = BASEURL + "/pattern/hospital/setting";

    public static final String GET_USERs_URL = BASEURL + "/user/list";
    public static final String UPDATE_USER_INFO_URL = BASEURL + "/user/update/info";

    public static final String ADD_SCHEDULE_URL = BASEURL + "/user/add/schedule";
    public static final String GET_SCHEDULEs_URL = BASEURL + "/user/get/schedules";
    public static final String UPDATE_SCHEDULE_URL = BASEURL + "/user/update/schedule";
    public static final String DELETE_SCHEDULE_URL = BASEURL + "/user/delete/schedule";


    public static final String GET_PATTERN_URL = BASEURL + "/pattern/list";
    public static final String REGISTER_PATTERN_URL = BASEURL + "/pattern/register";
    public static final String DELETE_PATTERNs_URL = BASEURL + "/pattern/delete";
    public static final String UPDATE_PATTERN_URL = BASEURL + "/pattern/update";

    public static final String GET_PATTERN_ITEMs_URL = BASEURL + "/pattern/item/list";
    public static final String UPDATE_PATTERN_ITEMs_URL = BASEURL + "/pattern/item/update";

    /*
            Params
     */
    public static final String PARAMS_RESULT = "result";
    public static final String PARAMS_ROWS = "rows";
    public static final String PARAMS_ERROR = "err";

    public static final String PARAMS_EMAIL = "email";
    public static final String PARAMS_PWD = "pwd";
    public static final String PARAMS_NAME = "name";
    public static final String PARAMS_ITEMs = "items";

    public static final String PARAMS_HOSPITAL_CODE = "hCode";
    public static final String PARAMS_HOSPITAL_NAME = "hName";

    public static final String PARAMS_USER_CODE = "uCode";
    public static final String PARAMS_USER_NAME = "name";
    public static final String PARAMS_USER_PHONE = "phone";

    public static final String PARAMS_PATTERN_CODE = "pCode";
    public static final String PARAMS_PATTERN_NAME = "pName";
    public static final String PARAMS_PATTERN_KEYWORD = "keyword";
    public static final String PARAMS_PATTERN_EXPLANATION = "explanation";
    public static final String PARAMS_PATTERN_RUN_TIME = "totalTime";

    public static final String PARAMS_NO = "no";
    public static final String PARAMS_ORDER = "_order";
    public static final String PARAMS_ITEM_LEFT_MOVE = "leftMove";
    public static final String PARAMS_ITEM_RIGHT_MOVE = "rightMove";
    public static final String PARAMS_DATE_TIME = "dateTime";
    public static final String PARAMS_FULFILL = "fulfill";


}
