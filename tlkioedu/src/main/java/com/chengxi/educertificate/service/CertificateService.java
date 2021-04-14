package com.chengxi.educertificate.service;

import com.chengxi.person.user.constant.EduRole;
import com.chengxi.person.user.domain.SchoolInfo;
import com.chengxi.person.user.domain.StudentInfo;
import com.chengxi.person.user.domain.UserDomain;
import com.chengxi.person.user.repo.SchoolInfoRepo;
import com.chengxi.person.user.repo.StudentInfoRepo;
import com.chengxi.person.user.repo.UserRepo;
import com.chengxi.educertificate.domain.EduCertificateDomain;
import com.chengxi.educertificate.dto.*;
import com.chengxi.educertificate.repo.EduCertificateRepo;
import com.go.basetool.APIResultCode;
import com.go.basetool.bean.UserClient;
import com.go.basetool.exception.GlobalException;
import com.go.basetool.idgenerator.IdGenerator;
import com.go.basetool.utils.JsonDtoWrapper;
import com.go.basetool.utils.Sha256;
import com.google.gson.Gson;
import com.yinni.tlk.olsapi.PushTxnResponse;
import com.yinni.tlk.service.OLSToolService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CertificateService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    StudentInfoRepo studentInfoRepo;

    @Autowired
    SchoolInfoRepo schoolInfoRepo;

    @Autowired
    EduCertificateRepo eduCertificateRepo;

    String contractName = "educationtlk";


    public JsonDtoWrapper createEduCertificate(CreateCertificateDto createCertificateDto, UserClient userClient) {
        UserDomain userDomain = userRepo.getUserByUserId(userClient.getUserID());
        if (!EduRole.school.equals(userDomain.getEduRole())) {
            return new JsonDtoWrapper(null, APIResultCode.NO_PRIVILEGE);
        }

        StudentInfo studentInfo = studentInfoRepo.getUserByStudentCode(createCertificateDto.getStudentCode());
        if (null == studentInfo) {
            return new JsonDtoWrapper(null, APIResultCode.STUDENT_NOT_EXIST);
        }

        if (!studentInfo.getStudentName().trim().equals(createCertificateDto.getStudentName().trim())) {
            return new JsonDtoWrapper(null, APIResultCode.STUDENT_NAME_NOTRIGHT);
        }

        SchoolInfo schoolInfo = schoolInfoRepo.getSchoolById(userClient.getUserID());
        EduCertificateDomain eduCertificateDomain = new EduCertificateDomain();
        eduCertificateDomain.setId("educerid" + IdGenerator.genUserId());
        eduCertificateDomain.setSchoolCode(schoolInfo.getSchoolCode());
        eduCertificateDomain.setStudentCode(studentInfo.getStudentCode());
        eduCertificateDomain.setCertificateUrl(createCertificateDto.getCertificateUrl());
        eduCertificateDomain.setNote(createCertificateDto.getNote());
        eduCertificateDomain.setScoresArrayJsonBase64(new String(Base64.getEncoder().encode(new Gson().toJson(createCertificateDto.getCourseItems()).getBytes(StandardCharsets.UTF_8))));
        eduCertificateDomain.setContentToHash(new String(Base64.getEncoder().encode(new Gson().toJson(eduCertificateDomain).getBytes(StandardCharsets.UTF_8))));
        eduCertificateDomain.setContentHashOnChain(Sha256.from(eduCertificateDomain.getContentToHash().getBytes(StandardCharsets.UTF_8)).toString());
        eduCertificateDomain.setIdLong(System.currentTimeMillis());




        SaveEduChain saveUserInfoDtoToChain = new SaveEduChain();
        saveUserInfoDtoToChain.setId(eduCertificateDomain.getIdLong());
        saveUserInfoDtoToChain.setEdujson(eduCertificateDomain.getContentHashOnChain()+"---"+eduCertificateDomain.getContentToHash());
        PushTxnResponse pushTxnResponse = OLSToolService.getInstance().pushAction(contractName, "saveedu", new Gson().toJson(saveUserInfoDtoToChain), OLSToolService.getActivePermission(contractName), OLSToolService.baseUrl, OLSToolService.educationtlkKey);

        if (StringUtils.isEmpty(pushTxnResponse.getTransaction_id())) {
            throw new GlobalException(APIResultCode.SAVE_INFO_TO_CHAIN_FAILURE);
        }


        eduCertificateDomain.setTxidOnChain(pushTxnResponse.getTransactionId());
        eduCertificateRepo.save(eduCertificateDomain);



        return new JsonDtoWrapper(eduCertificateDomain.getId(), APIResultCode.SUCCESS);
    }



    public String onChain(String value) {
        return "testTxidOnTlkio";
    }

    public JsonDtoWrapper searchStudent(SearchStudentReq searchStudentReq, UserClient userClient) {
        List<StudentInfo> studentInfoList = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        if (null != searchStudentReq.getStudentCode()) {
            StudentInfo studentInfo = studentInfoRepo.getUserByStudentCode(searchStudentReq.getStudentCode());
            if (null != studentInfo) {
                studentInfoList.add(studentInfo);
                ids.add(studentInfo.getStudentId());
            }
        }

        if (null != searchStudentReq.getStudentName()) {
            List<StudentInfo> stus = studentInfoRepo.getUserByName(searchStudentReq.getStudentName());
            for (StudentInfo studentInfo : stus) {
                if (!ids.contains(studentInfo.getStudentId())) {
                    studentInfoList.add(studentInfo);
                    ids.add(studentInfo.getStudentId());
                }
            }
        }

        if (null != searchStudentReq.getPhoneNum()) {
            List<StudentInfo> stus = studentInfoRepo.getUserByPhone(searchStudentReq.getPhoneNum());
            for (StudentInfo studentInfo : stus) {
                if (!ids.contains(studentInfo.getStudentId())) {
                    studentInfoList.add(studentInfo);
                    ids.add(studentInfo.getStudentId());
                }
            }
        }

        return new JsonDtoWrapper(studentInfoList, APIResultCode.SUCCESS);
    }

    public JsonDtoWrapper getEduCertificate(GetEduCertificateReq getEduCertificateReq){
        EduCertificateDomain eduCertificateDomain = eduCertificateRepo.getByCertId(getEduCertificateReq.getCertificateId());
        EduCertificateRes eduCertificateRes = new EduCertificateRes();
        BeanUtils.copyProperties(eduCertificateDomain, eduCertificateRes);

        StudentInfo studentInfo = studentInfoRepo.getUserByStudentCode(eduCertificateDomain.getStudentCode());
        if (null == studentInfo) {
            return new JsonDtoWrapper(null, APIResultCode.NOT_STUDENT);
        }

        eduCertificateRes.setStudentName(studentInfo.getStudentName());
        eduCertificateRes.setCertificateId(eduCertificateDomain.getId());

        SchoolInfo schoolInfo = schoolInfoRepo.getSchoolInfoByCode(eduCertificateDomain.getSchoolCode());
        eduCertificateRes.setSchoolName(schoolInfo.getSchoolName());

        List<CourseItem> courseItems = new ArrayList<>();
        courseItems = new Gson().fromJson(new String(Base64.getDecoder().decode(eduCertificateDomain.getScoresArrayJsonBase64())), courseItems.getClass());
        eduCertificateRes.setCourseItems(courseItems);

        return new JsonDtoWrapper(eduCertificateRes, APIResultCode.SUCCESS);
    }

    public JsonDtoWrapper getMyEduCertificate(GetSomeOneEduCertiReq getSomeOneEduCertiReq) {
        StudentInfo studentInfo = studentInfoRepo.getStudentInfoById(getSomeOneEduCertiReq.getUserId());
        if (null == studentInfo) {
            return new JsonDtoWrapper(null, APIResultCode.NOT_STUDENT);
        }


        List<EduCertificateDomain> eduCertificateDomains = eduCertificateRepo.getByStudentCode(studentInfo.getStudentCode());
        List<EduCertificateRes> myCer = new ArrayList<>();

        for (EduCertificateDomain eduCertificateDomain : eduCertificateDomains) {
            EduCertificateRes eduCertificateRes = new EduCertificateRes();
            BeanUtils.copyProperties(eduCertificateDomain, eduCertificateRes);
            eduCertificateRes.setStudentName(studentInfo.getStudentName());

            eduCertificateRes.setCertificateId(eduCertificateDomain.getId());

            SchoolInfo schoolInfo = schoolInfoRepo.getSchoolInfoByCode(eduCertificateDomain.getSchoolCode());
            eduCertificateRes.setSchoolName(schoolInfo.getSchoolName());

            List<CourseItem> courseItems = new ArrayList<>();
            courseItems = new Gson().fromJson(new String(Base64.getDecoder().decode(eduCertificateDomain.getScoresArrayJsonBase64())), courseItems.getClass());
            eduCertificateRes.setCourseItems(courseItems);

            myCer.add(eduCertificateRes);
        }
        return new JsonDtoWrapper(myCer, APIResultCode.SUCCESS);
    }
}
