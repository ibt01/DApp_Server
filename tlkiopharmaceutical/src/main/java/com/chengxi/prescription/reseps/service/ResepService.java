package com.chengxi.prescription.reseps.service;

import com.chengxi.cache.service.RedisClient;
import com.chengxi.prescription.reseps.constant.ResepStatus;
import com.chengxi.prescription.reseps.domain.ResepDomain;
import com.chengxi.prescription.reseps.dto.*;
import com.chengxi.prescription.reseps.repo.ResepRepo;
import com.chengxi.prescription.user.constant.EntityStatus;
import com.chengxi.prescription.user.constant.PrescriptionRole;
import com.chengxi.prescription.user.domain.DoctorInfo;
import com.chengxi.prescription.user.domain.UserDomain;
import com.chengxi.prescription.user.repo.DoctorInfoRepo;
import com.chengxi.prescription.user.repo.UserRepo;
import com.go.basetool.APIResultCode;
import com.go.basetool.bean.UserClient;
import com.go.basetool.commonreq.PageReq;
import com.go.basetool.exception.GlobalException;
import com.go.basetool.idgenerator.IdGenerator;
import com.go.basetool.utils.JsonDtoWrapper;
import com.google.gson.Gson;
import com.yinni.tlk.olsapi.PushTxnResponse;
import com.yinni.tlk.service.OLSToolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.print.DocFlavor;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.jar.JarEntry;

@Slf4j
@Service
public class ResepService {
    @Autowired
    ResepRepo resepRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    DoctorInfoRepo doctorInfoRepo;

    @Autowired
    RedisClient redis;


    public JsonDtoWrapper createResep(CreateRespReq createRespReq, UserClient userClient) {
        UserDomain doctorDomain = userRepo.getUserByUserId(userClient.getUserID());

        if (!PrescriptionRole.doctor.equals(doctorDomain.getPrescriptionRole())) {
            return new JsonDtoWrapper(null, APIResultCode.ONLY_DOCTOR_CAN_CREATE_RESEP);
        }


        UserDomain patientDomain = userRepo.getUserByUserId(createRespReq.getPatientId());
        if (null == patientDomain) {
            return new JsonDtoWrapper(null, APIResultCode.PATIENT_NOT_EXIST);
        }

        DoctorInfo doctorInfo = doctorInfoRepo.getDoctorInfoById(userClient.getUserID());

        ResepDomain resepDomain = new ResepDomain();

        resepDomain.setNote(createRespReq.getNote());

        resepDomain.setPatientId(createRespReq.getPatientId());
        resepDomain.setPatientName(patientDomain.getName());
        resepDomain.setPatientPhoneNum(patientDomain.getPhoneNum());

        resepDomain.setPictureUrl(createRespReq.getPictureUrl());
        resepDomain.setHospitalId(doctorInfo.getHospitalBelong());


        resepDomain.setStatus(ResepStatus.statusToGet);


        resepRepo.save(resepDomain);


        resepDomain.setDoctorName(doctorInfo.getDoctorName());
        resepDomain.setDoctorId(doctorDomain.getId());
        resepDomain.setDoctorPhoneNum(doctorDomain.getPhoneNum());
        resepDomain.setDoctorResepOnChainTxid(doctorResepOnChain(resepDomain));

        resepDomain.setMedicalJsonBase64(new String(Base64.getEncoder().encode(new Gson().toJson(createRespReq.getMedicalItemList()).getBytes())));

        resepRepo.save(resepDomain);

        return new JsonDtoWrapper(null, APIResultCode.SUCCESS);
    }

    public JsonDtoWrapper getMyReseps(GetMyResepReq pageReq, UserClient userClient) {
        UserDomain userDomain = userRepo.getUserByUserId(userClient.getUserID());

        List<ResepDomain> resepDomains = new ArrayList<>();
        Integer totalLines = 0;
        if (StringUtils.isEmpty(pageReq.getId())) {
            if (PrescriptionRole.doctor.equals(userDomain.getPrescriptionRole())) {
                resepDomains = resepRepo.getDoctorsReseps(userClient.getUserID(), PageReq.of(pageReq.getPageReq()));
                totalLines = resepRepo.getDoctorsResepsCounts(userClient.getUserID());
            }

            if (PrescriptionRole.patient.equals(userDomain.getPrescriptionRole())) {
                resepDomains = resepRepo.getPatientsReseps(userClient.getUserID(), PageReq.of(pageReq.getPageReq()));
                totalLines = resepRepo.getPatientsResepsCounts(userClient.getUserID());
            }

            if (PrescriptionRole.hospital.equals(userDomain.getPrescriptionRole())) {
                resepDomains = resepRepo.getHospitalsReseps(userClient.getUserID(), PageReq.of(pageReq.getPageReq()));
                totalLines = resepRepo.getHospitalsResepsCounts(userClient.getUserID());
            }

            if (PrescriptionRole.drugstore.equals(userDomain.getPrescriptionRole())) {
                resepDomains = resepRepo.getDrugsReseps(userClient.getUserID(), PageReq.of(pageReq.getPageReq()));
                totalLines = resepRepo.getDrugsResepsCounts(userClient.getUserID());
            }
        }else{
            ResepDomain resepDomainItem = resepRepo.getResepById(pageReq.getId());
            if (null != resepDomainItem){
                resepDomains.add(resepDomainItem);
                totalLines = 1;
            }else{
                totalLines = 0;
            }

        }

        GetResepRes getResepRes = new GetResepRes();
        getResepRes.setResepDomains(resepDomains);
        getResepRes.setTotalLines(totalLines);

        return new JsonDtoWrapper(getResepRes, APIResultCode.SUCCESS);
    }

    public JsonDtoWrapper getResepById(GetMyResepReq getMyResepReq){
        ResepDomain resepDomain = resepRepo.getResepById(getMyResepReq.getId());
        return new JsonDtoWrapper(resepDomain, APIResultCode.SUCCESS);
    }


    public static String resetQrPrefix = "resetPrivilege_";

    public String saveResep(Long resetId) {
        String key = IdGenerator.genOrderId();
        redis.set(resetQrPrefix + key, ""+resetId);
        return key;
    }

    public String getResetIdFromQr(String qr) {
        String resetsCache = redis.get(resetQrPrefix + qr);
        return resetsCache;
    }

    public JsonDtoWrapper showMyResepsQr(ShowMyResepReq showMyResepReq, UserClient userClient) {
        ResepDomain resepDomain = resepRepo.getResepById(showMyResepReq.getResepId(), userClient.getUserID());
        if (null == resepDomain) {
            return new JsonDtoWrapper(null, APIResultCode.NO_PRIVILEGE);
        }

        if (ResepStatus.statusTakened.equals(resepDomain.getStatus())) {
            return new JsonDtoWrapper(null, APIResultCode.RESET_GETED);
        }

        ShowMyResepQrDto showMyResepQrDto = new ShowMyResepQrDto();
        showMyResepQrDto.setResepsQr(saveResep(resepDomain.getId()));
        return new JsonDtoWrapper(showMyResepQrDto, APIResultCode.SUCCESS);
    }

    public JsonDtoWrapper getResetByQr(ShowMyResepQrDto showMyResepQrDto, UserClient userClient) {
        String resetId = getResetIdFromQr(showMyResepQrDto.getResepsQr());
        if (StringUtils.isEmpty(resetId)) {
            return new JsonDtoWrapper(null, APIResultCode.NO_QR_RESEP);
        }

        UserDomain drugStore = userRepo.getUserByUserId(userClient.getUserID());
        if (!PrescriptionRole.drugstore.equals(drugStore.getPrescriptionRole())) {
            return new JsonDtoWrapper(null, APIResultCode.YOU_ARE_NOT_DRUG_STORE);
        }

        ResepDomain resepDomain = resepRepo.getResepById(Long.parseLong(resetId));
        if (null == resepDomain) {
            return new JsonDtoWrapper(null, APIResultCode.NO_QR_RESEP);
        }

//        if (ResepStatus.statusTakened.equals(resepDomain.getStatus())) {
//            return new JsonDtoWrapper(null, APIResultCode.RESET_GETED);
//        }

        return new JsonDtoWrapper(resepDomain, APIResultCode.SUCCESS);
    }

    public JsonDtoWrapper dealResetByQr(ShowMyResepQrDto showMyResepQrDto, UserClient userClient) {
        String resetId = getResetIdFromQr(showMyResepQrDto.getResepsQr());

        UserDomain drugStore = userRepo.getUserByUserId(userClient.getUserID());
        if (!PrescriptionRole.drugstore.equals(drugStore.getPrescriptionRole())) {
            return new JsonDtoWrapper(null, APIResultCode.YOU_ARE_NOT_DRUG_STORE);
        }

        if (StringUtils.isEmpty(resetId)) {
            return new JsonDtoWrapper(null, APIResultCode.NO_QR_RESEP);
        }
        ResepDomain resepDomain = resepRepo.getResepById(Long.parseLong(resetId));
        if (null == resepDomain) {
            return new JsonDtoWrapper(null, APIResultCode.NO_QR_RESEP);
        }

        if (ResepStatus.statusTakened.equals(resepDomain.getStatus())) {
            return new JsonDtoWrapper(null, APIResultCode.RESET_GETED);
        }

        resepDomain.setStatus(ResepStatus.statusTakened);
        resepDomain.setDrugStoreId(userClient.getUserID());
        resepDomain.setDrugStorePhoneNum(drugStore.getPhoneNum());
        resepDomain.setDrugStoreResepOnChainTxid(drugStoreResepOnChain(resepDomain));
        resepRepo.save(resepDomain);
        return new JsonDtoWrapper(null, APIResultCode.SUCCESS);
    }

    public String doctorResepOnChain(ResepDomain resepDomain) {
        ResepOnChainDto resepOnChainDto = new ResepOnChainDto();
        BeanUtils.copyProperties(resepDomain, resepOnChainDto);

        CreateResepReqToChain createResepReqToChain = new CreateResepReqToChain();
        createResepReqToChain.setId(resepDomain.getId());
        createResepReqToChain.setPrescr(Base64.getEncoder().encodeToString(new Gson().toJson(resepOnChainDto).getBytes(StandardCharsets.UTF_8)));
        PushTxnResponse pushTxnResponse = OLSToolService.getInstance().pushAction(contractName, "saveprescr", new Gson().toJson(createResepReqToChain), OLSToolService.getActivePermission(contractName), OLSToolService.baseUrl, OLSToolService.resepstlkKey);

        if (StringUtils.isEmpty(pushTxnResponse.getTransaction_id())) {
            throw new GlobalException(APIResultCode.SAVE_INFO_TO_CHAIN_FAILURE);
        }
        return pushTxnResponse.getTransaction_id();
    }

    public static String contractName = "prescription";

    public String drugStoreResepOnChain(ResepDomain resepDomain) {
        ResepOnChainDto resepOnChainDto = new ResepOnChainDto();
        BeanUtils.copyProperties(resepDomain, resepOnChainDto);

        CreateResepReqToChain createResepReqToChain = new CreateResepReqToChain();
        createResepReqToChain.setId(resepDomain.getId());
        PushTxnResponse pushTxnResponse = OLSToolService.getInstance().pushAction(contractName, "useprescr", new Gson().toJson(createResepReqToChain), OLSToolService.getActivePermission(contractName), OLSToolService.baseUrl, OLSToolService.resepstlkKey);

        if (StringUtils.isEmpty(pushTxnResponse.getTransaction_id())) {
            throw new GlobalException(APIResultCode.SAVE_INFO_TO_CHAIN_FAILURE);
        }
        return pushTxnResponse.getTransaction_id();
    }



}
