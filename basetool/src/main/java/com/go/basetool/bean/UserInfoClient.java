package com.go.basetool.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.go.basetool.APIResultCode;
import lombok.Data;

@Data
public class UserInfoClient<DTO> {
    String userID;

    Integer myRole;

    Integer status;

    Integer statusEntity;

    DTO info;
}
