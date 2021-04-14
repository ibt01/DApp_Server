package com.chengxi.person.user.constant;

public class EduRole {
    //student = 1;school = 2;
    public static final Integer student = 1;
    public static final Integer school = 2;

    public static boolean isGoodRole(Integer role){
        if (role.equals(student) || role.equals(school)){
            return true;
        }

        return false;
    }
}
