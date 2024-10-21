package com.nlm.services

import com.nlm.model.DashboardResponse
import com.nlm.model.GetDropDownRequest
import com.nlm.model.GetDropDownResponse
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.ImplementingAgencyRequest
import com.nlm.model.ImplementingAgencyResponse
import com.nlm.model.ImplementingAgencyResponseNlm
import com.nlm.model.LoginRequest
import com.nlm.model.LoginResponse
import com.nlm.model.LogoutRequest
import com.nlm.model.LogoutResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

const val LOGIN = "rest/login"
const val LOGOUT = "rest/logout"
const val DASHBOARD = "rest/dashboard"
const val GET_DROP_DOWN = "rest/getDropdown"
const val IMPLEMENTING_AGENCY_LIST = "nationalLivestockMission/implimentingAgencyList"
const val IMPLEMENTING_AGENCY_ADD = "nationalLivestockMission/implimentingAgencyAddEdit"

interface MyService {

    @POST(LOGIN)
    suspend fun getLogin(@Body request: LoginRequest): Response<LoginResponse>

    @POST(LOGOUT)
    suspend fun getLogout(@Body request: LogoutRequest): Response<LogoutResponse>

    @POST(DASHBOARD)
    suspend fun getDashboard(@Body request: LogoutRequest): Response<DashboardResponse>

    @POST(GET_DROP_DOWN)
    suspend fun getDropDown(@Body request: GetDropDownRequest): Response<GetDropDownResponse>

    @POST(IMPLEMENTING_AGENCY_LIST)
    suspend fun getImplementingAgency(@Body request: ImplementingAgencyRequest): Response<ImplementingAgencyResponse>
    @POST(IMPLEMENTING_AGENCY_ADD)
    suspend fun getImplementingAgencyAdd(@Body request: ImplementingAgencyAddRequest): Response<ImplementingAgencyResponseNlm>


}

