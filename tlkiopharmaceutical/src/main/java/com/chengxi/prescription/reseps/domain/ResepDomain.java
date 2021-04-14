package com.chengxi.prescription.reseps.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Date;

@Slf4j
@Data
@Entity
@Table(name = "resep")
public class ResepDomain {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "patient_id")
    String patientId;

    @Column(name = "patient_name")
    String patientName;

    @Column(name = "patient_phone_num")
    String patientPhoneNum;

    @Column(name = "doctor_id")
    String doctorId;

    @Column(name = "doctor_name")
    String doctorName;

    @Column(name = "doctor_phone_num")
    String doctorPhoneNum;

    @Column(name = "hospital_id")
    String hospitalId;

    @Column(name = "drugstore_id")
    String drugStoreId;

    @Column(name = "drug_store_name")
    String drugStoreName;

    @Column(name = "drug_store_phonenum")
    String drugStorePhoneNum;

    @Column(name = "medical_json_base64")
    String medicalJsonBase64;

    @Column(name = "picture_url")
    String pictureUrl;

    @Column(name = "status")
    Integer status;//statusToGet = 1;statusTakened = 2;

    @Column(name = "doctor_resep_onchain_txid")
    String doctorResepOnChainTxid;

    @Column(name = "drug_store_resep_onchain_txid")
    String drugStoreResepOnChainTxid;

    @Column(name = "note")
    String note;

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
