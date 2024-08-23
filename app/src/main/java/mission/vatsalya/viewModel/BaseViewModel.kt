package mission.vatsalya.viewModel

//open class BaseViewModel: ViewModel() {
//
//    @Inject
//    protected lateinit var mRepository: Repository
//    @Inject
//    lateinit var mApplication:UDID
//    @Inject
//    protected lateinit var mApiHelper: MyService
//    @Inject
//    protected lateinit var mSchedulers: SchedulerProvider
//    @Inject
//    protected lateinit var mCompositeDisposable: CompositeDisposable
//
//    var observerSnackBarInt = ObservableInt()
//    var observerSnackBarString: ObservableString = ObservableString("")
//
//    var observerProgressBar = ObservableBoolean(false)
//    open var observerIsNoRecords = ObservableBoolean(false)
//
//    open var observableStatusBarHeight = ObservableInt(0)
//
//
//    var observableSwipeRefreshIndicatorColor = ObservableInt(R.color.color_primary)
//    var observableSwipeRefreshBackgroundColor = ObservableInt(R.color.color_background_theme)
//
//    fun showLoader(context: Context) {
//        ProcessDialog.start(context)
//    }
//
//    fun dismissLoader() {
//        if (ProcessDialog.isShowing())
//            ProcessDialog.dismiss()
//    }
//
////    open fun isInternetConnected(): Boolean {
////        return mApplication.isInternetConnected()
////    }
////
////    open fun isInternetConnectedWMsg(): Boolean {
////        if (!isInternetConnected()) {
////            observerSnackBarInt.set(R.string.no_internet)
////            return false
////        }
////        return true
////    }
//    open fun networkCheck(context: Context?, showLoader: Boolean) { Log.d("NETWORK",CommonUtils.isNetworkAvailable(context).toString())
//        if (CommonUtils.isNetworkAvailable(context) && showLoader) {
//            context?.let { showLoader(context)
//
//
//            }
//        } else if (!CommonUtils.isNetworkAvailable(context)) {
////            CommonUtils.displayNetworkAlert(context, false)
//            observerSnackBarInt.set(R.string.no_internet)
//            observerSnackBarString.set("No Internet")
//            return
//        }
//    }
//    open fun handleApiErrorMessage(message: String) {
//        if (!TextUtils.isEmpty(message)) {
//            Logger.e("Exception Message", message)
//            if (message.contains("Certificate pinning failure!")) {
//                observerSnackBarString.set(mApplication.getString(R.string.message_ssl_pinning_failure))
//            } else {
//                observerSnackBarString.set(message)
//            }
//        } else {
//            onApiFailure()
//        }
//    }
//    fun onApiFailure() {
//        observerSnackBarInt.set(R.string.message_try_again)
//    }
//}