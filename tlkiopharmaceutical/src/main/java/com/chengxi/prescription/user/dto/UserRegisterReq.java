package com.chengxi.prescription.user.dto;

import lombok.Data;

@Data
public class UserRegisterReq {
    String phoneNum;
    String phoneMessageCode;
    String loginPWD;
    String name;
    String headerPicUrl;
    Integer hosRole;//supermanager = 1;hospital = 2;doctor = 3;drugstore=4;patient=5
    DoctorDto doctorDto;
    HospitalDto hospitalDto;
    DrugstoreDto drugstoreDto;
    PatientDto patientDto;
}
