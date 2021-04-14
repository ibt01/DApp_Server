package com.chengxi.prescription.user.repo;

import com.chengxi.prescription.user.domain.DoctorInfo;
import com.chengxi.prescription.user.domain.PatientInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientRepo extends JpaRepository<PatientInfo, Long>, JpaSpecificationExecutor<PatientInfo> {
    @Query("select o from PatientInfo o where o.patientId=?1")
    PatientInfo getPatientInfoById(String patientId);

    @Query("select o from PatientInfo o where o.patientName=?1")
    List<PatientInfo> getUserByName(String patientName);

}
