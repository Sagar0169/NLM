package com.nlm.repository

import com.nlm.model.AppLoginResponse
import com.nlm.model.ApplicationStatusRequest
import com.nlm.model.ApplicationStatusResponse
import com.nlm.model.LoginRequest
import com.nlm.model.LoginResponse
import com.nlm.model.MyAccountRequest
import com.nlm.model.MyAccountResponse
import com.nlm.model.OtpRequest

import retrofit2.Response

interface RepositoryInt {

    suspend fun getLogin(request: LoginRequest): Response<AppLoginResponse>

    suspend fun getMyAccount(request: MyAccountRequest): Response<MyAccountResponse>

    suspend fun getOtpLogin(request: OtpRequest): Response<LoginResponse>

    suspend fun getAppStatus(request: ApplicationStatusRequest): Response<ApplicationStatusResponse>

}