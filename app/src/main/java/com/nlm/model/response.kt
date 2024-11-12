package com.nlm.model

import java.io.Serializable

data class CommonResponse(
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


data class LogoutResponse(
    val _result: String,
    val _resultflag: Int,
    val message: String,
)

data class DashboardResponse(
    val _result: DashData,
    val _resultflag: Int,
    val statuscode: Int,
    val message: String,
)

data class DashData(
    val no_of_covered_scheme: Int,
    val total_visit: Int,
    val no_of_state_covered: Int,
    val report_submitted_by_nlm: Int,
)


data class StateSemenBankResponse(
    val _result: ResultStateSemen,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class ResultStateSemen(
    val `data`: List<DataSemen>,
    val is_add: Boolean,
    val total_count: Int
)

data class DataSemen(
    val created: String,
    val district_code: Int,
    val district_name: String,
    val id: Int,
    val is_delete: Boolean,
    val is_draft: String,
    val is_edit: Boolean,
    val is_view: Boolean,
    val state_code: Int,
    val state_name: String,
    val updated: Any,
    val year_of_establishment: String
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

    val username: String
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
) : Serializable

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
) : Serializable

data class ImplementingAgencyResponse(
    val _result: ResultImplementingAgency,
    val _resultflag: Int,
    val fileurl: Any,
    val message: String,
    val statuscode: Int,
)


data class ResultImplementingAgency(
    val data: List<DataImplementingAgency>,
    val total_count: Int,
    val is_add: Boolean
)

data class ArtificialInseminationResponse(
    val _result: ResultArtificialInsemination,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)
data class ImportExocticGoatListResponse(
    val _result: ResultIE,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class ResultIE(
    val data: List<DataIE>,
    val is_add: Boolean,
    val total_count: Int
)

data class DataIE(
    val created_at: String?,
    val id: Int?,
    val is_delete: Boolean?,
    val is_draft_ia: String?,
    val is_draft_nlm: String?,
    val created_by: String?,
    val is_edit: Boolean?,
    val is_view: Boolean?,
    val state_code: Int?,
    val state_name: String?,
    val updated_at: String?
)
data class ResultArtificialInsemination(
    val `data`: List<DataArtificialInsemination>,
    val is_add: Boolean,
    val total_count: Int
)

data class DataArtificialInsemination(
    val state_code: Int,
    val state_name: String,
    val district_code: Int,
    val district_name: String,
    val liquid_nitrogen: String,
    val frozen_semen_straws: String,
    val cryocans: String,
    val created: String,
    val id: Int,
    val is_delete: Boolean,
    val is_draft: String,
    val is_draft_ia: String,
    val is_draft_nlm: String,
    val is_edit: Boolean,
    val is_view: Boolean,
)
data class DataImplementingAgency(
    val id: Int,
    val is_delete: Boolean,
    val is_draft: String,
    val is_edit: Boolean,
    val is_view: Boolean,
    val name_location_of_ai: String,
    val state_name: String,
    val created: String
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

data class StateSemenAddResponse(
    val _result: StateSemenAddResult,
    val _resultflag: Int,
    val fileurl: Any,
    val message: String,
    val statuscode: Int
)

data class StateSemenAddResult(
    val address: String,
    val area_fodder_cultivation: String,
    val area_under_buildings: String,
    val created: String,
    val created_by: Int,
    val district_code: Int,
    val district_name: String,
    val id: Int,
    val is_deleted: Int,
    val is_draft: Int,
    val location: String,
    val major_clients_coop_fin_year_one: String,
    val major_clients_coop_fin_year_three: String,
    val major_clients_coop_fin_year_two: String,
    val major_clients_ngo_fin_year_one: String,
    val major_clients_ngo_fin_year_three: String,
    val major_clients_ngo_fin_year_two: String,
    val major_clients_other_states_fin_year_one: String,
    val major_clients_other_states_fin_year_three: String,
    val major_clients_other_states_fin_year_two: String,
    val major_clients_private_fin_year_one: String,
    val major_clients_private_fin_year_three: String,
    val major_clients_private_fin_year_two: String,
    val manpower_no_of_people: Int,
    val officer_in_charge_name: String,
    val phone_no: Long,
    val pin_code: Int,
    val quality_status: String,
    val role_id: Int,
    val state_code: Int,
    val state_semen_bank_document: List<StateSemenBankAddDocument>,
    val state_semen_bank_infrastructure: List<StateSemenInfraGoat>,
    val state_semen_bank_other_manpower: MutableList<StateSemenBankOtherAddManpower>,
    val status: Int,
    val storage_capacity: String,
    val type_of_semen_station: String,
    val user_id: String,
    val year_of_establishment: String
)

data class StateSemenBankAddDocument(
    val description: String,
    val id: Int,
    val state_semen_bank_id: Int
)

data class StateSemenBankAddInfrastructure(
    val id: Int,
    val infrastructure_list_of_equipment: String,
    val infrastructure_year_of_procurement: String,
    val state_semen_bank_id: Int
)


data class NLMIAResponse(
    val _result: ResultIA,
    val _resultflag: Int,
    val fileurl: Any,
    val message: String,
    val statuscode: Int
)

data class ResultIA(
    val actual_production_location: String?,
    val actual_production_number: Int?,
    val admn_staff_manpower_deputed: Int?,
    val admn_staff_regular_employee: Int?,
    val any_assets_created: String?,
    val any_other: String?,
    val assessments_of_green: String?,
    val availability_liquid_nitrogen_location: String?,
    val availability_liquid_nitrogen_number: Int?,
    val availability_of_common: String?,
    val availability_of_concentrate: String?,
    val availability_of_dry: String?,
    val availability_of_green_area: String?,
    val breeding_farms_location: String?,
    val breeding_farms_number: Int?,
    val created: String?,
    val created_by: Int?,
    val director_dg_ceo_name: String?,
    val distribution_channel: String?,
    val efforts_of_state: String?,
    val frequency_of_monitoring_1: String?,
    val frequency_of_monitoring_2: String?,
    val frozen_semen_goat_location: String?,
    val frozen_semen_goat_number: Int?,
    val funds: String?,
    val goat_breeding_farm_location: String?,
    val goat_breeding_farm_number: Int?,
    val id: Int?,
    val implementing_agency_advisory_committee: List<ImplementingAgencyAdvisoryCommittee>?,
    val implementing_agency_document: MutableList<ImplementingAgencyDocument>?,
    val implementing_agency_funds_received: List<ImplementingAgencyFundsReceived>?,
    val implementing_agency_involved_district_wise: List<ImplementingAgencyInvolvedDistrictWise>?,
    val implementing_agency_project_monitoring: List<ImplementingAgencyProjectMonitoring>?,
    val infrastructural: String?,
    val is_deleted: Int?,
    val is_draft: Int?,
    val liquid_semen_sheep_location: String?,
    val liquid_semen_sheep_number: Int?,
    val name_location_of_ai: String?,
    val name_of_the_agency: String?,
    val no_of_al_technicians: Int?,
    val no_semen_distributed_location: String?,
    val no_semen_distributed_number: Int?,
    val no_semen_neighboring_location: String?,
    val no_semen_neighboring_number: Int?,
    val number_of_ai: Int?,
    val number_of_cattle_ai_location: String?,
    val number_of_cattle_ai_number: Int?,
    val number_of_fodder: String?,
    val organizational: String?,
    val organizational_chart: String?,
    val other_staff_manpower_deputed: Int?,
    val other_staff_regular_employee: Int?,
    val present_system: String?,
    val production_capacity_location: String?,
    val production_capacity_number: Int?,
    val quantity_of_fodder: String?,
    val regularity_1: String?,
    val regularity_2: String?,
    val reporting_mechanism_1: String?,
    val reporting_mechanism_2: String?,
    val sheep_breeding_farms_location: String?,
    val sheep_breeding_farms_number: Int?,
    val state_code: Int?,
    val status: Int?,
    val studies_surveys_conducted: String?,
    val submission_of_quarterly_1: String?,
    val submission_of_quarterly_2: String?,
    val technical_staff_manpower_deputed: Int?,
    val technical_staff_regular_employee: Int?,
    val total_ai_performed_location: String?,
    val total_ai_performed_number: Int?,
    val total_paravet_trained: Int?,
    val total_reginal_semen_bank: Int?,
    val training_centers_location: String?,
    val training_centers_number: Int?,
    val updated: Any?
)

//data class ImplementingAgencyAdvisoryCommittee(
//    val designation: String,
//    val id: Int,
//    val implementing_agency_id: Int,
//    val name_of_the_official: String,
//    val organization: String
//)
//
data class ImplementingAgencyDocument(
    val description: String?,
    val ia_document: String?,
    val id: Int?,
    val implementing_agency_id: Int?,
    val nlm_document: String?
)
//
//data class ImplementingAgencyFundsReceived(
//    val any_other: Int,
//    val from_dahd: Int,
//    val id: Int,
//    val implementing_agency_id: Int,
//    val physical_progress: Int,
//    val state_govt: Int,
//    val year: String
//)

//data class ImplementingAgencyInvolvedDistrictWise(
//    val ai_performed: String,
//    val id: Int,
//    val implementing_agency_id: Int,
//    val location_of_ai_centre: String,
//    val name_of_district: String,
//    val year: Any
//)
//
//data class ImplementingAgencyProjectMonitoring(
//    val designation: String,
//    val id: Int,
//    val implementing_agency_id: Int,
//    val name_of_official: String,
//    val organization: String
//)
data class ImplementingAgencyResponseNlm(
    val _result: ResultNLMIA,
    val _resultflag: Int,
    val fileurl: Any,
    val message: String,
    val statuscode: Int
)
data class UploadDocument_Response(
    val _result: Result_Doc,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)
data class ArtificialInsemenationAddResponse(
    val _result: ResultArtificialInsemenation,
    val _resultflag: Int,
    val fileurl: Any,
    val message: String,
    val statuscode: Int
)

data class ResultArtificialInsemenation(
    val ai_centre_cattle_buffalo: Int,
    val ai_centre_goat_frozen_semen: Int,
    val ai_performed_average: Double,
    val artificial_insemination_document: List<ArtificialInseminationDocumentResponse>,
    val artificial_insemination_observation_by_nlm: List<ArtificialInseminationObservationByNlmResponse>,
    val created: String,
    val created_by: Int,
    val cryocans: String,
    val district_code: Int,
    val equipments_centers_provided: Int,
    val exotic_sheep_goat: String,
    val frozen_semen_straws: String,
    val id: Int,
    val is_deleted: Int,
    val is_draft_nlm: Int,
    val liquid_nitrogen: String,
    val percentage_of_conception_rates: Double,
    val role_id: Int,
    val state_code: Int?,
    val status: Int?,
    val total_sheep_goat_labs: Int?,
    val user_id: String
)
data class ImportExoticGoatAddEditResponse(
    val _result: ResultIEG,
    val _resultflag: Int,
    val fileurl: Any,
    val message: String,
    val statuscode: Int
)

data class ResultIEG(
    val comment_by_nlm_whether: Int,
    val created_by: Int,
    val id: Int,
    val import_of_exotic_goat_achievement: List<ImportOfExoticGoatAchievement>?,
    val import_of_exotic_goat_detail_import: List<ImportOfExoticGoatDetailImport>?,
    val import_of_exotic_goat_verified_nlm: List<ImportOfExoticGoatVerifiedNlm>?
)

data class ArtificialInseminationDocumentResponse(
    val artificial_insemination_id: Int,
    val description: String,
    val id: Int
)

data class ArtificialInseminationObservationByNlmResponse(
    val name_of_center: String,
    val number_of_ai_performed: Int,
    val power_trained_ai: String,
    val quipment_available: String
)

data class Result_Doc(
    val created_at: String,
    val description: String,
    val ia_document: String,
    val id: Int,
    val implementing_agency_id: Int,
    val nlm_document: Any
)
data class ResultNLMIA(
    val actual_production_location: String?,
    val actual_production_number: Int?,
    val admn_staff_manpower_deputed: Int?,
    val admn_staff_regular_employee: Int?,
    val any_assets_created: String?,
    val any_other: String?,
    val assessments_of_green: String?,
    val availability_liquid_nitrogen_location: String?,
    val availability_liquid_nitrogen_number: Int?,
    val availability_of_common: String?,
    val availability_of_concentrate: String?,
    val availability_of_dry: String?,
    val availability_of_green_area: String?,
    val breeding_farms_location: String?,
    val breeding_farms_number: Int?,
    val created: String?,
    val created_by: Int?,
    val director_dg_ceo_name: String?,
    val distribution_channel: String?,
    val efforts_of_state: String?,
    val frequency_of_monitoring_1: String?,
    val frequency_of_monitoring_2: String?,
    val frozen_semen_goat_location: String?,
    val frozen_semen_goat_number: Int?,
    val funds: String?,
    val goat_breeding_farm_location: String?,
    val goat_breeding_farm_number: Int?,
    val id: Int?,
    val implementing_agency_advisory_committee: List<ImplementingAgencyAdvisoryCommittee>?,
    val implementing_agency_document: List<ImplementingAgencyDocument>?,
    val implementing_agency_involved_district_wise: List<ImplementingAgencyInvolvedDistrictWise>?,
    val implementing_agency_project_monitoring: List<ImplementingAgencyProjectMonitoring>?,
    val implementing_agency_funds_received: List<ImplementingAgencyFundsReceived>?,
    val infrastructural: String?,
    val is_deleted: Int?,
    val is_draft: Int?,
    val liquid_semen_sheep_location: String?,
    val liquid_semen_sheep_number: Int?,
    val name_location_of_ai: String?,
    val name_of_the_agency: String?,
    val no_of_al_technicians: Int?,
    val no_semen_distributed_location: String?,
    val no_semen_distributed_number: Int?,
    val no_semen_neighboring_location: String?,
    val no_semen_neighboring_number: Any?,
    val number_of_ai: Int?,
    val number_of_cattle_ai_location: String?,
    val number_of_cattle_ai_number: Int?,
    val number_of_fodder: String?,
    val organizational: String?,
    val organizational_chart: String?,
    val other_staff_manpower_deputed: Int?,
    val other_staff_regular_employee: Int?,
    val present_system: String?,
    val production_capacity_location: String?,
    val production_capacity_number: Int?,
    val quantity_of_fodder: String?,
    val regularity_1: String?,
    val regularity_2: String?,
    val reporting_mechanism_1: String?,
    val reporting_mechanism_2: String?,
    val sheep_breeding_farms_location: String?,
    val sheep_breeding_farms_number: Int?,
    val state_code: Int?,
    val status: Int?,
    val studies_surveys_conducted: String?,
    val submission_of_quarterly_1: String?,
    val submission_of_quarterly_2: String?,
    val technical_staff_manpower_deputed: Int?,
    val technical_staff_regular_employee: Int?,
    val total_ai_performed_location: String?,
    val total_ai_performed_number: Int?,
    val total_paravet_trained: Int?,
    val total_reginal_semen_bank: Int?,
    val training_centers_location: String?,
    val training_centers_number: Int?,
    val updated: Any?,
    val user_id: String?
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

data class RSPLabListResponse(
    val _result: RSPLabListResult,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class RSPLabListResult(
    val data: List<RSPLabListData>,
    val is_add: Boolean,
    val total_count: Int
)

data class RSPLabListData(
    val created_at: String,
    val district_code: Int,
    val district_name: String,
    val id: Int,
    val is_delete: Boolean,
    val is_draft_ia: String,
    val is_draft_nlm: String,
    val is_edit: Boolean,
    val is_view: Boolean,
    val phone_no: Long,
    val state_code: Int,
    val state_name: String,
    val updated_at: String,
    val year_of_establishment: String
)

data class NlmAssistanceForQFSPListResponse(
    val _result: NlmAssistanceForQFSPResult,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class NlmAssistanceForQFSPResult(
    val data: List<NlmAssistanceForQFSPData>,
    val is_add: Boolean,
    val total_count: Int
)

data class NlmAssistanceForQFSPData(
    val created: String,
    val created_by: String,
    val district_code: Int,
    val district_name: String,
    val id: Int,
    val is_delete: Boolean,
    val is_draft: String,
    val is_edit: Boolean,
    val is_view: Boolean,
    val name_of_organization: String,
    val organogram: String,
    val state_code: Int,
    val state_name: String,
    val technical_competance: String,
    val updated: String
)