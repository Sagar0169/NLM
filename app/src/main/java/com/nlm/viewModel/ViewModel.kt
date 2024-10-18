package com.nlm.viewModel

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nlm.model.GetDropDownRequest
import com.nlm.model.GetDropDownResponse
import com.nlm.model.ImplementingAgencyRequest
import com.nlm.model.ImplementingAgencyResponse
import com.nlm.model.LoginRequest
import com.nlm.model.LoginResponse
import com.nlm.repository.Repository
import com.nlm.utilities.Utility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.SocketTimeoutException

class ViewModel : ViewModel() {

    private lateinit var repository: Repository
    private val scope = CoroutineScope(Dispatchers.IO)
    private var job: Job? = null

    var loginResult = MutableLiveData<LoginResponse>()
    var getDropDownResult = MutableLiveData<GetDropDownResponse>()
    var implementingAgencyResult = MutableLiveData<ImplementingAgencyResponse>()

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

    fun getDropDownApi(context: Context, request: GetDropDownRequest) {
        // can be launched in a separate asynchronous job
        networkCheck(context, true)

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
}
