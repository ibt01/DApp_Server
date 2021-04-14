package com.chengxi.cache.constant;

public class CacheConstant {

    public static String getPhoneCheckCodeprefix(String phone){
        return "phonecodeprefix"+phone;
    }

    public static String getPhoneCookie(String phone){
        return "phonecookie"+phone;
    }

    public static String getUserInfoRrefix(String keyUser){
        return "userInfoPrefix"+keyUser;
    }
}
