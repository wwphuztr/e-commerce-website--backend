package com.nails.api.dto;

public class ErrorCode {
    /**
     * Province error code
     */
    public static final String PROVINCE_ERROR_UNAUTHORIZED = "ERROR-PROVINCE-000";
    public static final String PROVINCE_ERROR_NOT_FOUND = "ERROR-PROVINCE-001";

    /**
     * General error code
     */
    public static final String GENERAL_ERROR_LOGIN_FAILED = "ERROR-GENERAL-003";
    public static final String GENERAL_ERROR_UNAUTHORIZED = "ERROR-GENERAL-000";

    /**
     * Orders error code
     */
    public static final String ORDERS_ERROR_UNAUTHORIZED = "ERROR-ORDERS-000";
    public static final String ORDERS_ERROR_NOT_FOUND_PRODUCT = "ERROR-ORDERS-003";
    public static final String ORDERS_ERROR_BAD_REQUEST = "ERROR-ORDERS-002";
    public static final String ORDERS_ERROR_NOT_FOUND = "ERROR-ORDERS-001";
    public static final String ORDERS_ERROR_INVALID_STATE = "ERROR-ORDERS-008";
    public static final String ORDERS_ERROR_STATE_DONE = "ERROR-ORDERS-009";
    public static final String ORDERS_ERROR_SETTINGS_NOT_FOUND = "ERROR-ORDERS-010";
    public static final String ORDERS_ERROR_SETTINGS_INVALID_VALUE = "ERROR-ORDERS-011";
    public static final String ORDERS_ERROR_CANCEL_TIME_NOT_ALLOWED = "ERROR-ORDERS-012";
    public static final String ORDERS_ERROR_NOT_FOUND_ORDERS_DETAIL = "ERROR-ORDERS-004";

    /**
     * Customer error code
     */
    public static final String CUSTOMER_ERROR_NOT_FOUND = "ERROR-CUSTOMER-001";
    public static final String CUSTOMER_ERROR_PHONE_EXIST = "ERROR-CUSTOMER-002";
    public static final String CUSTOMER_ERROR_NOT_FOUND_PROVINCE = "ERROR-CUSTOMER-004";
    public static final String CUSTOMER_ERROR_GROUP_NOT_EXIST = "ERROR-CUSTOMER-003";
    public static final String CUSTOMER_ERROR_EMAIL_EXIST = "ERROR-CUSTOMER-005";
    public static final String CUSTOMER_ERROR_OLD_PWD_NOT_MATCH = "ERROR-CUSTOMER-006";
    /**
     * Address error code
     */
    public static final String ADDRESS_ERROR_UNAUTHORIZED = "ERROR-ADDRESS-000";
    public static final String ADDRESS_ERROR_BAD_REQUEST = "ERROR-ADDRESS-002";
    public static final String ADDRESS_ERROR_NOT_FOUND = "ERROR-ADDRESS-001";
    /**
     * News error code
     */
    public static final String NEWS_ERROR_UNAUTHORIZED = "ERROR-NEWS-000";
    public static final String NEWS_ERROR_NOT_FOUND = "ERROR-NEWS-001";
}
