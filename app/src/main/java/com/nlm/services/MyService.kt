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
import com.nlm.model.BlockMobileVeterinaryUnitAddRequest
import com.nlm.model.BlockMobileVeterinaryUnitAddResponse
import com.nlm.model.DashboardResponse
import com.nlm.model.DistrictAscadAddRequest
import com.nlm.model.DistrictAscadAddResponse
import com.nlm.model.DistrictMobileVeterinaryUnitAddRequest
import com.nlm.model.DistrictMobileVeterinaryUnitAddResponse
import com.nlm.model.DistrictVaccinationProgrammeAddRequest
import com.nlm.model.DistrictVaccinationProgrammeAddResponse
import com.nlm.model.FarmerMobileVeterinaryUnitAddResponse
import com.nlm.model.FarmerMobileVeterinaryUnitsAddRequest
import com.nlm.model.FarmerVaccinationProgrammeAddRequest
import com.nlm.model.FarmerVaccinationProgrammeAddResponse
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
import com.nlm.model.NDDComponentBAddRequest
import com.nlm.model.NDDComponentBAddResponse
import com.nlm.model.NDDComponentBListRequest
import com.nlm.model.NDDComponentBListResponse
import com.nlm.model.NDDDairyPlantListRequest
import com.nlm.model.NDDDairyPlantListResponse
import com.nlm.model.NDDDcsBmcListRequest
import com.nlm.model.NDDDcsBmcListResponse
import com.nlm.model.NDDMilkProcessingListRequest
import com.nlm.model.NDDMilkProcessingListResponse
import com.nlm.model.NDDMilkProductMarketingListRequest
import com.nlm.model.NDDMilkProductMarketingListResponse
import com.nlm.model.NDDMilkUnionListRequest
import com.nlm.model.NDDMilkUnionListResponse
import com.nlm.model.NDDProductivityEnhancementServicesListRequest
import com.nlm.model.NDDProductivityEnhancementServicesListResponse
import com.nlm.model.NDDStateCenterLabListRequest
import com.nlm.model.NDDStateCenterLabListResponse
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
import com.nlm.model.StateAscadAddRequest
import com.nlm.model.StateAscadAddResponse
import com.nlm.model.StateMobileVeterinaryUnitAddRequest
import com.nlm.model.StateMobileVeterinaryUnitAddResponse
import com.nlm.model.StateSemenAddResponse
import com.nlm.model.StateSemenBankNLMRequest
import com.nlm.model.StateSemenBankRequest
import com.nlm.model.StateSemenBankResponse
import com.nlm.model.StateVaccinationProgrammeAddRequest
import com.nlm.model.StateVaccinationProgrammeAddResponse
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
const val STATE_VACCINATION_PROGRAMMER_ADD = "livestockHealthDisease/stateVaccinationProgrammeAddEdit"
const val DISTRICT_VACCINATION_PROGRAMMER_LIST = "livestockHealthDisease/districtVaccinationProgrammeList"
const val DISTRICT_VACCINATION_PROGRAMMER_ADD = "livestockHealthDisease/districtVaccinationProgrammeAddEdit"
const val FARMER_VACCINATION_PROGRAMMER_LIST = "livestockHealthDisease/farmerVaccinationProgrammeList"
const val FARMER_VACCINATION_PROGRAMMER_ADD = "livestockHealthDisease/farmerVaccinationProgrammeAddEdit"
const val STATE_MOBILE_VETERINARY_UNITS_LIST = "livestockHealthDisease/mobileVeterinaryUnitStateList"
const val STATE_MOBILE_VETERINARY_UNITS_ADD = "livestockHealthDisease/mobileVeterinaryUnitStateAddEdit"
const val DISTRICT_MOBILE_VETERINARY_UNITS_LIST = "livestockHealthDisease/mobileVeterinaryUnitDistrictList"
const val DISTRICT_MOBILE_VETERINARY_UNITS_ADD = "livestockHealthDisease/mobileVeterinaryUnitDistrictAddEdit"
const val BLOCK_MOBILE_VETERINARY_UNITS_LIST = "livestockHealthDisease/mobileVeterinaryUnitBlockList"
const val BLOCK_MOBILE_VETERINARY_UNITS_ADD = "livestockHealthDisease/mobileVeterinaryUnitBlockAddEdit"
const val FARMER_MOBILE_VETERINARY_UNITS_LIST = "livestockHealthDisease/mobileVeterinaryUnitFarmerList"
const val FARMER_MOBILE_VETERINARY_UNITS_ADD = "livestockHealthDisease/mobileVeterinaryUnitFarmerAddEdit"
const val STATE_ASCAD_LIST = "livestockHealthDisease/ascadStateList"
const val STATE_ASCAD_ADD = "livestockHealthDisease/ascadStateAddEdit"
const val DISTRICT_ASCAD_LIST = "livestockHealthDisease/ascadDistrictList"
const val DISTRICT_ASCAD_ADD = "livestockHealthDisease/ascadDistrictAddEdit"


//NDD
const val COMPONENT_B_LIST = "NationalDairyDevelopment/componentBList"
const val COMPONENT_B_ADD = "NationalDairyDevelopment/componentBAddEdit"
const val MILK_UNION_LIST = "NationalDairyDevelopment/milkUnionList"
const val DAIRY_PLANT_LIST = "NationalDairyDevelopment/dairyPlantList"
const val DCS_BMC_LIST = "NationalDairyDevelopment/dcsBmcList"
const val STATE_CENTER_LAB_LIST = "NationalDairyDevelopment/stateCenterLabList"
const val MILK_PROCESSING_LIST = "NationalDairyDevelopment/milkProcessingList"
const val MILK_PRODUCT_MARKETING_LIST = "NationalDairyDevelopment/milkProductMarketingList"
const val PRODUCTIVITY_ENHANCEMENT_SERVICES_LIST = "NationalDairyDevelopment/productivityEnhancementServicesList"



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

    @POST(STATE_VACCINATION_PROGRAMMER_ADD)
    suspend fun getStateVaccinationProgrammerAdd(@Body request: StateVaccinationProgrammeAddRequest): Response<StateVaccinationProgrammeAddResponse>

    @POST(DISTRICT_VACCINATION_PROGRAMMER_LIST)
    suspend fun getDistrictVaccinationProgrammerList(@Body request: VaccinationProgrammerListRequest): Response<VaccinationProgrammerListResponse>

    @POST(DISTRICT_VACCINATION_PROGRAMMER_ADD)
    suspend fun getDistrictVaccinationProgrammerAdd(@Body request: DistrictVaccinationProgrammeAddRequest): Response<DistrictVaccinationProgrammeAddResponse>

    @POST(FARMER_VACCINATION_PROGRAMMER_LIST)
    suspend fun getFarmerVaccinationProgrammerList(@Body request: VaccinationProgrammerListRequest): Response<VaccinationProgrammerListResponse>

    @POST(FARMER_VACCINATION_PROGRAMMER_ADD)
    suspend fun getFarmerVaccinationProgrammerAdd(@Body request: FarmerVaccinationProgrammeAddRequest): Response<FarmerVaccinationProgrammeAddResponse>

    @POST(STATE_MOBILE_VETERINARY_UNITS_LIST)
    suspend fun getStateMobileVeterinaryUnitsList(@Body request: MobileVeterinaryUnitsListRequest): Response<MobileVeterinaryUnitsListResponse>

    @POST(STATE_MOBILE_VETERINARY_UNITS_ADD)
    suspend fun getStateMobileVeterinaryUnitsAdd(@Body request: StateMobileVeterinaryUnitAddRequest): Response<StateMobileVeterinaryUnitAddResponse>

    @POST(DISTRICT_MOBILE_VETERINARY_UNITS_LIST)
    suspend fun getDistrictMobileVeterinaryUnitsList(@Body request: MobileVeterinaryUnitsListRequest): Response<MobileVeterinaryUnitsListResponse>

    @POST(DISTRICT_MOBILE_VETERINARY_UNITS_ADD)
    suspend fun getDistrictMobileVeterinaryUnitsAdd(@Body request: DistrictMobileVeterinaryUnitAddRequest): Response<DistrictMobileVeterinaryUnitAddResponse>

    @POST(BLOCK_MOBILE_VETERINARY_UNITS_LIST)
    suspend fun getBlockMobileVeterinaryUnitsList(@Body request: MobileVeterinaryUnitsListRequest): Response<MobileVeterinaryUnitsListResponse>

    @POST(BLOCK_MOBILE_VETERINARY_UNITS_ADD)
    suspend fun getBlockMobileVeterinaryUnitsAdd(@Body request: BlockMobileVeterinaryUnitAddRequest): Response<BlockMobileVeterinaryUnitAddResponse>

    @POST(FARMER_MOBILE_VETERINARY_UNITS_LIST)
    suspend fun getFarmerMobileVeterinaryUnitsList(@Body request: MobileVeterinaryUnitsListRequest): Response<MobileVeterinaryUnitsListResponse>

    @POST(FARMER_MOBILE_VETERINARY_UNITS_ADD)
    suspend fun getFarmerMobileVeterinaryUnitsAdd(@Body request: FarmerMobileVeterinaryUnitsAddRequest): Response<FarmerMobileVeterinaryUnitAddResponse>

    @POST(STATE_ASCAD_LIST)
    suspend fun getStateAscadList(@Body request: AscadListRequest): Response<AscadListResponse>

    @POST(STATE_ASCAD_ADD)
    suspend fun getStateAscadAdd(@Body request: StateAscadAddRequest): Response<StateAscadAddResponse>

    @POST(DISTRICT_ASCAD_LIST)
    suspend fun getDistrictAscadList(@Body request: AscadListRequest): Response<AscadListResponse>

    @POST(DISTRICT_ASCAD_ADD)
    suspend fun getDistrictAscadAdd(@Body request: DistrictAscadAddRequest): Response<DistrictAscadAddResponse>



//NDD


    @POST(COMPONENT_B_LIST)
    suspend fun getComponentBList(@Body request: NDDComponentBListRequest): Response<NDDComponentBListResponse>

    @POST(COMPONENT_B_ADD)
    suspend fun getComponentBAdd(@Body request: NDDComponentBAddRequest): Response<NDDComponentBAddResponse>

    @POST(MILK_UNION_LIST)
    suspend fun getMilkUnionList(@Body request: NDDMilkUnionListRequest): Response<NDDMilkUnionListResponse>

    @POST(DAIRY_PLANT_LIST)
    suspend fun dairyPlantList(@Body request: NDDDairyPlantListRequest): Response<NDDDairyPlantListResponse>

    @POST(DCS_BMC_LIST)
    suspend fun dcsBmcList(@Body request: NDDDcsBmcListRequest): Response<NDDDcsBmcListResponse>

    @POST(STATE_CENTER_LAB_LIST)
    suspend fun stateCenterLabList(@Body request: NDDStateCenterLabListRequest): Response<NDDStateCenterLabListResponse>

    @POST(MILK_PROCESSING_LIST)
    suspend fun milkProcessingList(@Body request: NDDMilkProcessingListRequest): Response<NDDMilkProcessingListResponse>

    @POST(MILK_PRODUCT_MARKETING_LIST)
    suspend fun milkProductMarketingList(@Body request: NDDMilkProductMarketingListRequest): Response<NDDMilkProductMarketingListResponse>

    @POST(PRODUCTIVITY_ENHANCEMENT_SERVICES_LIST)
    suspend fun productivityEnhancementServicesList(@Body request: NDDProductivityEnhancementServicesListRequest): Response<NDDProductivityEnhancementServicesListResponse>
}
