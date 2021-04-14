package com.chengxi.prescription.user.service;

import com.chengxi.cache.constant.CacheConstant;
import com.chengxi.cache.service.RedisClient;
import com.chengxi.prescription.user.constant.EntityStatus;
import com.chengxi.prescription.user.constant.PrescriptionRole;
import com.chengxi.prescription.user.domain.*;
import com.chengxi.prescription.user.dto.*;
import com.chengxi.prescription.user.repo.*;
import com.go.basetool.APIResultCode;
import com.go.basetool.bean.UserClient;
import com.go.basetool.bean.UserInfoClient;
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
import java.util.List;

@Slf4j
@Service
public class UserService {
    @Autowired
    RedisClient redislient;

    @Autowired
    UserRepo userRepo;

    @Autowired
    DoctorInfoRepo doctorInfoRepo;

    @Autowired
    HospitalInfoRepo hospitalInfoRepo;

    @Autowired
    DrugstoreInfoRepo drugstoreInfoRepo;

    @Autowired
    PatientRepo patientRepo;

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

        if ((userDomain.getPrescriptionRole().equals(PrescriptionRole.hospital) ||
                userDomain.getPrescriptionRole().equals(PrescriptionRole.doctor) ||
                userDomain.getPrescriptionRole().equals(PrescriptionRole.drugstore)) &&
                !userDomain.getStatusEntity().equals(EntityStatus.statusPermitted)) {

            UserInfoClient userClient = new UserInfoClient();
            userClient.setStatusEntity(userDomain.getStatusEntity());
            userClient.setMyRole(userDomain.getPrescriptionRole());
            userClient.setUserID(userDomain.getId());

            if (EntityStatus.statusToPermit.equals(userDomain.getStatusEntity())) {
                j.setCodeMsg(APIResultCode.WAIT_FOR_PERMITTER);
            } else if (EntityStatus.statusRejected.equals(userDomain.getStatusEntity())) {

                userClient.setInfo(getUserInfo(userDomain.getId()));
                j.setCodeMsg(APIResultCode.REJECTED_FOR_INFOMATION_SUBMITTED_PLEASE_EDIT_AND_RESUBMIT);
            } else {
                j.setCodeMsg(APIResultCode.WAIT_FOR_PERMITTER);
            }

            j.setData(userClient);
            return j;
        }

        if (userLoginReq.getPhoneMessageCode().equals(cacheContent)) {
            UserClient userClient = new UserClient();
            String cookie = updateUserCookie(userLoginReq.getPhoneNum());
            userClient.setCookie(cookie);
            userClient.setUserID(userDomain.getId());
            userClient.setMyRole(userDomain.getPrescriptionRole());
            userClient.setStatus(userDomain.getStatus());
            userClient.setStatusEntity(userDomain.getStatusEntity());
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


        String cacheContent = redislient.get(CacheConstant.getPhoneCheckCodeprefix(userRegisterReq.getPhoneNum()));
        if ("1234".equals(userRegisterReq.getPhoneMessageCode())) {
            cacheContent = userRegisterReq.getPhoneMessageCode();
        }

        if (StringUtils.isEmpty(cacheContent)) {
            j.setCodeMsg(APIResultCode.CHECK_CODE_NOT_RIGHT);
            return j;
        }

        if (!PrescriptionRole.isGoodRole(userRegisterReq.getHosRole())) {
            j.setCodeMsg(APIResultCode.UserRoleNotExist);
            return j;
        }

        if (userRegisterReq.getPhoneMessageCode().equalsIgnoreCase(cacheContent)) {

            UserDomain userDomain = userRepo.getUserByPhoneNum(userRegisterReq.getPhoneNum());
            if (null == userDomain) {
                userDomain = new UserDomain();
            } else {
                userDomain.setStatusEntity(EntityStatus.statusToPermit);
            }

            userDomain.setStatus(UserStatus.normal);
            userDomain.setName(userRegisterReq.getName());
            BeanUtils.copyProperties(userRegisterReq, userDomain);

            if (PrescriptionRole.doctor.equals(userRegisterReq.getHosRole())) {
                if (null == hospitalInfoRepo.getHospitalById(userRegisterReq.getDoctorDto().getHospitalBelong())) {
                    j.setCodeMsg(APIResultCode.SCHOOL_NOT_EXIST);
                    throw new GlobalException(j);
                }
                DoctorInfo doctorInfo = new DoctorInfo();
                BeanUtils.copyProperties(userRegisterReq.getDoctorDto(), doctorInfo);

                if (StringUtils.isEmpty(userDomain.getId())) {
                    userDomain.setId("doctor" + IdGenerator.genUserId());
                }
                doctorInfo.setDoctorId(userDomain.getId());
                doctorInfo.setDoctorName(userRegisterReq.getName());
                doctorInfoRepo.save(doctorInfo);

                userDomain.setHospitalBelong(userRegisterReq.getDoctorDto().getHospitalBelong());

                userDomain.setPrescriptionRole(PrescriptionRole.doctor);
                userDomain.setStatusEntity(EntityStatus.statusToPermit);


            } else if (PrescriptionRole.hospital.equals(userRegisterReq.getHosRole())) {
                HospitalInfo hos = new HospitalInfo();
                BeanUtils.copyProperties(userRegisterReq.getHospitalDto(), hos);

                if (StringUtils.isEmpty(userDomain.getId())) {
                    userDomain.setId("hospital" + IdGenerator.genUserId());
                }


                hos.setHospitalId(userDomain.getId());

                hos.setHospitallName(userRegisterReq.getName());
                hospitalInfoRepo.save(hos);


                userDomain.setPrescriptionRole(PrescriptionRole.hospital);
                userDomain.setStatusEntity(EntityStatus.statusToPermit);
            } else if (PrescriptionRole.drugstore.equals(userRegisterReq.getHosRole())) {
                DrugstoreInfo di = new DrugstoreInfo();
                BeanUtils.copyProperties(userRegisterReq.getDrugstoreDto(), di);

                if (StringUtils.isEmpty(userDomain.getId())) {
                    userDomain.setId("drugstore" + IdGenerator.genUserId());
                }
                di.setDrugstoreId(userDomain.getId());

                di.setDrugstoreName(userRegisterReq.getName());
                drugstoreInfoRepo.save(di);

                userDomain.setPrescriptionRole(PrescriptionRole.drugstore);
                userDomain.setStatusEntity(EntityStatus.statusToPermit);
            } else if (PrescriptionRole.patient.equals(userRegisterReq.getHosRole())) {
                PatientInfo pa = new PatientInfo();
                BeanUtils.copyProperties(userRegisterReq.getPatientDto(), pa);

                if (StringUtils.isEmpty(userDomain.getId())) {
                    userDomain.setId("patient" + IdGenerator.genUserId());
                }
                pa.setPatientId(userDomain.getId());
                pa.setPatientName(userRegisterReq.getName());
                patientRepo.save(pa);

                userDomain.setPrescriptionRole(PrescriptionRole.patient);
                userDomain.setStatusEntity(EntityStatus.statusPermitted);
            }

            userRepo.save(userDomain);
            j.setCodeMsg(APIResultCode.SUCCESS);
        } else {
            j.setCodeMsg(APIResultCode.CHECK_CODE_NOT_RIGHT);
        }
        return j;
    }

    public JsonDtoWrapper getMyInfo(UserClient userClient) {
        return new JsonDtoWrapper(getUserInfo(userClient.getUserID()), APIResultCode.SUCCESS);
    }

    public JsonDtoWrapper getUserToCheckDetailInfo(GetDoctorInfoReq doctorInfoReq, UserClient userClient){

        UserDomain userDomainManager = userRepo.getUserByUserId(userClient.getUserID());
        if (userDomainManager.getPrescriptionRole().equals(PrescriptionRole.hospital)) {
            DoctorDto doctorDto = new DoctorDto();
            DoctorInfo doctorInfo = doctorInfoRepo.getDoctorInfoById(doctorInfoReq.getUserId());

            HospitalInfo hospitalInfo = hospitalInfoRepo.getHospitalById(doctorInfo.getHospitalBelong());

            if (null == doctorInfo) {
                return new JsonDtoWrapper(null, APIResultCode.USERID_NOT_EXIST);
            }

            if (null == hospitalInfo) {
                return new JsonDtoWrapper(null, APIResultCode.NO_HOSPITAL);
            }

            if (!userClient.getUserID().equals(hospitalInfo.getHospitalId())) {
                return new JsonDtoWrapper(null, APIResultCode.DOCTOR_NOT_BELONG_YOUR_HOSPITAL);
            }
        }else if(!userDomainManager.getPrescriptionRole().equals(PrescriptionRole.supermanager)){
            return new JsonDtoWrapper(null, APIResultCode.NO_PRIVILEGE);
        }

        return new JsonDtoWrapper(getUserInfo(doctorInfoReq.getUserId()), APIResultCode.SUCCESS);
    }
    public UserInfoDto getUserInfo(String userId) {
        UserDomain userDomain = userRepo.getUserByUserId(userId);
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(userDomain, userInfoDto);

        if (PrescriptionRole.doctor.equals(userDomain.getPrescriptionRole())) {
            DoctorDto doctorDto = new DoctorDto();
            DoctorInfo doctorInfo = doctorInfoRepo.getDoctorInfoById(userId);

            HospitalInfo hospitalInfo = hospitalInfoRepo.getHospitalById(doctorInfo.getHospitalBelong());
            doctorDto.setHospitalBelongName(hospitalInfo.getHospitallName());
            BeanUtils.copyProperties(doctorInfo, doctorDto);
            userInfoDto.setDoctorDto(doctorDto);
        } else if (PrescriptionRole.hospital.equals(userDomain.getPrescriptionRole())) {
            HospitalDto hospitalDto = new HospitalDto();
            HospitalInfo hospitalInfo = hospitalInfoRepo.getHospitalById(userId);

            BeanUtils.copyProperties(hospitalInfo, hospitalDto);
            userInfoDto.setHospitalDto(hospitalDto);
        } else if (PrescriptionRole.patient.equals(userDomain.getPrescriptionRole())) {
            PatientDto patientDto = new PatientDto();
            PatientInfo patientInfo = patientRepo.getPatientInfoById(userId);
            BeanUtils.copyProperties(patientInfo, patientDto);
            userInfoDto.setPatientDto(patientDto);
        } else if (PrescriptionRole.drugstore.equals(userDomain.getPrescriptionRole())) {
            DrugstoreDto drugStoreDto = new DrugstoreDto();
            DrugstoreInfo drugstoreInfo = drugstoreInfoRepo.getDrugstoreById(userId);
            BeanUtils.copyProperties(drugstoreInfo, drugStoreDto);
            userInfoDto.setDrugstoreDto(drugStoreDto);
        }

        return userInfoDto;
    }

    public JsonDtoWrapper getCheckTypes(UserClient userClient) {
        JsonDtoWrapper j = new JsonDtoWrapper();
        UserDomain userDomain = userRepo.getUserByUserId(userClient.getUserID());
        List<GetRoleNameTypes> getRoleNameTypesList = new ArrayList<>();
        if (userDomain.getPrescriptionRole().equals(PrescriptionRole.supermanager)) {
            GetRoleNameTypes getRoleNameTypes = new GetRoleNameTypes("drugstore", PrescriptionRole.drugstore);
            getRoleNameTypesList.add(getRoleNameTypes);
            getRoleNameTypes = new GetRoleNameTypes("hospital", PrescriptionRole.hospital);
            getRoleNameTypesList.add(getRoleNameTypes);
        } else if (userDomain.getPrescriptionRole().equals(PrescriptionRole.hospital)) {
            GetRoleNameTypes getRoleNameTypes = new GetRoleNameTypes("doctor", PrescriptionRole.doctor);
            getRoleNameTypesList.add(getRoleNameTypes);
        }
        j.setData(getRoleNameTypesList);
        j.setCodeMsg(APIResultCode.SUCCESS);
        return j;
    }

    public JsonDtoWrapper getUserToCheck(GetToCheckReq getToCheckReq, UserClient userClient) {
        UserDomain userDomain = userRepo.getUserByUserId(userClient.getUserID());
        JsonDtoWrapper j = new JsonDtoWrapper();
        List<UserDomain> userDomainList = new ArrayList<>();
        if (userDomain.getPrescriptionRole().equals(PrescriptionRole.supermanager)) {
            if (PrescriptionRole.drugstore.equals(getToCheckReq.getUserRole())) {
                userDomainList = userRepo.getUserByRoleStatus(PrescriptionRole.drugstore, EntityStatus.statusToPermit);
            } else if (PrescriptionRole.hospital.equals(getToCheckReq.getUserRole())) {
                userDomainList = userRepo.getUserByRoleStatus(PrescriptionRole.hospital, EntityStatus.statusToPermit);
            }
        } else if (userDomain.getPrescriptionRole().equals(PrescriptionRole.hospital)) {
            if (PrescriptionRole.doctor.equals(getToCheckReq.getUserRole())) {
                userDomainList = userRepo.getUserByRoleStatusWithBelong(PrescriptionRole.doctor, EntityStatus.statusToPermit, userClient.getUserID());
            }
        }
        j.setCodeMsg(APIResultCode.SUCCESS);
        j.setData(userDomainList);
        return j;
    }

    public JsonDtoWrapper doCheckSomeOne(DoCheckReq doCheckReq, UserClient userClient) {
        UserDomain userDomain = userRepo.getUserByUserId(doCheckReq.getUserId());
        UserDomain userDomainManager = userRepo.getUserByUserId(userClient.getUserID());

        if (userDomainManager.getPrescriptionRole().equals(PrescriptionRole.supermanager)) {
            if (!PrescriptionRole.drugstore.equals(userDomain.getPrescriptionRole()) &&
                    !PrescriptionRole.hospital.equals(userDomain.getPrescriptionRole())) {
                return new JsonDtoWrapper(null, APIResultCode.NO_PRIVILEGE);
            }
        } else if (userDomainManager.getPrescriptionRole().equals(PrescriptionRole.hospital)) {
            if (!PrescriptionRole.doctor.equals(userDomain.getPrescriptionRole())) {
                return new JsonDtoWrapper(null, APIResultCode.NO_PRIVILEGE);
            }
            DoctorInfo doctorInfo = doctorInfoRepo.getDoctorInfoById(doCheckReq.getUserId());
            if (!doctorInfo.getHospitalBelong().equals(userDomainManager.getId())) {
                return new JsonDtoWrapper(null, APIResultCode.NO_PRIVILEGE);
            }
        }

        userDomain.setStatusEntity(doCheckReq.getEntityStatus());
        userDomain.setNotesFoUserinfoByManager(doCheckReq.getNotes());

        userRepo.save(userDomain);
        return new JsonDtoWrapper(null, APIResultCode.SUCCESS);
    }

    public JsonDtoWrapper searchUserByPhone(SearchUserReq searchUserReq) {
        List<UserDomain> userDomains = userRepo.getUserByPhoneNumArray(searchUserReq.getPhoneNum(), PrescriptionRole.patient);

        List<SearchUserByPhoneRes> searchUserByPhoneResList = new ArrayList<>();
        for (UserDomain userDomain : userDomains) {
            SearchUserByPhoneRes searchUserByPhoneRes = new SearchUserByPhoneRes();
            BeanUtils.copyProperties(userDomain, searchUserByPhoneRes);
            searchUserByPhoneResList.add(searchUserByPhoneRes);
        }
        return new JsonDtoWrapper(searchUserByPhoneResList, APIResultCode.SUCCESS);
    }

    public JsonDtoWrapper searchHospitalByName(SearchHospitalReq searchHospitalReq) {
        List<HospitalInfo> hospitals = hospitalInfoRepo.getSchoolInfoByBlurryName(searchHospitalReq.getSearchByName());
        return new JsonDtoWrapper(hospitals, APIResultCode.SUCCESS);
    }
}
