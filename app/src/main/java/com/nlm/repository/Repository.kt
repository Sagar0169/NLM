package com.nlm.repository

import com.nlm.model.ArtificialInseminationRequest
import com.nlm.model.ArtificialInseminationResponse
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
import com.nlm.model.StateSemenBankRequest
import com.nlm.model.StateSemenBankResponse
import com.nlm.model.UploadDocument_Response
import com.nlm.services.MyService
import com.nlm.services.ServiceGenerator
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    suspend fun getLogout(request: LogoutRequest): Response<LogoutResponse> {
        return api.getLogout(request)
    }

    suspend fun getDashboard(request: LogoutRequest): Response<DashboardResponse> {
        return api.getDashboard(request)
    }

    suspend fun getDropDown(request: GetDropDownRequest): Response<GetDropDownResponse> {
        return api.getDropDown(request)
    }

    suspend fun getImplementingAgency(request: ImplementingAgencyRequest): Response<ImplementingAgencyResponse> {
        return api.getImplementingAgency(request)
    }

    suspend fun getArtificialInsemination(request: ArtificialInseminationRequest): Response<ArtificialInseminationResponse> {
        return api.getArtificialInsemination(request)
    }
    suspend fun getStateSemenBank(request: StateSemenBankRequest): Response<StateSemenBankResponse> {
        return api.getStateSemenBank(request)
    }

    suspend fun getImplementingAgencyAdd(request: ImplementingAgencyAddRequest): Response<ImplementingAgencyResponseNlm> {
        return api.getImplementingAgencyAdd(request)
    }
    suspend fun getProfileFileUpload(
        user_id:  RequestBody?,
        table_name: RequestBody?,
        id:  RequestBody?,
        ia_document:MultipartBody.Part?
    ): Response<UploadDocument_Response> {
        return api.getProfileFileUpload(
            user_id,
            table_name,
            id,
            ia_document
        )
    }
}


