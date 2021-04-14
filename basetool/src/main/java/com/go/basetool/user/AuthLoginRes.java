package com.go.basetool.user;

import com.go.basetool.bean.UserClient;
import lombok.Data;

@Data
public class AuthLoginRes {
    Integer b;//是否验证cookie成功
    UserClient userClient;
}
