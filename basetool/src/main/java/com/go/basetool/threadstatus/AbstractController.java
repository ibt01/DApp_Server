package com.go.basetool.threadstatus;


import com.go.basetool.bean.UserClient;
import com.go.basetool.bean.UserLoginInfo;

public abstract class AbstractController {

    private static final DataBinder<UserClient> REQUEST_USER_BINDER = DataBinderManager.getDataBinder(ThreadConstant.REQUEST_USER_BINDER);

    private static final DataBinder<UserLoginInfo> REQUEST_USER_BINDERUserLogin = DataBinderManager.getDataBinder(ThreadConstant.REQUEST_USER_BINDER_LOGIN_STATUS);
    protected UserClient getLoginUser() {
        return REQUEST_USER_BINDER.get();
    }

    public static UserClient getsLoginUser() {
        return REQUEST_USER_BINDER.get();
    }


    protected UserLoginInfo getLoginUserStatus() {
        return REQUEST_USER_BINDERUserLogin.get();
    }

}