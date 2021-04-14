package com.go.basetool.bean;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserLoginInfo {
    UserClient userClient = new UserClient();
    Map<String, String> headers = new HashMap<>();
}