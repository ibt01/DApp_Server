package com.chengxi.prescription.reseps.repo;

import com.chengxi.prescription.reseps.domain.ResepDomain;
import com.chengxi.prescription.user.domain.DoctorInfo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResepRepo extends JpaRepository<ResepDomain, Long>, JpaSpecificationExecutor<ResepDomain> {
    @Query("select o from ResepDomain o where o.id=?1 and o.patientId=?2")
    ResepDomain getResepById(Long resepId, String userId);

    @Query("select o from ResepDomain o where o.id=?1")
    ResepDomain getResepById(Long resepId);

    @Query("select o from ResepDomain o where o.doctorId=?1")
    List<ResepDomain> getDoctorsReseps(String doctorId, PageRequest pageRequest);

    @Query("select count(o.id) from ResepDomain o where o.doctorId=?1")
    Integer getDoctorsResepsCounts(String doctorId);

    @Query("select o from ResepDomain o where o.hospitalId=?1")
    List<ResepDomain> getHospitalsReseps(String hospitalId, PageRequest pageRequest);

    @Query("select count(o.id) from ResepDomain o where o.hospitalId=?1")
    Integer getHospitalsResepsCounts(String hospitalId);

    @Query("select o from ResepDomain o where o.patientId=?1")
    List<ResepDomain> getPatientsReseps(String patientId, PageRequest pageRequest);

    @Query("select count(o.id) from ResepDomain o where o.patientId=?1")
    Integer getPatientsResepsCounts(String patientId);

    //-
    @Query("select o from ResepDomain o where o.drugStoreId=?1")
    List<ResepDomain> getDrugsReseps(String drugStoreId, PageRequest pageRequest);

    @Query("select count(o.id) from ResepDomain o where o.drugStoreId=?1")
    Integer getDrugsResepsCounts(String drugStoreId);

    @Query("select o from ResepDomain o where o.id=?1")
    List<ResepDomain> searchResep(String id);
}
