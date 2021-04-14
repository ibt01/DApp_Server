/*
 * Copyright (c) 2017 Mithril coin.
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
package com.yinni.tlk.util;


import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yinni.tlk.chain.PackedTransaction;
import com.yinni.tlk.olsapi.*;
import org.apache.commons.lang3.StringUtils;



public  class NodOLSApi {

    public static EosChainInfo readInfo(String infoType, String baseUrl){
        try {
            String url = baseUrl+  "/v1/chain/"+infoType;
            String data = HttpRequest.post(url).readTimeout(30000).connectTimeout(600).body();
            return GsonUtil.providesGson().fromJson(data, EosChainInfo.class);
        }catch (Exception e){
            return null;
        }
    }


    public static JsonObject getAccountInfo(AccountInfoRequest body, String baseUrl){
        String url = baseUrl+  "/v1/chain/get_account";
        String sendStr = GsonUtil.providesGson().toJson(body);
        JsonParser jsonParser = new JsonParser();
        return (JsonObject) jsonParser.parse(HttpRequest.post(url).readTimeout(30000).connectTimeout(600).send(sendStr).body());
    }


    public static String getTable(GetTableRequest body, String baseUrl){
        String url = baseUrl+  "/v1/chain/get_table_rows";
        String sendStr = GsonUtil.providesGson().toJson(body);
        JsonParser jsonParser = new JsonParser();
        String bodyReaded = HttpRequest.post(url).readTimeout(30000).connectTimeout(600).send(sendStr).body();
        return bodyReaded;
    }


    public static PushTxnResponse pushTransaction(PackedTransaction body, String baseUrl){
        String url = baseUrl+  "/v1/chain/push_transaction";
        String sendStr = GsonUtil.providesGson().toJson(body);
        String data = HttpRequest.post(url).readTimeout(30000).connectTimeout(600).send(sendStr).body();

        PushTxnResponse pushTxnResponse =  GsonUtil.providesGson().fromJson(data, PushTxnResponse.class);

        if(StringUtils.isEmpty(pushTxnResponse.getTransactionId())){
            pushTxnResponse.setLastError(data);
        }
        return pushTxnResponse;
    }


    public static JsonObject pushTransactionRetJson( PackedTransaction body, String baseUrl){
        String url = baseUrl+  "/v1/chain/push_transaction";
        String sendStr = GsonUtil.providesGson().toJson(body);
        JsonParser jsonParser = new JsonParser();
        return (JsonObject) jsonParser.parse(HttpRequest.post(url).readTimeout(30000).connectTimeout(600).send(sendStr).body());
    }


    public static RequiredKeysResponse getRequiredKeys(GetRequiredKeys body, String baseUrl){
        String url = baseUrl+  "/v1/chain/get_required_keys";
        String sendStr = GsonUtil.providesGson().toJson(body);
        String responseBody = HttpRequest.post(url).readTimeout(30000).connectTimeout(600).accept(HttpRequest.CONTENT_TYPE_JSON).send(sendStr).body();
        return GsonUtil.providesGson().fromJson(responseBody, RequiredKeysResponse.class);
    }


    public static JsonToBinResponse jsonToBin(JsonToBinRequest body, String baseUrl){
        String url = baseUrl+  "/v1/chain/abi_json_to_bin";
        String sendStr = GsonUtil.providesGson().toJson(body);
        String data = HttpRequest.post(url).readTimeout(30000).connectTimeout(600).send(sendStr).body();
        return GsonUtil.providesGson().fromJson(data, JsonToBinResponse.class);
    }


    public static GetCodeResponse getCode(GetCodeRequest body, String baseUrl){
        String url = baseUrl+  "/v1/chain/get_code";
        String sendStr = GsonUtil.providesGson().toJson(body);
        return GsonUtil.providesGson().fromJson(HttpRequest.post(url).readTimeout(30000).connectTimeout(600).send(sendStr).body(), GetCodeResponse.class);
    }


    public static JsonObject getAccountHistory(String historyPath,   JsonObject body, String baseUrl){
        String url = baseUrl+  "/v1/account_history/"+historyPath;
        String sendStr = GsonUtil.providesGson().toJson(body);
        JsonParser jsonParser = new JsonParser();
        return (JsonObject) jsonParser.parse(HttpRequest.post(url).readTimeout(30000).connectTimeout(600).send(sendStr).body());
    }

    public static String ACCOUNT_HISTORY_GET_TRANSACTIONS = "get_transactions" ;
    public static String GET_TRANSACTIONS_KEY = "account_name";

    public static String ACCOUNT_HISTORY_GET_SERVANTS = "get_controlled_accounts" ;
    public static String GET_SERVANTS_KEY = "controlling_account";

}
