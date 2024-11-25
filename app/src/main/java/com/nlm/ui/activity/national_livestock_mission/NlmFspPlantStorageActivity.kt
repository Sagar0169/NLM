package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityNlmFspPlantStorageBinding
import com.nlm.model.AddFspPlantStorageRequest
import com.nlm.model.FpsPlantStorageRequest
import com.nlm.model.FspPlantStorageData
import com.nlm.model.Result
import com.nlm.model.StateSemenBankNLMRequest
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockMissionIAList.Companion.FILTER_REQUEST_CODE
import com.nlm.ui.adapter.FpsPlantStorageAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class NlmFspPlantStorageActivity : BaseActivity<ActivityNlmFspPlantStorageBinding>(),
    CallBackDeleteAtId {
    private var mBinding: ActivityNlmFspPlantStorageBinding? = null
    private var fpsPlantStorageAdapter: FpsPlantStorageAdapter? = null
    private var fpsPlantStorageList = ArrayList<FspPlantStorageData>()
    private var layoutManager: LinearLayoutManager? = null
    private val viewModel = ViewModel()
    private var currentPage = 1
    private var totalPage = 1
    private var itemPosition: Int? = null
    private var loading = true
    var stateId: Int = 0
    var districtId: Int = 0
    var districtName: String = ""
    var NOA: String = ""

    override val layoutId: Int
        get() = R.layout.activity_nlm_fsp_plant_storage

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        fpsPlantStorageAdapter()
        swipeForRefreshFpsPlantStorage()
    }

    override fun onResume() {
        super.onResume()
        fpsPlantStorageAPICall(paginate = false, loader = true, districtId, NOA)
    }

    private fun fpsPlantStorageAPICall(
        paginate: Boolean,
        loader: Boolean,
        district: Int,
        noa: String
    ) {
        if (paginate) {
            currentPage++
        }
        viewModel.getFpsPlantStorageList(
            this, loader, FpsPlantStorageRequest(
                role_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id,
                user_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
                state_code = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                district_code = district,
                name_of_organization = noa,
                page = currentPage,
                limit = 10
            )
        )
    }

    private var recyclerScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val visibleItemCount: Int? = layoutManager?.childCount
                    val totalItemCount: Int? = layoutManager?.itemCount
                    val pastVisiblesItems: Int? = layoutManager?.findFirstVisibleItemPosition()
                    if (loading) {
                        if ((visibleItemCount!! + pastVisiblesItems!!) >= totalItemCount!!) {
                            loading = false
                            if (currentPage < totalPage) {
                                //Call API here
                                fpsPlantStorageAPICall(
                                    paginate = true,
                                    loader = true,
                                    districtId,
                                    NOA
                                )
                            }
                        }
                    }
                }
            }
        }

    private fun swipeForRefreshFpsPlantStorage() {
        mBinding?.srlFpsPlantStorage?.setOnRefreshListener {
            fpsPlantStorageAPICall(paginate = false, loader = true, districtId, NOA)
            mBinding?.srlFpsPlantStorage?.isRefreshing = false
        }
    }

    private fun fpsPlantStorageAdapter() {
        fpsPlantStorageAdapter = FpsPlantStorageAdapter(this, fpsPlantStorageList, this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding?.rvFpsPlantStorage?.layoutManager = layoutManager
        mBinding?.rvFpsPlantStorage?.adapter = fpsPlantStorageAdapter
        mBinding?.rvFpsPlantStorage?.addOnScrollListener(recyclerScrollListener)
    }

    inner class ClickActions {

        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun filter(view: View) {
            val intent =
                Intent(this@NlmFspPlantStorageActivity, FilterStateActivity::class.java)
            intent.putExtra("isFrom", 14)
            intent.putExtra("selectedStateId", stateId) // previously selected state ID
            intent.putExtra("districtId", districtId) // previously selected state ID
            intent.putExtra("NOA", NOA)
            intent.putExtra("districtName", districtName)
            startActivityForResult(intent, NationalLiveStockMissionIAList.FILTER_REQUEST_CODE)
        }


        fun add(view: View) {
            startActivity(
                Intent(this@NlmFspPlantStorageActivity, AddNewFspPlantStorageActivity::class.java)
                    .putExtra("isFrom", 3)
            )
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the data passed from FilterStateActivity
            districtId = data?.getIntExtra("districtId", 0)!!
            stateId = data.getIntExtra("stateId", 0)
            NOA = data.getStringExtra("NOA").toString()
            districtName = data.getStringExtra("districtName").toString()
            //Need to add year also
            // Log the data
            fpsPlantStorageAPICall(paginate = false, loader = true, districtId, NOA)
            Log.d("FilterResult", "Received data from FilterStateActivity: $districtId")
            Log.d("FilterResult", "Received data from FilterStateActivity: $NOA")
        }
    }

    override fun setVariables() {

    }

    override fun setObservers() {

        viewModel.fpsPlantStorageResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result?.data != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        fpsPlantStorageList.clear()

                        val remainingCount = userResponseModel._result.total_count % 10
                        totalPage = if (remainingCount == 0) {
                            val count = userResponseModel._result.total_count / 10
                            count
                        } else {
                            val count = userResponseModel._result.total_count / 10
                            count + 1
                        }
                    }
                    if (userResponseModel._result.is_add) {
                        mBinding?.fabAddAgency?.showView()
                    } else {
                        mBinding?.fabAddAgency?.hideView()//need to change to hide
                    }
                    fpsPlantStorageList.addAll(userResponseModel._result.data)
                    fpsPlantStorageAdapter?.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvFpsPlantStorage?.showView()
                } else {
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvFpsPlantStorage?.hideView()
                }
            }
        }

        viewModel.fpsPlantStorageADDResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {

                    showSnackbar(mBinding!!.clParent, userResponseModel.message)

                } else {

                    itemPosition?.let { it1 -> fpsPlantStorageAdapter?.onDeleteButtonClick(it1) }
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }
            }
        }
    }

    override fun onClickItem(ID: Int?, position: Int,isFrom:Int) {
        viewModel.getFpsPlantStorageADD(
            this, true,
            AddFspPlantStorageRequest(
                id = ID,
                role_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id,
                state_code = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                user_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id.toString(),
                is_deleted = 1
            )
        )
        itemPosition = position
    }
}