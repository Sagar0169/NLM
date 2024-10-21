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
    val _result: Result,
    val _resultflag: Int,
    val message: String,


)


data class MyAccountResponse(
    val _result: ResultData,
    val _resultflag: Int,
    val message: String
)

data class ResultData(
    val user_id: Int?,
    val name: String?,
    val username: String?,
    val state_code: String?,
    val district_code: String?,
    val role_id: String?,
    val role_name: String?,
    val token: String?,
    val dob: String?,
    val email: String?,
    val first_login: Int?,
    val status: Boolean,
    val mobile: String?,
    val mobile1: String?,
    val photo_url: String?,


)
data class LoginRes(
    val _result: Result,
    val _resultflag: Int,
    val message: String
)

data class Result(
//    val address: Any,
//    val designation_id: Int,
//    val district_code: Any,
//    val email: String,
//    val first_login: Int,
//    val implementing_agency_id: Any,
//    val mobile: String,
//    val mobile1: String,
    val name: String,
//    val organisation_id: Any,
    val role_id: Int,
    val role_name: String,
    val schemes: List<Scheme>,
    val state_code: Int,
    val state_name: String,
//    val status: Boolean,
    val token: String?,
    val user_id: Int,

//    val username: String
)

data class Scheme(
    val forms: List<Form>,
    val id: Int,
    val name: String
)

data class Form(
    val action: String,
    val controller: String,
    val id: Int,
    val name: String,
    val pid: Int
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

data class ImplementingAgencyResponse(
    val _result: ResultImplementingAgency,
    val _resultflag: Int,
    val fileurl: Any,
    val message: String,
    val statuscode: Int,
    val total_count: Int
)

data class ResultImplementingAgency(
    val data: List<DataImplementingAgency>,
    val is_add: Boolean
)

data class DataImplementingAgency(
    val is_delete: Boolean,
    val is_draft: String,
    val is_edit: Boolean,
    val is_view: Boolean,
    val name_location_of_ai: String,
    val state_name: String
)

data class GetDropDownResponse(
    val _result: List<ResultGetDropDown>,
    val _resultflag: Int,
    val message: String,
    val model: String,
    val statuscode: Int,
    val total_count: Int
)

data class ResultGetDropDown(
    val id: Int,
    val name: String
)
data class ImplementingAgencyResponseNlm(
    val _result: ResultNLMIA,
    val _resultflag: Int,
    val fileurl: Any,
    val message: String,
    val statuscode: Int
)

data class ResultNLMIA(
    val actual_production_location: String,
    val actual_production_number: Int,
    val admn_staff_manpower_deputed: Int,
    val admn_staff_regular_employee: Int,
    val any_assets_created: String,
    val any_other: String,
    val assessments_of_green: String,
    val availability_liquid_nitrogen_location: String,
    val availability_liquid_nitrogen_number: Int,
    val availability_of_common: String,
    val availability_of_concentrate: String,
    val availability_of_dry: String,
    val availability_of_green_area: String,
    val breeding_farms_location: String,
    val breeding_farms_number: Int,
    val created: String,
    val created_by: Int,
    val director_dg_ceo_name: String,
    val distribution_channel: String,
    val efforts_of_state: String,
    val frequency_of_monitoring_1: String,
    val frequency_of_monitoring_2: String,
    val frozen_semen_goat_location: String,
    val frozen_semen_goat_number: Int,
    val funds: String,
    val goat_breeding_farm_location: String,
    val goat_breeding_farm_number: Int,
    val id: Int,
    val implementing_agency_advisory_committee: List<ImplementingAgencyAdvisoryCommitteeAdd>,
    val infrastructural: String,
    val is_deleted: Int,
    val is_draft: Int,
    val liquid_semen_sheep_location: String,
    val liquid_semen_sheep_number: Int,
    val name_location_of_ai: String,
    val name_of_the_agency: String,
    val no_of_al_technicians: Int,
    val no_semen_distributed_location: String,
    val no_semen_distributed_number: Int,
    val no_semen_neighboring_location: String,
    val no_semen_neighboring_number: Any,
    val number_of_ai: Int,
    val number_of_cattle_ai_location: String,
    val number_of_cattle_ai_number: Int,
    val number_of_fodder: String,
    val organizational: String,
    val organizational_chart: String,
    val other_staff_manpower_deputed: Int,
    val other_staff_regular_employee: Int,
    val present_system: String,
    val production_capacity_location: String,
    val production_capacity_number: Int,
    val quantity_of_fodder: String,
    val regularity_1: String,
    val regularity_2: String,
    val reporting_mechanism_1: String,
    val reporting_mechanism_2: String,
    val sheep_breeding_farms_location: String,
    val sheep_breeding_farms_number: Int,
    val state_code: Int,
    val status: Int,
    val studies_surveys_conducted: String,
    val submission_of_quarterly_1: String,
    val submission_of_quarterly_2: String,
    val technical_staff_manpower_deputed: Int,
    val technical_staff_regular_employee: Int,
    val total_ai_performed_location: String,
    val total_ai_performed_number: Int,
    val total_paravet_trained: Int,
    val total_reginal_semen_bank: Int,
    val training_centers_location: String,
    val training_centers_number: Int,
    val updated: Any,
    val user_id: String
)

data class ImplementingAgencyAdvisoryCommitteeAdd(
    val created: String,
    val created_by: Any,
    val designation: String,
    val id: Int,
    val implementing_agency_id: Int,
    val name_of_the_official: String,
    val organization: String,
    val status: Int,
    val updated: Any
)