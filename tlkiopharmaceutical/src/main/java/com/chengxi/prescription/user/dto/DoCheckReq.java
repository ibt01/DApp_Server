package com.chengxi.prescription.user.dto;

import lombok.Data;

@Data
public class DoCheckReq {
    String userId;
    Integer entityStatus;//statusToPermit = 1待审核;statusRejected = 2拒绝;statusPermitted = 3同意;
    String notes;
}
