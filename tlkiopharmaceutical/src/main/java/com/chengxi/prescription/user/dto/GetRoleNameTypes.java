package com.chengxi.prescription.user.dto;

import lombok.Data;

@Data
public class GetRoleNameTypes {
    String name;
    Integer role;
    public GetRoleNameTypes(String name, Integer role){
        this.name = name;
        this.role = role;
    }
}
