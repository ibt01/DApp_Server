package com.yinni.tlk.olsapi;

import com.google.gson.annotations.Expose;


public class GetTableRequest {
    @Expose
    private boolean json = true;

    @Expose
    private String scope;

    @Expose
    private String code;

    @Expose
    private String table;

    public GetTableRequest( String scope, String code, String table ) {
        this.scope = scope;
        this.code = code;
        this.table = table;
    }
}
