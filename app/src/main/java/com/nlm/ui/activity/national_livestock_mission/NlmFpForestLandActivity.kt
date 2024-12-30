package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityNlmFpForestLandBinding
import com.nlm.model.FodderProductionFromNonForestData
import com.nlm.model.FpFromForestLandData
import com.nlm.model.FpFromForestLandRequest
import com.nlm.model.FpFromForestLandResponse
import com.nlm.model.Result
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockMissionIAList.Companion.FILTER_REQUEST_CODE
import com.nlm.ui.adapter.FpFromForestLandAdapter
import com.nlm.ui.adapter.FpFromNonForestAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel
import kotlin.math.log

class NlmFpForestLandActivity : BaseActivity<ActivityNlmFpForestLandBinding>(), CallBackDeleteAtId {
    private var mBinding: ActivityNlmFpForestLandBinding? = null
    private var fpFromForestLandAdapter: FpFromForestLandAdapter? = null
    private var fpsFromForestLandList = ArrayList<FpFromForestLandData>()
    private var layoutManager: LinearLayoutManager? = null
    private val viewModel = ViewModel()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    var stateId: Int ?= null
    var districtId: Int ?= null
    var nameOfAgency: String ?= null
    var districtName: String ?= null
    var areaCoverd: String ?= null


    override val layoutId: Int
        get() = R.layout.activity_nlm_fp_forest_land

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        fpFromForestLandAdapter()
        swipeForRefreshFpFromForestLand()
        mBinding?.fabAddAgency?.setOnClickListener {
            val intent =
                Intent(this@NlmFpForestLandActivity, AddNlmFpForestLandActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        currentPage = 1
        fpFromForestLandAPICall(paginate = false, loader = true)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the data passed from FilterStateActivity
            districtId = data?.getIntExtra("districtId", 0)!!
            stateId = data.getIntExtra("stateId", 0)
            nameOfAgency = data.getStringExtra("nameOfAgency").toString()
            areaCoverd = data.getStringExtra("areaCovered").toString()
            districtName = data.getStringExtra("districtName").toString()

            fpFromForestLandAPICall(paginate = false, loader = true,)
           Log.d("FILTERDATA","districtId: $districtId")
           Log.d("FILTERDATA","stateId: $stateId")
           Log.d("FILTERDATA","nameOfAgency: $nameOfAgency")
           Log.d("FILTERDATA","areaCovered: $areaCoverd")
        }
    }
    private fun fpFromForestLandAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getFpFromForestLandList(
            this, loader, FpFromForestLandRequest(
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
                page = currentPage,
                limit = 10,
                name_implementing_agency = nameOfAgency,
                area_covered = areaCoverd,
                district_code=districtId
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
                                fpFromForestLandAPICall(paginate = true, loader = true)
                            }
                        }
                    }
                }
            }
        }

    private fun swipeForRefreshFpFromForestLand() {
        mBinding?.srlFpFromForestLand?.setOnRefreshListener {
            currentPage = 1
            fpFromForestLandAPICall(paginate = false, loader = true)
            mBinding?.srlFpFromForestLand?.isRefreshing = false
        }
    }

    private fun fpFromForestLandAdapter() {
        fpFromForestLandAdapter = FpFromForestLandAdapter(this, fpsFromForestLandList, this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding?.rvFpFromForestLand?.layoutManager = layoutManager
        mBinding?.rvFpFromForestLand?.adapter = fpFromForestLandAdapter
        mBinding?.rvFpFromForestLand?.addOnScrollListener(recyclerScrollListener)
    }

    override fun setVariables() {
    }

    override fun setObservers() {

        viewModel.fpFromForestLandResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result?.data != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        fpsFromForestLandList.clear()

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
                        mBinding?.fabAddAgency?.hideView()
                    }
                    fpsFromForestLandList.addAll(userResponseModel._result.data)
                    fpFromForestLandAdapter?.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvFpFromForestLand?.showView()
                } else {
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvFpFromForestLand?.hideView()
                }
            }
        }
    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun filter(view: View) {
            val intent = Intent(
                this@NlmFpForestLandActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 15)
                .putExtra("nameOfAgency", nameOfAgency)
            .putExtra("areaCovered", areaCoverd)
            .putExtra("stateId", stateId) // Add selected data to intent
            .putExtra("districtId", districtId) // Add selected data to intent
            .putExtra("districtName", districtName)
            startActivityForResult(intent,FILTER_REQUEST_CODE)

        }
    }

    override fun onClickItem(ID: Int?, position: Int,isFrom:Int) {
    }
}