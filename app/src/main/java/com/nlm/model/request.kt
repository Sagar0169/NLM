package com.nlm.model

import java.io.Serializable

data class DashboardData(
    val title: String,
    val image: Int,
): Serializable

data class TrackerData(
    val title: String,
    val unCompleteCircle: Int?
): Serializable

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


data class ArtificialInseminationRequest(
    val role_id: Int?,
    val state_code: Int?,
    val user_id: Int?,
    val limit: Int,
    val page: Int
)
data class StateSemenBankRequest(
    val role_id: Int?,
    val state_code: Int?,
    val user_id: Int?,
    val limit: Int,
    val page: Int
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
    val part:String?=null,
    val state_code: Int?=null,
    val name_location_of_ai: String?=null,
    val director_dg_ceo_name: String?=null,
    val technical_staff_regular_employee: Int?=null,
    val technical_staff_manpower_deputed: Int?=null,
    val admn_staff_regular_employee: Int?=null,
    val admn_staff_manpower_deputed: Int?=null,
    val other_staff_regular_employee: Int?=null,
    val other_staff_manpower_deputed: Int?=null,
    val organizational_chart: String?=null,
    val frozen_semen_goat_number: Int?=null,
    val frozen_semen_goat_location: String?=null,
    val liquid_semen_sheep_number: Int?=null,
    val liquid_semen_sheep_location: String?=null,
    val production_capacity_number: Int?=null,
    val production_capacity_location: String?=null,
    val actual_production_number: Int?=null,
    val actual_production_location: String?=null,
    val no_semen_distributed_number: Int?=null,
    val no_semen_distributed_location: String?=null,
    val no_semen_neighboring_number: Int?=null,
    val no_semen_neighboring_location: String?=null,
    val availability_liquid_nitrogen_number: Int?=null,
    val availability_liquid_nitrogen_location: String?=null,
    val breeding_farms_number: Int?=null,
    val breeding_farms_location: String?=null,
    val goat_breeding_farm_number: Int?=null,
    val goat_breeding_farm_location: String?=null,
    val training_centers_number: Int?=null,
    val training_centers_location: String?=null,
    val number_of_cattle_ai_number: Int?=null,
    val number_of_cattle_ai_location: String?=null,
    val total_ai_performed_number: Int?=null,
    val total_ai_performed_location: String?=null,
    val total_reginal_semen_bank: Int?=null,
    val implementing_agency_advisory_committee: List<ImplementingAgencyAdvisoryCommittee>?=null,
    val implementing_agency_project_monitoring: List<ImplementingAgencyProjectMonitoring>?=null,
    val frequency_of_monitoring_1: String?=null,
    val frequency_of_monitoring_2: String?=null,
    val reporting_mechanism_1: String?=null,
    val reporting_mechanism_2: String?=null,
    val regularity_1: String?=null,
    val regularity_2: String?=null,
    val submission_of_quarterly_1: String?=null,
    val submission_of_quarterly_2: String?=null,
    val studies_surveys_conducted: String?=null,
    val implementing_agency_funds_received: List<ImplementingAgencyFundsReceived>?=null,
    val no_of_al_technicians: Int?=null,
    val number_of_ai: Int?=null,
    val total_paravet_trained: Int?=null,
    val implementing_agency_involved_district_wise: List<ImplementingAgencyInvolvedDistrictWise>?=null,
    val infrastructural: String?=null,
    val organizational: String?=null,
    val funds: String?=null,
    val any_other: String?=null,
    val any_assets_created: String?=null,
    val assessments_of_green: String?=null,
    val availability_of_green_area: String?=null,
    val availability_of_dry: String?=null,
    val availability_of_concentrate: String?=null,
    val availability_of_common: String?=null,
    val efforts_of_state: String?=null,
    val name_of_the_agency: String?=null,
    val quantity_of_fodder: String?=null,
    val distribution_channel: String?=null,
    val number_of_fodder: String?=null,
    val id: Int?=null,// it is the id of the element we want to edit
    val is_draft: Int?=null,//always 1 till the user clicks on submit button then it will be 0
    val present_system: String?=null,
    val sheep_breeding_farms_location: String?=null,
    val sheep_breeding_farms_number: Int?=null,
    val status: Int?=null,
    val user_id: String?=null,
    val implementing_agency_document: List<ImplementingAgencyDocument>?=null,
    val is_deleted: Int?=null,

)

data class ImplementingAgencyAdvisoryCommittee(
    val name_of_the_official: String?,
    val designation: String?,
    val organization: String?,
    val implementing_agency_id: Int?,
    val id: Int?,
)

data class ImplementingAgencyDocument(
    val description: String?,
    val id: Int?,
    val name_doc: String?
)

data class ImplementingAgencyFundsReceived(
    val year: Int?,
    val from_dahd: Double?,
    val state_govt: Double?,
    val any_other: Double?,
    val physical_progress: Double?,
    val id: Int?,



)

data class ImplementingAgencyInvolvedDistrictWise(
    val name_of_district: Int?,
    val location_of_ai_centre: String?,
    val ai_performed: String?,
    val id: Int?,
    val year: Int?
)

data class ImplementingAgencyProjectMonitoring(
    val name_of_official: String?,
    val designation: String?,
    val organization: String?,
    val id: Int?,


)