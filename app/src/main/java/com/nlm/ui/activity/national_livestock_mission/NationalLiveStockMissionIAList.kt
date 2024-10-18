package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ActivityNationalLiveStockIaBinding
import com.nlm.model.DataImplementingAgency
import com.nlm.model.ImplementingAgencyRequest
import com.nlm.model.Result
import com.nlm.ui.activity.FilterStateActivity

import com.nlm.ui.adapter.NationalLiveStockMissionIAAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreferenceOfScheme

import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel


class NationalLiveStockMissionIAList : BaseActivity<ActivityNationalLiveStockIaBinding>() {

    private var viewModel = ViewModel()
    private lateinit var implementingAdapter: NationalLiveStockMissionIAAdapter
    private var mBinding: ActivityNationalLiveStockIaBinding? = null
    private var implementingAgencyList = ArrayList<DataImplementingAgency>()
    private var layoutManager: LinearLayoutManager? = null
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true

    override val layoutId: Int
        get() = R.layout.activity_national_live_stock_ia

    override fun initView() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        implementingAgencyAdapter()
        implementingAgencyAPICall(paginate = false, loader = true)
        swipeForRefreshImplementingAgency()
    }
    private fun implementingAgencyAdapter() {
        implementingAdapter = NationalLiveStockMissionIAAdapter(implementingAgencyList,1,Utility.getPreferenceString(this,AppConstants.ROLE_NAME))
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding?.rvImplementingAgency?.layoutManager = layoutManager
        mBinding?.rvImplementingAgency?.adapter = implementingAdapter
        mBinding?.rvImplementingAgency?.addOnScrollListener(recyclerScrollListener)
    }

    override fun setVariables() {

    }

    override fun setObservers() {

        viewModel.implementingAgencyResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        implementingAgencyList.clear()

                        val remainingCount = userResponseModel.total_count % 10
                        totalPage = if (remainingCount == 0) {
                            val count = userResponseModel.total_count / 10
                            count
                        } else {
                            val count = userResponseModel.total_count / 10
                            count + 1
                        }
                    }
                    if(userResponseModel._result.is_add){
                        mBinding?.fabAddAgency?.showView()
                    }
                    else{
                        mBinding?.fabAddAgency?.hideView()
                    }
                    implementingAgencyList.addAll(userResponseModel._result.data)
                    implementingAdapter.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvImplementingAgency?.showView()
                } else {
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvImplementingAgency?.hideView()
                }
            }
        }

    }
    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
        fun addImplementingAgency(view: View) {
            startActivity(Intent(this@NationalLiveStockMissionIAList, NLMIAForm::class.java).putExtra("isFrom",1))
        }
        fun filter(view: View) {
            startActivity(Intent(this@NationalLiveStockMissionIAList, FilterStateActivity::class.java))
        }
    }

    private fun swipeForRefreshImplementingAgency(){
        mBinding?.srlImplementingAgency?.setOnRefreshListener {
            implementingAgencyAPICall(paginate = false, loader = true)
            mBinding?.srlImplementingAgency?.isRefreshing = false
        }
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
                                implementingAgencyAPICall(paginate = true, loader = true)
                            }
                        }
                    }
                }
            }
        }

    private fun implementingAgencyAPICall(paginate: Boolean,loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getImplementingAgencyApi(this, loader, ImplementingAgencyRequest(
            getPreferenceOfScheme(this@NationalLiveStockMissionIAList, AppConstants.SCHEME, Result::class.java)?.role_id,
            getPreferenceOfScheme(this@NationalLiveStockMissionIAList, AppConstants.SCHEME, Result::class.java)?.state_code,
            getPreferenceOfScheme(this@NationalLiveStockMissionIAList, AppConstants.SCHEME, Result::class.java)?.user_id,
            10,
            currentPage
            ))
    }
}