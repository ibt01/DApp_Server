package com.chengxi.person.user.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Date;

@Slf4j
@Data
@Entity
@Table(name = "User")
public class UserDomain {
    @Id
    @Column(name = "id")
    String id;

    @Column(name = "phonenum")
    String phoneNum;

    @Column(name = "headerpicurl")
    String headerPicUrl;

    @Column(name = "status")
    private Integer status;

    @Column(name = "edu_role")
    private Integer eduRole;//1普通用户

    @Column(name = "loginpwd")
    String loginPWD;

    @Column(name = "created")
    private Long created;

    @Column(name = "updated")
    private Long updated;

    @PrePersist
    protected void onCreate() {
        created = new Date().getTime();
        updated = new Date().getTime();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date().getTime();
    }
}
