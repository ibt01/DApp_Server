package com.chengxi.prescription.user.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Date;

@Slf4j
@Data
@Entity
@Table(name = "patient")
public class PatientInfo {
    @Id
    @Column(name = "id")
    String patientId;

    @Column(name = "patient_name")
    String patientName;

    @Column(name = "patient_gender")
    Integer patientGender;

    @Column(name = "patient_email")
    String patientEmail;

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
