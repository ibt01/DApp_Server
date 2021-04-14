package com.go.basetool.bean;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.go.basetool.APIResultCode;
import com.go.basetool.BigDecimalTool;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserClient {
    String userID;

    @JsonIgnore
    String ip;

    String cookie;

    Integer myRole;

    Integer status;

    Integer statusEntity;

    @JsonIgnore
    APIResultCode currentAPIResultCode;
}
