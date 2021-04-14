package com.yinni.tlk.util;


import com.yinni.tlk.chain.Action;
import com.yinni.tlk.chain.SignedTransaction;
import com.yinni.tlk.crypto.ec.TLKPrivateKey;
import com.yinni.tlk.olsapi.EosChainInfo;
import com.yinni.tlk.tlktypes.TypeChainId;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TLKTxUtils {

    public static final Integer TX_EXPIRATION_IN_MILSEC = 30000;

    public static SignedTransaction createTransaction(String contract,
                                                      String actionName,
                                                      String dataAsHex,
                                                      String[] permissions,
                                                      EosChainInfo chainInfo) throws Exception {

        try{

            Action action = new Action(contract, actionName);
            action.setAuthorization(permissions);
            action.setData( dataAsHex );


            SignedTransaction txn = new SignedTransaction();
            txn.addAction(action);
            txn.putSignatures(new ArrayList<>());


            if (null != chainInfo) {
                txn.setReferenceBlock(chainInfo.getHeadBlockId());
                txn.setExpiration(chainInfo.getTimeAfterHeadBlockTime(TX_EXPIRATION_IN_MILSEC));
            }

            return txn;
        }
        catch (Exception e)
        {
            return null;
        }

    }


    public static String[] getActivePermission(String accountName) {
        return new String[]{accountName + "@active"};
    }


    public static SignedTransaction signTransaction(SignedTransaction tx_for_sign, String oracle_key, String chainId) throws Exception {

        try{
            //sign
            TLKPrivateKey private_key = new TLKPrivateKey(oracle_key);
            tx_for_sign.sign(private_key, new TypeChainId(chainId));

            return tx_for_sign;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static BigDecimal translateToken(String quantity, String symbol){
        String[] tokenHolderArray = quantity.split(" ");
        if(tokenHolderArray[1].equals(symbol))
        {
            return new BigDecimal(tokenHolderArray[0]);
        }
        return new BigDecimal(0);
    }
    public static SignedTransaction addAction(SignedTransaction txn,
                                              String contract,
                                              String actionName,
                                              String dataAsHex,
                                              String[] permissions,
                                              EosChainInfo chainInfo) {
        Action action = new Action(contract, actionName);
        action.setAuthorization(permissions);
        action.setData( dataAsHex );


        txn.addAction(action);
        txn.putSignatures(new ArrayList<>());

        //TODO zen add exception here
        if (null != chainInfo) {
            txn.setReferenceBlock(chainInfo.getHeadBlockId());
            txn.setExpiration(chainInfo.getTimeAfterHeadBlockTime(TX_EXPIRATION_IN_MILSEC));
        }

        return txn;
    }
}
