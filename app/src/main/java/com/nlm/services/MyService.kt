package com.nlm.services

import com.nlm.model.AddAnimalRequest
import com.nlm.model.AddAnimalResponse
import com.nlm.model.AddAssistanceEARequest
import com.nlm.model.AddAssistanceEAResponse
import com.nlm.model.AddFspPlantStorageRequest
import com.nlm.model.AddFspPlantStorageResponse
import com.nlm.model.AddNlmEdpRequest
import com.nlm.model.AddNlmEdpResponse
import com.nlm.model.ArtificialInseminationAddRequest
import com.nlm.model.ArtificialInsemenationAddResponse
import com.nlm.model.ArtificialInseminationRequest
import com.nlm.model.ArtificialInseminationResponse
import com.nlm.model.AscadListRequest
import com.nlm.model.AscadListResponse
import com.nlm.model.AssistanceForEARequest
import com.nlm.model.AssistanceForEAResponse
import com.nlm.model.DashboardResponse
import com.nlm.model.FodderProductionFromNonForestRequest
import com.nlm.model.FodderProductionFromNonForestResponse
import com.nlm.model.Format6AssistanceForQspAddEdit
import com.nlm.model.Format6AssistanceForQspAddResponse
import com.nlm.model.FpFromForestLandAddEditFormat9Request
import com.nlm.model.FpFromForestLandAddEditFormat9Response
import com.nlm.model.FpFromForestLandRequest
import com.nlm.model.FpFromForestLandResponse
import com.nlm.model.FpsPlantStorageRequest
import com.nlm.model.FspPlantStorageResponse

import com.nlm.model.GetDropDownRequest
import com.nlm.model.GetDropDownResponse
import com.nlm.model.GetNlmDropDownRequest
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.ImplementingAgencyRequest
import com.nlm.model.ImplementingAgencyResponse
import com.nlm.model.ImportExocticGoatListResponse
import com.nlm.model.ImportExocticGoatRequest
import com.nlm.model.ImportExoticGoatAddEditRequest
import com.nlm.model.ImportExoticGoatAddEditResponse
import com.nlm.model.LoginRequest
import com.nlm.model.LoginResponse
import com.nlm.model.LogoutRequest
import com.nlm.model.LogoutResponse
import com.nlm.model.MobileVeterinaryUnitsListRequest
import com.nlm.model.MobileVeterinaryUnitsListResponse
import com.nlm.model.NLMAhidfRequest
import com.nlm.model.NLMEdpRequest
import com.nlm.model.NLMIAResponse
import com.nlm.model.NlmAhidfResponse
import com.nlm.model.NlmAssistanceForQFSPListRequest
import com.nlm.model.NlmAssistanceForQFSPListResponse
import com.nlm.model.NlmEdpResponse
import com.nlm.model.NlmFpFromNonForestAddRequest
import com.nlm.model.NlmFpFromNonForestAddResponse
import com.nlm.model.NlmFpFromNonForestAddResult
import com.nlm.model.RSPAddRequest
import com.nlm.model.RSPLabListResponse
import com.nlm.model.RspAddResponse
import com.nlm.model.RspLabListRequest
import com.nlm.model.StateSemenAddResponse
import com.nlm.model.StateSemenBankNLMRequest
import com.nlm.model.StateSemenBankRequest
import com.nlm.model.StateSemenBankResponse
import com.nlm.model.TempUploadDocResponse
import com.nlm.model.VaccinationProgrammerListRequest
import com.nlm.model.VaccinationProgrammerListResponse
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
//NLM
const val GET_NLM_DROP_DOWN = "nationalLivestockMission/nlmDropdown"
const val IMPLEMENTING_AGENCY_LIST = "nationalLivestockMission/implimentingAgencyList"
const val IMPLEMENTING_AGENCY_ADD = "nationalLivestockMission/implimentingAgencyAddEdit"
const val STATE_SEMEN_ADD1 = "nationalLivestockMission/stateSemenBankAddEdit1"
const val STATE_SEMEN_ADD2 = "nationalLivestockMission/stateSemenBankAddEdit2"
const val ARTIFICIAL_INSEMINATION_ADD = "nationalLivestockMission/artificialInseminationAddEdit"
const val ARTIFICIAL_INSEMINATION_LIST = "nationalLivestockMission/artificialInseminationList"
const val IMPORT_EXOTIC_GOAT_LIST = "nationalLivestockMission/importOfExoticGoatList"
const val STATE_SEMEN_BANK_LIST = "nationalLivestockMission/stateSemenBankList"
const val UPLOAD_DOCUMENT = "rest/tempUploadDocument"
const val RSP_LAP_LIST = "nationalLivestockMission/rspLaboratorySemenList"
const val RSP_LAP_ADD = "nationalLivestockMission/rspLaboratorySemenAddEdit"
const val IMPORT_EXOTIC_GOAT_ADD_EDIT = "nationalLivestockMission/importOfExoticGoatAddEdit"
const val ASSISTANCE_FOR_QFSP_LIST = "nationalLivestockMission/assistanceForQfspList"
const val ASSISTANCE_FOR_QFSP_ADD_EDIT = "nationalLivestockMission/assistanceForQfspAddEdit"
const val FODDER_PRODUCTION_FROM_NON_FOREST = "nationalLivestockMission/FpFromNonForestList"
const val FODDER_PRODUCTION_FROM_FOREST_LAND = "nationalLivestockMission/FpFromForestLandList"
const val FODDER_PRODUCTION_FROM_FOREST_LAND_ADD_EDIT = "nationalLivestockMission/fpFromForestLandAddEdit"
const val NLM_AHIDF = "nationalLivestockMission/ahidfList"
const val ADD_NLM_AHIDF = "nationalLivestockMission/ahidfAddEdit"
const val NLM_EDP = "nationalLivestockMission/nlmEdpList"
const val ADD_NLM_EDP = "nationalLivestockMission/nlmEdpAddEdit"
const val ASSISTANCE_FOR_EA = "nationalLivestockMission/assistanceForEaList"
const val ADD_ASSISTANCE_FOR_EA = "nationalLivestockMission/assistanceForEaAddEdit"
const val FPS_PLANT_STORAGE = "nationalLivestockMission/fspPlantStorageList"
const val NLM_FP_FROM_NON_FOREST = "nationalLivestockMission/fpFromNonForestAddEdit"
const val ADD_FPS_PLANT_STORAGE = "nationalLivestockMission/fspPlantStorageAddEdit"
//LHD
const val STATE_VACCINATION_PROGRAMMER_LIST = "livestockHealthDisease/stateVaccinationProgrammeList"
const val DISTRICT_VACCINATION_PROGRAMMER_LIST = "livestockHealthDisease/districtVaccinationProgrammeList"
const val FARMER_VACCINATION_PROGRAMMER_LIST = "livestockHealthDisease/farmerVaccinationProgrammeList"
const val STATE_MOBILE_VETERINARY_UNITS_LIST = "livestockHealthDisease/mobileVeterinaryUnitStateList"
const val DISTRICT_MOBILE_VETERINARY_UNITS_LIST = "livestockHealthDisease/mobileVeterinaryUnitDistrictList"
const val BLOCK_MOBILE_VETERINARY_UNITS_LIST = "livestockHealthDisease/mobileVeterinaryUnitBlockList"
const val FARMER_MOBILE_VETERINARY_UNITS_LIST = "livestockHealthDisease/mobileVeterinaryUnitFarmerList"
const val STATE_ASCAD_LIST = "livestockHealthDisease/ascadStateList"
const val DISTRICT_ASCAD_LIST = "livestockHealthDisease/ascadDistrictList"


interface MyService {

    @POST(LOGIN)
    suspend fun getLogin(@Body request: LoginRequest): Response<LoginResponse>

    @POST(LOGOUT)
    suspend fun getLogout(@Body request: LogoutRequest): Response<LogoutResponse>

    @POST(DASHBOARD)
    suspend fun getDashboard(@Body request: LogoutRequest): Response<DashboardResponse>

    @POST(GET_DROP_DOWN)
    suspend fun getDropDown(@Body request: GetDropDownRequest): Response<GetDropDownResponse>

    @POST(GET_NLM_DROP_DOWN)
    suspend fun getNlmDropDown(@Body request: GetNlmDropDownRequest): Response<GetDropDownResponse>

    @POST(IMPLEMENTING_AGENCY_LIST)
    suspend fun getImplementingAgency(@Body request: ImplementingAgencyRequest): Response<ImplementingAgencyResponse>

    @POST(RSP_LAP_LIST)
    suspend fun getRSPLabList(@Body request: RspLabListRequest): Response<RSPLabListResponse>


    @POST(RSP_LAP_ADD)
    suspend fun getRSPLabAdd(@Body request: RSPAddRequest): Response<RspAddResponse>

    @POST(ARTIFICIAL_INSEMINATION_LIST)
    suspend fun getArtificialInsemination(@Body request: ArtificialInseminationRequest): Response<ArtificialInseminationResponse>

    @POST(IMPORT_EXOTIC_GOAT_LIST)
    suspend fun getImportExocticGoatList(@Body request: ImportExocticGoatRequest): Response<ImportExocticGoatListResponse>

    @POST(STATE_SEMEN_BANK_LIST)
    suspend fun getStateSemenBank(@Body request: StateSemenBankRequest): Response<StateSemenBankResponse>

    @POST(STATE_SEMEN_ADD1)
    suspend fun getStateSemenAdd1(@Body request: StateSemenBankNLMRequest): Response<StateSemenAddResponse>

    @POST(STATE_SEMEN_ADD2)
    suspend fun getStateSemenAdd2(@Body request: StateSemenBankNLMRequest): Response<StateSemenAddResponse>

    @POST(IMPLEMENTING_AGENCY_ADD)
    suspend fun getImplementingAgencyAdd(@Body request: ImplementingAgencyAddRequest): Response<NLMIAResponse>

    @POST(ARTIFICIAL_INSEMINATION_ADD)
    suspend fun getArtificialInseminationAdd(@Body request: ArtificialInseminationAddRequest): Response<ArtificialInsemenationAddResponse>

    @POST(IMPORT_EXOTIC_GOAT_ADD_EDIT)
    suspend fun getImportExoticGoatAdd(@Body request: ImportExoticGoatAddEditRequest): Response<ImportExoticGoatAddEditResponse>

    @POST(ASSISTANCE_FOR_QFSP_LIST)
    suspend fun getAssistanceForQfspList(@Body request: NlmAssistanceForQFSPListRequest): Response<NlmAssistanceForQFSPListResponse>

    @POST(ASSISTANCE_FOR_QFSP_ADD_EDIT)
    suspend fun getAssistanceForQfspAddEdit(@Body request: Format6AssistanceForQspAddEdit): Response<Format6AssistanceForQspAddResponse>

    @POST(NLM_FP_FROM_NON_FOREST)
    suspend fun getNlmFpFromNonForestAddEdit(@Body request: NlmFpFromNonForestAddRequest): Response<NlmFpFromNonForestAddResponse>

    @POST(FPS_PLANT_STORAGE)
    suspend fun getFpsPlantStorageList(@Body request: FpsPlantStorageRequest): Response<FspPlantStorageResponse>

    @POST(ADD_FPS_PLANT_STORAGE)
    suspend fun getFpsPlantStorageADD(@Body request: AddFspPlantStorageRequest): Response<AddFspPlantStorageResponse>

    @POST(FODDER_PRODUCTION_FROM_NON_FOREST)
    suspend fun getFpFromNonForestList(@Body request: FodderProductionFromNonForestRequest): Response<FodderProductionFromNonForestResponse>

    @POST(FODDER_PRODUCTION_FROM_FOREST_LAND)
    suspend fun getFpFromForestLandList(@Body request: FpFromForestLandRequest): Response<FpFromForestLandResponse>

    @POST(FODDER_PRODUCTION_FROM_FOREST_LAND_ADD_EDIT)
    suspend fun getFpFromForestLandAddEdit(@Body request: FpFromForestLandAddEditFormat9Request): Response<FpFromForestLandAddEditFormat9Response>

   @POST(ASSISTANCE_FOR_EA)
    suspend fun getAssistanceForEaList(@Body request: AssistanceForEARequest): Response<AssistanceForEAResponse>

    @POST(ADD_ASSISTANCE_FOR_EA)
    suspend fun getAssistanceForEaADD(@Body request: AddAssistanceEARequest): Response<AddAssistanceEAResponse>

    @POST(NLM_EDP)
    suspend fun getNlmEdpList(@Body request: NLMEdpRequest): Response<NlmEdpResponse>

    @POST(ADD_NLM_EDP)
    suspend fun getNlmEdpADD(@Body request: AddNlmEdpRequest): Response<AddNlmEdpResponse>

    @POST(NLM_AHIDF)
    suspend fun getNlmAhidfList(@Body request: NLMAhidfRequest): Response<NlmAhidfResponse>

    @POST(ADD_NLM_AHIDF)
    suspend fun getNlmAhidfADD(@Body request: AddAnimalRequest): Response<AddAnimalResponse>

    @Multipart
    @POST(UPLOAD_DOCUMENT)
    suspend fun getProfileFileUpload(
        @Part("user_id") user_id: Int?,
        @Part("table_name") table_name: RequestBody?,
        @Part document_name: MultipartBody.Part?,
//        @Part ia_document: MultipartBody.Part?,
//        @Part("implementing_agency_id") implementing_agency_id: Int?,
//        @Part("role_id") role_id: Int?,

    ): Response<TempUploadDocResponse>

    @POST(STATE_VACCINATION_PROGRAMMER_LIST)
    suspend fun getStateVaccinationProgrammerList(@Body request: VaccinationProgrammerListRequest): Response<VaccinationProgrammerListResponse>

    @POST(DISTRICT_VACCINATION_PROGRAMMER_LIST)
    suspend fun getDistrictVaccinationProgrammerList(@Body request: VaccinationProgrammerListRequest): Response<VaccinationProgrammerListResponse>

    @POST(FARMER_VACCINATION_PROGRAMMER_LIST)
    suspend fun getFarmerVaccinationProgrammerList(@Body request: VaccinationProgrammerListRequest): Response<VaccinationProgrammerListResponse>

    @POST(STATE_MOBILE_VETERINARY_UNITS_LIST)
    suspend fun getStateMobileVeterinaryUnitsList(@Body request: MobileVeterinaryUnitsListRequest): Response<MobileVeterinaryUnitsListResponse>

    @POST(DISTRICT_MOBILE_VETERINARY_UNITS_LIST)
    suspend fun getDistrictMobileVeterinaryUnitsList(@Body request: MobileVeterinaryUnitsListRequest): Response<MobileVeterinaryUnitsListResponse>

    @POST(BLOCK_MOBILE_VETERINARY_UNITS_LIST)
    suspend fun getBlockMobileVeterinaryUnitsList(@Body request: MobileVeterinaryUnitsListRequest): Response<MobileVeterinaryUnitsListResponse>

    @POST(FARMER_MOBILE_VETERINARY_UNITS_LIST)
    suspend fun getFarmerMobileVeterinaryUnitsList(@Body request: MobileVeterinaryUnitsListRequest): Response<MobileVeterinaryUnitsListResponse>

    @POST(STATE_ASCAD_LIST)
    suspend fun getStateAscadList(@Body request: AscadListRequest): Response<AscadListResponse>

    @POST(DISTRICT_ASCAD_LIST)
    suspend fun getDistrictAscadList(@Body request: AscadListRequest): Response<AscadListResponse>
}

