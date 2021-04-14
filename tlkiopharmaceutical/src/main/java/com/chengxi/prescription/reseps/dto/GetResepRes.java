package com.chengxi.prescription.reseps.dto;

import com.chengxi.prescription.reseps.domain.ResepDomain;
import lombok.Data;

import java.util.List;

@Data
public class GetResepRes {
    List<ResepDomain> resepDomains;
    Integer totalLines;
}
