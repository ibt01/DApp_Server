package com.chengxi.prescription.user.repo;

import com.chengxi.prescription.user.domain.DoctorInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.print.Doc;
import java.util.List;

public interface DoctorInfoRepo extends JpaRepository<DoctorInfo, String>, JpaSpecificationExecutor<DoctorInfo> {
    @Query("select o from DoctorInfo o where o.doctorId=?1")
    DoctorInfo getDoctorInfoById(String doctorId);

    @Query("select o from DoctorInfo o where o.doctorName=?1")
    List<DoctorInfo> getUserByName(String doctorName);

}
