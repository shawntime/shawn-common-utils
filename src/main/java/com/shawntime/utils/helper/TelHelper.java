package com.shawntime.utils.helper;

import java.util.regex.Pattern;

/**
 * Created by zhouxiaoming on 2016/1/27.
 */
public final class TelHelper {

    private TelHelper() {
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
