
package com.yinni.tlk.chain;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.Expose;
import com.yinni.tlk.crypto.util.HexUtils;
import com.yinni.tlk.tlktypes.TLKType;
import com.yinni.tlk.tlktypes.TypeAccountName;
import com.yinni.tlk.tlktypes.TypeActionName;
import com.yinni.tlk.tlktypes.TypePermissionLevel;
import lombok.Data;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@Data
public class Action implements TLKType.Packer {
    @Expose
    private TypeAccountName account;

    @Expose
    private TypeActionName name;

    @Expose
    private List<TypePermissionLevel> authorization = null;

    @Expose
    private JsonElement data;

    public Action(String account, String name, TypePermissionLevel authorization, String data){
        this.account = new TypeAccountName(account);
        this.name = new TypeActionName(name);
        this.authorization = new ArrayList<>();
        if ( null != authorization ) {
            this.authorization.add(authorization);
        }

        if ( null != data ) {
            this.data = new JsonPrimitive(data);
        }
    }

    public Action(String account, String name) {
        this (account, name, null, null);
    }

    public Action(){
        this ( null, null, null, null);
    }

    public String getAccount() {
        return account.toString();
    }

    public void setAccount(String account) {
        this.account = new TypeAccountName(account);
    }

    public String getName() {
        return name.toString();
    }

    public void setName(String name) {
        this.name = new TypeActionName(name);
    }

    public List<TypePermissionLevel> getAuthorization() {
        return authorization;
    }

    public void setAuthorization(List<TypePermissionLevel> authorization) {
        this.authorization = authorization;
    }

    public void setAuthorization(TypePermissionLevel[] authorization) {
        this.authorization.addAll( Arrays.asList( authorization) );
    }

    public void setAuthorization(String[] accountWithPermLevel) {
        if ( null == accountWithPermLevel){
            return;
        }

        for ( String permissionStr : accountWithPermLevel ) {
            String[] split = permissionStr.split("@", 2);
            authorization.add( new TypePermissionLevel(split[0], split[1]) );
        }
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(String data) {
        this.data = new JsonPrimitive(data);
    }

    @Override
    public void pack(TLKType.Writer writer) {
        account.pack(writer);
        name.pack(writer);

        writer.putCollection( authorization );

        if ( null != data ) {
            byte[] dataAsBytes = HexUtils.toBytes( data.getAsString());
            writer.putVariableUInt(dataAsBytes.length);
            writer.putBytes( dataAsBytes );
        }
        else {
            writer.putVariableUInt(0);
        }
    }
}
