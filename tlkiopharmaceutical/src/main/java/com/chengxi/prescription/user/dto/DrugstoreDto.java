package com.chengxi.prescription.user.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class DrugstoreDto {
    String drugstoreAddress;
    Long establishTime;
}
