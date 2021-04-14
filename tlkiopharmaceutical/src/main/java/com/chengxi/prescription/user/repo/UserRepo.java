package com.chengxi.prescription.user.repo;


import com.chengxi.prescription.user.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<UserDomain, Long>, JpaSpecificationExecutor<UserDomain> {
    @Query("select o from UserDomain o where o.phoneNum=?1")
    UserDomain getUserByPhoneNum(String phoneNum);

    @Query("select o from UserDomain o where o.phoneNum like (%?1%) and o.prescriptionRole=?2")
    List<UserDomain> getUserByPhoneNumArray(String phoneNum, Integer role);

    @Query("select o from UserDomain o where o.prescriptionRole=?1 and o.statusEntity=?2")
    List<UserDomain> getUserByRoleStatus(Integer prescriptionRole, Integer statusEntity);

    @Query("select o from UserDomain o where o.prescriptionRole=?1 and o.statusEntity=?2 and o.hospitalBelong=?3")
    List<UserDomain> getUserByRoleStatusWithBelong(Integer prescriptionRole, Integer statusEntity, String hospitalBelong);

    @Query("select o from UserDomain o where o.id=?1")
    UserDomain getUserByUserId(String userId);


}
