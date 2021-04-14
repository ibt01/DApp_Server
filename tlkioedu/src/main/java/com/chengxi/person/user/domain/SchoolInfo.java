package com.chengxi.person.user.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Date;

@Slf4j
@Data
@Entity
@Table(name = "School")
public class SchoolInfo {
    @Id
    @Column(name = "id")
    String schoolId;

    @Column(name = "school_name")
    String schoolName;

    @Column(name = "school_address")
    String schoolAddress;

    @Column(name = "school_code")
    String schoolCode;

    @Column(name = "school_email")
    String schoolEmail;

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
