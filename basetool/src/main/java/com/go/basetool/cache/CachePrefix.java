package com.go.basetool.cache;

public class CachePrefix {
    public static String userInfoPrefix = "userInfoPrefix";
    public static String getUserInfoCacheID(String uid){
        return userInfoPrefix+uid;
    }
    public static String phoneCodeMsgInfoPrefix = "phoneCodeMsgInfoPrefix";
}//redis userInfoPrefix+jintong_id_cookie=logininfo/**/
