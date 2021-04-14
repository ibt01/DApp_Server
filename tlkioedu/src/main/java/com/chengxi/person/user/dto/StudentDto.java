package com.chengxi.person.user.dto;

import lombok.Data;

@Data
public class StudentDto {
    String studentName;
    Long studentBirthday;
    Integer studentGender;//UserGender:male = 1;female = 2;
    String studentCode;
    String studentEmail;
    String schoolCode;
}
