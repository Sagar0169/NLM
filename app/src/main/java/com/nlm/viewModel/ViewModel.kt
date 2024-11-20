package com.nlm.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nlm.model.ArtificialInseminationAddRequest
import com.nlm.model.ArtificialInsemenationAddResponse
import com.nlm.model.ArtificialInseminationRequest
import com.nlm.model.ArtificialInseminationResponse
import com.nlm.model.AssistanceForEARequest
import com.nlm.model.AssistanceForEAResponse
import com.nlm.model.DashboardResponse
import com.nlm.model.FodderProductionFromNonForestRequest
import com.nlm.model.FodderProductionFromNonForestResponse
import com.nlm.model.FpFromForestLandRequest
import com.nlm.model.FpFromForestLandResponse
import com.nlm.model.FpsPlantStorageRequest
import com.nlm.model.FspPlantStorageResponse
import com.nlm.model.GetDropDownRequest
import com.nlm.model.GetDropDownResponse
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
import com.nlm.model.NLMAhidfRequest
import com.nlm.model.NLMEdpRequest
import com.nlm.model.NLMIAResponse
import com.nlm.model.NlmAhidfResponse
import com.nlm.model.NlmAssistanceForQFSPListRequest
import com.nlm.model.NlmAssistanceForQFSPListResponse
import com.nlm.model.NlmEdpResponse
import com.nlm.model.RSPAddRequest
import com.nlm.model.RSPLabListResponse
import com.nlm.model.RspAddResponse
import com.nlm.model.RspLabListRequest
import com.nlm.model.StateSemenAddResponse
import com.nlm.model.StateSemenBankNLMRequest
import com.nlm.model.StateSemenBankRequest
import com.nlm.model.StateSemenBankResponse
import com.nlm.model.TempUploadDocResponse
import com.nlm.repository.Repository
import com.nlm.utilities.Utility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.net.SocketTimeoutException

class ViewModel : ViewModel() {

    private lateinit var repository: Repository
    private val scope = CoroutineScope(Dispatchers.IO)
    private var job: Job? = null

    var loginResult = MutableLiveData<LoginResponse>()
    var logoutResult = MutableLiveData<LogoutResponse>()
    var dashboardResult = MutableLiveData<DashboardResponse>()
    var getDropDownResult = MutableLiveData<GetDropDownResponse>()
    var implementingAgencyResult = MutableLiveData<ImplementingAgencyResponse>()
    var rspLabListResult = MutableLiveData<RSPLabListResponse>()
    var rspLabAddResult = MutableLiveData<RspAddResponse>()
    var artificialInseminationResult = MutableLiveData<ArtificialInseminationResponse>()
    var importExocticGoatResult = MutableLiveData<ImportExocticGoatListResponse>()
    var statesemenBankResult = MutableLiveData<StateSemenBankResponse>()
    var implementingAgencyAddResult = MutableLiveData<NLMIAResponse>()
    var stateSemenBankAddResult = MutableLiveData<StateSemenAddResponse>()
    var artificialInseminationAddResult = MutableLiveData<ArtificialInsemenationAddResponse>()
    var importExoticGoatAddEditResult = MutableLiveData<ImportExoticGoatAddEditResponse>()
    var nlmAssistanceForQFSPResult = MutableLiveData<NlmAssistanceForQFSPListResponse>()
    var fpsPlantStorageResult = MutableLiveData<FspPlantStorageResponse>()
    var fpFromNonForestResult = MutableLiveData<FodderProductionFromNonForestResponse>()
    var fpFromForestLandResult = MutableLiveData<FpFromForestLandResponse>()
    var assistanceForEaResult = MutableLiveData<AssistanceForEAResponse>()
    var nlmEdpResult = MutableLiveData<NlmEdpResponse>()
    var nlmAhidfResult = MutableLiveData<NlmAhidfResponse>()
    var getProfileUploadFileResult = MutableLiveData<TempUploadDocResponse>()
    var id = 0

    val errors = MutableLiveData<String>()

    fun init() {
        repository = Repository.instance
    }

    private fun showLoader(context: Context) {
        com.nlm.utilities.ProcessDialog.start(context)
    }

    private fun dismissLoader() {
        if (com.nlm.utilities.ProcessDialog.isShowing())
            com.nlm.utilities.ProcessDialog.dismiss()
    }

    private fun networkCheck(context: Context?, showLoader: Boolean) {
        if (com.nlm.utilities.CommonUtils.isNetworkAvailable(context) && showLoader) {
            context?.let { showLoader(context) }
        } else if (!com.nlm.utilities.CommonUtils.isNetworkAvailable(context)) {
            com.nlm.utilities.CommonUtils.displayNetworkAlert(context, false)
            return
        }
    }


    fun getLoginApi(context: Context, request: LoginRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, true)

        job = scope.launch {
            try {
                val response = repository.getLogin(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                loginResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                dismissLoader()
            }
        }
    }

    fun getLogoutApi(context: Context, request: LogoutRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, true)

        job = scope.launch {
            try {
                val response = repository.getLogout(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                logoutResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                dismissLoader()
            }
        }
    }

    fun getDashboardApi(context: Context, request: LogoutRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, true)

        job = scope.launch {
            try {
                val response = repository.getDashboard(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                dashboardResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                dismissLoader()
            }
        }
    }

    fun getDropDownApi(context: Context, loader: Boolean, request: GetDropDownRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getDropDown(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                getDropDownResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                dismissLoader()
            }
        }
    }

    fun getImplementingAgencyApi(context: Context, loader: Boolean, request: ImplementingAgencyRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getImplementingAgency(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                implementingAgencyResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                dismissLoader()
            }
        }
    }
    fun getImplementingAgencyAddApi(context: Context, loader: Boolean, request: ImplementingAgencyAddRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getImplementingAgencyAdd(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                implementingAgencyAddResult.postValue(response.body())
                                dismissLoader()
                                Log.e("responseAdd", implementingAgencyAddResult.toString())
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                dismissLoader()
            }
        }
    }
    fun getStateSemenAddBankApi(context: Context, loader: Boolean, request: StateSemenBankNLMRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getStateSemenAdd1(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                stateSemenBankAddResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                dismissLoader()
            }
        }
    }
    fun getStateSemenAddBankApi2(context: Context, loader: Boolean, request: StateSemenBankNLMRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getStateSemenAdd2(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                stateSemenBankAddResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                dismissLoader()
            }
        }
    }

    fun getArtificialInseminationAdd(context: Context, loader: Boolean, request: ArtificialInseminationAddRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getArtificialInseminationAdd(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                artificialInseminationAddResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                dismissLoader()
            }
        }
    }
    fun getImportExoticGoatAdd(context: Context, loader: Boolean, request: ImportExoticGoatAddEditRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getImportExoticGoatAdd(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                importExoticGoatAddEditResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                dismissLoader()
            }
        }
    }

    fun getArtificialInseminationApi(context: Context, loader: Boolean, request: ArtificialInseminationRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getArtificialInsemination(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                artificialInseminationResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                else{
                    errors.postValue(e.message.toString())
                }
                dismissLoader()
            }
        }
    }
    fun getImportExocticGoatList(context: Context, loader: Boolean, request: ImportExocticGoatRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getImportExocticGoatList(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                importExocticGoatResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                else{
                    errors.postValue(e.message.toString())
                }
                dismissLoader()
            }
        }
    }

    fun getRspLabListApi(context: Context, loader: Boolean, request: RspLabListRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getRspLabList(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                rspLabListResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                else{
                    errors.postValue(e.message.toString())
                }
                dismissLoader()
            }
        }
    }

    fun getRspLabAddApi(context: Context, loader: Boolean, request: RSPAddRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getRSPLabAdd(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                rspLabAddResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                else{
                    errors.postValue(e.message.toString())
                }
                dismissLoader()
            }
        }
    }


    fun getStateSemenBankApi(context: Context, loader: Boolean, request: StateSemenBankRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getStateSemenBank(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                statesemenBankResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                else{
                    errors.postValue(e.message.toString())
                }
                dismissLoader()
            }
        }
    }

    fun getFpsPlantStorageList(context: Context, loader: Boolean, request: FpsPlantStorageRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getFpsPlantStorageList(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                fpsPlantStorageResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                else{
                    errors.postValue(e.message.toString())
                }
                dismissLoader()
            }
        }
    }

    fun getFpFromNonForestList(context: Context, loader: Boolean, request: FodderProductionFromNonForestRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getFpFromNonForestList(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                fpFromNonForestResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                else{
                    errors.postValue(e.message.toString())
                }
                dismissLoader()
            }
        }
    }

    fun getFpFromForestLandList(context: Context, loader: Boolean, request: FpFromForestLandRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getFpFromForestLandList(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                fpFromForestLandResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                else{
                    errors.postValue(e.message.toString())
                }
                dismissLoader()
            }
        }
    }

    fun getAssistanceForEaList(context: Context, loader: Boolean, request: AssistanceForEARequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getAssistanceForEaList(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                assistanceForEaResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                else{
                    errors.postValue(e.message.toString())
                }
                dismissLoader()
            }
        }
    }

    fun getNlmEdp(context: Context, loader: Boolean, request: NLMEdpRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getNlmEdpList(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                nlmEdpResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                else{
                    errors.postValue(e.message.toString())
                }
                dismissLoader()
            }
        }
    }

    fun getNlmAhidf(context: Context, loader: Boolean, request: NLMAhidfRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getNlmAhidfList(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                nlmAhidfResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                else{
                    errors.postValue(e.message.toString())
                }
                dismissLoader()
            }
        }
    }

    fun getAssistanceForQfspList(context: Context, loader: Boolean, request: NlmAssistanceForQFSPListRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, loader)

        job = scope.launch {
            try {
                val response = repository.getAssistanceForQfspList(request)

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                nlmAssistanceForQFSPResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                else{
                    errors.postValue(e.message.toString())
                }
                dismissLoader()
            }
        }
    }
    fun getProfileUploadFile(
        context: Context,
        user_id: Int?,
        table_name: RequestBody?,
        document_name:MultipartBody.Part?
    ) {
        networkCheck(context, true)

        job = scope.launch {
            try {
                val response = repository.getProfileFileUpload(
                    user_id,table_name,document_name
                )

                Log.e("response", response.toString())
                when (response.isSuccessful) {
                    true -> {
                        when (response.code()) {
                            200, 201 -> {
                                getProfileUploadFileResult.postValue(response.body())
                                dismissLoader()
                            }
                        }
                    }

                    false -> {
                        when (response.code()) {
                            400, 403, 404 -> {//Bad Request & Invalid Credentials
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                dismissLoader()
                            }

                            401 -> {
                                val errorBody = JSONObject(response.errorBody()!!.string())
                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
                                Utility.logout(context)
                                dismissLoader()
                            }

                            500 -> {//Internal Server error
                                errors.postValue("Internal Server error")

                                dismissLoader()
                            }

                            else -> dismissLoader()
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    errors.postValue("Time out Please try again")
                }
                else{
                    errors.postValue(e.message.toString())
                }
                dismissLoader()
            }
        }
    }
}
