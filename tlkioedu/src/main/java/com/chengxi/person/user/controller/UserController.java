package com.chengxi.person.user.controller;

import com.chengxi.person.user.dto.LoginReq;
import com.chengxi.person.user.dto.SearchStudent;
import com.chengxi.person.user.dto.UserRegisterReq;
import com.chengxi.person.user.service.UserService;
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


    @PostMapping("/search")
    @ResponseBody
    public JsonDtoWrapper search(@RequestBody SearchStudent searchStudent) {
        return userService.search(searchStudent);
    }
}
