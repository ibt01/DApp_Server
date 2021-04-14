package com.go.basetool.exception;


import com.go.basetool.APIResultCode;
import com.go.basetool.bean.UserClient;
import com.go.basetool.threadstatus.AbstractController;
import com.go.basetool.threadstatus.DataBinderManager;
import com.go.basetool.threadstatus.ThreadConstant;
import com.go.basetool.utils.JsonDtoWrapper;
import lombok.Data;

@Data
public class GlobalException extends RuntimeException {

    private String myException;
    private String code;
    private String extraMsg;

    public GlobalException(APIResultCode exception, String extraMsg) {
        super(exception.getMessage());

        UserClient userClient = AbstractController.getsLoginUser();
        if(null != userClient) {
            userClient.setCurrentAPIResultCode(exception);
            DataBinderManager.<UserClient>getDataBinder(ThreadConstant.REQUEST_USER_BINDER).put(userClient);
        }
        setMyException(exception.getMessage());
        setCode(exception.getCode());
        setExtraMsg(extraMsg);
    }
    public GlobalException(APIResultCode exception) {
        super(exception.getMessage());

        UserClient userClient = AbstractController.getsLoginUser();
        if(null != userClient) {
            userClient.setCurrentAPIResultCode(exception);
            DataBinderManager.<UserClient>getDataBinder(ThreadConstant.REQUEST_USER_BINDER).put(userClient);
        }
        setMyException(exception.getMessage());
        setCode(exception.getCode());
    }

    public GlobalException(JsonDtoWrapper jo){
        this.code = jo.getCode();
        this.myException = jo.getMsg();
        this.extraMsg = jo.getExtraMsg();
    }
}

