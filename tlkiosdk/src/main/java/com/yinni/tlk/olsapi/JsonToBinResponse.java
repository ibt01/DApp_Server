package com.yinni.tlk.olsapi;

import com.google.gson.annotations.Expose;

import java.util.List;


public class JsonToBinResponse {
    @Expose
    private String binargs;

    @Expose
    private List<String> required_scope;

    @Expose
    private List<String> required_auth;

    public String getBinargs() {
        return binargs;
    }

    public List<String> getRequiredScope(){
        return required_scope;
    }

    public List<String> getRequiredAuth(){
        return required_auth;
    }
}
