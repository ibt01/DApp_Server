package com.chengxi.educertificate.dto;

import lombok.Data;

@Data
public class SearchStudentReq {
    String studentName;
    String studentCode;
    String phoneNum;
}
