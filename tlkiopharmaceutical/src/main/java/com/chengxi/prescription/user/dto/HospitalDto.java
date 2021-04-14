package com.chengxi.prescription.user.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class HospitalDto {
    String hospitalAddress;
    Long establishTime;
}
