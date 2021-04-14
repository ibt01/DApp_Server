package com.chengxi.prescription.reseps.dto;

import com.go.basetool.commonreq.PageReq;
import lombok.Data;

@Data
public class GetMyResepReq {
    PageReq pageReq;
    Long id;
}
