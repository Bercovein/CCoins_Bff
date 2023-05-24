package com.ccoins.bff.utils;

public class RegexUtils {

    public static final String A_TO_Z_AND_MIDDLE_DASH = "^[A-Z0-9\\-]+$";

    private RegexUtils() {
    }

    public static boolean validateRegex(String text, String regex){
        return text == null || (!text.matches(regex));
    }

    public static boolean validateRegexAtoZMiddleDash(String text){
        return RegexUtils.validateRegex(text, A_TO_Z_AND_MIDDLE_DASH);
    }
}
