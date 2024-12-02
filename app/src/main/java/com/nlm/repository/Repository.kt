package com.nlm.repository

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
import com.nlm.model.NLMAhidfRequest
import com.nlm.model.NLMEdpRequest
import com.nlm.model.NLMIAResponse
import com.nlm.model.NlmAhidfResponse
import com.nlm.model.NlmAssistanceForQFSPListRequest
import com.nlm.model.NlmAssistanceForQFSPListResponse
import com.nlm.model.NlmEdpResponse
import com.nlm.model.NlmFpFromNonForestAddRequest
import com.nlm.model.NlmFpFromNonForestAddResponse
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
    suspend fun getNlmDropDown(request: GetNlmDropDownRequest): Response<GetDropDownResponse> {
        return api.getNlmDropDown(request)
    }

    suspend fun getImplementingAgency(request: ImplementingAgencyRequest): Response<ImplementingAgencyResponse> {
        return api.getImplementingAgency(request)
    }

    suspend fun getRspLabList(request: RspLabListRequest): Response<RSPLabListResponse> {
        return api.getRSPLabList(request)
    }

    suspend fun getRSPLabAdd(request: RSPAddRequest): Response<RspAddResponse> {
        return api.getRSPLabAdd(request)
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

    suspend fun getArtificialInseminationAdd(request: ArtificialInseminationAddRequest): Response<ArtificialInsemenationAddResponse> {
        return api.getArtificialInseminationAdd(request)
    }
    suspend fun getImportExoticGoatAdd(request: ImportExoticGoatAddEditRequest): Response<ImportExoticGoatAddEditResponse> {
        return api.getImportExoticGoatAdd(request)
    }
    suspend fun getAssistanceForQfspList(request: NlmAssistanceForQFSPListRequest): Response<NlmAssistanceForQFSPListResponse> {
        return api.getAssistanceForQfspList(request)
    }
    suspend fun getAssistanceForQfspAddEdit(request: Format6AssistanceForQspAddEdit): Response<Format6AssistanceForQspAddResponse> {
        return api.getAssistanceForQfspAddEdit(request)
    }
    suspend fun getNlmFpFromNonForestAddEdit(request: NlmFpFromNonForestAddRequest): Response<NlmFpFromNonForestAddResponse> {
        return api.getNlmFpFromNonForestAddEdit(request)
    }
    suspend fun getFpsPlantStorageList(request: FpsPlantStorageRequest): Response<FspPlantStorageResponse> {
        return api.getFpsPlantStorageList(request)
    }

    suspend fun getFpsPlantStorageADD(request: AddFspPlantStorageRequest): Response<AddFspPlantStorageResponse> {
        return api.getFpsPlantStorageADD(request)
    }
    suspend fun getFpFromNonForestList(request: FodderProductionFromNonForestRequest): Response<FodderProductionFromNonForestResponse> {
        return api.getFpFromNonForestList(request)
    }
    suspend fun getFpFromForestLandList(request: FpFromForestLandRequest): Response<FpFromForestLandResponse> {
        return api.getFpFromForestLandList(request)
    }
    suspend fun getFpFromForestLandAddEdit(request: FpFromForestLandAddEditFormat9Request): Response<FpFromForestLandAddEditFormat9Response> {
        return api.getFpFromForestLandAddEdit(request)
    }
    suspend fun getAssistanceForEaList(request: AssistanceForEARequest): Response<AssistanceForEAResponse> {
        return api.getAssistanceForEaList(request)
    }
    suspend fun getAssistanceForEaADD(request: AddAssistanceEARequest): Response<AddAssistanceEAResponse> {
        return api.getAssistanceForEaADD(request)
    }
    suspend fun getNlmEdpList(request: NLMEdpRequest): Response<NlmEdpResponse> {
        return api.getNlmEdpList(request)
    }
    suspend fun getNlmEdpADD(request: AddNlmEdpRequest): Response<AddNlmEdpResponse> {
        return api.getNlmEdpADD(request)
    }
    suspend fun getNlmAhidfList(request: NLMAhidfRequest): Response<NlmAhidfResponse> {
        return api.getNlmAhidfList(request)
    }
    suspend fun getNlmAhidfADD(request: AddAnimalRequest): Response<AddAnimalResponse> {
        return api.getNlmAhidfADD(request)
    }

    suspend fun getProfileFileUpload(
        user_id: Int?,
        table_name: RequestBody?,
        document_name:MultipartBody.Part?,
//        ia_document: MultipartBody.Part?,
//        implementing_agency_id: Int?,
//        role_id: Int?,

    ): Response<TempUploadDocResponse> {
        return api.getProfileFileUpload(
            user_id,
            table_name,
            document_name,
//            ia_document,
//            implementing_agency_id,
//            role_id

        )
    }

    suspend fun getStateVaccinationProgrammerList(request: VaccinationProgrammerListRequest): Response<VaccinationProgrammerListResponse> {
        return api.getStateVaccinationProgrammerList(request)
    }
    suspend fun getStateVaccinationProgrammerAdd(request: StateVaccinationProgrammeAddRequest): Response<StateVaccinationProgrammeAddResponse> {
        return api.getStateVaccinationProgrammerAdd(request)
    }
    suspend fun getDistrictVaccinationProgrammerList(request: VaccinationProgrammerListRequest): Response<VaccinationProgrammerListResponse> {
        return api.getDistrictVaccinationProgrammerList(request)
    }
    suspend fun getDistrictVaccinationProgrammerAdd(request: DistrictVaccinationProgrammeAddRequest): Response<DistrictVaccinationProgrammeAddResponse> {
        return api.getDistrictVaccinationProgrammerAdd(request)
    }
    suspend fun getFarmerVaccinationProgrammerList(request: VaccinationProgrammerListRequest): Response<VaccinationProgrammerListResponse> {
        return api.getFarmerVaccinationProgrammerList(request)
    }
    suspend fun getFarmerVaccinationProgrammerAdd(request: FarmerVaccinationProgrammeAddRequest): Response<FarmerVaccinationProgrammeAddResponse> {
        return api.getFarmerVaccinationProgrammerAdd(request)
    }
    suspend fun getStateMobileVeterinaryUnitsList(request: MobileVeterinaryUnitsListRequest): Response<MobileVeterinaryUnitsListResponse> {
        return api.getStateMobileVeterinaryUnitsList(request)
    }
    suspend fun getStateMobileVeterinaryUnitsAdd(request: StateMobileVeterinaryUnitAddRequest): Response<StateMobileVeterinaryUnitAddResponse> {
        return api.getStateMobileVeterinaryUnitsAdd(request)
    }
    suspend fun getDistrictMobileVeterinaryUnitsList(request: MobileVeterinaryUnitsListRequest): Response<MobileVeterinaryUnitsListResponse> {
        return api.getDistrictMobileVeterinaryUnitsList(request)
    }
    suspend fun getDistrictMobileVeterinaryUnitsAdd(request: DistrictMobileVeterinaryUnitAddRequest): Response<DistrictMobileVeterinaryUnitAddResponse> {
        return api.getDistrictMobileVeterinaryUnitsAdd(request)
    }
    suspend fun getBlockMobileVeterinaryUnitsList(request: MobileVeterinaryUnitsListRequest): Response<MobileVeterinaryUnitsListResponse> {
        return api.getBlockMobileVeterinaryUnitsList(request)
    }
    suspend fun getBlockMobileVeterinaryUnitsAdd(request: BlockMobileVeterinaryUnitAddRequest): Response<BlockMobileVeterinaryUnitAddResponse> {
        return api.getBlockMobileVeterinaryUnitsAdd(request)
    }
    suspend fun getFarmerMobileVeterinaryUnitsList(request: MobileVeterinaryUnitsListRequest): Response<MobileVeterinaryUnitsListResponse> {
        return api.getFarmerMobileVeterinaryUnitsList(request)
    }
    suspend fun getFarmerMobileVeterinaryUnitsAdd(request: FarmerMobileVeterinaryUnitsAddRequest): Response<FarmerMobileVeterinaryUnitAddResponse> {
        return api.getFarmerMobileVeterinaryUnitsAdd(request)
    }
    suspend fun getStateAscadList(request: AscadListRequest): Response<AscadListResponse> {
        return api.getStateAscadList(request)
    }
    suspend fun getStateAscadAdd(request: StateAscadAddRequest): Response<StateAscadAddResponse> {
        return api.getStateAscadAdd(request)
    }
    suspend fun getDistrictAscadList(request: AscadListRequest): Response<AscadListResponse> {
        return api.getDistrictAscadList(request)
    }
    suspend fun getDistrictAscadAdd(request: DistrictAscadAddRequest): Response<DistrictAscadAddResponse> {
        return api.getDistrictAscadAdd(request)
    }
}


