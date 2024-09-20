package com.nlm.model

import java.io.Serializable

data class  CommonResponse(
    val statuscode: Int,
    val _result: Any,
    val _resultflag: Int,
    val message: String
)

data class OTPResponse(
    val _resultflag: Int,
    val message: String,
    val otp: Int
)
data class LoginResponse(
    val _result: ResultData,
    val _resultflag: Int,
    val message: String
)




data class Result(
    val data: Data,
    val token: String
)
data class MyAccountResponse(
    val _result: ResultData,
    val _resultflag: Int,
    val message: String
)

data class ResultData(
    val userId: Int,
    val usertype: String,
    val name: String,
    val token: String,
    val aadhaar_no: String,
    val application_number: String,
    val certificate_generate_date: String,
    val current_address: String,
    val current_pincode: Int,
    val disability_type_id: String,
    val disability_type_pt: Any,
    val disability_types: String,
    val district: District,
    val dob: String,
    val email: String,
    val father_name: String,
    val final_disability_percentage: Int,
    val full_name: String,
    val full_name_i18n: String,
    val gender: String,
    val hospital: Hospital,
    val hospital_district: HospitalDistrict,
    val hospital_state: HospitalState,
    val mobile: Long,
    val photo: Any,
    val photo_path: String,
    val pwd_card_expiry_date: Any,
    val pwdapplicationstatus: PwdApplicationStatus,
    val state: State,
    val subdistrict: Subdistrict,
    val udid_number: String
)

data class District(
    val district_name: String
)

data class AppLoginResponse(
    val _resultflag: Int,
    val message: String,
    val result: OtpResult
)

data class OtpResult(
    val application_number: String,
    val oto: Int
)

data class Hospital(
    val hospital_name: Any
)

data class HospitalDistrict(
    val district_name: Any
)

data class HospitalState(
    val name: Any
)

data class State(
    val name: String
)

data class Subdistrict(
    val subdistrict_name: String
)
data class Data(
    val application_number: String,
    val current_address: String,
    val current_district_code: Int,
    val current_pincode: Int,
    val current_state_code: Int,
    val current_subdistrict_code: Int,
    val current_village_code: Int,
    val disability_type_id: String,
    val disability_type_pt: Any,
    val dob: String,
    val email: String,
    val father_name: String,
    val full_name: String,
    val full_name_i18n: String,
    val gender: String,
    val id: Int,
    val mobile: Long,
    val photo: Any,
    val pwd_card_expiry_date: Any,
    val pwdapplicationstatus: PwdApplicationStatus,
    val regional_language: Any,
    val udid_number: String
)

data class PwdApplicationStatus(
    val status_name: String
):Serializable

data class ApplicationStatusResponse(
    val _resultflag: Int,
    val applicationstatus: List<ApplicationStatus>,
    val message: String,
    val statusArray: List<StatusArray>
)

data class ApplicationStatus(
    val Pwdapplicationstatus: PwdApplicationStatus,
    val pwd_application_number: String,
    val pwd_application_status: Int,
    val remark: Any
)

data class StatusArray(
    val id: Int,
    val status_name: String
): Serializable
