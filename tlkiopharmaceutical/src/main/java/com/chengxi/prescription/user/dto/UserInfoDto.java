package com.chengxi.prescription.user.dto;

import lombok.Data;

@Data
public class UserInfoDto {
    String id;
    String phoneNum;
    String name;
    String headerPicUrl;
    Integer prescriptionRole;//EduRole:student = 1;school = 2;
    DoctorDto doctorDto;
    HospitalDto hospitalDto;
    DrugstoreDto drugstoreDto;
    PatientDto patientDto;
}
