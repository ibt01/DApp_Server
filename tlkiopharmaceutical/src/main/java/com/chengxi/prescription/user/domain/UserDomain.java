package com.chengxi.prescription.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Date;

@Slf4j
@Data
@Entity
@Table(name = "user")
public class UserDomain {

    @Id
    @Column(name = "id")
    String id;

    @Column(name = "phonenum")
    String phoneNum;

    @Column(name = "headerpicurl")
    String headerPicUrl;

    @Column(name = "name")
    String name;

    @Column(name = "hospital_belong")
    String hospitalBelong;

    @Column(name = "status")
    private Integer status;

    @Column(name = "status_entity")
    private Integer statusEntity;//statusToPermit = 1;statusRejected = 2;statusPermitted = 3;

    @Column(name = "hos_role")
    private Integer prescriptionRole;////supermanager = 1;hospital = 2;doctor = 3;drugstore=4;patient=5

    @JsonIgnore
    @Column(name = "loginpwd")
    String loginPWD;

    @Column(name = "notes_for_userinfo_by_manager")
    String notesFoUserinfoByManager;

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
