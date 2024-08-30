package com.nlm.repository

import com.nlm.services.ServiceGenerator
import com.nlm.services.ServiceGeneratorLogin
import retrofit2.Response
import com.nlm.model.AppLoginResponse
import com.nlm.model.ApplicationStatusRequest
import com.nlm.model.ApplicationStatusResponse
import com.nlm.model.LoginRequest
import com.nlm.model.LoginResponse
import com.nlm.model.MyAccountRequest
import com.nlm.model.MyAccountResponse
import com.nlm.model.OtpRequest
import com.nlm.services.MyService
import javax.inject.Inject

class Repository @Inject constructor(api: MyService,
                                     apiLogin: MyService): BaseRepository(),RepositoryInt{


    init {
        mApi=api
        mApiLogin=apiLogin
    }

    override suspend fun getLogin(request: LoginRequest): Response<AppLoginResponse> {
        mApiLogin = ServiceGeneratorLogin.createServiceLogin(MyService::class.java)
        return mApiLogin.getLogin(request)
    }

    override suspend fun getMyAccount(request: MyAccountRequest): Response<MyAccountResponse> {
        mApi = ServiceGenerator.createService(MyService::class.java)
        return mApi.getMyAccount(request)
    }

    override suspend fun getOtpLogin(request: OtpRequest): Response<LoginResponse> {
        mApiLogin = ServiceGeneratorLogin.createServiceLogin(MyService::class.java)
        return mApiLogin.getOtpLogin(request)
    }

    override suspend fun getAppStatus(request: ApplicationStatusRequest): Response<ApplicationStatusResponse> {
        mApi = ServiceGenerator.createService(MyService::class.java)
        return mApi.getAppStatus(request)
    }
}


