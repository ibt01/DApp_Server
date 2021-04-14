package com.chengxi.educertificate.dto;

import lombok.Data;

import java.util.List;

@Data
public class EduCertificateRes {
    String certificateId;



    String studentCode;
    String studentName;


    String schoolCode;
    String schoolName;

    String txidOnChain;

    String certificateUrl;
    String note;

    List<CourseItem> courseItems;
}
