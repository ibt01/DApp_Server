package com.yinni.tlk.crypto.ec;


import com.yinni.tlk.crypto.digest.Ripemd160;
import com.yinni.tlk.crypto.digest.Sha256;
import com.yinni.tlk.crypto.util.Base58;
import com.yinni.tlk.crypto.util.BitUtils;
import com.yinni.tlk.dto.TLKRefValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.regex.PatternSyntaxException;

public class TLKEcUtil {

    public static final String PREFIX_K1 = "K1";
    public static final String PREFIX_R1 = "R1";


    public static byte[] extractFromRipemd160( String base58Data ) {
        byte[] data= Base58.decode( base58Data );
        if ( data[0] == data.length) {
            return Arrays.copyOfRange(data, 2, data.length );
        }

        return null;
    }


    public static byte[] getBytesIfMatchedRipemd160(String base58Data, String prefix, TLKRefValue<Long> checksumRef ){
        byte[] prefixBytes = StringUtils.isEmpty(prefix) ? new byte[0] : prefix.getBytes();

        byte[] data= Base58.decode( base58Data );

        byte[] toHashData = new byte[data.length - 4 + prefixBytes.length];
        System.arraycopy( data, 0, toHashData, 0, data.length - 4); // key data

        System.arraycopy( prefixBytes, 0, toHashData, data.length - 4, prefixBytes.length);

        Ripemd160 ripemd160 = Ripemd160.from( toHashData); //byte[] data, int startOffset, int length
        long checksumByCal = BitUtils.uint32ToLong( ripemd160.bytes(), 0);
        long checksumFromData= BitUtils.uint32ToLong(data, data.length - 4 );
        if ( checksumByCal != checksumFromData ) {
            throw new IllegalArgumentException("Invalid format, checksum mismatch");
        }

        if ( checksumRef != null ){
            checksumRef.data = checksumFromData;
        }

        return Arrays.copyOfRange(data, 0, data.length - 4);
    }

    public static byte[] getBytesIfMatchedSha256(String base58Data, TLKRefValue<Long> checksumRef ){
        byte[] data= Base58.decode( base58Data );

        // offset 0은 제외, 뒤의 4바이트 제외하고, private key 를 뽑자
        Sha256 checkOne = Sha256.from( data, 0, data.length - 4 );
        Sha256 checkTwo = Sha256.from( checkOne.getBytes() );
        if ( checkTwo.equalsFromOffset( data, data.length - 4, 4)
                || checkOne.equalsFromOffset( data, data.length - 4, 4) ){

            if ( checksumRef != null ){
                checksumRef.data = BitUtils.uint32ToLong( data, data.length - 4);
            }

            return Arrays.copyOfRange(data, 1, data.length - 4);
        }

        throw new IllegalArgumentException("Invalid format, checksum mismatch");
    }



    public static String encodeEosCrypto(String prefix, CurveParam curveParam, byte[] data ) {
        String typePart = "";
        if ( curveParam != null ) {
            if ( curveParam.isType( CurveParam.SECP256_K1)) {
                typePart = PREFIX_K1;
            }
            else
            if ( curveParam.isType( CurveParam.SECP256_R1)){
                typePart = PREFIX_R1;
            }
        }

        byte[] toHashData = new byte[ data.length + typePart.length() ];
        System.arraycopy( data, 0, toHashData, 0, data.length);
        if ( typePart.length() > 0 ) {
            System.arraycopy( typePart.getBytes(), 0, toHashData, data.length, typePart.length());
        }

        byte[] dataToEncodeBase58 = new byte[ data.length + 4 ];

        Ripemd160 ripemd160 = Ripemd160.from( toHashData);
        byte[] checksumBytes = ripemd160.bytes();

        System.arraycopy( data, 0, dataToEncodeBase58, 0, data.length); // copy source data
        System.arraycopy( checksumBytes, 0, dataToEncodeBase58, data.length, 4); // copy checksum data


        String result;
        if ( StringUtils.isEmpty( typePart)) {
            result = prefix;
        }
        else {
            result = prefix + OLS_CRYPTO_STR_SPLITTER + typePart + OLS_CRYPTO_STR_SPLITTER;
        }

        return result + Base58.encode( dataToEncodeBase58 );
    }




    private static final String OLS_CRYPTO_STR_SPLITTER = "_";
    public static String[] safeSplitEosCryptoString( String cryptoStr ) {
        if ( StringUtils.isEmpty( cryptoStr)) {
            return new String[]{ cryptoStr };
        }

        try {
            return cryptoStr.split( OLS_CRYPTO_STR_SPLITTER );
        }
        catch (PatternSyntaxException e){
            e.printStackTrace();
            return new String[]{ cryptoStr };
        }
    }

    public static String concatEosCryptoStr( String... strData ) {

        String result="";

        for ( int i = 0; i < strData.length; i++) {
            result += strData[i] + ( i < strData.length -1 ? OLS_CRYPTO_STR_SPLITTER : "");
        }
        return result;
    }

    public static CurveParam getCurveParamFrom(String curveType ) {
        return EcTools.getCurveParam( PREFIX_R1.equals( curveType ) ? CurveParam.SECP256_R1 : CurveParam.SECP256_K1);
    }


}
