package com.chengxi.educertificate.repo;

import com.chengxi.educertificate.domain.EduCertificateDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EduCertificateRepo  extends JpaRepository<EduCertificateDomain, String>, JpaSpecificationExecutor<EduCertificateDomain> {
    @Query("select o from EduCertificateDomain o where o.studentCode=?1")
    List<EduCertificateDomain> getByStudentCode(String studentCode);

    @Query("select o from EduCertificateDomain o where o.schoolCode=?1")
    List<EduCertificateDomain> getBySchoolCode(String schoolCode);

    @Query("select o from EduCertificateDomain o where o.id=?1")
    EduCertificateDomain getByCertId(String certificateId);

    @Query("select count(o.id) from EduCertificateDomain o")
    Integer getCounts();
}
