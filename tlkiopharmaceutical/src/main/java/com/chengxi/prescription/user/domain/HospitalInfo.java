package com.chengxi.prescription.user.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Date;

@Slf4j
@Data
@Entity
@Table(name = "hospital")
public class HospitalInfo {
    @Id
    @Column(name = "id")
    String hospitalId;


    @Column(name = "hospitall_name")
    String hospitallName;

    @Column(name = "hospital_address")
    String hospitalAddress;

    @Column(name = "enstablish_time_l")
    Long establishTime;

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
