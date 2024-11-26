package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityNlmFpFromNonForestBinding
import com.nlm.model.FodderProductionFromNonForestData
import com.nlm.model.FodderProductionFromNonForestRequest
import com.nlm.model.NlmFpFromNonForestAddRequest
import com.nlm.model.Result
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockMissionIAList.Companion.FILTER_REQUEST_CODE
import com.nlm.ui.adapter.FpFromNonForestAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class NlmFpFromNonForestListActivity : BaseActivity<ActivityNlmFpFromNonForestBinding>(),
    CallBackDeleteAtId {
    private var mBinding: ActivityNlmFpFromNonForestBinding? = null
    private var fpFromNonForestAdapter: FpFromNonForestAdapter?= null
    private var fpsFromNonForestList= ArrayList<FodderProductionFromNonForestData>()
    private var layoutManager: LinearLayoutManager? = null
    private val viewModel= ViewModel()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    private var itemPosition : Int ?= null
    var stateId: Int = 0
    var districtId: Int = 0
    var districtName: String = ""
    var nameOfAgency: String = ""
    var areaCovered: String = ""

    override val layoutId: Int
        get() = R.layout.activity_nlm_fp_from_non_forest

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        fpFromNonForestAdapter()
        swipeForRefreshFpFromNonForest()
    }

    override fun onResume() {
        super.onResume()
        fpFromNonForestAPICall(paginate = false, loader = true,districtId,nameOfAgency,areaCovered)
    }

    private fun fpFromNonForestAPICall(paginate: Boolean, loader: Boolean,district:Int,nameOfAgency:String,areaCovered:String) {
        if (paginate) {
            currentPage++
        }
        viewModel.getFpFromNonForestList(
            this, loader, FodderProductionFromNonForestRequest(
                role_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id,
                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                state_code = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
                page = currentPage,
                limit = 10,
                district_code = district,
                name_implementing_agency = nameOfAgency,
                area_covered = areaCovered.toIntOrNull()
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
                                fpFromNonForestAPICall(paginate = true, loader = true,districtId,nameOfAgency,areaCovered)
                            }
                        }
                    }
                }
            }
        }

    private fun swipeForRefreshFpFromNonForest() {
        mBinding?.srlFpFromNonForest?.setOnRefreshListener {
            fpFromNonForestAPICall(paginate = false, loader = true,districtId,nameOfAgency,areaCovered)
            mBinding?.srlFpFromNonForest?.isRefreshing = false
        }
    }

    private fun fpFromNonForestAdapter() {
        fpFromNonForestAdapter = FpFromNonForestAdapter(this,fpsFromNonForestList,this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding?.rvNlmFpFromNonForest?.layoutManager = layoutManager
        mBinding?.rvNlmFpFromNonForest?.adapter = fpFromNonForestAdapter
        mBinding?.rvNlmFpFromNonForest?.addOnScrollListener(recyclerScrollListener)
    }


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
        fun filter(view: View) {
            val intent =
                Intent(this@NlmFpFromNonForestListActivity, FilterStateActivity::class.java)
            intent.putExtra("isFrom", 15)
            intent.putExtra("selectedStateId", stateId) // previously selected state ID
            intent.putExtra("districtId", districtId) // previously selected state ID
            intent.putExtra("nameOfAgency", nameOfAgency)
            intent.putExtra("districtName", districtName)
            intent.putExtra("areaCovered", areaCovered)
            startActivityForResult(intent, NationalLiveStockMissionIAList.FILTER_REQUEST_CODE)
        }
        fun add(view: View){
            startActivity(Intent(this@NlmFpFromNonForestListActivity, AddNlmFpFromNonForestActivity::class.java))
        }
    }
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the data passed from FilterStateActivity
            districtId = data?.getIntExtra("districtId", 0)!!
            stateId = data.getIntExtra("stateId", 0)
            nameOfAgency = data.getStringExtra("nameOfAgency").toString()
            areaCovered = data.getStringExtra("areaCovered").toString()
            districtName = data.getStringExtra("districtName").toString()
            //Need to add year also
            // Log the data
            fpFromNonForestAPICall(paginate = false, loader = true,districtId,nameOfAgency,areaCovered)
            Log.d("FilterResult", "Received data from FilterStateActivity: $districtId")
            Log.d("FilterResult", "Received data from FilterStateActivity: $areaCovered")
        }
    }

    override fun setVariables() {
    }

    override fun setObservers() {

        viewModel.fpFromNonForestResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result?.data != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        fpsFromNonForestList.clear()

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
                    fpsFromNonForestList.addAll(userResponseModel._result.data)
                    fpFromNonForestAdapter?.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvNlmFpFromNonForest?.showView()
                } else {
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvNlmFpFromNonForest?.hideView()
                }
            }
        }
        viewModel.nlmFpFromNonForestAddEditResult.observe(this){
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel!=null)
            {
                if(userResponseModel._resultflag==0){
                    mBinding?.rlParent?.let { it1 -> showSnackbar(it1, userResponseModel.message) }
                }
                else{
                    itemPosition?.let { it1 -> fpFromNonForestAdapter?.onDeleteButtonClick(it1) }
                    mBinding?.rlParent?.let { it1 -> showSnackbar(it1, userResponseModel.message) }
                }
            }
        }
    }

    override fun onClickItem(ID: Int?, position: Int,isFrom:Int) {
        viewModel.getNlmFpFromNonForestAddEdit(this,true,
            NlmFpFromNonForestAddRequest(
                state_code = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
                user_id = getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id.toString(),
                role_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id,
                id = ID,
                is_draft=null,
                is_deleted = 1
            )
        )
        itemPosition = position
    }
}