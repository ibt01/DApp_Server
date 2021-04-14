package com.chengxi.person.user.service;


import com.chengxi.cache.service.RedisClient;
import com.chengxi.person.user.domain.DappDomain;
import com.chengxi.person.user.domain.UserDomain;
import com.chengxi.person.user.dto.GetUserInfoReq;
import com.chengxi.person.user.dto.SaveUserInfoDtoToChain;
import com.chengxi.person.user.dto.UserInfoDto;
import com.chengxi.person.user.repo.DappRepo;
import com.chengxi.person.user.repo.UserRepo;
import com.go.basetool.APIResultCode;
import com.go.basetool.bean.UserClient;
import com.go.basetool.exception.GlobalException;
import com.go.basetool.utils.JsonDtoWrapper;
import com.google.gson.Gson;
import com.yinni.tlk.olsapi.PushTxnResponse;
import com.yinni.tlk.service.OLSToolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.IllegalFormatCodePointException;
import java.util.List;

@Slf4j
@Service
public class UserService {
    @Autowired
    RedisClient redislient;

    @Autowired
    UserRepo userRepo;

    @Autowired
    DappRepo dappRepo;

    public static String contractName = "personalinfo";

    public JsonDtoWrapper getMyInfo(GetUserInfoReq getUserInfoReq) {
        UserDomain userDomain = userRepo.getUserByUserId(getUserInfoReq.getIdLong());
        if (null == userDomain) {
            return new JsonDtoWrapper(null, APIResultCode.USERID_NOT_EXIST);
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(userDomain, userInfoDto);
        return new JsonDtoWrapper(userInfoDto, APIResultCode.SUCCESS);
    }

    public JsonDtoWrapper saveUserInfo(UserInfoDto userInfoDto) {

        if (userInfoDto.getIdLong() == null || userInfoDto.getIdLong() < 0) {
            return new JsonDtoWrapper(null, APIResultCode.USERID_NOT_EXIST);
        }

        UserDomain userDomain = userRepo.getUserByUserId(userInfoDto.getIdLong());

        if (null == userDomain) {
            userDomain = new UserDomain();
            userDomain.setIdLong(userDomain.getIdLong());
        }
        if (userInfoDto.getPersonalInfoJson().length() > 1024) {
            return new JsonDtoWrapper(null, APIResultCode.USERID_NOT_EXIST);
        }


        SaveUserInfoDtoToChain saveUserInfoDtoToChain = new SaveUserInfoDtoToChain();
        saveUserInfoDtoToChain.setId(userInfoDto.getIdLong());
        saveUserInfoDtoToChain.setPerson(userInfoDto.getPersonalInfoJson());
        PushTxnResponse pushTxnResponse = OLSToolService.getInstance().pushAction(contractName, "saveperson", new Gson().toJson(saveUserInfoDtoToChain), OLSToolService.getActivePermission(contractName), OLSToolService.baseUrl, OLSToolService.personInfoKey);

        if (StringUtils.isEmpty(pushTxnResponse.getTransaction_id())) {
            throw new GlobalException(APIResultCode.SAVE_INFO_TO_CHAIN_FAILURE);
        }
        userDomain.setPersonalInfoJson(userInfoDto.getPersonalInfoJson());
        userDomain.setIdLong(userInfoDto.getIdLong());
        userDomain.setTxid(pushTxnResponse.getTransactionId());
        userRepo.save(userDomain);

        return new JsonDtoWrapper(null, APIResultCode.SUCCESS);
    }

    public JsonDtoWrapper getDapps() {
        List<DappDomain> dappDomains = dappRepo.getDapps();
        return new JsonDtoWrapper(dappDomains, APIResultCode.SUCCESS);
    }
}
