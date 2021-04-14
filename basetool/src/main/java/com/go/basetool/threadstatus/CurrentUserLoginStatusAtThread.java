package com.go.basetool.threadstatus;


import com.go.basetool.bean.UserLoginInfo;

public class CurrentUserLoginStatusAtThread {
    private static final DataBinder<UserLoginInfo> REQUEST_USER_BINDERUserLogin = DataBinderManager.getDataBinder(ThreadConstant.REQUEST_USER_BINDER_LOGIN_STATUS);
    public static UserLoginInfo getLoginUserStatus() {
        return REQUEST_USER_BINDERUserLogin.get();
    }
}
