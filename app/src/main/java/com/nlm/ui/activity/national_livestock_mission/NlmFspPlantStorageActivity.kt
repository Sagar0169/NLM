package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityNlmFspPlantStorageBinding
import com.nlm.model.FpsPlantStorageRequest
import com.nlm.model.FspPlantStorageData
import com.nlm.model.Result
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.FpsPlantStorageAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class NlmFspPlantStorageActivity : BaseActivity<ActivityNlmFspPlantStorageBinding>(),
    CallBackDeleteAtId {
    private var mBinding: ActivityNlmFspPlantStorageBinding? = null
    private var fpsPlantStorageAdapter: FpsPlantStorageAdapter?= null
    private var fpsPlantStorageList= ArrayList<FspPlantStorageData>()
    private var layoutManager: LinearLayoutManager? = null
    private val viewModel= ViewModel()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true

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
        fpsPlantStorageAPICall(paginate = false, loader = true)
    }

    private fun fpsPlantStorageAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getFpsPlantStorageList(
            this, loader, FpsPlantStorageRequest(
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
                                fpsPlantStorageAPICall(paginate = true, loader = true)
                            }
                        }
                    }
                }
            }
        }

    private fun swipeForRefreshFpsPlantStorage() {
        mBinding?.srlFpsPlantStorage?.setOnRefreshListener {
            fpsPlantStorageAPICall(paginate = false, loader = true)
            mBinding?.srlFpsPlantStorage?.isRefreshing = false
        }
    }

    private fun fpsPlantStorageAdapter() {
        fpsPlantStorageAdapter = FpsPlantStorageAdapter(this,fpsPlantStorageList,this)
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
            startActivity(Intent(
                this@NlmFspPlantStorageActivity,
                FilterStateActivity::class.java).putExtra("isFrom", 14))
        }

        fun add(view: View){
            startActivity(Intent(this@NlmFspPlantStorageActivity, AddNewFspPlantStorageActivity::class.java).putExtra("isFrom", 3))
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
                if (userResponseModel?._result != null && userResponseModel._result.data.isNotEmpty()) {
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
                        mBinding?.fabAddAgency?.hideView()
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
    }

    override fun onClickItem(ID: Int?, position: Int,isFrom:Int) {
    }
}