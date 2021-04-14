package com.chengxi.person.user.dto;

import lombok.Data;

@Data
public class UserRegisterReq {
    String phoneNum;
    String phoneMessageCode;
    String loginPWD;
    String headerPicUrl;
    Integer eduRole;//EduRole:student = 1;school = 2;
    StudentDto studentDto;
    SchoolDto schoolDto;
}
