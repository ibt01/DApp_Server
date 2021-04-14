package com.chengxi.prescription.user.constant;

public class PrescriptionRole {
    //supermanager = 1;hospital = 2;doctor = 3;drugstore=4;patient=5
    public static final Integer supermanager = 1;
    public static final Integer hospital = 2;
    public static final Integer doctor = 3;
    public static final Integer drugstore = 4;
    public static final Integer patient = 5;

    public static boolean isGoodRole(Integer role) {
        if (role.equals(supermanager) ||
                role.equals(hospital) ||
                role.equals(doctor) ||
                role.equals(drugstore) ||
                role.equals(patient)) {
            return true;
        }

        return false;
    }
}
