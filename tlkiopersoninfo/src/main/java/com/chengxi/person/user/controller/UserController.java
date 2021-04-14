package com.chengxi.person.user.controller;


import com.chengxi.person.user.dto.DappItem;
import com.chengxi.person.user.dto.GetUserInfoReq;
import com.chengxi.person.user.dto.UserInfoDto;
import com.chengxi.person.user.service.UserService;
import com.go.basetool.threadstatus.AbstractController;
import com.go.basetool.utils.JsonDtoWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("user")
public class UserController extends AbstractController {

    @Autowired
    UserService userService;

    @PostMapping("/getUserInfo")
    @ResponseBody
    public JsonDtoWrapper getMyInfo(@RequestBody GetUserInfoReq getUserInfoReq) {
        return userService.getMyInfo(getUserInfoReq);
    }

    @PostMapping("/saveUserInfo")
    @ResponseBody
    public JsonDtoWrapper saveUserInfo(@RequestBody UserInfoDto userInfoDto) {
        return userService.saveUserInfo(userInfoDto);
    }

    @GetMapping("/getDapps")
    @ResponseBody
    public JsonDtoWrapper getDapps() {

        return userService.getDapps();
    }
}
