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
    val no_of_covered_scheme: Int?,
    val total_visit: Int?,
    val no_of_state_covered: Int?,
    val report_submitted_by_nlm: Int?,
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

    val username: String,
    val siteurl: String,
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

data class Format6AssistanceForQspAddResponse(
    val _result: ResultFormat6,
    val _resultflag: Int,
    val fileurl: String?,
    val message: String,
    val statuscode: Int
)

data class ResultFormat6(
    val area_under_production: Int? = null,
    val assistance_for_qfsp_cost_assistance: List<AssistanceForQfspCostAssistance>? = null,
    val assistance_for_qfsp_document: ArrayList<ImplementingAgencyDocument>? = null,
    val assistance_for_qfsp_financial_progress: List<AssistanceForQfspFinancialProgres>? = null,
    val created: String? = null,
    val created_by: Int? = null,
    val district_code: Int? = null,
    val effective_seed: Any? = null,
    val effective_seed_inputs: String? = null,
    val id: Int? = null,
    val is_deleted: Int? = null,
    val is_draft: Int? = null,
    val location_address: String? = null,
    val name_of_organization: String? = null,
    val organogram: String? = null,
    val quantity_of_fodder_seed_class: String? = null,
    val quantity_of_fodder_seed_variety: String? = null,
    val quantity_of_seed_class: String? = null,
    val quantity_of_seed_variety: String? = null,
    val source_of_seed: String? = null,
    val district_name: String? = null,
    val state_code: Int? = null,
    val status: Int? = null,
    val target_achievement_class: String? = null,
    val target_achievement_variety: String? = null,
    val technical_competance: String? = null,
    val updated: String? = null
)

data class AssistanceForQfspCostAssistance(
    val assistance_for_qfsp_id: Int? = null,
    val cost_assistance_first_year: String? = null,
    val cost_assistance_second_year: Any? = null,
    val cost_assistance_third_year: Any? = null,
    val created: String? = null,
    val created_by: Any? = null,
    val id: Int? = null,
    val name_of_fodder_seed: String? = null,
    val seed_produced_first_year: String? = null,
    val seed_produced_second_year: Any? = null,
    val seed_produced_third_year: Any? = null,
    val status: Any? = null,
    val updated: Any? = null
)

data class AssistanceForQfspFinancialProgres(
    val amount_utilized_second: Any? = null,
    val amount_utilized_state_first: Int? = null,
    val area_covered_first: Int? = null,
    val area_covered_second: Any? = null,
    val assistance_for_qfsp_id: Int? = null,
    val assistance_provided_first: String? = null,
    val assistance_provided_second: Any? = null,
    val assistance_provided_third: Any? = null,
    val created: String? = null,
    val district: District_list? = null,
    val district_code: Int? = null,
    val created_by: Any? = null,
    val farmers_impacted_first: String? = null,
    val farmers_impacted_second: Any? = null,
    val financial_year_first: Any? = null,
    val financial_year_second: Any? = null,
    val financial_year_third: Any? = null,
    val id: Int? = null,
    val name_of_district: String? = null,
    val status: Any? = null,
    val updated: String? = null
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

data class SubTableDeleteResponse(
    val `data`: SubTableDeleteData,
    val message: String,
    val status: Int
)

data class SubTableDeleteData(
    val dairy_plant_visit_report_id: Int,
    val id: Int,
    val major_equipments_milk_products: String,
    val npdd_scheme_available: Int,
    val npdd_scheme_major_lab_available: Any,
    val npdd_scheme_major_lab_no: Any,
    val npdd_scheme_major_lab_present_status: Any,
    val npdd_scheme_major_lab_remarks: Any,
    val npdd_scheme_major_lab_strengthening: Any,
    val npdd_scheme_present_status: Int,
    val npdd_scheme_proj_no: Int,
    val npdd_scheme_remarks: String
)

data class ResultGetDropDown(
    val id: Int,
    val name: String
)

data class StateSemenAddResponse(
    val _result: StateSemenAddResult,
    val _resultflag: Int,
    val fileurl: String?,
    val message: String,
    val statuscode: Int
)

data class StateSemenAddResult(
    val address: String,
    val area_fodder_cultivation: String,
    val area_under_buildings: String,
    val created: String,
    val created_by: Int,
    val district_code: Int? = null,
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
    val state_semen_bank_document: List<ImplementingAgencyDocument>,
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
    val fileurl: String,
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
    val description: String? = null,
    val ia_document: String? = null,
    val id: Int? = null,
    val implementing_agency_id: Int? = null,
    val rsp_laboratory_semen_id: Int? = null,
    val state_semen_bank_id: Int? = null,
    val artificial_insemination_id: Int? = null,
    val import_of_exotic_goat_id: Int? = null,
    val assistance_for_qfsp_id: Int? = null,
    val fsp_plant_storage_id: Int? = null,
    val nlm_edp_id: Int? = null,
    val ahidf_id: Int? = null,
    val assistance_for_ea_id: Int? = null,
    val nlm_document: String? = null,
    val fp_from_non_forest_id: Int? = null,
    val dairy_plant_visit_report_id: Int? = null,
    val rgm_implementing_agency_id: Int? = null,
    val nlm_b_component_id: Int?=null,
    val is_edit: Boolean?=null,
    val is_ia:Boolean?=null,
    val onsite_dcs_center_visit_id: Int?=null,
    val state_central_lab_visit_id: Int?=null,
    val milk_processing_id: Int?=null,
    val productivity_enhancement_services_id: Int?=null,
    val milk_product_marketing_id: Int?=null,
    val milk_union_visit_report_id: Int?=null


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
    val fileurl: String?,
    val message: String,
    val statuscode: Int
)

data class ResultArtificialInsemenation(
    val ai_centre_cattle_buffalo: Int,
    val ai_centre_goat_frozen_semen: Int,
    val ai_performed_average: Double,
    val artificial_insemination_document: ArrayList<ImplementingAgencyDocument>,
    val artificial_insemination_observation_by_nlm: List<ArtificialInseminationObservationByNlm>,
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
    val user_id: String,
    val state_name: String,
    val district_name: String
)

data class ImportExoticGoatAddEditResponse(
    val _result: ResultIEG,
    val _resultflag: Int,
    val fileurl: String?,
    val message: String,
    val statuscode: Int
)

data class ResultIEG(
    val comment_by_nlm_whether: Int,
    val created_by: Int,
    val id: Int,
    val number_of_farmers_benefited: String?,
    val import_of_exotic_goat_achievement: List<ImportOfExoticGoatAchievement>?,
    val import_of_exotic_goat_detail_import: List<ImportOfExoticGoatDetailImport>?,
    val import_of_exotic_goat_verified_nlm: List<ImportOfExoticGoatVerifiedNlm>?,
    val import_of_exotic_goat_document: List<ImplementingAgencyDocument>?
)

data class ImportOfExoticgoatDocumnet(
    val artificial_insemination_id: Int,
    val description: String,
    val id: Int
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

data class TempUploadDocResponse(
    val _result: ResultDoc,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class ResultDoc(
    val created: String?,
    val document_name: String?,
    val id: Int?,
    val table_name: String?,
    val user_id: String?
)

data class Result_Doc(
    val created_at: String?,
    val description: String?,
    val ia_document: String?,
    val id: Int?,
    val implementing_agency_id: Int?,
    val nlm_document: String?
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

data class FspPlantStorageResponse(
    val _result: FspPlantStorageResult,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class FspPlantStorageResult(
    val data: List<FspPlantStorageData>,
    val is_add: Boolean,
    val total_count: Int
)

data class FspPlantStorageData(
    val created: String,
    val created_by: String,
    val district_code: Int,
    val district_name: String,
    val id: Int,
    val is_delete: Boolean,
    val is_draft: String,
    val is_draft_ia: String,
    val is_draft_nlm: String,
    val is_edit: Boolean,
    val is_view: Boolean,
    val name_of_organization: String,
    val state_code: Int,
    val state_name: String
)

data class FodderProductionFromNonForestResponse(
    val _result: FodderProductionFromNonForestResult,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class FodderProductionFromNonForestResult(
    val data: List<FodderProductionFromNonForestData>,
    val is_add: Boolean,
    val total_count: Int
)

data class FodderProductionFromNonForestData(
    val area_covered: Double? = null,
    val created: String,
    val created_by: String? = null,
    val district_code: Int? = null,
    val district_name: String? = null,
    val id: Int? = null,
    val is_delete: Boolean,
    val is_draft: Int? = null,
    val is_draft_ia: String? = null,
    val is_draft_nlm: String? = null,
    val is_edit: Boolean,
    val is_view: Boolean,
    val name_implementing_agency: String? = null,
    val state_code: Int? = null,
    val state_name: String? = null
)

data class FpFromForestLandResponse(
    val _result: FpFromForestLandResult,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class FpFromForestLandResult(
    val data: List<FpFromForestLandData>,
    val is_add: Boolean,
    val total_count: Int
)

data class FpFromForestLandAddEditFormat9Response(
    val _result: ResultFormat9,
    val _resultflag: Int?,
    val message: String?,
    val fileurl: String?,
    val statuscode: Int?
)

data class ResultFormat9(
    val area_covered: String?,
    val created: String?,
    val created_by: Int?,
    val district_code: Int?,
    val fp_from_forest_land_document: List<ImplementingAgencyDocument>?,
    val fp_from_forest_land_filled_by_nlm: List<FpFromForestLandFilledByNlm>?,
    val grant_received: String?,
    val id: Int?,
    val is_deleted: Int?,
    val is_draft: Int?,
    val is_draft_ia: Int?,
    val is_draft_nlm: Int?,
    val location_address: String?,
    val district_name: String?,
    val name_implementing_agency: String?,
    val role_id: String?,
    val scheme_guidelines: String?,
    val state_code: Int?,
    val status: Int?,
    val target_achievement: String?,
    val type_of_agency: String?,
    val type_of_land: String?,
    val updated: String?,
    val updated_at: String?,
    val user_id: String?,
    val variety_of_fodder: String?
)

data class FpFromForestLandDocument(
    val description: String,
    val fp_from_forest_land_id: Int,
    val id: Int,
    val nlm_document: String
)


data class District_list(
    val census2001_code: String,
    val census2011_code: String,
    val created: Any,
    val district_code: Int,
    val district_name_hindi: String,
    val flag: Boolean,
    val id: Int,
    val name: String,
    val state_code: Int
)

data class FpFromForestLandFilledByNlm(
    val agency_involved: String?,
    val area_covered: String?,
    val block_name: String?,
    val consumer_fodder: String?,
    val district_code: Int?,
    val district_name: String?,
    val district: District_list?,
    val estimated_quantity: String?,
    val fp_from_forest_land_id: Int?,
    val id: Int?,
    val village_name: String?
)

data class FpFromForestLandData(
    val area_covered: String,
    val created: String,
    val created_by: String,
    val district_code: Int,
    val district_name: String,
    val id: Int,
    val is_delete: Boolean,
    val is_draft: Int,
    val is_draft_ia: String,
    val is_draft_nlm: String,
    val is_edit: Boolean,
    val is_view: Boolean,
    val name_implementing_agency: String,
    val state_code: Int,
    val state_name: String
)

data class RspAddResponse(
    val _result: RspAddResult,
    val _resultflag: Int,
    val fileurl: String?,
    val message: String,
    val statuscode: Int
)


data class RspAddEquipment(
    val id: Int? = null,
    val list_of_equipment: String? = null,
    val make: String? = null,
    val year_of_procurement: String? = null,
    val rsp_laboratory_semen_id: Int? = null,
)

data class RspAddAverage(
    val id: Int? = null,
    val name_of_breed: String,
    val twentyOne_twentyTwo: String,
    val twentyTwo_twentyThree: String,
    val twentyThree_twentyFour: String,
    val rsp_laboratory_semen_id: Int? = null,
)

data class RspAddDocument(
    val description: String,
    val id: Int,
    val rsp_laboratory_semen_id: Int
)

data class RspAddResult(
    val Infrastructure_faced_Institute: String,
    val address: String,
    val area_for_fodder_cultivation: String,
    val area_under_buildings: Double,
    val availability_no_of_computers: Int,
    val availability_type_of_records: String,
    val created: String,
    val created_by: Int,
    val district_code: Int,
    val id: Int,
    val is_deleted: Int,
    val is_draft_ia: Int,
    val location: String,
    val is_draft_nlm: Int,
    val major_clients_coop_one_year: String,
    val major_clients_coop_three_year: String,
    val district_name: String,
    val major_clients_coop_two_year: String,
    val major_clients_ngo_one_year: String,
    val major_clients_ngo_three_year: String,
    val major_clients_ngo_two_year: String,
    val major_clients_other_states_one_year: String,
    val major_clients_other_states_three_year: String,
    val major_clients_other_states_two_year: String,
    val major_clients_private_one_year: String,
    val major_clients_private_three_year: String,
    val major_clients_private_two_year: String,
    val major_clients_sia_one_year: String,
    val major_clients_sia_three_year: String,
    val major_clients_sia_two_year: String,
    val manpower: String,
    val comments_infrastructure: String,
    val equipments_per_msp: String,
    val fund_properly_utilized: String,
    val semen_straws_produced: String,
    val suggestions_physical: String,
    val suggestions_financial: String,
    val suggestions_any_other: String,
    val processing_semen: String,
    val phone_no: Long,
    val pin_code: Int,
    val role_id: Int,
    val rsp_laboratory_semen_availability_equipment: List<RspAddEquipment>? = null,
    val rsp_laboratory_semen_average: List<RspAddAverage>,
    val rsp_laboratory_semen_document: List<ImplementingAgencyDocument>,
    val rsp_laboratory_semen_station_quality_buck: List<RspAddBucksList>? = null,
    val state_code: Int,
    val user_id: String,
    val year_of_establishment: String
)

data class RspAddBucksList(
    val breed_maintained: String?,
    val no_of_animals: Int? = null,
    val average_age: String? = null,
    val id: Int?,
    val rsp_laboratory_semen_id: Int? = null,
)

data class RspBreedList(
    val breed_maintained: String?,
    val no_of_animals: String? = null,
    val average_age: String? = null,
    val id: Int?,
    val rsp_laboratory_semen_id: Int? = null,
)

data class NlmEdpResponse(
    val _result: NlmEdpResult,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class NlmEdpResult(
    val data: List<NlmEdpData>,
    val is_add: Boolean,
    val total_count: Int
)

data class NlmEdpData(
    val created: String,
    val created_by: String,
    val id: Int,
    val is_delete: Boolean,
    val is_draft: Int,
    val is_draft_ia: String,
    val is_draft_nlm: String,
    val is_edit: Boolean,
    val is_view: Boolean,
    val state_code: Int,
    val state_name: String
)

data class AssistanceForEAResponse(
    val _result: AssistanceForEAResult,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class AssistanceForEAResult(
    val data: List<AssistanceForEAData>,
    val is_add: Boolean,
    val total_count: Int
)

data class AssistanceForEAData(
    val created: String,
    val created_by: String,
    val id: Int,
    val is_delete: Boolean,
    val is_draft: Int,
    val is_draft_ia: String,
    val is_draft_nlm: String,
    val is_edit: Boolean,
    val is_view: Boolean,
    val state_code: Int,
    val state_name: String
)

data class NlmFpFromNonForestAddResponse(
    val _result: NlmFpFromNonForestAddResult,
    val _resultflag: Int,
    val message: String,
    val fileurl: String,
    val statuscode: Int
)

data class NlmFpFromNonForestAddResult(
    val created: String,
    val created_at: String,
    val created_by: Int,
    val district_code: Int,
    val fp_from_non_forest_document: List<ImplementingAgencyDocument>,
    val fp_from_non_forest_filled_by_nlm_team: List<FpFromNonForestFilledByNlmTeam>,
    val id: Int,
    val is_deleted: Int,
    val is_draft: Int,
    val is_draft_nlm: Int,
    val name_implementing_agency: String,
    val role_id: String,
    val state_code: Int,
    val status: Int,
    val user_id: String,
    val state_name: String,
    val district_name: String,
    val area_covered: Double,
    val grant_received: String,
    val location: String,
    val scheme_guidelines: String,
    val target_achievement: String,
    val type_of_agency: String,
    val type_of_land: String,
    val variety_of_fodder: String
)

data class NlmAhidfResponse(
    val _result: NlmAhidfResult,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class NlmAhidfResult(
    val data: List<NlmAhidfData>,
    val is_add: Boolean,
    val total_count: Int
)

data class NlmAhidfData(
    val created: String,
    val created_by: String,
    val id: Int,
    val is_delete: Boolean,
    val is_draft: Int,
    val is_draft_ia: String,
    val is_draft_nlm: String,
    val is_edit: Boolean,
    val is_view: Boolean,
    val state_code: Int,
    val state_name: String
)

data class AddFspPlantStorageResponse(
    val _result: AddFspPlantStorageResult,
    val _resultflag: Int,
    val message: String,
    val fileurl: String?,
    val statuscode: Int
)

data class AddFspPlantStorageResult(
    val assistance_for_qfsp_document: List<ImplementingAgencyDocument>? = null,
    val capacity_of_plant: String? = null,
    val certification_recognition: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val district_code: Int? = null,
    val fsp_plant_storage_comments_of_nlm: ArrayList<FspPlantStorageCommentsOfNlm>,
    val fsp_plant_storage_document: List<ImplementingAgencyDocument>,
    val id: Int? = null,
    val is_deleted: Int? = null,
    val is_draft: Int? = null,
    val is_draft_ia: Int? = null,
    val location_address: String? = null,
    val district_name: String? = null,
    val machinery_equipment_available: String? = null,
    val name_of_organization: String? = null,
    val purpose_of_establishment: String? = null,
    val quantity_fodder_seed_class: String? = null,
    val quantity_fodder_seed_variety: String? = null,
    val role_id: String? = null,
    val state_code: Int? = null,
    val status: Int? = null,
    val technical_expertise: String? = null,
    val user_id: String? = null
)


data class FspPlantStorageCommentsOfNlm(
    val id: Int? = null,
    val name_of_agency: String? = null,
    val address: String? = null,
    val quantity_of_seed_graded: String? = null,
    val infrastructure_available: String? = null,
    val fsp_plant_storage_id: Int? = null,
)

data class AddAssistanceEAResponse(
    val _result: AddAssistanceEAResult?,
    val _resultflag: Int?,
    val message: String,
    val fileurl: String?,
    val statuscode: Int?
)

data class AddAssistanceEAResult(
    val assistance_for_ea_document: List<ImplementingAgencyDocument>? = null,
    val assistance_for_ea_training_institute: List<AssistanceForEaTrainingInstitute>? = null,
    val created: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val details_of_training_programmes: String? = null,
    val district_code: Int? = null,
    val id: Int? = null,
    val is_deleted: Int? = null,
    val is_draft: Int? = null,
    val is_draft_ia: Int? = null,
    val no_of_camps: Int? = null,
    val no_of_participants: Int? = null,
    val role_id: String? = null,
    val state_code: Int? = null,
    val status: Int? = null,
    val user_id: String? = null,
    val whether_the_state_developed: String? = null,
    val whether_the_state_trainers: String? = null
)


data class AssistanceForEaTrainingInstitute(
    val id: Int? = null,
    val name_of_institute: String? = null,
    val address_for_training: String? = null,
    val training_courses_run: String? = null,
    val no_of_participants_trained: Int?,
    val no_of_provide_information: Int?,
    val assistance_for_ea_id: Int? = null,

    )


data class AddNlmEdpResponse(
    val _result: AddNlmEdpResult?,
    val _resultflag: Int?,
    val message: String,
    val fileurl: String?,
    val statuscode: Int?
)

data class AddNlmEdpResult(
    val created: String?,
    val remarks_by_nlm: String?,
    val created_at: String?,
    val created_by: Int?,
    val id: Int?,
    val is_deleted: Int?,
    val is_draft_nlm: Any?,
    val nlm_edp_document: List<ImplementingAgencyDocument>?,
    val nlm_edp_format_for_nlm: List<NlmEdpFormatForNlm>?,
    val nlm_edp_monitoring: List<NlmEdpMonitoring>?,
    val role_id: String?,
    val state_code: Int?,
    val status: Int?,
    val user_id: String?
)


data class NlmEdpFormatForNlm(
    val id: Int?,
    val category_of_project: String?,
    val no_of_project: Int?,
    val cost_of_project: Double?,
    val total_animal_inducted: Int?,
    val total_farmers_impacted: Int?,
    val total_employment_generated: Int?,
    val birth_percentage: Int?,
    val average_revenue_earned: Double?,
    val nlm_edp_id: Int?,
    val created: String? = null,

    )

data class NlmEdpMonitoring(
    val id: Int?,
    val name_of_beneficiary: String?,
    val category_of_project: String?,
    val project_financing: String?,
    val type_of_farming: String?,
    val capacity: String?,
    val whether_full: String?,
    val financial_status: String?,
    val number_of_animals_marketed: Int?,
    val balance_of_animal: Int?,
    val number_of_farmers: Int?,
    val number_of_job: Int?,
    val nlm_edp_id: Int?,
    val created: String? = null,

    )


data class AddAnimalResponse(
    val _result: AddAnimalResult,
    val _resultflag: Int?,
    val message: String,
    val fileurl: String?,
    val statuscode: Int?
)

data class AddAnimalResult(
    val ahidf_document: List<ImplementingAgencyDocument>?,
    val ahidf_format_for_nlm: List<AhidfFormatForNlm>?,
    val ahidf_monitoring: List<AhidfMonitoring>?,
    val created: String?,
    val created_at: String?,
    val remarks_by_nlm: String?,
    val created_by: Int?,
    val id: Int?,
    val is_deleted: Int?,
    val is_draft: Int?,
    val is_draft_nlm: Int?,
    val role_id: String?,
    val state_code: Int?,
    val status: Int?,
    val user_id: String?
)


data class AhidfFormatForNlm(
    val id: Int?,
    val category_of_project: String?,
    val no_of_project: Int?,
    val cost_of_project: Double?,
    val term_loan: Int?,
    val total_employment_generated: Int?,
    val processing_capacity: Double?,
    val birth_percentage: Int?,
    val average_revenue_earned: Double?,
    val ahidf_id: Int?,
    val created: String? = null,
)

data class AhidfMonitoring(
    val id: Int?,
    val name_of_beneficiary: String?,
    val category_of_project: String?,
    val project_financing: String?,
    val type_of_farming: String?,
    val capacity: String?,
    val whether_full: String?,
    val financial_status: Int?,
    val processing_capacity_nlm: Int?,
    val number_of_farmers: Int?,
    val number_of_job: Int?,
    val ahidf_id: Int?,
    val created: String? = null,
)

data class VaccinationProgrammerListResponse(
    val _result: VaccinationProgrammerListResult,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class VaccinationProgrammerListResult(
    val data: List<VaccinationProgrammerListData>,
    val is_add: Boolean,
    val total_count: Int
)

data class VaccinationProgrammerListData(
    val created_at: String,
    val created: String,
    val created_by: String,
    val district_code: Int,
    val visit: Int,
    val district_name: String,
    val id: Int,
    val is_delete: Boolean,
    val is_edit: Boolean,
    val is_view: Boolean,
    val state_code: Int,
    val state_name: String,
    val status: String,
    val village_name: String
)

data class MobileVeterinaryUnitsListResponse(
    val _result: MobileVeterinaryUnitsListResult,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class MobileVeterinaryUnitsListResult(
    val data: List<MobileVeterinaryUnitsListData>,
    val is_add: Boolean,
    val total_count: Int
)

data class MobileVeterinaryUnitsListData(
    val block_name: String,
    val created: String,
    val created_by: String,
    val district_code: Int,
    val district_name: String,
    val id: Int,
    val is_delete: Boolean,
    val is_edit: Boolean,
    val is_view: Boolean,
    val state_code: Int,
    val state_name: String,
    val status: String,
    val village_name: String,
    val created_at: String,
)

data class AscadListResponse(
    val _result: AscadListResult,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class AscadListResult(
    val data: List<AscadListData>,
    val is_add: Boolean,
    val total_count: Int
)

data class AscadListData(
    val created_at: String,
    val created_by: String,
    val district_code: Int,
    val district_name: String,
    val id: Int,
    val is_delete: Boolean,
    val is_edit: Boolean,
    val is_view: Boolean,
    val state_code: Int,
    val state_name: String,
    val status: String
)

data class StateVaccinationProgrammeAddResponse(
    val _result: StateVaccinationProgrammeAddResult,
    val _resultflag: Int,
    val message: String,
    val fileurl: String?,
    val statuscode: Int
)

data class StateVaccinationProgrammeAddResult(
    val created_at: String,
    val created_by: Int,
    val id: Int,
    val is_deleted: String,
    val process_plan_monitoring: Any,
    val process_plan_monitoring_inputs: String,
    val process_plan_monitoring_remarks: String,
    val process_plan_monitoring_upload: String,
    val schedule_vaccination_arrangement_inputs: String,
    val schedule_vaccination_arrangement_remarks: String,
    val schedule_vaccination_arrangement_upload: String,
    val schedule_vaccination_assign_areas_inputs: String,
    val schedule_vaccination_assign_areas_remarks: String,
    val schedule_vaccination_assign_areas_upload: String,
    val schedule_vaccination_cold_chain_avail_inputs: String,
    val schedule_vaccination_cold_chain_avail_remarks: String,
    val schedule_vaccination_cold_chain_avail_upload: String,
    val schedule_vaccination_focal_point: Any,
    val schedule_vaccination_focal_point_input: String,
    val schedule_vaccination_focal_point_remark: String,
    val schedule_vaccination_focal_point_upload: String,
    val schedule_vaccination_timeline_inputs: String,
    val schedule_vaccination_timeline_remarks: String,
    val schedule_vaccination_timeline_upload: String,
    val seromonitoring_facilitie_remarks: String,
    val seromonitoring_facilities_input: String,
    val seromonitoring_facilities_upload: String,
    val state_code: Int,
    val state_name: String,
    val status: String,
    val updated_at: Any,
    val visit: Any
)

data class DistrictVaccinationProgrammeAddResponse(
    val _result: DistrictVaccinationProgrammeAddResult,
    val _resultflag: Int,
    val message: String,
    val fileurl: String?,
    val statuscode: Int
)

data class DistrictVaccinationProgrammeAddResult(
    val are_functionaries_aware_inputs: String,
    val are_functionaries_aware_remarks: String,
    val are_functionaries_aware_uploads: String,
    val created_at: String,
    val created_by: Int,
    val district_code: Int,
    val district_name: String,
    val id: Int,
    val investigate_suspected_outbreak_inputs: String,
    val investigate_suspected_outbreak_remarks: String,
    val investigate_suspected_outbreak_uploads: String,
    val is_deleted: Int,
    val mass_education_campaign_inputs: String,
    val mass_education_campaign_remarks: String,
    val mass_education_campaign_uploads: String,
    val mechanisim_followed_inputs: String,
    val mechanisim_followed_remarks: String,
    val mechanisim_followed_uploads: String,
    val role_id: Int,
    val state_code: Int,
    val state_name: String,
    val status: String,
    val trained_staff_engaged_inputs: String,
    val trained_staff_engaged_remarks: String,
    val trained_staff_engaged_uploads: String,
    val user_id: Int
)

data class FarmerVaccinationProgrammeAddResponse(
    val _result: FarmerVaccinationProgrammeAddResult,
    val _resultflag: Int,
    val message: String,
    val fileurl: String?,
    val statuscode: Int
)

data class FarmerVaccinationProgrammeAddResult(
    val animal_vaccinated: Any,
    val animal_vaccinated_inputs: String,
    val animal_vaccinated_remarks: String,
    val animal_vaccinated_uploads: String,
    val awarness_of_the_govt: Any,
    val awarness_of_the_govt_inputs: String,
    val awarness_of_the_govt_remarks: String,
    val awarness_of_the_govt_uploads: String,
    val awarness_reg_ear_tagging: Any,
    val created_at: String,
    val created_by: Int,
    val district_code: Int,
    val district_name: String?= null,
    val state_name: String?= null,
    val id: Int,
    val is_type: String,
    val is_deleted: Int,
    val paid_for_vaccination: Any,
    val recall_vaccination_inputs: String,
    val recall_vaccination_remarks: String,
    val recall_vaccination_uploads: String,
    val role_id: String,
    val state_code: Int,
    val status: String,
    val updated_at: String,
    val user_id: String,
    val vaccination_carrier_inputs: String,
    val vaccination_carrier_remarks: String,
    val vaccination_carrier_uploads: String,
    val vaccinator_visit: Any,
    val vaccinator_visit_inputs: String,
    val vaccinator_visit_remarks: String,
    val vaccinator_visit_uploads: String,
    val village_name: String
)

data class StateMobileVeterinaryUnitAddResponse(
    val _result: StateMobileVeterinaryUnitAddResult,
    val _resultflag: Int,
    val message: String,
    val fileurl: String?,
    val statuscode: Int
)

data class StateMobileVeterinaryUnitAddResult(
    val are_adequate_staff_inputs: String,
    val are_adequate_staff_remarks: String,
    val are_operators_engaged_inputs: String,
    val are_operators_engaged_remarks: String,
    val call_center_inputs: String,
    val call_center_remarks: String,
    val created: String,
    val created_by: String,
    val data_compilation_analysis_done_inputs: String,
    val data_compilation_analysis_done_remarks: String,
    val engagement_indicators_inputs: String,
    val engagement_indicators_remarks: String,
    val id: Int,
    val input_are_adequate_staff: String,
    val input_are_operators_engaged: String,
    val input_call_center: String,
    val input_data_compilation_analysis_done: String,
    val input_engagement_indicators: String,
    val input_is_app_crm_place: String,
    val input_is_building_provided_operation_seats: String,
    val input_is_monitoring_supervision_fuel: String,
    val input_is_monitoring_supervision_medic_equip: String,
    val input_is_service_provider_engaged: String,
    val input_mechanism_operation: String,
    val state_name: String,
    val input_procurement_procedure: String,
    val input_supply_procedure: String,
    val is_app_crm_place_inputs: String,
    val is_app_crm_place_remarks: String,
    val is_building_provided_operation_seats_inputs: String,
    val is_building_provided_operation_seats_remarks: String,
    val is_deleted: Int,
    val is_monitoring_supervision_fuel_inputs: String,
    val is_monitoring_supervision_fuel_remarks: String,
    val is_monitoring_supervision_medic_equip_inputs: String,
    val is_monitoring_supervision_medic_equip_remarks: String,
    val is_service_provider_engaged_inputs: String,
    val is_service_provider_engaged_remaks: String,
    val mechanism_operation_inputs: String,
    val mechanism_operation_remarks: String,
    val procurement_procedure_inputs: String,
    val procurement_procedure_remarks: String,
    val role_id: Int,
    val state_code: Int,
    val status: String,
    val supply_procedure_inputs: String,
    val supply_procedure_remarks: String,
    val user_id: Int
)

data class DistrictMobileVeterinaryUnitAddResponse(
    val _result: DistrictMobileVeterinaryUnitAddResult,
    val _resultflag: Int,
    val message: String,
    val fileurl: String?,
    val statuscode: Int
)

data class DistrictMobileVeterinaryUnitAddResult(
    val created: String,
    val created_by: Int,
    val distribution_fuel_role_inputs: String,
    val distribution_fuel_role_inputs_remarks: String,
    val distribution_medicines_role_inputs: String,
    val distribution_medicines_role_remaks: String,
    val district_code: Int,
    val id: Int,
    val input_distribution_fuel_role: String,
    val input_distribution_medicines_role: String,
    val input_mechanism_medicines: String,
    val input_medicine_requirement: String,
    val input_organize_awareness_camp: String,
    val is_deleted: Int,
    val mechanism_medicines_inputs: String,
    val mechanism_medicines_remaks: String,
    val medicine_requirement_inputs: String,
    val medicine_requirement_remarks: String,
    val district_name: String,
    val organize_awareness_camp_inputs: String,
    val organize_awareness_camp_remarks: String,
    val role_id: Int,
    val state_code: Int,
    val status: String,
    val user_id: Int
)

data class BlockMobileVeterinaryUnitAddResponse(
    val _result: BlockMobileVeterinaryUnitAddResult,
    val _resultflag: Int,
    val message: String,
    val fileurl: String?,
    val statuscode: Int
)

data class BlockMobileVeterinaryUnitAddResult(
    val any_other_block_inputs: String,
    val any_other_block_remarks: String,
    val block_name: String,
    val created: String, val district_name: String,


    val created_by: Int,
    val district_code: Int,
    val general_monitoring_block_inputs: String,
    val general_monitoring_block_remarks: String,
    val id: Int,
    val input_any_other_block: String,
    val input_general_monitoring_block: String,
    val input_machanism_attendance_staff: String,
    val input_monitoring_tracking_call: String,
    val input_stock_management: String,
    val is_deleted: Int,
    val machanism_attendance_staff_inputs: String,
    val machanism_attendance_staff_remarks: String,
    val monitoring_tracking_call_inputs: String,
    val monitoring_tracking_call_remarks: String,
    val role_id: String,
    val state_code: Int,
    val status: String,
    val stock_management_inputs: String,
    val stock_management_remarks: String,
    val user_id: String
)

data class FarmerMobileVeterinaryUnitAddResponse(
    val _result: FarmerMobileVeterinaryUnitAddResult,
    val _resultflag: Int,
    val message: String,
    val fileurl: String?,
    val statuscode: Int
)

data class FarmerMobileVeterinaryUnitAddResult(
    val attended_call: Int,
    val attended_call_inputs: String,
    val attended_call_remarks: String,
    val block_name: String,
    val come_know_about_inputs: String,
    val come_know_about_remarks: String,
    val created: String,
    val created_by: Int,
    val district_code: Int,
    val district_name: String,
    val id: Int,
    val input_come_know_about: String,
    val input_mvu_arrive_call: String,
    val input_services_mvu: String,
    val input_services_offered_by_mvu: String,
    val is_deleted: Int,
    val mvu_arrive_call_inputs: String,
    val mvu_arrive_call_remarks: String,
    val services_mvu: Any,
    val services_mvu_inputs: String,
    val services_mvu_remarks: String,
    val services_offered_by_mvu_inputs: String,
    val services_offered_by_mvu_remarks: String,
    val state_code: Int,
    val state_name: String,
    val status: String,
    val updated_at: String,
    val village_name: String,
    val visit: Int
)

data class StateAscadAddResponse(
    val _result: StateAscadAddResult,
    val _resultflag: Int,
    val message: String,
    val fileurl: String?,
    val statuscode: Int
)

data class StateAscadAddResult(
    val created: String,
    val created_by: Int,
    val id: Int,
    val status: String,
    val user_id: Int,
    val is_deleted: Int,
    val role_id: String,
    val state_code: Int,
    val state_name:String,
    val annual_action_plan_input: String,
    val annual_action_plan_remarks: String,
    val financial_planning_for_state_share_input: String,
    val financial_planning_for_state_share_remarks: String,
    val input_annual_action_plan: String,
    val input_financial_planning_for_state_share: String,
    val input_purchase_of_vaccines_accessories: String,
    val input_scheduling_of_vaccination: String,
    val input_state_prioritizes_critical_disease: String,
    val purchase_of_vaccines_accessories_input: String,
    val purchase_of_vaccines_accessories_remarks: String,
    val scheduling_of_vaccination_input: String,
    val scheduling_of_vaccination_remarks: String,
    val state_prioritizes_critical_disease_input: String,
    val state_prioritizes_critical_disease_remarks: String,
)

data class DistrictAscadAddResponse(
    val _result: DistrictAscadAddResult,
    val _resultflag: Int,
    val message: String,
    val fileurl: String?,
    val statuscode: Int
)

data class DistrictAscadAddResult(
    val state_name: String,
    val district_name: String,
    val compensation_farmer_against_culling_of_animals_input: String,
    val compensation_farmer_against_culling_of_animals_remarks: String,
    val created: String,
    val created_by: Int,
    val disease_diagnostic_labs_input: String,
    val disease_diagnostic_labs_remarks: String,
    val district_code: Int,
    val id: Int,
    val input_compensation_farmer_against_culling_of_animals: String,
    val input_disease_diagnostic_labs: String,
    val input_status_of_vaccination_against_economically: String,
    val input_status_of_vaccination_against_zoonotic: String,
    val input_training_of_veterinarians_and_para_vets_last_year: String,
    val is_deleted: Int,
    val role_id: String,
    val state_code: Int,
    val status: String,
    val status_of_vaccination_against_economically_input: String,
    val status_of_vaccination_against_economically_remarks: String,
    val status_of_vaccination_against_zoonotic_input: String,
    val status_of_vaccination_against_zoonotic_remarks: String,
    val training_of_veterinarians_and_para_vets_last_year_input: String,
    val training_of_veterinarians_and_para_vets_last_year_remarks: String,
    val user_id: Int
)


data class NDDComponentBListResponse(
    val _result: NDDComponentBListResult,
    val _resultflag: Int?,
    val message: String?,
    val statuscode: Int?
)

data class NDDComponentBListResult(
    val data: List<NDDComponentBListData>,
    val is_add: Boolean,
    val total_count: Int
)

data class NDDComponentBListData(
    val created_by_id: Int,
    val created_on: String,
    val district_name: String,
    val id: Int,
    val is_delete: Boolean,
    val is_draft: Int,
    val is_view: Boolean,
    val is_draft_text: String,
    val is_edit: Boolean,
    val name_of_dcs_mpp: String,
    val name_of_revenue_village: String,
    val name_of_tehsil: String,
    val state_id: Int,
    val state_name: String
)

data class NDDComponentBAddResponse(
    val _result: NDDComponentBAddResult,
    val _resultflag: Int,
    val fileurl: String?,
    val message: String,
    val statuscode: Int
)

data class NDDComponentBAddResult(
    val any_other: String,
    val asset_earmarked: String,
    val asset_earmarked_remarks: String,
    val assured_marked_surplus: String,
    val assured_marked_surplus_remarks: String,
    val better_price_realisation: String,
    val better_price_realisation_remarks: String,
    val created_by: Int,
    val created_by_id: Int,
    val created_on: String,
    val date_of_inspection: String,
    val district_id: Int,
    val id: Int,
    val is_deleted: Int,
    val latitude: String,
    val longitude: String,
    val district_name: String,
    val state_name: String,
    val name_of_dcs_mpp: String,
    val name_of_revenue_village: String,
    val name_of_tehsil: String,
    val nlm_b_components_document: List<ImplementingAgencyDocument>,
    val overall_hygiene: String,
    val overall_hygiene_remarks: String,
    val overall_interventions: String,
    val overall_interventions_remarks: String,
    val overall_upkeep: String,
    val overall_upkeep_remarks: String,
    val positive_impact: String,
    val positive_impact_remarks: String,
    val role_id: String,
    val standard_operating_procedures: String,
    val state_code: String,
    val state_id: Int,
    val status: String,
    val timely_milk_payment: String,
    val timely_milk_payment_remarks: String,
    val transparency_milk_pricing: String,
    val transparency_milk_pricing_remarks: String,
    val user_id: String
)

data class NlmBComponentsDocument(
    val description: String,
    val id: Int,
    val nlm_b_component_id: Int,
    val nlm_document: String
)


data class NDDMilkUnionListResponse(
    val _result: NDDMilkUnionListResult,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class NDDMilkUnionListResult(
    val `data`: List<NDDMilkUnionListData>,
    val is_add: Boolean,
    val total_count: Int
)

data class NDDMilkUnionListData(
    val created_at: String,
    val created_by: Int,
    val created_by_text: String,
    val district_names: String,
    val is_draft_text: String,
    val id: Int,
    val is_delete: Boolean,
    val is_draft: Int,
    val is_edit: Boolean,
    val is_view: Boolean,
    val name_of_milk_union: String,
    val state_code: Int,
    val state_name: String
)


data class NDDDairyPlantListResponse(
    val _result: NDDDairyPlantListResult,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class NDDDairyPlantListResult(
    val `data`: List<NDDDairyPlantListData>,
    val is_add: Boolean,
    val total_count: Int
)

data class NDDDairyPlantListData(
    val created_at: String,
    val created_by: Int,
    val created_by_text: String,
    val district_name: String,
    val is_draft_text: String,
    val fssai_license_no: String,
    val id: Int,
    val is_delete: Boolean,
    val is_draft: Int,
    val is_edit: Boolean,
    val is_view: Boolean,
    val state_code: Int,
    val state_name: String
)


data class NDDDcsBmcListResponse(
    val _result: NDDDcsBmcListResult,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class NDDDcsBmcListResult(
    val `data`: List<NDDDcsBmcListData>,
    val is_add: Boolean?,
    val total_count: Int
)

data class NDDDcsBmcListData(
    val created: String,
    val created_by: Int,
    val created_by_text: String,
    val district_name: String,
    val is_draft_text: String,
    val fssai_lic_no: Int,
    val fssai_lic_validity_date: String,
    val id: Int,
    val is_draft: Boolean,
    val name_of_dcs: String,
    val state_code: Int,
    val state_name: String,
    val is_delete: Boolean,
    val is_edit: Boolean,
    val is_view: Boolean,
)



data class NDDStateCenterLabListResponse(
    val _result: NDDStateCenterLabListResult,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class NDDStateCenterLabListResult(
    val `data`: List<NDDStateCenterLabListData>,
    val is_add: Boolean,
    val total_count: Int
)

data class NDDStateCenterLabListData(
    val created: String,
    val created_by: Int,
    val created_by_text: String,
    val district_name: String,
    val is_draft_text: String,
    val id: Int,
    val is_delete: Boolean,
    val is_draft: Int,
    val is_edit: Boolean,
    val is_view: Boolean,
    val location_state_central_lab: String,
    val state_code: Int,
    val state_name: String
)

data class NDDMilkProcessingListResponse(
    val _result: NDDMilkProcessingListResult,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class NDDMilkProcessingListResult(
    val `data`: List<NDDMilkProcessingListData>,
    val is_add: Boolean,
    val total_count: Int
)

data class NDDMilkProcessingListData(
    val created_at: String,
    val created_by: Int,
    val created_by_text: String,
    val district_name: String,
    val is_draft_text: String,
    val id: Int,
    val is_delete: Boolean,
    val is_draft: Int,
    val is_edit: Boolean,
    val is_view: Boolean,
    val name_milk_union: String,
    val name_processing_plant: String,
    val state_code: Int,
    val state_name: String
)


data class NDDMilkProductMarketingListResponse(
    val _result: NDDMilkProductMarketingListResult,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class NDDMilkProductMarketingListResult(
    val `data`: List<NDDMilkProductMarketingListData>,
    val is_add: Boolean,
    val total_count: Int
)

data class NDDMilkProductMarketingListData(
    val created_at: String,
    val created_by: Int,
    val created_by_text: String,
    val is_draft_text: String,
    val district_name: String,
    val id: Int,
    val is_delete: Boolean,
    val is_draft: Int,
    val is_edit: Boolean,
    val is_view: Boolean,
    val name_milk_union: String,
    val name_retail_shop: String,
    val state_code: Int,
    val state_name: String
)

data class NDDProductivityEnhancementServicesListResponse(
    val _result: NDDProductivityEnhancementServicesListResult,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
)

data class NDDProductivityEnhancementServicesListResult(
    val `data`: List<NDDProductivityEnhancementServicesListData>,
    val is_add: Boolean,
    val total_count: Int
)

data class NDDProductivityEnhancementServicesListData(
    val created_at: String,
    val created_by: Int,
    val created_by_text: String,
    val district_name: String,
    val name_tehsil: String,
    val name_revenue_village: String,
    val is_draft_text: String,
    val id: Int,
    val is_delete: Boolean,
    val is_draft: Int,
    val is_edit: Boolean,
    val is_view: Boolean,
    val name_dcs_mpp: String,
    val state_code: Int,
    val state_name: String
)



//data class AddDairyPlantResponse(
//    val _result: AddDairyPlantResult,
//    val _resultflag: Int,
//    val message: String,
//    val statuscode: Int
//)
//
//data class AddDairyPlantResult(
//    val capacity_of_dairy_plant: String,
//    val chemical_frequency_tests_performed: String,
//    val chemical_snf_frequency: String,
//    val chemical_snf_result: String,
//    val chemical_tests_performed: String,
//    val created_by: Int,
//    val created_by_id: String,
//    val created_on: String,
//    val daily_avg_last_year_liquid_milk: String,
//    val daily_avg_last_year_value_added_curd: String,
//    val daily_avg_last_year_value_added_ghee: String,
//    val daily_avg_last_year_value_added_lassi: String,
//    val dairy_plant_visit_report_document: List<ImplementingAgencyDocument>,
//    val dairy_plant_visit_report_npdd_scheme: List<DairyPlantVisitReportNpddScheme>,
//    val districts: Int,
//    val fssai_license_no: String,
//    val haccp_quality_management_system_complied: String,
//    val id: Int,
//    val is_deleted: Int,
//    val location_of_dairy_plant: String,
//    val district_name: String,
//    val state_name: String,
//    val number_of_bmc_route_milk: String,
//    val number_of_non_bmc_route_milk: String,
//    val number_of_total_route_milk: String,
//    val other_details_capacity: String,
//    val other_details_etp_functioning: String,
//    val photographs_of_site: String,
//    val plant_haccp_complied: String,
//    val remark_by_nlm: String,
//    val role_id: String,
//    val state_code: Int,
//    val status: Int,
//    val total_bmc_capacity: String,
//    val total_dcs_covered: String,
//    val user_id: String,
//    val year_of_establishment: String
//)
//
//
//data class DairyPlantVisitReportNpddScheme(
//    val dairy_plant_visit_report_id: Int,
//    val id: Int,
//    val major_equipments_milk_products: String,
//    val npdd_scheme_available: Int,
//    val npdd_scheme_major_lab_available: Int,
//    val npdd_scheme_major_lab_no: Int,
//    val npdd_scheme_major_lab_present_status: String,
//    val npdd_scheme_major_lab_remarks: String,
//    val npdd_scheme_major_lab_strengthening: String,
//    val npdd_scheme_present_status: Int,
//    val npdd_scheme_proj_no: Int,
//    val npdd_scheme_remarks: String
//)


data class AddDairyPlantResponse(
    val _result: AddDairyPlantVisitResult,
    val _resultflag: Int,
    val fileurl: String?,
    val message: String,
    val statuscode: Int
)

data class AddDairyPlantVisitResult(
    val capacity_of_dairy_plant: String,
    val chemical_acidity_frequency: Any,
    val chemical_acidity_result: Any,
    val chemical_adulterants_frequency: Any,
    val chemical_adulterants_result: Any,
    val chemical_antibiotic_frequency: Any,
    val chemical_antibiotic_result: Any,
    val chemical_bod_frequency: Any,
    val chemical_bod_result: Any,
    val chemical_cod_frequency: Any,
    val chemical_cod_result: Any,
    val chemical_coliform_frequency: Any,
    val chemical_coliform_result: Any,
    val chemical_frequency_tests_performed: String,
    val chemical_heavy_metal_frequency: Any,
    val chemical_heavy_metal_result: Any,
    val chemical_mbrt_frequency: Any,
    val chemical_mbrt_result: Any,
    val chemical_pesticide_residue_frequency: Any,
    val chemical_pesticide_residue_result: Any,
    val chemical_protein_frequency: Any,
    val chemical_protein_result: Any,
    val chemical_snf_frequency: String,
    val chemical_snf_result: String,
    val chemical_tests_performed: String,
    val created_at: String,
    val created_by: Int,
    val daily_avg_current_year_liquid_milk: Any,
    val daily_avg_current_year_value_added_curd: Any,
    val daily_avg_current_year_value_added_ghee: Any,
    val daily_avg_current_year_value_added_lassi: Any,
    val daily_avg_last_year_liquid_milk: String,
    val daily_avg_last_year_value_added_curd: String,
    val daily_avg_last_year_value_added_ghee: String,
    val daily_avg_last_year_value_added_lassi: String,
    val dairy_plant_visit_report_document: List<ImplementingAgencyDocument>,
    val dairy_plant_visit_report_npdd_scheme: List<DairyPlantVisitReportNpddScheme>,
    val date_of_license_validity: Any,
    val district_name: String,
    val districts: Int,
    val fssai_license_no: String,
    val haccp_quality_management_system_complied: String,
    val id: Int,
    val is_deleted: Int,
    val is_draft: Int,
    val lab_available_operational: Any,
    val lab_major_equipments_provided: Any,
    val lab_present_number: Any,
    val lab_scheme_project_no: Any,
    val lat_nlm: Any,
    val location_of_dairy_plant: String,
    val long_nlm: Any,
    val major_equipments_actual_installation_status: Any,
    val major_equipments_capacity: Any,
    val major_equipments_present_status: Any,
    val major_equipments_provided: Any,
    val major_equipments_scheme_project_no: Any,
    val number_of_bmc_route_milk: String,
    val number_of_non_bmc_route_milk: String,
    val number_of_total_route_milk: String,
    val other_details_capacity: String,
    val other_details_etp_functioning: String,
    val photographs_of_site: String,
    val plant_haccp_complied: String,
    val remark_by_nlm: String,
    val state_code: Int,
    val state_name: String,
    val status: String,
    val total_bmc_capacity: String,
    val total_bmc_no: Any,
    val total_dcs_covered: String,
    val updated_at: Any,
    val verification_of_tendering_process: Any,
    val visit: Any,
    val year_of_establishment: String
)

data class DairyPlantVisitReportDocument(
    val created_at: String,
    val dairy_plant_visit_report_id: Int,
    val description: String,
    val ia_document: Any,
    val id: Int,
    val nlm_document: String
)

data class DairyPlantVisitReportNpddScheme(
    val dairy_plant_visit_report_id: Int?=null,
    val id: Int?=null,
    val major_equipments_milk_products: String?=null,
    val npdd_scheme_available: Int?=null,
    val npdd_scheme_present_status: Int?=null,
    val npdd_scheme_proj_no: Int?=null,
    val npdd_scheme_remarks: String?=null,
    val npdd_scheme_major_lab_available: Int?=null,
    val npdd_scheme_major_lab_no: Int?=null,
    val npdd_scheme_major_lab_present_status: String?=null,
    val npdd_scheme_major_lab_remarks: String?=null,
    val npdd_scheme_major_lab_strengthening: String?=null,
)





data class AddDcsBmcResponse(
    val _result: AddDcsBmcResult,
    val _resultflag: Int,
    val fileurl: String?,
    val message: String,
    val statuscode: Int
)

data class AddDcsBmcResult(
    val actual_pourer_members: String,
    val additional_facilities: String,
    val amc_equip_covered: String,
    val amc_equip_quality: String,
    val amc_equip_valid_years: String,
    val amcu_npdd_calib_frequency: Any,
    val amcu_npdd_calib_last_date: Any,
    val amcu_npdd_date_install: String,
    val amcu_npdd_make: Any,
    val amcu_npdd_project_no: String,
    val avg_composition_milk_fat: Double,
    val avg_composition_milk_snf: Double,
    val avg_procurement_price_qmp: Double,
    val avg_procurement_price_total_ap: Double,
    val bmc_details_avg_chil_cost_ltr: Double,
    val bmc_details_avg_milk_collection: Double,
    val bmc_details_capacity: String,
    val bmc_details_date_of_install: String,
    val bmc_details_design: Any,
    val bmc_details_dg_set_av_rt: String,
    val bmc_details_dg_set_available: String,
    val bmc_details_maintenance_log_book: String,
    val bmc_details_make: Any,
    val bmc_details_npdd_prj_no: String,
    val bmc_details_other_reg: String,
    val bmc_details_spare_parts: String,
    val bmc_details_temp_control_log_book: String,
    val bmc_details_temp_control_log_book_temp: Int,
    val bmc_operator_secretary_date_of_training: String,
    val bmc_operator_secretary_no_of_days: Int,
    val bmc_operator_secretary_place_of_training: String,
    val chemical_frequency_tests: String,
    val chemical_tests_performed: String,
    val condition_electricity_supply_hrs_sply: Double,
    val cooperative_society_reg_date: String,
    val cooperative_society_reg_num: Int,
    val created: String,
    val created_by: Int,
    val daily_avg_procurement_milk_current_year: Double,
    val daily_avg_procurement_milk_last_year: Double,
    val dcs_bmc_calibration_status: String,
    val dcs_bmc_fre_interval: String,
    val dcs_bmc_installation: String,
    val dcs_bmc_last_date: String,
    val dcs_bmc_make: String,
    val dcs_last_audit_year: String,
    val dcs_profit_loss: String,
    val district_name: String,
    val districts: Int,
    val dsc_any_other: String,
    val dsc_bonus_distribution: String,
    val dsc_general: String,
    val dsc_status_held: String,
    val farmer_interaction_feedback: String,
    val fssai_lic_no: Int,
    val fssai_lic_validity_date: String,
    val id: Int,
    val incentive_procurement_price_buffalo: Double,
    val incentive_procurement_price_cow: Double,
    val is_deleted: Int,
    val is_draft: Boolean,
    val lab_tests_performed_adulter_no_of_day: Any,
    val lab_tests_performed_adulter_test_result: Any,
    val lab_tests_performed_alcohol_no_of_day: Any,
    val lab_tests_performed_alcohol_test_result: Any,
    val lab_tests_performed_any_other_no_of_day: Any,
    val lab_tests_performed_any_other_test_result: Any,
    val lab_tests_performed_cob_no_of_day: Any,
    val lab_tests_performed_cob_test_result: Any,
    val lab_tests_performed_fat_no_of_day: Any,
    val lab_tests_performed_fat_test_result: Any,
    val lab_tests_performed_mbrt_no_of_day: Any,
    val lab_tests_performed_mbrt_test_result: Any,
    val lat_nlm: Any,
    val long_nlm: Any,
    val member_bank_ac: Long,
    val milk_payment_cycle: String,
    val milk_payment_last_date: String,
    val name_of_dcs: String,
    val onsite_dcs_center_visit: Any,
    val onsite_dcs_center_visit_document: List<ImplementingAgencyDocument>,
    val overall_remarks: String,
    val photographs_of_site: String,
    val photographs_of_site_assets: String,
    val photographs_of_site_assets_date: String,
    val photographs_of_site_assets_location: String,
    val procurement_price_buffalo_milk_current_year: Int,
    val procurement_price_buffalo_milk_last_year: Int,
    val procurement_price_cow_milk_current_year: Int,
    val procurement_price_cow_milk_last_year: Int,
    val staff_strength_at_dcs_bmc_operator: String,
    val staff_strength_at_dcs_cleaner: String,
    val staff_strength_at_dcs_milk_tester: String,
    val staff_strength_at_dcs_secretary: String,
    val state_code: Int,
    val state_name: String,
    val status: String,
    val total_reg_numbers: Int,
    val updated: String,
    val updated_by: Any,
    val village: String,
    val visit: Int,
    val year_of_estb: String
)

data class AddStateCenterLabResponse(
    val _result: AddStateCenterLabResult,
    val _resultflag: Int,
    val message: String,
    val fileurl: String,
    val statuscode: Int
)

data class AddStateCenterLabResult(
    val chemical_test_acid_profile_frequency: String,
    val chemical_test_acid_profile_result: String,
    val chemical_test_aflatoxins_frequency: String,
    val chemical_test_aflatoxins_result: String,
    val chemical_test_antibiotic_frequency: String,
    val chemical_test_antibiotic_result: String,
    val chemical_test_metal_frequency: String,
    val chemical_test_metal_result: String,
    val chemical_test_microbiological_frequency: String,
    val chemical_test_microbiological_result: String,
    val chemical_test_others_frequency: String,
    val chemical_test_others_result: String,
    val chemical_test_pesticide_frequency: String,
    val chemical_test_pesticide_result: String,
    val chemical_test_protein_frequency: String,
    val chemical_test_protein_result: String,
    val comments_of_nlm: String,
    val created: String,
    val created_by: Int,
    val data_from: String,
    val district_name: String,
    val districts: Int,
    val doc: Any,
    val doe: Any,
    val equipments_bacterial_avail_site: String,
    val equipments_bacterial_month_install: String,
    val equipments_bacterial_status: Int,
    val equipments_civil_work_avail_site: Int,
    val equipments_civil_work_month_install: String,
    val equipments_civil_work_status: Int,
    val equipments_ftir_avail_site: String,
    val equipments_ftir_month_install: String,
    val equipments_ftir_status: Int,
    val equipments_gcms_avail_site: String,
    val equipments_gcms_month_install: String,
    val equipments_gcms_status: Int,
    val equipments_induced_couple_avail_site: String,
    val equipments_induced_couple_month_install: String,
    val equipments_induced_couple_status: String,
    val equipments_lcms_avail_site: String,
    val equipments_lcms_month_install: String,
    val equipments_lcms_status: Int,
    val financial_viability_cost_incurred: Any,
    val financial_viability_cost_loss: String,
    val financial_viability_cost_profit: String,
    val financial_viability_revenue: Any,
    val future_plans: String,
    val id: Int,
    val is_deleted: Int,
    val is_draft: Int,
    val lat_nlm: Any,
    val location_state_central_lab: String,
    val long_nlm: Any,
    val nabl_certified: String,
    val nabl_certified_date: Any,
    val nabl_certified_status: Any,
    val nabl_certified_validity: Any,
    val skill_emp_details_no: List<String>,
    val skill_emp_details_qualification: List<String>,
    val state_central_lab_visit_document: List<ImplementingAgencyDocument>,
    val state_code: Int,
    val state_name: String,
    val status: String,
    val updated: Any,
    val updated_by: Any,
    val visit: Any,
    val year_of_establishment: String
)

data class AddMilkProcessingResponse(
    val _result: AddMilkProcessingResult,
    val _resultflag: Int,
    val fileurl: String,
    val message: String,
    val statuscode: Int
)

data class AddMilkProcessingResult(
    val addition_new_products: Int?,
    val any_other: String,
    val asset_earmarked_remarks: String,
    val asset_earmarked_under: Int?,
    val created_at: String,
    val created_by: Int,
    val data_from: String,
    val date_of_inspection: String,
    val district_code: Int,
    val district_name: String,
    val has_new_plant: Int?,
    val has_new_plant_remarks: String,
    val id: Int,
    val increase_operational: Int?,
    val increase_sales: Int?,
    val ip_address: Any,
    val is_deleted: Int,
    val is_draft: Int,
    val lat_nlm: Double,
    val lat_processing_plant: Double,
    val long_nlm: Double,
    val long_processing_plant: Double,
    val name_milk_union: String,
    val name_of_tehsil: String,
    val name_processing_plant: String,
    val overall_hygiene: Int?,
    val overall_hygiene_remarks: String,
    val overall_upkeep_asset: Int?,
    val overall_upkeep_asset_remarks: String,
    val processing_capacity_milk: Int?,
    val milk_processing_document: List<ImplementingAgencyDocument>?=null,
    val processing_capacity_productivity: Int?,
    val state_code: Int,
    val state_name: String,
    val status: String,
    val updated_at: Any,
    val updated_by: Any,
    val visit: Any,
    val whether_there: Int?,
    val whether_there_remarks: String
)



data class AddMilkProductMarketingResponse(
    val _result: AddMilkProductMarketingResult,
    val _resultflag: Int,
    val fileurl: String,
    val message: String,
    val statuscode: Int
)

data class AddMilkProductMarketingResult(
    val any_other: String,
    val asset_earmarked_project: Int?,
    val asset_earmarked_project_remarks: String,
    val better_availability_milk: Int?,
    val created_at: String,
    val created_by: Int,
    val data_from: String,
    val date_inspection: String,
    val district_code: Int,
    val district_name: String,
    val id: Int,
    val improvement_shelf_life_milk: Int?,
    val improvement_visibility: Int?,
    val increase_milk: Int?,
    val ip_address: Any,
    val is_deleted: Int,
    val is_draft: Int,
    val lat_nlm: Any,
    val lat_of_milk_parlour: String,
    val long_nlm: Any,
    val long_of_milk_parlour: String,
    val milk_product_marketing_document: List<ImplementingAgencyDocument>,
    val name_milk_union: String,
    val name_retail_shop: String,
    val name_tehsil: String,
    val overall_hygiene: Int?,
    val overall_hygiene_remarks: String,
    val overall_upkeep: Int?,
    val overall_upkeep_remarks: String,
    val provided_used_branding: Int?,
    val provided_used_branding_remarks: String,
    val selection_retailer: Int?,
    val selection_retailer_remarks: String,
    val state_code: Int,
    val state_name: String,
    val status: String,
    val updated_at: Any,
    val updated_by: Any,
    val visit: Any
)



data class AddProductivityEnchancementResponse(
    val _result: AddProductivityEnchancementResult,
    val _resultflag: Int,
    val fileurl: String,
    val message: String,
    val statuscode: Int
)

data class AddProductivityEnchancementResult(
    val animal_mineral_mixture: Int?,
    val are_calves_born: Int?,
    val are_calves_born_remarks: String,
    val assets_distributed: Int?,
    val assets_distributed_remarks: String,
    val assistance_been_provided_remarks: String,
    val created_at: String,
    val created_by: Int,
    val data_from: String,
    val date_of_inspection: String,
    val deworming_of_female_calves: Int?,
    val distribution_fodder_seeds: Int?,
    val district_id: Int,
    val farmer_under_calf_rearing_program_remarks: String,
    val fodder_mineral_mixture: Int?,
    val id: Int,
    val ip_address: Any,
    val is_deleted: Int,
    val is_draft: Int,
    val is_record_keeping: Int?,
    val is_record_keeping_remarks: String,
    val lat_dcs: String,
    val lat_nlm: Any,
    val long_dcs: String,
    val long_nlm: Any,
    val low_cost_silage_making: Int?,
    val name_dcs_mpp: String,
    val name_revenue_village: String,
    val name_tehsil: String,
    val plantation_root_stems: Int?,
    val pregnancy_feed: Int?,
    val productivity_enhancement_services_document: List<ImplementingAgencyDocument>,
    val record_keeping: Int?,
    val record_keeping_remarks: String,
    val selection_village: Int?,
    val selection_village_remarks: String,
    val state_code: Int,
    val state_name: String,
    val district_name: String,
    val status: String,
    val transition_feed_early: Int?,
    val under_anas_program_remarks: String,
    val under_anas_tagged: Int?,
    val under_anas_tagged_remarks: String,
    val updated_at: Any,
    val updated_by: Any,
    val vaccination_female_calves: String?,
    val visible_impacts: String,
    val visit: Any
)


data class MilkUnionAddResponse(
    val _result: MilkUnionAddResult,
    val _resultflag: Int,
    val message: String,
    val fileurl: String,
    val statuscode: Int
)

data class MilkUnionAddResult(
    val avg_buffalo_milk_price: Any,
    val avg_cow_milk_price: Any,
    val buffalo_milk_current_year_incentives: String,
    val buffalo_milk_last_year_incentives: String,
    val capacity_of_milk_chillers: String,
    val cow_milk_current_year_incentives: String,
    val cow_milk_last_year_incentives: String,
    val created_at: String,
    val created_by: Int,
    val current_month_avg_milk: String,
    val current_year_avg_milk: String,
    val current_year_buffalo_milk_price: String,
    val current_year_cow_milk_price: String,
    val daily_bulk_sale_to_other: String,
    val daily_liquid_milk_sale: String,
    val daily_value_added_product: String,
    val data_from: String,
    val districts_covered: String,
    val future_plan_action: String,
    val ia_draft_date: Any,
    val ia_id: Any,
    val ia_status: Int,
    val ia_submitted_date: Any,
    val id: Int,
    val is_deleted: Int,
    val is_draft: Int,
    val last_fy_cumulative_profit_loss: String,
    val last_fy_net_profit_loss: String,
    val last_fy_turnover: String,
    val last_year_avg_milk: String,
    val last_year_buffalo_milk_price: String,
    val last_year_cow_milk_price: String,
    val lat_nlm: Any,
    val long_nlm: Any,
    val milk_chilling_plant_capacity: Any,
    val milk_chilling_plant_location: Any,
    val milk_chilling_plant_total: Any,
    val milk_processing_plant_capacity: String,
    val milk_processing_plant_location: String,
    val milk_processing_plant_total: Int,
    val milk_union_visit_report_document: List<ImplementingAgencyDocument>,
    val name_of_milk_union: String,
    val nlm_draft_date: Any,
    val nlm_id: Any,
    val nlm_status: Int,
    val nlm_submitted_date: Any,
    val no_of_dairy_functional: Int,
    val no_of_dairy_organised: Int,
    val no_of_dairy_total: Any,
    val no_of_milk_chillers: Int,
    val no_of_milk_producers_enr: Int,
    val no_of_villages: Int,
    val no_of_villages_covered: Int,
    val photographs_of_site: String,
    val remarks_by_nlm: String,
    val staff_available: String,
    val staff_sanctioned: String,
    val state_code: Int,
    val state_name: String,
    val status: String,
    val total_field_staff: Any,
    val total_non_technical_staff: Any,
    val total_plant_staff: Any,
    val updated_at: Any,
    val visit: Int
)

