package com.go.config.repository;


import com.go.config.domain.GlobalConfigDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface GlobalConfigRepository extends JpaRepository<GlobalConfigDomain, Integer>, JpaSpecificationExecutor<GlobalConfigDomain> {
    @Query("select o from GlobalConfigDomain o where o.type=?1")
    List<GlobalConfigDomain> getConfigs(Integer typs);


    @Query("select o from GlobalConfigDomain o where o.key=?1")
    GlobalConfigDomain getConfig(String key);
}
