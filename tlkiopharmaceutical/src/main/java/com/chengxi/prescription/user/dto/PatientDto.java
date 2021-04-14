package com.chengxi.prescription.user.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class PatientDto {

    Integer patientGender;

    String patientEmail;
}
