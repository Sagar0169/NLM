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