package com.chengxi.prescription.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Date;

@Slf4j
@Data
@Entity
@Table(name = "drugstore")
public class DrugstoreInfo {
    @Id
    @Column(name = "id")
    String drugstoreId;

    @Column(name = "drugstore_name")
    String drugstoreName;

    @Column(name = "drugstore_address")
    String drugstoreAddress;

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
