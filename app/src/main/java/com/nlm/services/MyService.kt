package com.nlm.services

import com.nlm.model.GetDropDownRequest
import com.nlm.model.GetDropDownResponse
import com.nlm.model.ImplementingAgencyRequest
import com.nlm.model.ImplementingAgencyResponse
import com.nlm.model.LoginRequest
import com.nlm.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

const val LOGIN = "rest/login"
const val GET_DROP_DOWN = "rest/getDropdown"
const val IMPLEMENTING_AGENCY_LIST = "nationalLivestockMission/implimentingAgencyList"

interface MyService {

    @POST(LOGIN)
    suspend fun getLogin(@Body request: LoginRequest): Response<LoginResponse>

    @POST(GET_DROP_DOWN)
    suspend fun getDropDown(@Body request: GetDropDownRequest): Response<GetDropDownResponse>

    @POST(IMPLEMENTING_AGENCY_LIST)
    suspend fun getImplementingAgency(@Body request: ImplementingAgencyRequest): Response<ImplementingAgencyResponse>

}

