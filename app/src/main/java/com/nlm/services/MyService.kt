package com.nlm.services

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
import com.nlm.model.RSPLabListResponse
import com.nlm.model.RspLabListRequest
import com.nlm.model.StateSemenAddResponse
import com.nlm.model.StateSemenAddResult
import com.nlm.model.StateSemenBankNLMRequest
import com.nlm.model.StateSemenBankRequest
import com.nlm.model.StateSemenBankResponse
import com.nlm.model.UploadDocument_Response
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

const val LOGIN = "rest/login"
const val LOGOUT = "rest/logout"
const val DASHBOARD = "rest/dashboard"
const val GET_DROP_DOWN = "rest/getDropdown"
const val IMPLEMENTING_AGENCY_LIST = "nationalLivestockMission/implimentingAgencyList"
const val IMPLEMENTING_AGENCY_ADD = "nationalLivestockMission/implimentingAgencyAddEdit"
const val STATE_SEMEN_ADD = "nationalLivestockMission/stateSemenBankAddEdit"
const val ARTIFICIAL_INSEMINATION_ADD = "nationalLivestockMission/artificialInseminationAddEdit"
const val ARTIFICIAL_INSEMINATION_LIST = "nationalLivestockMission/artificialInseminationList"
const val IMPORT_EXOTIC_GOAT_LIST = "nationalLivestockMission/importOfExoticGoatList"
const val STATE_SEMEN_BANK_LIST = "nationalLivestockMission/stateSemenBankList"
const val UPLOAD_DOCUMENT = "rest/uploadDocument"
const val RSP_LAP_LIST = "nationalLivestockMission/rspLaboratorySemenList"

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

    @POST(RSP_LAP_LIST)
    suspend fun getRSPLabList(@Body request: RspLabListRequest): Response<RSPLabListResponse>

    @POST(ARTIFICIAL_INSEMINATION_LIST)
    suspend fun getArtificialInsemination(@Body request: ArtificialInseminationRequest): Response<ArtificialInseminationResponse>
    @POST(IMPORT_EXOTIC_GOAT_LIST)
    suspend fun getImportExocticGoatList(@Body request: ImportExocticGoatRequest): Response<ImportExocticGoatListResponse>
    @POST(STATE_SEMEN_BANK_LIST)
    suspend fun getStateSemenBank(@Body request: StateSemenBankRequest): Response<StateSemenBankResponse>
    @POST(STATE_SEMEN_ADD)
    suspend fun getStateSemenBank(@Body request: StateSemenBankNLMRequest): Response<StateSemenAddResponse>

    @POST(IMPLEMENTING_AGENCY_ADD)
    suspend fun getImplementingAgencyAdd(@Body request: ImplementingAgencyAddRequest): Response<ImplementingAgencyResponseNlm>


    @POST(ARTIFICIAL_INSEMINATION_ADD)
    suspend fun getArtificialInseminationAdd(@Body request: ArtificialInsemenNationAddRequest): Response<ArtificialInsemenationAddResponse>

    @Multipart
    @POST(UPLOAD_DOCUMENT)
    suspend fun getProfileFileUpload(
        @Part("user_id") user_id: Int?,
        @Part("table_name") table_name: RequestBody?,
        @Part nlm_document: MultipartBody.Part?,
        @Part("implementing_agency_id") implementing_agency_id: Int?,
        @Part("role_id") role_id: Int?,

    ): Response<UploadDocument_Response>

}

