package com.nlm.services

import com.nlm.model.AppLoginResponse
import com.nlm.model.ApplicationStatusRequest
import com.nlm.model.LoginRequest
import com.nlm.model.LoginResponse
import com.nlm.model.MyAccountRequest
import com.nlm.model.MyAccountResponse
import com.nlm.model.ApplicationStatusResponse
import com.nlm.model.OtpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

const val LOGIN = "applogin"
const val OTP_LOGIN = "otplogin"
const val MY_ACCOUNT = "myaccount"
const val APP_STATUS = "getAppapplicationstatus "



interface MyService {

    @POST(LOGIN)
    suspend fun getLogin(@Body request: LoginRequest): Response<AppLoginResponse>
    @POST(OTP_LOGIN)
    suspend fun getOtpLogin(@Body request: OtpRequest): Response<LoginResponse>
    @POST(MY_ACCOUNT)
    suspend fun getMyAccount(@Body request: MyAccountRequest): Response<MyAccountResponse>
    @POST(APP_STATUS)
    suspend fun getAppStatus(@Body request: ApplicationStatusRequest): Response<ApplicationStatusResponse>

}

