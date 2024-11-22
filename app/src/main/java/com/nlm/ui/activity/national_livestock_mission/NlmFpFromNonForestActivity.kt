package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityNlmFpFromNonForestBinding
import com.nlm.model.FodderProductionFromNonForestData
import com.nlm.model.FodderProductionFromNonForestRequest
import com.nlm.model.Result
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.FpFromNonForestAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class NlmFpFromNonForestActivity : BaseActivity<ActivityNlmFpFromNonForestBinding>(),
    CallBackDeleteAtId {
    private var mBinding: ActivityNlmFpFromNonForestBinding? = null
    private var fpFromNonForestAdapter: FpFromNonForestAdapter?= null
    private var fpsFromNonForestList= ArrayList<FodderProductionFromNonForestData>()
    private var layoutManager: LinearLayoutManager? = null
    private val viewModel= ViewModel()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true

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
        fpFromNonForestAPICall(paginate = false, loader = true)
    }

    private fun fpFromNonForestAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getFpFromNonForestList(
            this, loader, FodderProductionFromNonForestRequest(
                role_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id,
                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                state_code = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
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
                                fpFromNonForestAPICall(paginate = true, loader = true)
                            }
                        }
                    }
                }
            }
        }

    private fun swipeForRefreshFpFromNonForest() {
        mBinding?.srlFpFromNonForest?.setOnRefreshListener {
            fpFromNonForestAPICall(paginate = false, loader = true)
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
            startActivity(Intent(
                this@NlmFpFromNonForestActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 15))
        }
        fun add(view: View){
            startActivity(Intent(this@NlmFpFromNonForestActivity, AddNlmFpForestLandActivity::class.java))
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
                if (userResponseModel?._result != null && userResponseModel._result.data.isNotEmpty()) {
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

    }

    override fun onClickItem(ID: Int?, position: Int,isFrom:Int) {
    }
}