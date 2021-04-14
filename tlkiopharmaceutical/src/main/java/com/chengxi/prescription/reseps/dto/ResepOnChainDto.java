package com.chengxi.prescription.reseps.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class ResepOnChainDto {
    String id;

    String patientId;

    String patientName;

    String doctorId;

    String doctorName;

    String hospitalId;

    String drugStoreId;

    String drugStoreName;

    String medicalJsonBase64;

    String pictureUrl;

    Integer status;
}
