package com.yinni.tlk.olsapi;


import com.google.gson.annotations.Expose;

public class Key {

    @Expose
    private String key;

    @Expose
    private Integer weight;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

}
