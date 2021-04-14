package com.chengxi.prescription.user.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Date;

@Slf4j
@Data
@Entity
@Table(name = "doctor")
public class DoctorInfo {
    @Id
    @Column(name = "id")
    String doctorId;

    @Column(name = "doctor_name")
    String doctorName;

    @Column(name = "doctor_gender")
    Integer doctorGender;

    @Column(name = "hospital_belong")
    String hospitalBelong;

    @Column(name = "doctor_email")
    String doctorEmail;

    @Column(name = "expertise")
    String expertise;

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
