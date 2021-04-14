package com.chengxi.prescription.user.repo;


import com.chengxi.prescription.user.domain.HospitalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HospitalInfoRepo extends JpaRepository<HospitalInfo, Long>, JpaSpecificationExecutor<HospitalInfo> {
    @Query("select o from HospitalInfo o where o.hospitallName=?1")
    HospitalInfo getSchoolInfoByName(String hospitallName);

    @Query("select o from HospitalInfo o where o.hospitallName like (%?1%)")
    List<HospitalInfo> getSchoolInfoByBlurryName(String hospitallName);

    @Query("select o from HospitalInfo o where o.hospitalId=?1")
    HospitalInfo getHospitalById(String hospitalId);
}
