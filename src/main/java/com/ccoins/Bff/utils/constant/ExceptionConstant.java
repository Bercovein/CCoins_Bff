package com.ccoins.Bff.utils.constant;

public class ExceptionConstant {

    //LABELS
    public static final String ERROR_LABEL = "Error when trying to ";
    public static final String LOGIN_WITH_ERROR_LABEL = ERROR_LABEL.concat("login with ");
    public static final String GET_ERROR_LABEL = ERROR_LABEL.concat("get ");
    public static final String CREATE_NEW_ERROR_LABEL = ERROR_LABEL.concat("create new ");

    //ERRORS
    public static final String GENERIC_ERROR_CODE = "0001";
    public static final String GENERIC_ERROR = "Something went wrong! Check with your administrator";

    public static final String GOOGLE_ERROR_CODE = "0002";
    public static final String GOOGLE_ERROR = LOGIN_WITH_ERROR_LABEL.concat("Google");

    public static final String FACEBOOK_ERROR_CODE = "0003";
    public static final String FACEBOOK_ERROR = LOGIN_WITH_ERROR_LABEL.concat("Facebook");

    public static final String USERS_NEW_OWNER_ERROR_CODE = "0004";
    public static final String USERS_NEW_OWNER_ERROR = CREATE_NEW_ERROR_LABEL.concat("owner");

    public static final String USERS_GET_OWNER_BY_EMAIL_ERROR_CODE = "0005";
    public static final String USERS_GET_OWNER_BY_EMAIL_ERROR = GET_ERROR_LABEL.concat("owner");

}
