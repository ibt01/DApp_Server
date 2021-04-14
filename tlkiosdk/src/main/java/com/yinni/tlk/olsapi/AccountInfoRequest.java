package com.yinni.tlk.olsapi;

import com.google.gson.annotations.Expose;


public class AccountInfoRequest {

    @Expose
    private String account_name;

    public AccountInfoRequest(String name) {
        setName(name);
    }

    public String getName() {
        return account_name;
    }

    public void setName(String name) {
        this.account_name = name;
    }
}
