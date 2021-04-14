package com.go.basetool;

import java.util.HashMap;
import java.util.Map;

public enum APIResultCode {

    SUCCESS("0", "sukses", "success"),
    FAILURE("1", "gagal", "failure"),
    NOT_ONLINE("2", "tidak online", "NOT_ONLINE"),
    UNKNOWN_ERROR("3", "kesalahan yang tidak diketahui", "UNKNOWN_ERROR"),
    PHONE_NOT_EXIST("4", "Nomor ponsel saat ini belum terdaftar", "PHONE_NOT_EXIST"),
    PASSWORD_ERROR("5", "kata sandi salah", "PASSWORD_ERROR"),
    PHONE_NUM_REGISTERED("6", "Nomor ponsel saat ini telah terdaftar", "PHONE_NUM_REGISTERED"),
    CHECK_CODE_NOT_RIGHT("7", "Kode verifikasi SMS sudah tidak berlaku atau salah", "CHECK_CODE_NOT_RIGHT"),
    NO_PRIVILEGE("8", "Tidak berwenang melakukan pengoperasian ini", "NO_PRIVILEGE"),
    NOT_LOGIN("9", "belum login", "NOT_LOGIN"),
    USERID_NOT_EXIST("10", "USERID_NOT_EXIST", "USERID_NOT_EXIST"),
    USER_FROZENED("11", "Pengguna dibekukan, harap hubungi administrator perusahaan", "USER_FROZENED"),
    UserRoleNotExist("12", "UserRoleNotExist", "UserRoleNotExist"),

    SCHOOL_NOT_EXIST("13", "Tidak ada sekolah seperti itu", "SCHOOL_NOT_EXIST"),
    FILE_NULL("14", "FILE_NULL", "FILE_NULL"),
    STUDENT_NOT_EXIST("15", "Siswa ini tidak ada", "STUDENT_NOT_EXIST"),
    STUDENT_NAME_NOTRIGHT("16", "Nama siswa salah", "STUDENT_NAME_NOTRIGHT"),
    SCHOOL_CODE_ALREADY_EXIST("17", "Kode sekolah sudah ada dan tidak bisa didaftarkan lagi", "SCHOOL_CODE_ALREADY_EXIST"),
    STUDENT_CODE_ALREADY_EXIST("18", "Kode siswa sudah ada dan tidak dapat didaftarkan lagi", "STUDENT_CODE_ALREADY_EXIST"),
    NOT_STUDENT("19", "Anda bukan siswa", "YOU ARE NOT STUDENT"),
    USER_INFO_BEYOND_1024_BYTES("20", "Panjang informasi pengguna melebihi 1024 byte", "USER_INFO_BEYOND_1024_BYTES"),
    e("", "kosong", "em"),


    PATIENT_NOT_EXIST("10000", "Pasien tidak ada", "PATIENT_NOT_EXIST"),
    RESET_GETED("10001", "Daftar obat sudah diambil dan tidak bisa diambil berulang kali", "RESEP_GETED"),
    NO_QR_RESEP("10002", "Tidak ada resep yang sesuai untuk kode QR ini", "NO_QR_RESEP"),
    YOU_ARE_NOT_DRUG_STORE("10003", "Anda bukan apotek dan resep tidak dapat dioperasikan", "YOU_ARE_NOT_DRUG_STORE"),
    SAVE_INFO_TO_CHAIN_FAILURE("10004", "Gagal menyimpan data ke blockchain", "SAVE_INFO_TO_CHAIN_FAILURE"),
    ONLY_DOCTOR_CAN_CREATE_RESEP("10005", "Hanya dokter yang dapat membuat daftar obat", "ONLY_DOCTOR_CAN_CREATE_RESEP"),
    REJECTED_FOR_INFOMATION_SUBMITTED_PLEASE_EDIT_AND_RESUBMIT("10006", "Informasi pengiriman ditolak, harap edit dan kirim ulang", "The submission information is rejected, please edit and resubmit"),
    WAIT_FOR_PERMITTER("10007", "Harap tunggu, informasi akun Anda sedang ditinjau", "Please wait, your account information is pending review"),
    NO_HOSPITAL("10008", "hospital not exist", "NO_HOSPITAL"),
    DOCTOR_NOT_BELONG_YOUR_HOSPITAL("10009", "doctor not belond your hospital", "DOCTOR_NOT_BELONG_YOUR_HOSPITAL"),
    ;

    private static final Map<String, APIResultCode> interToEnum = new HashMap<String, APIResultCode>();

    static {
        for (APIResultCode type : APIResultCode.values()) {
            interToEnum.put(type.getCode(), type);
        }
    }

    private String result;
    private String message;
    private String englishMessage;

    private APIResultCode(String code, String message, String englishMessage) {
        this.result = code;
        this.message = message;
        this.englishMessage = englishMessage;
    }

    public static APIResultCode fromCode(String code) {
        return interToEnum.get(code);
    }

    public String getCode() {
        return result;
    }

    public void setCode(String code) {
        this.result = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEnglishMessage() {
        return englishMessage;
    }

    public void setEnglishMessage(String englishMessage) {
        this.englishMessage = englishMessage;
    }
}
