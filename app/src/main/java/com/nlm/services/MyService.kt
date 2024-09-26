package com.nlm.services

import com.nlm.model.LoginRequest
import com.nlm.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

const val LOGIN = "login"

interface MyService {

    @POST(LOGIN)
    suspend fun getLogin(@Body request: LoginRequest): Response<LoginResponse>

}

