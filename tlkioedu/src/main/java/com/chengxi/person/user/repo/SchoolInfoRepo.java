package com.chengxi.person.user.repo;

import com.chengxi.person.user.domain.SchoolInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface SchoolInfoRepo extends JpaRepository<SchoolInfo, Long>, JpaSpecificationExecutor<SchoolInfo> {
    @Query("select o from SchoolInfo o where o.schoolCode=?1")
    SchoolInfo getSchoolInfoByCode(String schoolCode);


    @Query("select o from SchoolInfo o where o.schoolId=?1")
    SchoolInfo getSchoolById(String schoolId);
}
