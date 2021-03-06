package com.yinni.tlk.crypto.ec;


import com.yinni.tlk.crypto.digest.Ripemd160;
import com.yinni.tlk.crypto.util.BitUtils;
import com.yinni.tlk.dto.TLKRefValue;

import java.util.Arrays;



public class TLKPublicKey {
    private static final String LEGACY_PREFIX = "OLS";
    private static final String PREFIX = "PUB";

    private static final int CHECK_BYTE_LEN = 4;

    private final long mCheck;
    private final CurveParam mCurveParam;
    private final byte[] mData;

    public static class IllegalEosPubkeyFormatException extends IllegalArgumentException {
        public IllegalEosPubkeyFormatException(String pubkeyStr) {
            super("invalid ols public key : " + pubkeyStr);
        }
    }

    public TLKPublicKey(byte[] data ){
        this( data, EcTools.getCurveParam( CurveParam.SECP256_K1));
    }

    public TLKPublicKey(byte[] data, CurveParam curveParam ){
        mData = Arrays.copyOf(data, 33);
        mCurveParam = curveParam;

        mCheck= BitUtils.uint32ToLong( Ripemd160.from( mData, 0, mData.length).bytes(), 0 );
    }

    public TLKPublicKey(String base58Str) {
        TLKRefValue<Long> checksumRef = new TLKRefValue<>();

        String[] parts = TLKEcUtil.safeSplitEosCryptoString( base58Str );
        if ( base58Str.startsWith(LEGACY_PREFIX) ) {
            if ( parts.length == 1 ){
                mCurveParam = EcTools.getCurveParam( CurveParam.SECP256_K1);
                mData = TLKEcUtil.getBytesIfMatchedRipemd160( base58Str.substring( LEGACY_PREFIX.length()), null, checksumRef);
            }
            else {
                throw new IllegalEosPubkeyFormatException( base58Str );
            }
        }
        else {
            if ( parts.length < 3 ) {
                throw new IllegalEosPubkeyFormatException( base58Str );
            }

            // [0]: prefix, [1]: curve type, [2]: data
            if ( false == PREFIX.equals( parts[0]) ) throw new IllegalEosPubkeyFormatException( base58Str );

            mCurveParam = TLKEcUtil.getCurveParamFrom( parts[1]);
            mData = TLKEcUtil.getBytesIfMatchedRipemd160( parts[2], parts[1], checksumRef);
        }

        mCheck = checksumRef.data;
    }

    public byte[] getBytes() {
        return mData;
    }




    @Override
    public String toString() {

        boolean isR1 = mCurveParam.isType( CurveParam.SECP256_R1 );

        return TLKEcUtil.encodeEosCrypto( isR1 ? PREFIX : LEGACY_PREFIX, isR1 ? mCurveParam : null, mData );

//        byte[] postfixBytes = isR1 ? EosEcUtil.PREFIX_R1.getBytes() : new byte[0] ;
//        byte[] toDigest = new byte[mData.length + postfixBytes.length];
//        System.arraycopy( mData, 0, toDigest, 0, mData.length);
//
//        if ( postfixBytes.length > 0) {
//            System.arraycopy(postfixBytes, 0, toDigest, mData.length, postfixBytes.length);
//        }
//
//        byte[] digest = Ripemd160.from( toDigest ).bytes();
//        byte[] result = new byte[ CHECK_BYTE_LEN + mData.length];
//
//        System.arraycopy( mData, 0, result, 0, mData.length);
//        System.arraycopy( digest, 0, result, mData.length, CHECK_BYTE_LEN);
//
//        if ( isR1 ){
//            return EosEcUtil.concatEosCryptoStr(PREFIX , EosEcUtil.PREFIX_R1, Base58.encode( result ) );
//        }
//        else {
//            return LEGACY_PREFIX + Base58.encode( result ) ;
//        }
    }

    @Override
    public int hashCode(){
        return (int)(mCheck & 0xFFFFFFFFL );
    }

    @Override
    public boolean equals(Object other) {
        if ( this == other ) return true;

        if ( null == other || getClass() != other.getClass())
            return false;

        return BitUtils.areEqual( this.mData, ((TLKPublicKey)other).mData);
    }

    public boolean isCurveParamK1() {
        return ( mCurveParam == null || CurveParam.SECP256_K1 == mCurveParam.getCurveParamType() );
    }
}