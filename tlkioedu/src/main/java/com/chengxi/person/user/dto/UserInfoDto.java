package com.chengxi.person.user.dto;

import lombok.Data;

@Data
public class UserInfoDto {
    String id;
    String phoneNum;
    String headerPicUrl;
    Integer eduRole;//EduRole:student = 1;school = 2;
    StudentDto studentDto;
    SchoolDto schoolDto;
}
