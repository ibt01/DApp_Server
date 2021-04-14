package com.chengxi.prescription.user.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class SearchUserByPhoneRes {
    String id;

    String phoneNum;

    String name;
}
