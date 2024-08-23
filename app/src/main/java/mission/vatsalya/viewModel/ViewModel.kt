package mission.vatsalya.viewModel


//@HiltViewModel
//class ViewModel @Inject constructor(repository: Repository,
//                                    application: Application,
//                                    apiHelper: MyService,
//                                    schedulers: SchedulerProvider,
//                                    compositeDisposable: CompositeDisposable
//
//) : BaseViewModel()
//{
//
//
//    private val scope = CoroutineScope(Dispatchers.IO)
//    private var job: Job? = null
//     val DEVICE_ID = "device_id"
//    var isRememberMe = ObservableBoolean(false)
//    var observableEmailId: ObservableString = ObservableString("")
//    var observablePassword: ObservableString = ObservableString("")
//    var isLoginSuccessFully = MutableLiveData<Boolean>()
////    var loginResult = MutableLiveData<AppLoginResponse>()
////    var otpLoginResult = MutableLiveData<LoginResponse>()
////    var myaccountResult = MutableLiveData<MyAccountResponse>()
////    var appStatusResult = MutableLiveData<ApplicationStatusResponse>()
//    val PARAMS_EMAIL = "email"
//    val PARAMS_EMAIL2 = "application_number"
//    val PARAMS_PASSWORD = "dob"
//    val errors = MutableLiveData<String>()
//    var compositeDisposable = CompositeDisposable()
//     init {
//        mRepository=repository
//         mApplication = application as UDID
//         mApiHelper = apiHelper
//         mSchedulers = schedulers
//         mCompositeDisposable=compositeDisposable
//
//    }
//
////    private  fun showLoader(context: Context) {
////        ProcessDialog.start(context)
////    }
////
////    private fun dismissLoader() {
////        if (ProcessDialog.isShowing())
////            ProcessDialog.dismiss()
////    }
//
//    fun validation(): Boolean {
//        return if (observableEmailId.trimmed.isEmpty()) {
//            observerSnackBarInt.set(R.string.text_email_required)
//            false
//        } else if (observablePassword.trimmed.isEmpty()) {
//            observerSnackBarInt.set(R.string.text_password_empty_required)
//            false
//        } else {
//            true
//        }
//    }
//   @SuppressLint("SuspiciousIndentation")
//   fun userLogin() {
//       mApiHelper = ServiceGeneratorLogin.createServiceLogin(MyService::class.java)
//
//       if (mApplication.isInternetConnected()) {
//                observerProgressBar.set(true)
//               Log.d("DATAA",observableEmailId.trimmed)
//               Log.d("DATAAA",observablePassword.trimmed)
//           val uniqueId: String = getUniqueIDWhitRandomString()
//           val map: MutableMap<String, String> = HashMap()
////           map[PARAMS_EMAIL] =getEncryptedString(observableEmailId.trimmed, uniqueId)
////           map[PARAMS_EMAIL2] =observableEmailId.trimmed
////           map[PARAMS_PASSWORD] =observablePassword.trimmed
////           map[DEVICE_ID] = uniqueId
//
//          compositeDisposable.add(mApiHelper.getLogin2(LoginRequest(
//              observableEmailId.trimmed,
//              observablePassword.trimmed
//          )).subscribeOn(mSchedulers.io()).observeOn(mSchedulers.ui()).subscribe({
//              response->   observerSnackBarString.set(response!!.message)
//              observerProgressBar.set(false)
//          }) { throwable ->
//              observerProgressBar.set(false)
//              handleApiErrorMessage(throwable.message!!)
//          })
//
////           mApiHelper.callLoginWithEmailOtp(map)
////               ?.subscribeOn(mSchedulers.io())
////               ?.observeOn(mSchedulers.ui())?.let {
////                   compositeDisposable.add(
////                       it.subscribe({ response ->
////
////                           observerSnackBarString.set(response.message)
////                           observerProgressBar.set(false)
////                       }) { throwable ->
////                           observerProgressBar.set(false)
////                           handleApiErrorMessage(throwable.message!!)
////                       })
////               }
//            } else {
//                observerSnackBarInt.set(R.string.message_no_internet)
//            }
//
//    }
//
//
//
//
//     fun getLoginApi(context: Context, request: LoginRequest) {
//        // can be launched in a separate asynchronous job
//        networkCheck(context, true)
//
//        job= scope.launch {
//            try {
//                val response = mRepository.getLogin(request)
//
//                Log.e("response", response.toString())
//                when (response.isSuccessful) {
//                    true -> {
//                        when (response.code()) {
//                            200, 201 -> {
//                                loginResult.postValue(response.body())
//                                dismissLoader()
//                            }
//                        }
//                    }
//
//                    false -> {
//                        when (response.code()) {
//                            400, 403, 404 -> {//Bad Request & Invalid Credentials
//                                val errorBody = JSONObject(response.errorBody()!!.string())
//                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
//                                dismissLoader()
//                            }
//
//                            401 -> {
//                                val errorBody = JSONObject(response.errorBody()!!.string())
//                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
////                                Utility.logout(context)
//                                dismissLoader()
//                            }
//
//                            500 -> {//Internal Server error
//                                errors.postValue("Internal Server error")
//
//                                dismissLoader()
//                            }
//
//                            else -> dismissLoader()
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                if (e is SocketTimeoutException) {
//                    errors.postValue("Time out Please try again")
//                }
//                dismissLoader()
//            }
//        }
//    }
//
//    fun getOtpLoginApi(context: Context, request: OtpRequest) {
//        // can be launched in a separate asynchronous job
//        networkCheck(context, true)
//
//        job = scope.launch {
//            try {
//                val response = mRepository.getOtpLogin(request)
//
//                Log.e("response", response.toString())
//                when (response.isSuccessful) {
//                    true -> {
//                        when (response.code()) {
//                            200, 201 -> {
//                                otpLoginResult.postValue(response.body())
//                                dismissLoader()
//                            }
//                        }
//                    }
//
//                    false -> {
//                        when (response.code()) {
//                            400, 403, 404 -> {//Bad Request & Invalid Credentials
//                                val errorBody = JSONObject(response.errorBody()!!.string())
//                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
//                                dismissLoader()
//                            }
//
//                            401 -> {
//                                val errorBody = JSONObject(response.errorBody()!!.string())
//                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
////                                Utility.logout(context)
//                                dismissLoader()
//                            }
//
//                            500 -> {//Internal Server error
//                                errors.postValue("Internal Server error")
//
//                                dismissLoader()
//                            }
//
//                            else -> dismissLoader()
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                if (e is SocketTimeoutException) {
//                    errors.postValue("Time out Please try again")
//                }
//                dismissLoader()
//            }
//        }
//    }
//
//    fun getMyAccount(context: Context, request: MyAccountRequest) {
//        // can be launched in a separate asynchronous job
//        networkCheck(context, true)
//
//        job = scope.launch {
//            try {
//                val response = mRepository.getMyAccount(request)
//
//                Log.e("response", response.toString())
//                when (response.isSuccessful) {
//                    true -> {
//                        when (response.code()) {
//                            200, 201 -> {
//                                myaccountResult.postValue(response.body())
//                                dismissLoader()
//                            }
//                        }
//                    }
//
//                    false -> {
//                        when (response.code()) {
//                            400, 403, 404 -> {//Bad Request & Invalid Credentials
//                                val errorBody = JSONObject(response.errorBody()!!.string())
//                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
//                                dismissLoader()
//                            }
//
//                            401 -> {
//                                val errorBody = JSONObject(response.errorBody()!!.string())
//                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
////                                 Utility.logout(context)
//                                dismissLoader()
//                            }
//
//                            500 -> {//Internal Server error
//                                errors.postValue("Internal Server error")
//
//                                dismissLoader()
//                            }
//
//                            else -> dismissLoader()
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                if (e is SocketTimeoutException) {
//                    errors.postValue("Time out Please try again")
//                }
//                dismissLoader()
//            }
//        }
//    }
//
//    fun getAppStatus(context: Context, request: ApplicationStatusRequest) {
//        // can be launched in a separate asynchronous job
//        networkCheck(context, true)
//
//        job = scope.launch {
//            try {
//                val response = mRepository.getAppStatus(request)
//
//                Log.e("response", response.toString())
//                when (response.isSuccessful) {
//                    true -> {
//                        when (response.code()) {
//                            200, 201 -> {
//                                appStatusResult.postValue(response.body())
//                                dismissLoader()
//                            }
//                        }
//                    }
//
//                    false -> {
//                        when (response.code()) {
//                            400, 403, 404 -> {//Bad Request & Invalid Credentials
//                                val errorBody = JSONObject(response.errorBody()!!.string())
//                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
//                                dismissLoader()
//                            }
//
//                            401 -> {
//                                val errorBody = JSONObject(response.errorBody()!!.string())
//                                errors.postValue(errorBody.getString("message") ?: "Bad Request")
////                                Utility.logout(context)
//                                dismissLoader()
//                            }
//
//                            500 -> {//Internal Server error
//                                errors.postValue("Internal Server error")
//
//                                dismissLoader()
//                            }
//
//                            else -> dismissLoader()
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                if (e is SocketTimeoutException) {
//                    errors.postValue("Time out Please try again")
//                }
//                dismissLoader()
//            }
//        }
//    }
//    fun getEncryptedString(value: String?, uniqueId: String): String {
//        var newValue = ""
//        try {
//            newValue =
//                if (!TextUtils.isEmpty(value) && !TextUtils.isEmpty(uniqueId)) CryptLib().encryptString(
//                    value!!, uniqueId
//                ) else ""
//        } catch (e: InvalidKeyException) {
//            Logger.e("UtilsCommon  setStringEncrypted", e.toString())
//        } catch (e: NoSuchPaddingException) {
//            Logger.e("UtilsCommon  setStringEncrypted", e.toString())
//        } catch (e: NoSuchAlgorithmException) {
//            Logger.e("UtilsCommon  setStringEncrypted", e.toString())
//        } catch (e: BadPaddingException) {
//            Logger.e("UtilsCommon  setStringEncrypted", e.toString())
//        } catch (e: IllegalBlockSizeException) {
//            Logger.e("UtilsCommon  setStringEncrypted", e.toString())
//        } catch (e: InvalidAlgorithmParameterException) {
//            Logger.e("UtilsCommon  setStringEncrypted", e.toString())
//        } catch (e: UnsupportedEncodingException) {
//            Logger.e("UtilsCommon  setStringEncrypted", e.toString())
//        }
//        return newValue
//    }
//
//}