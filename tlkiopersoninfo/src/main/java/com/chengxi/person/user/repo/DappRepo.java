package com.chengxi.person.user.repo;

import com.chengxi.person.user.domain.DappDomain;
import com.chengxi.person.user.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DappRepo extends JpaRepository<DappDomain, Long>, JpaSpecificationExecutor<DappDomain> {
    @Query("select o from DappDomain o")
    List<DappDomain> getDapps();
}
