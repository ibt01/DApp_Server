package com.chengxi.person.user.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Date;

@Slf4j
@Data
@Entity
@Table(name = "Student")
public class StudentInfo {
    @Id
    @Column(name = "id")
    String studentId;

    @Column(name = "student_name")
    String studentName;

    @Column(name = "phonenum")
    String phoneNum;

    @Column(name = "student_birthday")
    Long studentBirthday;

    @Column(name = "student_gender")
    Integer studentGender;

    @Column(name = "student_id_code")
    String studentCode;

    @Column(name = "student_email")
    String studentEmail;

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
