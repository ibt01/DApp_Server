package com.chengxi.person.user.repo;


import com.chengxi.person.user.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface UserRepo extends JpaRepository<UserDomain, Long>, JpaSpecificationExecutor<UserDomain> {
    @Query("select o from UserDomain o where o.phoneNum=?1")
    UserDomain getUserByPhoneNum(String phoneNum);

    @Query("select o from UserDomain o where o.id=?1")
    UserDomain getUserByUserId(String userId);
}
