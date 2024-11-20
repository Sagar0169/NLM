package com.nlm.model

import java.io.Serializable

data class DashboardData(
    val title: String,
    val image: Int,
) : Serializable

data class TrackerData(
    val title: String,
    val unCompleteCircle: Int?
) : Serializable

data class OnBoardingImageData(
    val imageResId: Int, // Resource ID of the image
    val title: String,
    val description: String
)

data class OtpRequest(
    val application_number: String,
    val otp: String
)

data class LoginRequest(
    var username: String,
    var password: String,
)

data class LogoutRequest(
    var user_id: Int,
)

data class MyAccountRequest(
    val application_number: String
)

data class ApplicationStatusRequest(
    val application_number: String
)

data class ImplementingAgencyRequest(
    val role_id: Int?,
    val state_code: Int?,
    val user_id: Int?,
    val name_location_of_ai: String?,
    val limit: Int,
    val page: Int
)

data class RspLabListRequest(
    val role_id: Int?,
    val state_code: Int?,
    val user_id: Int?,
    val phone_no: String?,
    val district_code: Int,
    val limit: Int,
    val page: Int
)

data class ImportExocticGoatRequest(
    val limit: Int?,
    val page: Int?,
    val role_id: String?,
    val state_code: Int?,
    val user_id: String?,
    val number_of_farmers_benefited: String?,
)

data class ArtificialInseminationRequest(
    val role_id: Int?,
    val state_code: Int?,
    val district_code: Int?,
    val user_id: Int?,
    val limit: Int,
    val liquid_nitrogen: String?,
    val frozen_semen_straws: String?,
    val cryocans: String?,
    val page: Int
)

data class StateSemenBankRequest(
    val role_id: Int?,
    val state_code: Int?,
    val user_id: Int?,
    val location: String?,
    val phone_no: String?,
    val district_code: Int,
    val limit: Int,

    val page: Int
)

data class ArtificialInseminationAddRequest(

    val artificial_insemination_document: ArrayList<ImplementingAgencyDocument>?=null,
    val artificial_insemination_observation_by_nlm: List<ArtificialInseminationObservationByNlm>?=null,
    val cryocans: String?=null,
    val district_code: Int?=null,
    val exotic_sheep_goat: String?=null,
    val frozen_semen_straws: String?=null,
    val is_deleted: Int?=null,
    val is_draft: Int?=null,
    val liquid_nitrogen: String?=null,
    val role_id: Int?=null,
    val state_code: Int?=null,
    val total_sheep_goat_labs: Int?=null,
    val user_id: Int?=null,
    val is_type: String?=null,
    val id: Int?=null
)

data class ArtificialInseminationDocument(
    val description: String,
    val id: Int
)

data class ArtificialInseminationObservationByNlm(
    val name_of_center: String,
    val number_of_ai_performed: Int,
    val power_trained_ai: String,
    val quipment_available: String
)

data class ImportExoticGoatAddEditRequest(
    val comment_by_nlm_whether: Int?,
    val import_of_exotic_goat_achievement: MutableList<ImportOfExoticGoatAchievement>?,
    val import_of_exotic_goat_detail_import: MutableList<ImportOfExoticGoatDetailImport>?,
    val import_of_exotic_goat_verified_nlm: MutableList<ImportOfExoticGoatVerifiedNlm>?,
    val import_of_exotic_goat_document: MutableList<ImplementingAgencyDocument>?,
    val role_id: Int?,
    val state_code: Int?,
    val number_of_farmers_benefited: Int?,
    val user_id: String?,
    val is_type: String?,
    val id: Int?,
    val is_deleted: Int?,
    val is_draft: Int?
)

data class ImportOfExoticGoatAchievement(
    val balance: String?,
    val f1_generation_produced: String?,
    val f2_generation_produced: String?,
    val no_of_animals_f1: Int?,
    val no_of_animals_f2: Int?,
    val number_of_animals: Int?,
    val performance_animals_doorstep: String?,
    val import_of_exotic_goat_id: Int?=null,
    val id: Int?,
)

data class ImportOfExoticGoatDetailImport(
    val place_of_induction: String?,
    val place_of_procurement: String?,
    val procurement_cost: Int?,
    val species_breed: String?,
    val unit: String?,
    val year: String?,
    val import_of_exotic_goat_id: Int?,
    val id: Int?,
)

data class ImportOfExoticGoatVerifiedNlm(
    val f1_generation_produced: String?,
    val f2_generation_distributed: String?,
    val f2_generation_produced: String?,
    val id: Int?,
    val import_of_exotic_goat_id: Int?,
    val number_of_animals: Int?,
    val species_breed: String?,
    val year: String?

)

data class GetDropDownRequest(
    val limit: Int?,
    val model: String?,
    val page: Int?,
    val state_code: Int?,
    val user_id: Int?
)

data class DocumentData(
    val description: String?,
    val name_doc: String?,
)

data class ImplementingAgencyAddRequest(
    val part: String? = null,
    val state_code: Int? = null,
    val name_location_of_ai: String? = null,
    val director_dg_ceo_name: String? = null,
    val technical_staff_regular_employee: Int? = null,
    val technical_staff_manpower_deputed: Int? = null,
    val admn_staff_regular_employee: Int? = null,
    val admn_staff_manpower_deputed: Int? = null,
    val other_staff_regular_employee: Int? = null,
    val other_staff_manpower_deputed: Int? = null,
    val organizational_chart: String? = null,
    val frozen_semen_goat_number: Int? = null,
    val frozen_semen_goat_location: String? = null,
    val liquid_semen_sheep_number: Int? = null,
    val liquid_semen_sheep_location: String? = null,
    val production_capacity_number: Int? = null,
    val production_capacity_location: String? = null,
    val actual_production_number: Int? = null,
    val actual_production_location: String? = null,
    val no_semen_distributed_number: Int? = null,
    val no_semen_distributed_location: String? = null,
    val no_semen_neighboring_number: Int? = null,
    val no_semen_neighboring_location: String? = null,
    val availability_liquid_nitrogen_number: Int? = null,
    val availability_liquid_nitrogen_location: String? = null,
    val breeding_farms_number: Int? = null,
    val breeding_farms_location: String? = null,
    val goat_breeding_farm_number: Int? = null,
    val goat_breeding_farm_location: String? = null,
    val training_centers_number: Int? = null,
    val training_centers_location: String? = null,
    val number_of_cattle_ai_number: Int? = null,
    val number_of_cattle_ai_location: String? = null,
    val total_ai_performed_number: Int? = null,
    val total_ai_performed_location: String? = null,
    val total_reginal_semen_bank: Int? = null,
    val implementing_agency_advisory_committee: List<ImplementingAgencyAdvisoryCommittee>? = null,
    val implementing_agency_project_monitoring: List<ImplementingAgencyProjectMonitoring>? = null,
    val frequency_of_monitoring_1: String? = null,
    val frequency_of_monitoring_2: String? = null,
    val reporting_mechanism_1: String? = null,
    val reporting_mechanism_2: String? = null,
    val regularity_1: String? = null,
    val regularity_2: String? = null,
    val submission_of_quarterly_1: String? = null,
    val submission_of_quarterly_2: String? = null,
    val studies_surveys_conducted: String? = null,
    val implementing_agency_funds_received: List<ImplementingAgencyFundsReceived>? = null,
    val no_of_al_technicians: Int? = null,
    val number_of_ai: Int? = null,
    val total_paravet_trained: Int? = null,
    val implementing_agency_involved_district_wise: List<ImplementingAgencyInvolvedDistrictWise>? = null,
    val infrastructural: String? = null,
    val organizational: String? = null,
    val funds: String? = null,
    val any_other: String? = null,
    val any_assets_created: String? = null,
    val assessments_of_green: String? = null,
    val availability_of_green_area: String? = null,
    val availability_of_dry: String? = null,
    val availability_of_concentrate: String? = null,
    val availability_of_common: String? = null,
    val efforts_of_state: String? = null,
    val name_of_the_agency: String? = null,
    val quantity_of_fodder: String? = null,
    val distribution_channel: String? = null,
    val number_of_fodder: String? = null,
    val id: Int? = null,// it is the id of the element we want to edit
    val is_draft: Int? = null,//always 1 till the user clicks on submit button then it will be 0
    val present_system: String? = null,
    val sheep_breeding_farms_location: String? = null,
    val sheep_breeding_farms_number: Int? = null,
    val status: Int? = null,
    val user_id: String? = null,
    val is_type: String? = null,
    val implementing_agency_document: List<ImplementingAgencyDocument>? = null,
    val is_deleted: Int? = null,

    )

data class ImplementingAgencyAdvisoryCommittee(
    val name_of_the_official: String?,
    val designation: String?,
    val organization: String?,
    val implementing_agency_id: Int?,
    val id: Int?,
)
data class IdAndDetails(
    val name_of_the_official: String?,
    val designation: String?,
    val organization: String?,
    val implementing_agency_id: Int?,
    val id: Int?,
)
data class manPower(
    val name_of_the_official: String?,
    val designation: String?,
    val organization: String?,
    val implementing_agency_id: Int?,
    val id: Int?,
)

data class StateSemenManPower(
    val designation: String?,
    val qualification: String?,
    val experience: String?,
    val trainingStatus: String?,
    val id: Int?,
)

data class StateSemenInfraGoat(
    val infrastructure_list_of_equipment: String?,
    val infrastructure_year_of_procurement: String?,
    val id: Int?,
    val infra_goat_id:Int?
)

data class RspBasicInfoEquipment(
    val rsp_list_of_equipment: String?,
    val rsp_year_of_procurement: String?,
    val rsp_make: String?,
    val id: Int?,
)




//data class ImplementingAgencyDocument(
//    val description: String?,
//    val id: Int?,
//    val name_doc: String?
//)


data class StateSemenBankNLMRequest(
    val is_type: String? = null,

    val address: String? = null,
    val area_fodder_cultivation: String? = null,
    val area_under_buildings: String? = null,
    val district_code: Int? = null,
    val is_deleted: Int? = null,
    val id: Int? = null,
    val is_draft: Int? = null,
    val location: String? = null,
    val major_clients_coop_fin_year_one: String? = null,
    val major_clients_coop_fin_year_three: String? = null,
    val major_clients_coop_fin_year_two: String? = null,
    val major_clients_ngo_fin_year_one: String? = null,
    val major_clients_ngo_fin_year_three: String? = null,
    val major_clients_ngo_fin_year_two: String? = null,
    val major_clients_other_states_fin_year_one: String? = null,
    val major_clients_other_states_fin_year_three: String? = null,
    val major_clients_other_states_fin_year_two: String? = null,
    val major_clients_private_fin_year_one: String? = null,
    val major_clients_private_fin_year_three: String? = null,
    val major_clients_private_fin_year_two: String? = null,
    val manpower_no_of_people: Int? = null,
    val officer_in_charge_name: String? = null,
    val phone_no: Long? = null,
    val pin_code: Int? = null,
    val quality_status: String? = null,
    val role_id: Int? = null,
    val state_code: Int? = null,
    val state_semen_bank_document: List<ImplementingAgencyDocument>? = null,
    val state_semen_bank_infrastructure: List<StateSemenInfraGoat>? = null,
    val state_semen_bank_other_manpower: MutableList<StateSemenBankOtherAddManpower>? = null,
    val status: Int? = null,
    val storage_capacity: String? = null,
    val type_of_semen_station: String? = null,
    val user_id: String? = null,
    val year_of_establishment: String? = null
)

data class StateSemenBankDocument(
    val description: String? = null
)

data class StateSemenBankInfrastructure(
    val infrastructure_list_of_equipment: String? = null,
    val infrastructure_year_of_procurement: String? = null
)

data class StateSemenBankOtherManpower(
    val designation: String? = null,
    val experience: String? = null,
    val qualification: String? = null,
    val training_status: String? = null
)
data class StateSemenBankOtherAddManpower(
    val designation: String? = null,
    val experience: String? = null,
    val qualification: String? = null,
    val training_status: String? = null,
    val created: String? = null,
    val id: Int? = null,
    val state_semen_bank_id: Int? = null,
)

data class ImplementingAgencyFundsReceived(
    val year: String?,
    val from_dahd: Int?,
    val state_govt: Int?,
    val any_other: Int?,
    val physical_progress: Int?,
    val id: Int?,
    val implementing_agency_id: Int?,
)

data class ImplementingAgencyInvolvedDistrictWise(
    val name_of_district: String?,
    val location_of_ai_centre: String?,
    val ai_performed: String?,
    val id: Int?,
    val year: Any?,
    val implementing_agency_id: Int?,
)

data class ImplementingAgencyProjectMonitoring(
    val name_of_official: String?,
    val designation: String?,
    val organization: String?,
    val id: Int?,
    val implementing_agency_id: Int?,
)

data class ArtificialInseminationAddEditRequest(
    val ai_centre_cattle_buffalo: Int,
    val ai_centre_goat_frozen_semen: Int,
    val ai_performed_average: Double,
    val artificial_insemination_observation_by_nlm: List<ArtificialInseminationObservationByNlm>,
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
    val state_code: Int,
    val status: Int,
    val total_sheep_goat_labs: Int,
    val user_id: String
)

data class NlmAssistanceForQFSPListRequest(
    val district_code: Int?=null,
    val limit: Int?=null,
    val name_of_organization: String?=null,
    val organogram: String?=null,
    val page: Int?=null,
    val role_id: Int?=null,
    val state_code: Int?=null,
    val user_id: Int?=null
)


data class RSPAddRequest(
    val Infrastructure_faced_Institute: String? = null,
    val address: String? = null,
    val location: String? = null,
    val is_draft: Int? = null,
    val area_for_fodder_cultivation: String? = null,
    val area_under_buildings: Double? = null,
    val availability_no_of_computers: Int? = null,
    val availability_type_of_records: String? = null,
    val district_code: Int? = null,
    val is_deleted: Int? = null,
    val is_draft_ia: Int? = null,
    val is_draft_nlm: Int? = null,
    val major_clients_coop_one_year: String? = null,
    val major_clients_coop_three_year: String? = null,
    val major_clients_coop_two_year: String? = null,
    val major_clients_ngo_one_year: String? = null,
    val major_clients_ngo_three_year: String? = null,
    val major_clients_ngo_two_year: String? = null,
    val major_clients_other_states_one_year: String? = null,
    val major_clients_other_states_three_year: String? = null,
    val major_clients_other_states_two_year: String? = null,
    val major_clients_private_one_year: String? = null,
    val major_clients_private_three_year: String? = null,
    val major_clients_private_two_year: String? = null,
    val major_clients_sia_one_year: String? = null,
    val major_clients_sia_three_year: String? = null,
    val major_clients_sia_two_year: String? = null,
    val manpower: String? = null,
    val phone_no: Long? = null,
    val pin_code: Int? = null,
    val role_id: Int? = null,
    val is_type: String? = null,

    val id: Int? = null,
    val rsp_laboratory_semen_availability_equipment: List<RspAddEquipment>? = null,
    val rsp_laboratory_semen_average: List<RspAddAverage>? = null,
    val rsp_laboratory_semen_document: List<ImplementingAgencyDocument>? = null,
    val rsp_laboratory_semen_station_quality_buck: List<RspAddBucksList>? = null,
    val state_code: Int? = null,
    val user_id: String? = null,
    val year_of_establishment: Int? = null,
    val comments_infrastructure: String?= null,
    val processing_semen: String?= null,
    val equipments_per_msp: String?= null,
    val fund_properly_utilized: String?= null,
    val semen_straws_produced: String?= null,
    val suggestions_physical: String?= null,
    val suggestions_financial: String?= null,
    val suggestions_any_other: String?= null,
)

data class RspLaboratorySemenAvailabilityEquipment(
    val list_of_equipment: String? = null,
    val make: String? = null,
    val year_of_procurement: String? = null
)

data class RspLaboratorySemenAverage(
    val name_of_breed: String? = null,
    val twentyOne_twentyTwo: String? = null,
    val twentyThree_twentyFour: String? = null,
    val twentyTwo_twentyThree: String? = null
)
data class RspLaboratorySemenDocument(
    val description: String? = null,
    val id: Int? = null
)

data class FpsPlantStorageRequest(
    val user_id: Int?= null,
    val role_id: Int?= null,
    val state_code: Int?= null,
    val district_code: Int?=null,
    val name_of_organization: String?=null,
    val limit: Int?= null,
    val page: Int?= null
)

data class FodderProductionFromNonForestRequest(
    val area_covered: Int?= null,
    val district_code: Int?= null,
    val limit: Int?= null,
    val name_implementing_agency: String?= null,
    val page: Int?= null,
    val role_id: Int?= null,
    val state_code: Int?= null,
    val user_id: Int?= null
)

data class FpFromForestLandRequest(
    val area_covered: String?= null,
    val district_code: Int?=null,
    val limit: Int?=null,
    val name_implementing_agency: String?=null,
    val page: Int?= null,
    val role_id: Int?= null,
    val state_code: Int?=null,
    val user_id: Int?=null
)

data class AssistanceForEARequest(
    val limit: Int?= null,
    val page: Int?= null,
    val role_id: Int?= null,
    val state_code: Int?= null,
    val user_id: Int?= null,
)

data class NLMEdpRequest(
    val limit: Int?= null,
    val page: Int?= null,
    val role_id: Int?= null,
    val state_code: Int?= null,
    val user_id: Int?= null,
)

data class NLMAhidfRequest(
    val limit: Int?= null,
    val page: Int?= null,
    val role_id: Int?= null,
    val state_code: Int?= null,
    val user_id: Int?= null,
)

