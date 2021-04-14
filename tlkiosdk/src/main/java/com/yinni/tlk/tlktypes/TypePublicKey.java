package com.yinni.tlk.tlktypes;


import com.yinni.tlk.crypto.ec.TLKPublicKey;


public class TypePublicKey implements TLKType.Packer {
    private static final byte PACK_VAL_CURVE_PARAM_TYPE_K1 = 0;
    private static final byte PACK_VAL_CURVE_PARAM_TYPE_R1 = 1;

    private final TLKPublicKey mPubKey;

    public static TypePublicKey from(TLKPublicKey publicKey) {
        return new TypePublicKey(publicKey);
    }

    public TypePublicKey( TLKPublicKey publicKey ) {
        mPubKey = publicKey;
    }


    @Override
    public void pack(TLKType.Writer writer) {
        writer.putVariableUInt( mPubKey.isCurveParamK1() ? PACK_VAL_CURVE_PARAM_TYPE_K1 : PACK_VAL_CURVE_PARAM_TYPE_R1 );

        writer.putBytes( mPubKey.getBytes());
    }
}
