package com.chengxi.person.user.repo;

import com.chengxi.person.user.domain.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentInfoRepo extends JpaRepository<StudentInfo, Long>, JpaSpecificationExecutor<StudentInfo> {
    @Query("select o from StudentInfo o where o.studentId=?1")
    StudentInfo getStudentInfoById(String studentId);

    @Query("select o from StudentInfo o where o.studentCode=?1")
    StudentInfo getUserByStudentCode(String studentCode);

    @Query("select o from StudentInfo o where o.studentName=?1")
    List<StudentInfo> getUserByName(String studentName);

    @Query("select o from StudentInfo o where o.phoneNum=?1")
    List<StudentInfo> getUserByPhone(String phoneNum);

    @Query("select o from StudentInfo o where o.studentName like (%?1%)")
    List<StudentInfo> searchUserByName(String studentName);
}
