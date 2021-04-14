package com.go.basetool.utils;




import com.go.basetool.APIResultCode;

import java.io.Serializable;

public class JsonDtoWrapper<DTO> implements Serializable {
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String code;
    private String msg;
    private String enMsg;


    private String extraMsg;

    private DTO data;

    public JsonDtoWrapper(){

    }
    public JsonDtoWrapper(DTO o, APIResultCode c){
        this.data = o;
        setCodeMsg(c);
    }

    public void setCodeMsg(APIResultCode c) {
        this.setCode(c.getCode());
        this.setMsg(c.getMessage());
        this.setEnMsg(c.getEnglishMessage());
    }


    public DTO getData() {
        return data;
    }

    public void setData(DTO data) {
        this.data = data;
    }

    public String getExtraMsg() {
        return extraMsg;
    }

    public void setExtraMsg(String extraMsg) {
        this.extraMsg = extraMsg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getEnMsg() {
        return enMsg;
    }

    public void setEnMsg(String enMsg) {
        this.enMsg = enMsg;
    }
}