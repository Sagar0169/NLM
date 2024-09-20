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
    var email: String,
    var password: String,
)
data class MyAccountRequest(
    val application_number: String
)
data class ApplicationStatusRequest(
    val application_number: String
)