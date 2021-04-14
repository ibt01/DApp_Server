package com.chengxi.prescription.user.dto;

import lombok.Data;

@Data
public class LoginReq {
    String phoneNum;
    String phoneMessageCode;
}