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



data class GetDropDownRequest(
    val limit: Int?,
    val model: String?,
    val page: Int?,
    val state_code: Int?,
    val user_id: Int?
)
data class ImplementingAgencyAddRequest(
    val part:String?,
    val state_code: Int?,
    val name_location_of_ai: String?,
    val director_dg_ceo_name: String?,
    val technical_staff_regular_employee: Int?,
    val technical_staff_manpower_deputed: Int?,
    val admn_staff_regular_employee: Int?,
    val admn_staff_manpower_deputed: Int?,
    val other_staff_regular_employee: Int?,
    val other_staff_manpower_deputed: Int?,
    val organizational_chart: String?,
    val frozen_semen_goat_number: Int?,
    val frozen_semen_goat_location: String?,
    val liquid_semen_sheep_number: Int?,
    val liquid_semen_sheep_location: String?,
    val production_capacity_number: Int?,
    val production_capacity_location: String?,
    val actual_production_number: Int?,
    val actual_production_location: String?,
    val no_semen_distributed_number: Int?,
    val no_semen_distributed_location: String?,
    val no_semen_neighboring_number: Int?,
    val no_semen_neighboring_location: String?,
    val availability_liquid_nitrogen_number: Int?,
    val availability_liquid_nitrogen_location: String?,
    val breeding_farms_number: Int?,
    val breeding_farms_location: String?,
    val goat_breeding_farm_number: Int?,
    val goat_breeding_farm_location: String?,
    val training_centers_number: Int?,
    val training_centers_location: String?,
    val number_of_cattle_ai_number: Int?,
    val number_of_cattle_ai_location: String?,
    val total_ai_performed_number: Int?,
    val total_ai_performed_location: String?,
    val total_reginal_semen_bank: Int?,
    val implementing_agency_advisory_committee: List<ImplementingAgencyAdvisoryCommittee>?,
    val implementing_agency_project_monitoring: List<ImplementingAgencyProjectMonitoring>?,
    val frequency_of_monitoring_1: String?,
    val frequency_of_monitoring_2: String?,
    val reporting_mechanism_1: String?,
    val reporting_mechanism_2: String?,
    val regularity_1: String?,
    val regularity_2: String?,
    val submission_of_quarterly_1: String?,
    val submission_of_quarterly_2: String?,
    val studies_surveys_conducted: String?,
    val implementing_agency_funds_received: List<ImplementingAgencyFundsReceived>?,
    val no_of_al_technicians: Int?,
    val total_paravet_trained: Int?,
    val implementing_agency_involved_district_wise: List<ImplementingAgencyInvolvedDistrictWise>?,
    val infrastructural: String?,
    val organizational: String?,
    val funds: String?,
    val any_other: String?,
    val any_assets_created: String?,
    val assessments_of_green: String?,
    val availability_of_green_area: String?,
    val availability_of_dry: String?,
    val availability_of_concentrate: String?,
    val availability_of_common: String?,
    val efforts_of_state: String?,
    val name_of_the_agency: String?,
    val quantity_of_fodder: String?,
    val distribution_channel: String?,
    val number_of_fodder: String?,
    val id: Int?,// it is the id of the element we want to edit
    val is_draft: Int?,//always 1 till the user clicks on submit button then it will be 0
    val number_of_ai: Int?,
    val present_system: String?,
    val sheep_breeding_farms_location: String?,
    val sheep_breeding_farms_number: Int?,
    val status: Int?,
    val user_id: String?,
    val implementing_agency_document: List<ImplementingAgencyDocument>?,


)

data class ImplementingAgencyAdvisoryCommittee(
    val name_of_the_official: String?,
    val designation: String?,
    val organization: String?,
    val implementing_agency_id: Int?,
    val id: Int?,
)

data class ImplementingAgencyDocument(
    val description: String,
    val id: Int
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