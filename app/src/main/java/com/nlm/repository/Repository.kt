package com.nlm.repository

import com.nlm.model.LoginRequest
import com.nlm.model.LoginResponse
import com.nlm.services.MyService
import com.nlm.services.ServiceGenerator
import retrofit2.Response

object Repository {

    private var repository: Repository? = null
    private lateinit var api: MyService

    val instance: Repository
        get() {
            repository = Repository
            api = ServiceGenerator.createService(MyService::class.java)

            return repository!!
        }

    suspend fun getLogin(request: LoginRequest): Response<LoginResponse> {
        return api.getLogin(request)
    }
}


