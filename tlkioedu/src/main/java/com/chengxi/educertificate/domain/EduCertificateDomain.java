package com.chengxi.educertificate.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Date;

@Slf4j
@Data
@Entity
@Table(name = "EduCertificate")
public class EduCertificateDomain {
    @Id
    @Column(name = "id")
    String id;

    @Column(name = "id_long")
    Long idLong;

    @Column(name = "student_code")
    String studentCode;

    @Column(name = "school_code")
    String schoolCode;

    @Column(name = "certificate_url")
    String certificateUrl;

    @Column(name = "note")
    String note;

    @Column(name = "scores_arrayjsonbase64")
    String scoresArrayJsonBase64;

    @Column(name = "txid_on_chain")
    String txidOnChain;

    @Column(name = "content_to_hash", length = 1000)
    String contentToHash;

    @Column(name = "content_hash_on_chain")
    String contentHashOnChain;

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
