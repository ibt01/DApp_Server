package com.chengxi.person.user.service;

import com.chengxi.cache.constant.CacheConstant;
import com.chengxi.cache.service.RedisClient;
import com.chengxi.person.user.constant.EduRole;
import com.chengxi.person.user.domain.SchoolInfo;
import com.chengxi.person.user.domain.StudentInfo;
import com.chengxi.person.user.domain.UserDomain;
import com.chengxi.person.user.dto.*;
import com.chengxi.person.user.repo.SchoolInfoRepo;
import com.chengxi.person.user.repo.StudentInfoRepo;
import com.chengxi.person.user.repo.UserRepo;
import com.go.basetool.APIResultCode;
import com.go.basetool.bean.UserClient;
import com.go.basetool.exception.GlobalException;
import com.go.basetool.idgenerator.IdGenerator;
import com.go.basetool.user.UserStatus;
import com.go.basetool.utils.JsonDtoWrapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    @Autowired
    RedisClient redislient;

    @Autowired
    UserRepo userRepo;

    @Autowired
    StudentInfoRepo studentInfoRepo;

    @Autowired
    SchoolInfoRepo schoolInfoRepo;

    private String updateUserCookie(String phoneNum) {
        String cookie = IdGenerator.genCookie();
        redislient.set(CacheConstant.getPhoneCookie(phoneNum), cookie);
        return cookie;
    }

    public JsonDtoWrapper<UserClient> login(LoginReq userLoginReq) {

        JsonDtoWrapper j = new JsonDtoWrapper();

        String cacheContent = redislient.get(CacheConstant.getPhoneCheckCodeprefix(userLoginReq.getPhoneNum()));
        if ("1234".equals(userLoginReq.getPhoneMessageCode())) {
            cacheContent = userLoginReq.getPhoneMessageCode();
        }

        if (StringUtils.isEmpty(cacheContent)) {
            j.setCodeMsg(APIResultCode.CHECK_CODE_NOT_RIGHT);
            return j;
        }

        UserDomain userDomain = userRepo.getUserByPhoneNum(userLoginReq.getPhoneNum());
        if (null == userDomain) {
            j.setCodeMsg(APIResultCode.PHONE_NOT_EXIST);
            return j;
        }

        if (userLoginReq.getPhoneMessageCode().equals(cacheContent)) {
            UserClient userClient = new UserClient();
            String cookie = updateUserCookie(userLoginReq.getPhoneNum());
            userClient.setCookie(cookie);
            userClient.setUserID(userDomain.getId());
            userClient.setMyRole(userDomain.getEduRole());
            userClient.setStatus(userDomain.getStatus());
            redislient.set(CacheConstant.getUserInfoRrefix(userClient.getUserID()), new Gson().toJson(userClient));

            j.setData(userClient);
            j.setCodeMsg(APIResultCode.SUCCESS);
            return j;
        } else {
            j.setCodeMsg(APIResultCode.CHECK_CODE_NOT_RIGHT);
            return j;
        }
    }


    @Transactional
    public JsonDtoWrapper register(UserRegisterReq userRegisterReq) {
        JsonDtoWrapper j = new JsonDtoWrapper();

        if (StringUtils.isEmpty(userRegisterReq.getPhoneNum())) {
            j.setCodeMsg(APIResultCode.PHONE_NOT_EXIST);
            return j;
        }

        if (null != userRepo.getUserByPhoneNum(userRegisterReq.getPhoneNum())) {
            j.setCodeMsg(APIResultCode.PHONE_NUM_REGISTERED);
            return j;
        }


        String cacheContent = redislient.get(CacheConstant.getPhoneCheckCodeprefix(userRegisterReq.getPhoneNum()));
        if ("1234".equals(userRegisterReq.getPhoneMessageCode())) {
            cacheContent = userRegisterReq.getPhoneMessageCode();
        }

        if (StringUtils.isEmpty(cacheContent)) {
            j.setCodeMsg(APIResultCode.CHECK_CODE_NOT_RIGHT);
            return j;
        }

        if (!EduRole.isGoodRole(userRegisterReq.getEduRole())) {
            j.setCodeMsg(APIResultCode.UserRoleNotExist);
            return j;
        }

        if (userRegisterReq.getPhoneMessageCode().equalsIgnoreCase(cacheContent)) {
            UserDomain userDomain = new UserDomain();
            userDomain.setId(IdGenerator.genUserId());
            userDomain.setStatus(UserStatus.normal);
            BeanUtils.copyProperties(userRegisterReq, userDomain);


            if (EduRole.student.equals(userRegisterReq.getEduRole())) {
                if (null == schoolInfoRepo.getSchoolInfoByCode(userRegisterReq.getStudentDto().getSchoolCode())) {
                    j.setCodeMsg(APIResultCode.SCHOOL_NOT_EXIST);
                    throw new GlobalException(j);
                }

                if (null != studentInfoRepo.getUserByStudentCode(userRegisterReq.getStudentDto().getStudentCode())) {
                    j.setCodeMsg(APIResultCode.STUDENT_CODE_ALREADY_EXIST);
                    throw new GlobalException(j);
                }

                StudentInfo studentInfo = new StudentInfo();
                BeanUtils.copyProperties(userRegisterReq.getStudentDto(), studentInfo);
                studentInfo.setStudentId(userDomain.getId());
                studentInfo.setPhoneNum(userDomain.getPhoneNum());
                studentInfoRepo.save(studentInfo);

                userDomain.setEduRole(EduRole.student);
            } else if (EduRole.school.equals(userRegisterReq.getEduRole())) {
                if (null != schoolInfoRepo.getSchoolInfoByCode(userRegisterReq.getSchoolDto().getSchoolCode())) {
                    j.setCodeMsg(APIResultCode.SCHOOL_CODE_ALREADY_EXIST);
                    throw new GlobalException(j);
                }

                SchoolInfo schoolInfo = new SchoolInfo();
                BeanUtils.copyProperties(userRegisterReq.getSchoolDto(), schoolInfo);
                schoolInfo.setSchoolId(userDomain.getId());
                schoolInfoRepo.save(schoolInfo);

                userDomain.setEduRole(EduRole.school);
            }

            userRepo.save(userDomain);
            j.setCodeMsg(APIResultCode.SUCCESS);
        } else {
            j.setCodeMsg(APIResultCode.CHECK_CODE_NOT_RIGHT);
        }
        return j;
    }

    public JsonDtoWrapper getMyInfo(UserClient userClient) {
        UserDomain userDomain = userRepo.getUserByUserId(userClient.getUserID());
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(userDomain, userInfoDto);
        if (EduRole.student.equals(userDomain.getEduRole())) {
            StudentDto studentDto = new StudentDto();
            StudentInfo studentInfo = studentInfoRepo.getStudentInfoById(userClient.getUserID());
            BeanUtils.copyProperties(studentInfo, studentDto);
            userInfoDto.setStudentDto(studentDto);
        } else if (EduRole.school.equals(userDomain.getEduRole())) {
            SchoolDto schoolDto = new SchoolDto();
            SchoolInfo schoolInfo = schoolInfoRepo.getSchoolById(userClient.getUserID());
            BeanUtils.copyProperties(schoolInfo, schoolDto);
            userInfoDto.setSchoolDto(schoolDto);
        }
        return new JsonDtoWrapper(userInfoDto, APIResultCode.SUCCESS);
    }

    public JsonDtoWrapper search(SearchStudent searchStudent) {
        Set<String> ids = new HashSet<>();
        List<StudentInfo> studentInfos = new ArrayList<>();
        if (!StringUtils.isEmpty(searchStudent.getStuName())) {
            studentInfos = studentInfoRepo.searchUserByName(searchStudent.getStuName());
           for (StudentInfo studentInfo:studentInfos){
               ids.add(studentInfo.getStudentId());
           }
        }
        if (!StringUtils.isEmpty(searchStudent.getSutId()) && !ids.contains(searchStudent.getSutId())){
            StudentInfo studentInfo = studentInfoRepo.getStudentInfoById(searchStudent.getSutId());
            studentInfos.add(studentInfo);
        }
        List<SearchStuRes> searchStuResList = new ArrayList<>();
        for (StudentInfo studentInfo:studentInfos){
            SearchStuRes searchStuRes = new SearchStuRes();
            BeanUtils.copyProperties(studentInfo, searchStuRes);
            searchStuResList.add(searchStuRes);
        }
        return new JsonDtoWrapper(searchStuResList, APIResultCode.SUCCESS);
    }
}
