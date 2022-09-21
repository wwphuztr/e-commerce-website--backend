package com.nails.api.constant;


import com.nails.api.utils.ConfigurationService;

public class NailsConstant {
    public static final String ROOT_DIRECTORY =  ConfigurationService.getInstance().getString("file.upload-dir","/tmp/upload");

    public static final Integer USER_KIND_ADMIN = 1;
    public static final Integer USER_KIND_CUSTOMER = 2;

    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_PENDING = 0;
    public static final Integer STATUS_LOCK = -1;
    public static final Integer STATUS_DELETE = -2;

    public static final Integer GROUP_KIND_SUPER_ADMIN = 1;
    public static final Integer GROUP_KIND_CUSTOMER = 2;

    public static final Integer MAX_ATTEMPT_FORGET_PWD = 5;
    public static final Integer MAX_TIME_FORGET_PWD = 5 * 60 * 1000; //5 minutes
    public static final Integer MAX_ATTEMPT_LOGIN = 5;


    public static final Integer GENDER_MALE = 1;
    public static final Integer GENDER_FEMALE = 2;
    public static final Integer GENDER_OTHER = 3;

    public static final Integer CATEGORY_KIND_IMPORT = 1;
    public static final Integer CATEGORY_KIND_EXPORT = 2;
    public static final Integer CATEGORY_KIND_PRODUCT = 3;
    public static final Integer CATEGORY_KIND_NEWS = 4;

    public static final String PROVINCE_KIND_PROVINCE = "PROVINCE_KIND_PROVINCE";
    public static final String PROVINCE_KIND_DISTRICT = "PROVINCE_KIND_DISTRICT";
    public static final String PROVINCE_KIND_COMMUNE = "PROVINCE_KIND_COMMUNE";

    public static final Integer ORDERS_STATE_CREATED = 0;
    public static final Integer ORDERS_STATE_ACCEPTED = 1;
    public static final Integer ORDERS_STATE_SHIPPING = 2;
    public static final Integer ORDERS_STATE_DONE = 3;
    public static final Integer ORDERS_STATE_CANCEL = 4;

    public static final Integer VAT = 10;

    public static final Integer CASH_ON_DELIVERY_PAYMENT_KIND = 1;
    public static final Integer CASH_ONLINE_PAYMENT_KIND = 2;

    private NailsConstant(){
        throw new IllegalStateException("Utility class");
    }

    public static final String ORDERS_LIMIT_CANCEL_TIME = "timetocancel";

    public static final Integer SETTING_KIND_TEXT = 1;
    public static final Integer SETTING_KIND_IMAGE = 2;
}
