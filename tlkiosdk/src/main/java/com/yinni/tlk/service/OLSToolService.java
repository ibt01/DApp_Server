/*
 * Copyright (c) 2017-2018 PLACTAL.
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.yinni.tlk.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yinni.tlk.dto.TlkTransfer;
import com.yinni.tlk.chain.Action;
import com.yinni.tlk.chain.PackedTransaction;
import com.yinni.tlk.chain.SignedTransaction;
import com.yinni.tlk.crypto.ec.TLKPrivateKey;
import com.yinni.tlk.dto.SignatureToChainDto;
import com.yinni.tlk.dto.TransferJson;
import com.yinni.tlk.tlktypes.TypeChainId;
import com.yinni.tlk.olsapi.*;
import com.yinni.tlk.util.Consts;
import com.yinni.tlk.util.NodOLSApi;

import java.util.ArrayList;


public class OLSToolService {

    private final NodOLSApi mNodOLSApi = new NodOLSApi();

    private static OLSToolService instance = null;

    String certificaterPrivateKey = "";

    public static String personInfoKey = "";

    public static String educationtlkKey = "";

    public static String resepstlkKey = "";

    public static String baseUrl = "http://127.0.0.1:8888";//telkom

    private OLSToolService() {

    }

    public static OLSToolService getInstance() {
        if (null == instance) {
            instance = new OLSToolService();
        }
        return instance;
    }




    public EosChainInfo getChainInfo(String baseUrl) {

        return mNodOLSApi.readInfo("get_info", baseUrl);
    }

    public PushTxnResponse transfer(String from, String to, String symbolName, String contractName, String amount, String memo, String baseUrl, String privateKey) throws Exception {

        TransferJson transferJson = new TransferJson(from, to, amount + " " + symbolName, memo);
        TlkTransfer tlkTransfer = new TlkTransfer(from, to, 0, memo);

        PushTxnResponse pushTxnResponse = pushAction(contractName, tlkTransfer.getActionName(), new Gson().toJson(transferJson), getActivePermission(from), baseUrl, privateKey);
        return pushTxnResponse;
    }

    public PushTxnResponse signatureToBlochChain(SignatureToChainDto signatureToChainDto, String baseUrl) {
        PushTxnResponse pushTxnResponse = pushAction("certificater", "storecertifi", new Gson().toJson(signatureToChainDto), getActivePermission("certificater"), baseUrl, certificaterPrivateKey);
        return pushTxnResponse;
    }

    private SignedTransaction createTransaction(String contract, String actionName, String dataAsHex,
                                                String[] permissions, EosChainInfo chainInfo) {
        Action action = new Action(contract, actionName);
        action.setAuthorization(permissions);
        action.setData(dataAsHex);

        SignedTransaction txn = new SignedTransaction();
        txn.addAction(action);
        txn.putSignatures(new ArrayList<>());


        if (null != chainInfo) {
            txn.setReferenceBlock(chainInfo.getHeadBlockId());
            txn.setExpiration(chainInfo.getTimeAfterHeadBlockTime(Consts.TX_EXPIRATION_IN_MILSEC));
        }

        return txn;
    }


    public SignedTransaction signTransaction(final SignedTransaction txn,
                                             final TLKPrivateKey privKey, final TypeChainId id) throws IllegalStateException {

        SignedTransaction stxn = new SignedTransaction(txn);
        stxn.sign(privKey, id);
        return stxn;
    }

    private PackedTransaction signAndPackTransaction(SignedTransaction txnBeforeSign, EosChainInfo info, String transferprivatekey) {
        final SignedTransaction stxn;
        stxn = signTransaction(txnBeforeSign, new TLKPrivateKey(transferprivatekey), new TypeChainId(info.getChain_id()));
        return new PackedTransaction(stxn);
    }


    public static String[] getActivePermission(String accountName) {
        return new String[]{accountName + "@active"};
    }


    public JsonObject readAccountInfo(String accountName, String baseUrl) {
        return mNodOLSApi.getAccountInfo(new AccountInfoRequest(accountName), baseUrl);
    }


    public PushTxnResponse pushAction(String contract, String action, String data, String[] permissions, String baseUrl, String transferprivatekey) {
        JsonToBinResponse jsonToBinResp = mNodOLSApi.jsonToBin(new JsonToBinRequest(contract, action, data), baseUrl);
        EosChainInfo chainInfo = getChainInfo(baseUrl);
        SignedTransaction st = createTransaction(contract, action, jsonToBinResp.getBinargs(), permissions, chainInfo);
        PushTxnResponse pushTxnResponse = mNodOLSApi.pushTransaction(signAndPackTransaction(st, chainInfo, transferprivatekey), baseUrl);
        pushTxnResponse.setBlockNum(chainInfo.getHeadBlockNum());
        return pushTxnResponse;
    }
}
