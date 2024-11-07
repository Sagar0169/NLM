package com.nlm.repository

import com.nlm.model.ArtificialInsemenNationAddRequest
import com.nlm.model.ArtificialInsemenationAddResponse
import com.nlm.model.ArtificialInseminationRequest
import com.nlm.model.ArtificialInseminationResponse
import com.nlm.model.DashboardResponse
import com.nlm.model.GetDropDownRequest
import com.nlm.model.GetDropDownResponse
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.ImplementingAgencyRequest
import com.nlm.model.ImplementingAgencyResponse
import com.nlm.model.ImplementingAgencyResponseNlm
import com.nlm.model.ImportExocticGoatListResponse
import com.nlm.model.ImportExocticGoatRequest
import com.nlm.model.LoginRequest
import com.nlm.model.LoginResponse
import com.nlm.model.LogoutRequest
import com.nlm.model.LogoutResponse
import com.nlm.model.NLMIAResponse
import com.nlm.model.RSPLabListResponse
import com.nlm.model.RspLabListRequest
import com.nlm.model.StateSemenAddResponse
import com.nlm.model.StateSemenAddResult
import com.nlm.model.StateSemenBankNLMRequest
import com.nlm.model.StateSemenBankRequest
import com.nlm.model.StateSemenBankResponse
import com.nlm.model.UploadDocument_Response
import com.nlm.services.MyService
import com.nlm.services.ServiceGenerator
import com.nlm.services.ServiceGeneratorLogin
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

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

    suspend fun getRspLabList(request: RspLabListRequest): Response<RSPLabListResponse> {
        return api.getRSPLabList(request)
    }

    suspend fun getArtificialInsemination(request: ArtificialInseminationRequest): Response<ArtificialInseminationResponse> {
        return api.getArtificialInsemination(request)
    }
    suspend fun getImportExocticGoatList(request: ImportExocticGoatRequest): Response<ImportExocticGoatListResponse> {
        return api.getImportExocticGoatList(request)
    }
    suspend fun getStateSemenBank(request: StateSemenBankRequest): Response<StateSemenBankResponse> {
        return api.getStateSemenBank(request)
    }

    suspend fun getImplementingAgencyAdd(request: ImplementingAgencyAddRequest): Response<NLMIAResponse> {
        return api.getImplementingAgencyAdd(request)
    }


    suspend fun getStateSemenAdd1(request: StateSemenBankNLMRequest): Response<StateSemenAddResponse> {
        return api.getStateSemenAdd1(request)
    }

    suspend fun getStateSemenAdd2(request: StateSemenBankNLMRequest): Response<StateSemenAddResponse> {
        return api.getStateSemenAdd2(request)
    }

    suspend fun getArtificialInseminationAdd(request: ArtificialInsemenNationAddRequest): Response<ArtificialInsemenationAddResponse> {
        return api.getArtificialInseminationAdd(request)
    }
    suspend fun getProfileFileUpload(
        user_id: Int?,
        table_name: RequestBody?,
        nlm_document:MultipartBody.Part?,
        implementing_agency_id: Int?,
        role_id: Int?,
    ): Response<UploadDocument_Response> {
        return api.getProfileFileUpload(
            user_id,
            table_name,
            nlm_document,
            implementing_agency_id,
            role_id
        )
    }
}


