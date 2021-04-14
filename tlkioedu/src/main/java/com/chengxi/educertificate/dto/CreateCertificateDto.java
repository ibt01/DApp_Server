package com.chengxi.educertificate.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateCertificateDto {
    String studentCode;
    String studentName;

    String certificateUrl;
    String note;

    List<CourseItem> courseItems;
}
