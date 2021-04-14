package com.chengxi.prescription.reseps.dto;

import lombok.Data;

@Data
public class MedicalItem {
    String medicalName;//药物名称
    String quantityWhole;//总数量
    String usageOneTime;//多久一次
}
