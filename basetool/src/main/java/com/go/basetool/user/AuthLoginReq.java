package com.go.basetool.user;

import lombok.Data;

@Data
public class AuthLoginReq {
    String uid;
    String cookie;
}