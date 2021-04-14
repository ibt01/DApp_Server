package com.chengxi.prescription.reseps.dto;

import lombok.Data;

import javax.persistence.Column;
import java.util.List;

@Data
public class CreateRespReq {
    String patientId;

    String pictureUrl;

    String note;

    List<MedicalItem> medicalItemList;
}
