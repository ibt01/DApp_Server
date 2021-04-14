package com.chengxi.tool.message.service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.chengxi.cache.constant.CacheConstant;
import com.chengxi.cache.service.RedisClient;
import com.chengxi.tool.message.AlyMessageUtil;
import com.chengxi.tool.message.MessageConstants;
import com.go.basetool.APIResultCode;
import com.go.basetool.utils.JsonDtoWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class MessageService {

    @Autowired
    MessageConstants messageConstants;

    @Autowired
    RedisClient redisClient;

    public JsonDtoWrapper sendPhoneMsg(String phoneNumber){

        //随机生成验证码
        Random random = new Random();
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int ran = random.nextInt(10);
            stringBuffer.append(ran);
        }
        JsonDtoWrapper jo = new JsonDtoWrapper();
        try {
            SendSmsResponse sendSmsResponse = AlyMessageUtil.sendSmsTip(messageConstants, phoneNumber, stringBuffer.toString());
            if ("OK".equalsIgnoreCase(sendSmsResponse.getCode())){
                jo.setCodeMsg(APIResultCode.SUCCESS);
                redisClient.set(CacheConstant.getPhoneCheckCodeprefix(phoneNumber), stringBuffer.toString(), 300);
            }else{
                jo.setCodeMsg(APIResultCode.UNKNOWN_ERROR);
                jo.setExtraMsg(sendSmsResponse.getMessage());
            }
            return jo;
        }catch (Exception e){
            log.error("", e);
            jo.setCodeMsg(APIResultCode.UNKNOWN_ERROR);
        }
        return jo;
    }
}
