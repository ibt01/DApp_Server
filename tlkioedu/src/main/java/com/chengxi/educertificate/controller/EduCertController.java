package com.chengxi.educertificate.controller;

import com.chengxi.educertificate.dto.CreateCertificateDto;
import com.chengxi.educertificate.dto.GetEduCertificateReq;
import com.chengxi.educertificate.dto.GetSomeOneEduCertiReq;
import com.chengxi.educertificate.dto.SearchStudentReq;
import com.chengxi.educertificate.service.CertificateService;
import com.go.basetool.bean.UserClient;
import com.go.basetool.threadstatus.AbstractController;
import com.go.basetool.utils.JsonDtoWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("educert")
public class EduCertController extends AbstractController {

    @Autowired
    CertificateService certificateService;

    @PostMapping("/createEduCertificate")
    @ResponseBody
    public JsonDtoWrapper createEduCertificate(@RequestBody CreateCertificateDto createCertificateDto) {
        return certificateService.createEduCertificate(createCertificateDto, getLoginUser());
    }

    @PostMapping("/getSomeOneEduCertificates")
    @ResponseBody
    public JsonDtoWrapper getMyEduCertificate(@RequestBody GetSomeOneEduCertiReq getSomeOneEduCertiReq) {
        return certificateService.getMyEduCertificate(getSomeOneEduCertiReq);
    }

    @PostMapping("/getEduCertificate")
    @ResponseBody
    public JsonDtoWrapper getEduCertificate(@RequestBody GetEduCertificateReq getEduCertificateReq){
        return certificateService.getEduCertificate(getEduCertificateReq);
    }

    @PostMapping("/searchStudent")
    @ResponseBody
    public JsonDtoWrapper searchStudent(@RequestBody SearchStudentReq searchStudentReq){
        return certificateService.searchStudent(searchStudentReq, getLoginUser());
    }
}
