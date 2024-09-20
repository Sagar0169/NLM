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

object Repository {
    private var repository: Repository? = null
    private lateinit var api: MyService
    private lateinit var apiLogin: MyService

    val instance: Repository
        get() {
            repository = Repository
            api = ServiceGenerator.createService(MyService::class.java)

            apiLogin = ServiceGeneratorLogin.createServiceLogin(MyService::class.java)
            return repository!!
        }

    suspend fun getLogin(request: LoginRequest): Response<LoginResponse> {
        return apiLogin.getLogin(request)
    }
}


