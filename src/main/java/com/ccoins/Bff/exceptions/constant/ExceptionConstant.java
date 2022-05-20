package com.ccoins.Bff.exceptions.constant;

public class ExceptionConstant {

    //LABELS
    public static final String ERROR_LABEL = "Error when trying to ";
    public static final String UNAUTHORIZED_LABEL = "User not authorized to ";
    public static final String LOGIN_WITH_ERROR_LABEL = ERROR_LABEL.concat("login with ");
    public static final String GET_ERROR_LABEL = ERROR_LABEL.concat("get ");
    public static final String GET_UNAUTHORIZED_LABEL = UNAUTHORIZED_LABEL.concat("get ");
    public static final String CREATE_NEW_ERROR_LABEL = ERROR_LABEL.concat("create new ");

    public static final String CREATE_OR_REPLACE_ERROR_LABEL = ERROR_LABEL.concat("create or replace ");

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

    public static final String BARS_CREATE_OR_UPDATE_ERROR_CODE = "0006";
    public static final String BARS_CREATE_OR_UPDATE_ERROR = CREATE_OR_REPLACE_ERROR_LABEL.concat("bar");

    public static final String BARS_FIND_BY_ID_ERROR_CODE = "0007";
    public static final String BARS_FIND_BY_ID_ERROR = GET_ERROR_LABEL.concat("bar by id");

    public static final String BARS_FIND_BY_OWNER_ERROR_CODE = "0008";
    public static final String BARS_FIND_BY_OWNER_ERROR = GET_ERROR_LABEL.concat("bars by owner");

    public static final String USER_NOT_FOUND_ERROR_CODE = "0009";
    public static final String USER_NOT_FOUND_ERROR = "User not found.";

    public static final String TOKEN_VARIABLE_NOT_FOUND_ERROR_CODE = "0010";
    public static final String TOKEN_VARIABLE_NOT_FOUND_ERROR = GET_ERROR_LABEL.concat("variable from Token.");

    public static final String BARS_UNAUTHORIZED_ERROR_CODE = "0011";
    public static final String BARS_UNAUTHORIZED_ERROR = GET_UNAUTHORIZED_LABEL.concat("bar");

}
