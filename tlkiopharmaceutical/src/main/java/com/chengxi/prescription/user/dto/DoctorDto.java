package com.chengxi.prescription.user.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class DoctorDto {
    Integer doctorGender;
    String hospitalBelong;
    String hospitalBelongName;
    String doctorEmail;
    String expertise;
}
