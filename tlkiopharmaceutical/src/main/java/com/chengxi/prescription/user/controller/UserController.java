package com.chengxi.prescription.user.controller;

import com.chengxi.prescription.user.dto.*;
import com.chengxi.prescription.user.service.UserService;
import com.go.basetool.bean.UserClient;
import com.go.basetool.threadstatus.AbstractController;
import com.go.basetool.utils.JsonDtoWrapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("user")
public class UserController extends AbstractController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public JsonDtoWrapper login(@RequestBody LoginReq userLoginReq) {
        JsonDtoWrapper j = userService.login(userLoginReq);
        UserClient u = getLoginUser();
        log.info(new Gson().toJson(j));
        return j;
    }

    @PostMapping("/register")
    @ResponseBody
    public JsonDtoWrapper register(@RequestBody UserRegisterReq userRegister) {
        return userService.register(userRegister);
    }

    @GetMapping("/getMyInfo")
    @ResponseBody
    public JsonDtoWrapper getMyInfo() {
        return userService.getMyInfo(getLoginUser());
    }

    @PostMapping("/getUserToCheckDetailInfo")
    @ResponseBody
    public JsonDtoWrapper getUserToCheckDetailInfo(@RequestBody GetDoctorInfoReq doctorInfoReq, UserClient userClient){
        return userService.getUserToCheckDetailInfo(doctorInfoReq, getLoginUser());
    }
    @PostMapping("/searchUserByPhone")
    @ResponseBody
    public JsonDtoWrapper searchUserByPhone(@RequestBody SearchUserReq searchUserReq){
        return userService.searchUserByPhone(searchUserReq);
    }

    @PostMapping("/searchHospitalByBluryName")
    @ResponseBody
    public JsonDtoWrapper searchHospitalByBluryName(@RequestBody SearchHospitalReq searchHospitalReq){
        return userService.searchHospitalByName(searchHospitalReq);
    }

    @PostMapping("/doCheckSomeOne")
    @ResponseBody
    public JsonDtoWrapper doCheckSomeOne(@RequestBody DoCheckReq doCheckReq) {
        return userService.doCheckSomeOne(doCheckReq, getLoginUser());
    }

    @PostMapping("/getUserToCheck")
    @ResponseBody
    public JsonDtoWrapper getUserToCheck(@RequestBody GetToCheckReq getToCheckReq) {
        return userService.getUserToCheck(getToCheckReq, getLoginUser());
    }

    @GetMapping("/getCheckTypes")
    @ResponseBody
    public JsonDtoWrapper getCheckTypes() {
        return userService.getCheckTypes(getLoginUser());
    }
}
