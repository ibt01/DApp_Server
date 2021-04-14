package com.chengxi.prescription.user.repo;


import com.chengxi.prescription.user.domain.DrugstoreInfo;
import com.chengxi.prescription.user.domain.HospitalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface DrugstoreInfoRepo extends JpaRepository<DrugstoreInfo, Long>, JpaSpecificationExecutor<DrugstoreInfo> {
    @Query("select o from DrugstoreInfo o where o.drugstoreName=?1")
    DrugstoreInfo getDrugStoreInfoByName(String drugstoreName);

    @Query("select o from DrugstoreInfo o where o.drugstoreId=?1")
    DrugstoreInfo getDrugstoreById(String drugstoreId);
}
