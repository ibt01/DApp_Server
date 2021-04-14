package com.yinni.tlk.olsapi;


import com.google.gson.annotations.Expose;
import com.yinni.tlk.crypto.ec.TLKPublicKey;


import java.util.ArrayList;
import java.util.List;


public class RequiredKeysResponse {

    @Expose
    private List<String> required_keys ;

    public List<TLKPublicKey> getKeys() {
        if ( null == required_keys ){
            return new ArrayList<>();
        }

        ArrayList<TLKPublicKey> retKeys = new ArrayList<>(required_keys.size());
        for ( String pubKey: required_keys ){
            retKeys.add( new TLKPublicKey( pubKey));
        }

        return retKeys;
    }
}
