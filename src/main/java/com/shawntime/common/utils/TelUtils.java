package com.shawntime.common.utils;

import java.util.regex.Pattern;

/**
 * Created by zhouxiaoming on 2016/1/27.
 */
public final class TelUtils {

    private TelUtils() {
    }

    public static boolean isValidMobiePhoneNum(String telNumber) {
        final String regexMobile = "^1\\d{10}$";
        return Pattern.matches(regexMobile, telNumber);
    }

    public static boolean isValidFixPhoneNum(String telNumber) {
        final String regexPhone = "^((0\\d{2,3})-)(\\d{7,8})(-(\\d{3,}))?$";
        return Pattern.matches(regexPhone, telNumber);
    }

    public static boolean isMayBeMobiePhoneNum(String telNumber) {
        final String regexMobile = "^\\d{11}$";
        return Pattern.matches(regexMobile, telNumber);
    }
}
