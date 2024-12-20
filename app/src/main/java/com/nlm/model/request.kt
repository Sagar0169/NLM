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
    val year_of_establishment: String?,
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
    val year_of_establishment: String?,
    val district_code: Int,
    val limit: Int,

    val page: Int
)
data class Format6AssistanceForQspAddEdit(
    val area_under_production: Double?=null,
    val assistance_for_qfsp_cost_assistance: List<AssistanceForQfspCostAssistance>?=null,
    val assistance_for_qfsp_document: List<ImplementingAgencyDocument>?=null,
    val assistance_for_qfsp_financial_progress: List<AssistanceForQfspFinancialProgres>?=null,
    val created_by: Int?=null,
    val district_code: Int?=null,
    val effective_seed: Int?=null,
    val id: Int?=null,
    val is_deleted: Int?=null,
    val is_draft: Int?=null,
    val is_type: String?=null,
    val location_address: String?=null,
    val name_of_organization: String?=null,
    val organogram: String?=null,
    val quantity_of_fodder_seed_class: Int?=null,
    val quantity_of_fodder_seed_variety: Int?=null,
    val quantity_of_seed_class: Int?=null,
    val quantity_of_seed_variety: Int?=null,
    val role_id: Int?=null,
    val source_of_seed: String?=null,
    val state_code: Int?=null,
    val status: Int?=null,
    val target_achievement_class: Int?=null,
    val target_achievement_variety: String?=null,
    val technical_competance: String?=null,
    val user_id: Int?=null,
    val lattitude: Double?=null,
    val longitude: Double?=null,
)



data class AssistanceForQfspDocument(
    val description: String
)
data class FpFromForestLandAddEditFormat9Request(
    val is_type: String?=null,
    val area_covered: Int?=null,
    val created_by: Int?=null,
    val district_code: Int?=null,
    val fp_from_forest_land_document: List<ImplementingAgencyDocument>?=null,
    val fp_from_forest_land_filled_by_nlm: List<FpFromForestLandFilledByNlm>?=null,
    val grant_received: String?=null,
    val id: Int?=null,
    val is_deleted: Int?=null,
    val is_draft: Int?=null,
    val location_address: String?=null,
    val name_implementing_agency: String?=null,
    val role_id: Int?=null,
    val scheme_guidelines: String?=null,
    val state_code: Int?=null,
    val status: Int?=null,
    val target_achievement: String?=null,
    val type_of_agency: String?=null,
    val type_of_land: String?=null,
    val user_id: Int?=null,
    val variety_of_fodder: String?=null,
    val lattitude_ia: Double? = null,
    val longitude_ia: Double? = null,
    val lattitude_nlm: Double? = null,
    val longitude_nlm : Double? = null,
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
    val id: Int?=null,
    val lattitude_ia: Double? = null,
    val longitude_ia: Double? = null,
    val lattitude_nlm: Double? = null,
    val longitude_nlm : Double? = null,
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
    val is_draft: Int?,
    val lattitude_ia: Double? = null,
    val longitude_ia: Double? = null,
    val lattitude_nlm: Double? = null,
    val longitude_nlm : Double? = null,
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
data class GetNlmDropDownRequest(
    val column: String?,
    val user_id: Int?
)
data class GetDropDownRequest(
    val limit: Int?,
    val model: String?,
    val page: Int?,
    val state_code: Int?,
    val user_id: Int?
)

data class SubTableDeleteRequest(
    val id: Int?=null,
    val tablename: String?=null
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
    val lattitude: Double? = null,
    val longitude: Double? = null,
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
    val longitude: Double? = null,
    val lattitude: Double? = null,
    val storage_capacity: String? = null,
    val type_of_semen_station: String? = null,
    val user_id: String? = null,
    val year_of_establishment: String? = null,

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
    val lattitude_ia: Double? = null,
    val longitude_ia : Double? = null,
    val lattitude_nlm : Double? = null,
    val longitude_nlm  : Double? = null,
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

data class NlmFpFromNonForestAddRequest(
    val created: String?=null,
    val created_at: String?=null,
    val created_by: Int?=null,
    val district_code: Int?=null,
    val fp_from_non_forest_document: List<ImplementingAgencyDocument>?=null,
    val fp_from_non_forest_filled_by_nlm_team: List<FpFromNonForestFilledByNlmTeam>?=null,
    val id: Int?=null,
    val is_deleted: Int?=null,
    val is_draft: Int?=null,
    val is_draft_nlm: Int?=null,
    val name_implementing_agency: String?=null,
    val role_id: Int?=null,
    val state_code: Int?=null,
    val status: Int?=null,
    val user_id: String?=null,
    val is_type: String?=null,
    val area_covered: Double?=null,
    val grant_received: String?=null,
    val location: String?=null,
    val scheme_guidelines: String?=null,
    val target_achievement: String?=null,
    val type_of_agency: String?=null,
    val type_of_land: String?=null,
    val variety_of_fodder: String?=null,
    val lattitude_ia: Double? = null,
    val longitude_ia: Double? = null,
    val lattitude_nlm: Double? = null,
    val longitude_nlm : Double? = null,
)




//data class Result(
//    val created: String?,
//    val created_at: String?,
//    val created_by: Int?,
//    val district_code: Int?,
//    val fp_from_non_forest_document: List<FpFromNonForestDocument?>?,
//    val fp_from_non_forest_filled_by_nlm_team: List<FpFromNonForestFilledByNlmTeam?>?,
//    val id: Int?,
//    val is_deleted: Int?,
//    val is_draft: Int?,
//    val is_draft_nlm: Int?,
//    val name_implementing_agency: String?,
//    val role_id: String?,
//    val state_code: Int?,
//    val status: Int?,
//    val user_id: String?
//)

//data class FpFromNonForestDocument(
//    val description: String?,
//    val fp_from_non_forest_id: Int?,
////    val id: Int?,
//    val nlm_document: String?
//)

data class FpFromNonForestFilledByNlmTeam(
    val id: Int?,
    val district_code: Int?,
    val district: District_list?,
    val district_name: String?,
    val block_name: String?,
    val village_name: String?,
    val area_covered: String?,
    val estimated_quantity: String?,
    val consumer_fodder: String?,
    val agency_involved: String?,
    val fp_from_non_forest_id: Int?,
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

data class AddFspPlantStorageRequest(
    val assistance_for_qfsp_document: List<ImplementingAgencyDocument>?=null,
    val capacity_of_plant: String?=null,
    val is_type: String? = null,
    val certification_recognition: String?=null,
    val created_at: String?=null,
    val created_by: Int?=null,
    val district_code: Int?=null,
    val fsp_plant_storage_comments_of_nlm: List<FspPlantStorageCommentsOfNlm>?=null,
    val fsp_plant_storage_document: List<ImplementingAgencyDocument>?=null,
    val id: Int?=null,
    val is_deleted: Int?=null,
    val is_draft: Int?=null,
    val is_draft_ia: Int?=null,
    val location_address: String?=null,
    val machinery_equipment_available: String?=null,
    val name_of_organization: String?=null,
    val purpose_of_establishment: String?=null,
    val quantity_fodder_seed_class: String?=null,
    val quantity_fodder_seed_variety: String?=null,
    val role_id: Int?=null,
    val state_code: Int?=null,
    val status: Int?=null,
    val technical_expertise: String?=null,
    val user_id: String?=null,
    val lattitude_ia: Double? = null,
    val longitude_ia: Double? = null,
    val lattitude_nlm: Double? = null,
    val longitude_nlm : Double? = null,
)
data class FspPlantStorageCommentsByNlm(
    val address: String,
    val infrastructure_available: String,
    val name_of_agency: String,
    val quantity_of_seed_graded: String
)

data class AddAssistanceEARequest(
    val assistance_for_ea_document: List<ImplementingAgencyDocument?>?=null,
    val assistance_for_ea_training_institute: List<AssistanceForEaTrainingInstitute?>??=null,
    val created_by: Int?=null,
    val details_of_training_programmes: String?=null,
    val district_code: Int?=null,
    val id: Int?=null,
    val is_deleted: Int?=null,
    val is_draft: Int?=null,
    val no_of_camps: Int?=null,
    val no_of_participants: Int?=null,
    val role_id: Int?=null,
    val is_type: String? = null,
    val state_code: Int?=null,
    val status: Int?=null,
    val user_id: String?=null,
    val whether_the_state_developed: String?=null,
    val whether_the_state_trainers: String?=null,
    val lattitude_ia: Double? = null,
    val longitude_ia: Double? = null,
    val lattitude_nlm: Double? = null,
    val longitude_nlm : Double? = null,
)


data class AddNlmEdpRequest(
    val created_by: Int?= null,
    val id: Int?= null,
    val is_type: String? = null,
    val remarks_by_nlm: String? = null,
    val is_deleted: Int?= null,
    val is_draft: Int?=null,
    val is_draft_nlm: Int?= null,
    val nlm_edp_document: List<ImplementingAgencyDocument?>?= null,
    val nlm_edp_format_for_nlm: List<NlmEdpFormatForNlm>?= null,
    val lattitude_ia: Double? = null,
    val longitude_ia : Double? = null,
    val lattitude_nlm : Double? = null,
    val longitude_nlm  : Double? = null,
    val nlm_edp_monitoring: List<NlmEdpMonitoring>?= null,
    val role_id: Int?= null,
    val state_code: Int?= null,
    val status: Int?= null,
    val user_id: String?= null
)


data class AddAnimalRequest(
    val ahidf_document: List<ImplementingAgencyDocument>?=null,
    val ahidf_format_for_nlm: List<AhidfFormatForNlm>?=null,
    val ahidf_monitoring: List<AhidfMonitoring>?=null,
    val created_by: Int?= null,
    val is_deleted: Int?= null,
    val id: Int?= null,
    val remarks_by_nlm: String? = null,
    val is_type: String? = null,
    val is_draft: Int?= null,
    val role_id: Int?= null,
    val state_code: Int?= null,
    val status: Int?= null,
    val user_id: String?= null,
    val lattitude_ia: Double? = null,
    val longitude_ia: Double? = null,
    val lattitude_nlm: Double? = null,
    val longitude_nlm : Double? = null,
)

data class VaccinationProgrammerListRequest(
    val limit: Int,
    val page: Int,
    val role_id: Int?,
    val state_code: Int?,
    val user_id: Int?,
)

data class MobileVeterinaryUnitsListRequest(
    val limit: Int,
    val page: Int,
    val role_id: Int?,
    val district_code: Int?=null,
    val block_name: String?=null,
    val village_name: String?=null,
    val state_code: Int?,
    val user_id: Int?,
)

data class AscadListRequest(
    val limit: Int,
    val page: Int,
    val role_id: Int?,
    val state_code: Int?,
    val district_code: Int?=null,
    val user_id: Int?,
)

data class StateVaccinationProgrammeAddRequest(
    val is_draft: Int?=null,
    val created_by: Int?= null,
    val id: Int?= null,
    val is_deleted: Int?= null,
    val is_type: String?= null,
    val process_plan_monitoring_inputs: String?= null,
    val process_plan_monitoring_remarks: String?= null,
    val process_plan_monitoring_upload: String?= null,
    val role_id: Int?= null,
    val schedule_vaccination_arrangement_inputs: String?= null,
    val schedule_vaccination_arrangement_remarks: String?= null,
    val schedule_vaccination_arrangement_upload: String?= null,
    val schedule_vaccination_assign_areas_inputs: String?= null,
    val schedule_vaccination_assign_areas_remarks: String?= null,
    val schedule_vaccination_assign_areas_upload: String?= null,
    val schedule_vaccination_cold_chain_avail_inputs: String?= null,
    val schedule_vaccination_cold_chain_avail_remarks: String?= null,
    val schedule_vaccination_cold_chain_avail_upload: String?= null,
    val schedule_vaccination_focal_point_input: String?= null,
    val schedule_vaccination_focal_point_remark: String?= null,
    val schedule_vaccination_focal_point_upload: String?= null,
    val schedule_vaccination_timeline_inputs: String?= null,
    val schedule_vaccination_timeline_remarks: String?= null,
    val schedule_vaccination_timeline_upload: String?= null,
    val seromonitoring_facilitie_remarks: String?= null,
    val seromonitoring_facilities_input: String?= null,
    val seromonitoring_facilities_upload: String?= null,
    val state_code: Int?= null,
    val status: Int?= null,
    val user_id: Int?= null,
    val longitude: Double?= null,
    val latitude: Double?= null
)

data class DistrictVaccinationProgrammeAddRequest(
    val are_functionaries_aware_inputs: String?= null,
    val are_functionaries_aware_remarks: String?= null,
    val are_functionaries_aware_uploads: String?= null,
    val created_by: Int?= null,
    val district_code: Int?= null,
    val district_name: String?= null,
    val investigate_suspected_outbreak_inputs: String?= null,
    val investigate_suspected_outbreak_remarks: String?= null,
    val investigate_suspected_outbreak_uploads: String?= null,
    val is_deleted: Int?= null,
    val mass_education_campaign_inputs: String?= null,
    val mass_education_campaign_remarks: String?= null,
    val mass_education_campaign_uploads: String?= null,
    val mechanisim_followed_inputs: String?= null,
    val mechanisim_followed_remarks: String?= null,
    val mechanisim_followed_uploads: String?= null,
    val role_id: Int?= null,
    val id: Int?= null,
    val is_type: String?= null,
    val state_code: Int?= null,
    val state_name: String?= null,
    val status: Int?= null,
    val trained_staff_engaged_inputs: String?= null,
    val trained_staff_engaged_remarks: String?= null,
    val trained_staff_engaged_uploads: String?= null,
    val user_id: Int?= null,
    val longitude: Double?= null,
    val latitude: Double?= null
)

data class FarmerVaccinationProgrammeAddRequest(
    val animal_vaccinated_inputs: String?= null,
    val animal_vaccinated_remarks: String?= null,
    val animal_vaccinated_uploads: String?= null,
    val awarness_of_the_govt_inputs: String?= null,
    val awarness_of_the_govt_remarks: String?= null,
    val awarness_of_the_govt_uploads: String?= null,
    val created_by: Int?= null,
    val district_code: Int?= null,
    val district_name: String?= null,
    val state_name: String?= null,
    val id: Int?= null,
    val is_type: String?= null,
    val is_deleted: Int?= null,
    val recall_vaccination_inputs: String?= null,
    val recall_vaccination_remarks: String?= null,
    val recall_vaccination_uploads: String?= null,
    val role_id: Int?= null,
    val state_code: Int?= null,
    val status: Int?= null,
    val user_id: Int?= null,
    val vaccination_carrier_inputs: String?= null,
    val vaccination_carrier_remarks: String?= null,
    val vaccination_carrier_uploads: String?= null,
    val vaccinator_visit_inputs: String?= null,
    val vaccinator_visit_remarks: String?= null,
    val vaccinator_visit_uploads: String?= null,
    val village_name: String?= null,
    val longitude: Double?= null,
    val latitude: Double?= null
)

data class StateMobileVeterinaryUnitAddRequest(
    val are_adequate_staff_inputs: String?= null,
    val are_adequate_staff_remarks: String?= null,
    val are_operators_engaged_inputs: String?= null,
    val are_operators_engaged_remarks: String?= null,
    val call_center_inputs: String?= null,
    val call_center_remarks: String?= null,
    val created_by: Int?= null,
    val data_compilation_analysis_done_inputs: String?= null,
    val data_compilation_analysis_done_remarks: String?= null,
    val engagement_indicators_inputs: String?= null,
    val engagement_indicators_remarks: String?= null,
    val input_are_adequate_staff: String?= null,
    val input_are_operators_engaged: String?= null,
    val input_call_center: String?= null,
    val input_data_compilation_analysis_done: String?= null,
    val input_engagement_indicators: String?= null,
    val input_is_app_crm_place: String?= null,
    val input_is_building_provided_operation_seats: String?= null,
    val input_is_monitoring_supervision_fuel: String?= null,
    val input_is_monitoring_supervision_medic_equip: String?= null,
    val input_is_service_provider_engaged: String?= null,
    val input_mechanism_operation: String?= null,
    val input_procurement_procedure: String?= null,
    val input_supply_procedure: String?= null,
    val is_app_crm_place_inputs: String?= null,
    val is_app_crm_place_remarks: String?= null,
    val is_building_provided_operation_seats_inputs: String?= null,
    val is_building_provided_operation_seats_remarks: String?= null,
    val is_deleted: Int?= null,
    val is_monitoring_supervision_fuel_inputs: String?= null,
    val is_monitoring_supervision_fuel_remarks: String?= null,
    val is_monitoring_supervision_medic_equip_inputs: String?= null,
    val is_monitoring_supervision_medic_equip_remarks: String?= null,
    val is_service_provider_engaged_inputs: String?= null,
    val is_service_provider_engaged_remaks: String?= null,
    val mechanism_operation_inputs: String?= null,
    val mechanism_operation_remarks: String?= null,
    val procurement_procedure_inputs: String?= null,
    val procurement_procedure_remarks: String?= null,
    val role_id: Int?= null,
    val id: Int?= null,
    val state_code: Int?= null,
    val status: Int?= null,
    val supply_procedure_inputs: String?= null,
    val supply_procedure_remarks: String?= null,
    val user_id: Int?= null,
    val is_draft: Int?= null,
    val is_type: String? = null,
    val longitude: Double?= null,
    val latitude: Double?= null
    )

data class DistrictMobileVeterinaryUnitAddRequest(
    val created_by: Int?= null,
    val distribution_fuel_role_inputs: String?= null,
    val distribution_fuel_role_inputs_remarks: String?= null,
    val distribution_medicines_role_inputs: String?= null,
    val distribution_medicines_role_remaks: String?= null,
    val district_code: Int?= null,
    val input_distribution_fuel_role: String?= null,
    val input_distribution_medicines_role: String?= null,
    val input_mechanism_medicines: String?= null,
    val input_medicine_requirement: String?= null,
    val input_organize_awareness_camp: String?= null,
    val is_deleted: Int?= null,
    val mechanism_medicines_inputs: String?= null,
    val mechanism_medicines_remaks: String?= null,
    val medicine_requirement_inputs: String?= null,
    val medicine_requirement_remarks: String?= null,
    val organize_awareness_camp_inputs: String?= null,
    val organize_awareness_camp_remarks: String?= null,
    val role_id: Int?= null,
    val state_code: Int?= null,
    val status: Int?= null,
    val user_id: Int?= null,
    val id: Int?= null,
    val is_draft: Int?= null,
    val is_type: String? = null,
    val longitude: Double?= null,
    val latitude: Double?= null
)

data class BlockMobileVeterinaryUnitAddRequest(
    val any_other_block_inputs: String?= null,
    val any_other_block_remarks: String?= null,
    val block_name: String?= null,
    val created_by: Int?= null,
    val district_code: Int?= null,
    val general_monitoring_block_inputs: String?= null,
    val general_monitoring_block_remarks: String?= null,
    val input_any_other_block: String?= null,
    val input_general_monitoring_block: String?= null,
    val input_machanism_attendance_staff: String?= null,
    val input_monitoring_tracking_call: String?= null,
    val input_stock_management: String?= null,
    val is_deleted: Int?= null,
    val machanism_attendance_staff_inputs: String?= null,
    val machanism_attendance_staff_remarks: String?= null,
    val monitoring_tracking_call_inputs: String?= null,
    val monitoring_tracking_call_remarks: String?= null,
    val role_id: Int?= null,
    val state_code: Int?= null,
    val status: String?= null,
    val stock_management_inputs: String?= null,
    val stock_management_remarks: String?= null,
    val user_id: Int?= null,
    val id: Int?= null,
    val is_draft: Int?= null,
    val is_type: String? = null,
    val longitude: Double?= null,
    val latitude: Double?= null
    )

data class FarmerMobileVeterinaryUnitsAddRequest(
    val attended_call: Int?= null,
    val attended_call_inputs: String?= null,
    val attended_call_remarks: String?= null,
    val block_name: String?= null,
    val come_know_about_inputs: String?= null,
    val come_know_about_remarks: String?= null,
    val created_by: Int?= null,
    val district_code: Int?= null,
    val id: Int?= null,
    val input_come_know_about: String?= null,
    val input_mvu_arrive_call: String?= null,
    val input_services_mvu: String?= null,
    val input_services_offered_by_mvu: String?= null,
    val is_deleted: Int?= null,
    val is_type: String?= null,
    val mvu_arrive_call_inputs: String?= null,
    val mvu_arrive_call_remarks: String?= null,
    val role_id: Int?= null,
    val services_mvu_inputs: String?= null,
    val services_mvu_remarks: String?= null,
    val services_offered_by_mvu_inputs: String?= null,
    val services_offered_by_mvu_remarks: String?= null,
    val state_code: Int?= null,
    val status: Int?= null,
    val user_id: Int?= null,
    val village_name: String?= null,
    val is_draft: Int?= null,
    val longitude: Double?= null,
    val latitude: Double?= null
)

data class StateAscadAddRequest(
    val annual_action_plan_input: String?= null,
    val annual_action_plan_remarks: String?= null,
    val input_state_prioritizes_critical_disease: String?= null,
    val financial_planning_for_state_share_input: String?= null,
    val financial_planning_for_state_share_remarks: String?= null,
    val input_annual_action_plan: String?= null,
    val input_financial_planning_for_state_share: String?= null,
    val input_purchase_of_vaccines_accessories: String?= null,
    val input_scheduling_of_vaccination: String?= null,
    val purchase_of_vaccines_accessories_input: String?= null,
    val purchase_of_vaccines_accessories_remarks: String?= null,
    val scheduling_of_vaccination_input: String?= null,
    val scheduling_of_vaccination_remarks: String?= null,
    val state_prioritizes_critical_disease_input: String?= null,
    val state_prioritizes_critical_disease_remarks: String?= null,
    val status: Int?= null,
    val user_id: Int?= null,
    val is_deleted: Int?= null,
    val is_type: String?= null,
    val id: Int?= null,
    val role_id: Int?= null,
    val created_by: Int?= null,
    val state_code: Int?= null,
    val longitude: Double?= null,
    val latitude: Double?= null
)

data class DistrictAscadAddRequest(
    val is_type: String?= null,
    val id: Int?= null,
    val compensation_farmer_against_culling_of_animals_input: String?= null,
    val compensation_farmer_against_culling_of_animals_remarks: String?= null,
    val created_by: Int?= null,
    val disease_diagnostic_labs_input: String?= null,
    val disease_diagnostic_labs_remarks: String?= null,
    val district_code: Int?= null,
    val input_compensation_farmer_against_culling_of_animals: String?= null,
    val input_disease_diagnostic_labs: String?= null,
    val input_status_of_vaccination_against_economically: String?= null,
    val input_status_of_vaccination_against_zoonotic: String?= null,
    val input_training_of_veterinarians_and_para_vets_last_year: String?= null,
    val is_deleted: Int?= null,
    val role_id: Int?= null,
    val state_code: Int?= null,
    val status: Int?= null,
    val status_of_vaccination_against_economically_input: String?= null,
    val status_of_vaccination_against_economically_remarks: String?= null,
    val status_of_vaccination_against_zoonotic_input: String?= null,
    val status_of_vaccination_against_zoonotic_remarks: String?= null,
    val training_of_veterinarians_and_para_vets_last_year_input: String?= null,
    val training_of_veterinarians_and_para_vets_last_year_remarks: String?= null,
    val user_id: Int?= null,
    val longitude: Double?= null,
    val latitude: Double?= null
)

data class NDDComponentBListRequest(
    val role_id: Int?,
    val state_code: Int?,
    val user_id: Int?,
    val limit: Int,
    val page: Int
)

data class NDDComponentBAddRequest(
    val any_other: String?=null,
    val is_type: String?=null,
    val asset_earmarked: String?=null,
    val asset_earmarked_remarks: String?=null,
    val assured_marked_surplus: String?=null,
    val assured_marked_surplus_remarks: String?=null,
    val better_price_realisation: String?=null,
    val better_price_realisation_remarks: String?=null,
    val created_by: Int?=null,
    val date_of_inspection: String?=null,
    val district_id: Int?=null,
    val id: Int?=null,
    val is_deleted: Int?=null,
    val latitude: Double?=null,
    val longitude: Double?=null,
    val lat_nlm: Double?=null,
    val long_nlm: Double?=null,
    val name_of_dcs_mpp: String?=null,
    val name_of_revenue_village: String?=null,
    val name_of_tehsil: String?=null,
    val nlm_b_components_document: List<ImplementingAgencyDocument>?=null,
    val overall_hygiene: String?=null,
    val overall_hygiene_remarks: String?=null,
    val overall_interventions: String?=null,
    val overall_interventions_remarks: String?=null,
    val overall_upkeep: String?=null,
    val overall_upkeep_remarks: String?=null,
    val positive_impact: String?=null,
    val positive_impact_remarks: String?=null,
    val role_id: Int?=null,
    val standard_operating_procedures: String?=null,
    val state_code: Int?=null,
    val state_id: Int?=null,
    val status: Int?=null,
    val is_draft: Int?=null,
    val timely_milk_payment: String?=null,
    val timely_milk_payment_remarks: String?=null,
    val transparency_milk_pricing: String?=null,
    val transparency_milk_pricing_remarks: String?=null,
    val user_id: String?=null
)


data class NDDMilkUnionListRequest(
    val role_id: Int?,
    val state_code: Int?,
    val user_id: Int?,
    val limit: Int,
    val page: Int
)


data class NDDDairyPlantListRequest(
    val role_id: Int?,
    val state_code: Int?,
    val user_id: Int?,
    val limit: Int,
    val page: Int
)

data class NDDDcsBmcListRequest(
    val role_id: Int?,
    val state_code: Int?,
    val user_id: Int?,
    val limit: Int,
    val page: Int
)

data class NDDStateCenterLabListRequest(
    val role_id: Int?,
    val state_code: Int?,
    val user_id: Int?,
    val limit: Int,
    val page: Int
)


data class NDDMilkProcessingListRequest(
    val role_id: Int?,
    val state_code: Int?,
    val user_id: Int?,
    val limit: Int,
    val page: Int
)

data class NDDMilkProductMarketingListRequest(
    val role_id: Int?,
    val state_code: Int?,
    val user_id: Int?,
    val limit: Int,
    val page: Int
)

data class NDDProductivityEnhancementServicesListRequest(
    val role_id: Int?,
    val state_code: Int?,
    val user_id: Int?,
    val limit: Int,
    val page: Int
)


data class AddDairyPlantRequest(
    val id: Int?=null,
    val capacity_of_dairy_plant: String?=null,
    val chemical_frequency_tests_performed: String?=null,
    val chemical_snf_frequency: String?=null,
    val chemical_snf_result: String?=null,
    val chemical_tests_performed: String?=null,
    val created_by: Int?=null,
    val daily_avg_last_year_liquid_milk: String?=null,
    val daily_avg_last_year_value_added_curd: String?=null,
    val daily_avg_last_year_value_added_ghee: String?=null,
    val daily_avg_last_year_value_added_lassi: String?=null,
    val dairy_plant_visit_report_document: List<ImplementingAgencyDocument>?=null,
    val dairy_plant_visit_report_npdd_scheme: List<DairyPlantVisitReportNpddScheme>?=null,
    val districts: Int?=null,
    val fssai_license_no: String?=null,
    val haccp_quality_management_system_complied: String?=null,
    val is_deleted: Int?=null,
    val location_of_dairy_plant: String?=null,
    val number_of_bmc_route_milk: String?=null,
    val number_of_non_bmc_route_milk: String?=null,
    val number_of_total_route_milk: String?=null,
    val other_details_capacity: String?=null,
    val other_details_etp_functioning: String?=null,
    val photographs_of_site: String?=null,
    val plant_haccp_complied: Int?=null,
    val remark_by_nlm: String?=null,
    val role_id: Int?=null,
    val is_type: String?=null,
    val state_code: Int?=null,
    val status: Int?=null,
    val is_draft: Int?=null,
    val total_bmc_capacity: String?=null,
    val total_dcs_covered: String?=null,
    val user_id: String?=null,
    val year_of_establishment: String?=null
)



data class AddDcsBmcRequest(
    val actual_pourer_members: String?=null,
    val additional_facilities: String?=null,
    val amc_equip_covered: String?=null,
    val amc_equip_quality: String?=null,
    val amc_equip_valid_years: String?=null,
    val amcu_npdd_date_install: String?=null,
    val amcu_npdd_project_no: String?=null,
    val avg_composition_milk_fat: Double?=null,
    val avg_composition_milk_snf: Double?=null,
    val avg_procurement_price_qmp: Double?=null,
    val avg_procurement_price_total_ap: Double?=null,
    val bmc_details_avg_chil_cost_ltr: Double?=null,
    val bmc_details_avg_milk_collection: Double?=null,
    val bmc_details_capacity: String?=null,
    val bmc_details_date_of_install: String?=null,
    val bmc_details_dg_set_av_rt: String?=null,
    val bmc_details_dg_set_available: String?=null,
    val bmc_details_maintenance_log_book: String?=null,
    val bmc_details_npdd_prj_no: String?=null,
    val bmc_details_other_reg: String?=null,
    val bmc_details_spare_parts: String?=null,
    val bmc_details_temp_control_log_book: String?=null,
    val bmc_details_temp_control_log_book_temp: String?=null,
    val bmc_operator_secretary_date_of_training: String?=null,
    val bmc_operator_secretary_no_of_days: Int?=null,
    val bmc_operator_secretary_place_of_training: String?=null,
    val chemical_frequency_tests: String?=null,
    val chemical_tests_performed: String?=null,
    val condition_electricity_supply_hrs_sply: Double?=null,
    val cooperative_society_reg_date: String?=null,
    val cooperative_society_reg_num: Int?=null,
    val created_by: Int?=null,
    val daily_avg_procurement_milk_current_year: Double?=null,
    val daily_avg_procurement_milk_last_year: Double?=null,
    val dcs_bmc_calibration_status: String?=null,
    val dcs_bmc_fre_interval: String?=null,
    val dcs_bmc_installation: String?=null,
    val dcs_bmc_last_date: String?=null,
    val dcs_bmc_make: String?=null,
    val dcs_last_audit_year: String?=null,
    val dcs_profit_loss: String?=null,
    val districts: Int?=null,
    val id: Int?=null,
    val dsc_any_other: String?=null,
    val dsc_bonus_distribution: String?=null,
    val dsc_general: String?=null,
    val dsc_status_held: String?=null,
    val farmer_interaction_feedback: String?=null,
    val fssai_lic_no: Int?=null,
    val fssai_lic_validity_date: String?=null,
    val incentive_procurement_price_buffalo: Double?=null,
    val incentive_procurement_price_cow: Double?=null,
    val is_deleted: Int?=null,
    val member_bank_ac: Long?=null,
    val milk_payment_cycle: String?=null,
    val milk_payment_last_date: String?=null,
    val name_of_dcs: String?=null,
    val onsite_dcs_center_visit_document: List<ImplementingAgencyDocument>?=null,
    val overall_remarks: String?=null,
    val photographs_of_site: String?=null,
    val photographs_of_site_assets: String?=null,
    val photographs_of_site_assets_date: String?=null,
    val photographs_of_site_assets_location: String?=null,
    val procurement_price_buffalo_milk_current_year: Int?=null,
    val procurement_price_buffalo_milk_last_year: Int?=null,
    val procurement_price_cow_milk_current_year: Int?=null,
    val procurement_price_cow_milk_last_year: Int?=null,
    val role_id: Int?=null,
    val staff_strength_at_dcs_bmc_operator: String?=null,
    val staff_strength_at_dcs_cleaner: String?=null,
    val staff_strength_at_dcs_milk_tester: String?=null,
    val staff_strength_at_dcs_secretary: String?=null,
    val state_code: Int?=null,
    val status: Int?=null,
    val is_draft: Int?=null,
    val total_reg_numbers: Int?=null,
    val user_id: String?=null,
    val is_type: String?=null,
    val village: String?=null,
    val visit: Int?=null,
    val year_of_estb: String?=null
)


