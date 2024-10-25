package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ActivityStateSemenBankListBinding
import com.nlm.model.ArtificialInseminationRequest
import com.nlm.model.DataSemen
import com.nlm.model.Result
import com.nlm.model.StateSemenBankRequest
import com.nlm.model.State_Semen_Bank

import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.StateSemenAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class StateSemenBankList : BaseActivity<ActivityStateSemenBankListBinding>() {
    private var mBinding: ActivityStateSemenBankListBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_state_semen_bank_list
    private lateinit var implementingAdapter: StateSemenAdapter
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    private var viewModel = ViewModel()
    private lateinit var nodalOfficerList: ArrayList<DataSemen>
    private var layoutManager: LinearLayoutManager? = null
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction=ClickActions()
        viewModel.init()
//        if(Utility.getPreferenceString(this, AppConstants.ROLE_NAME)=="Super Admin")
//        {
//            mBinding!!.fabAddAgency.hideView()
//        }
        nodalOfficerList = arrayListOf()
        implementingAgency()
        stateSemenBankAPICall(paginate = false, loader = true)
        swipeForRefreshImplementingAgency()
        mBinding!!.fabAddAgency.setOnClickListener{
            val intent = Intent(this@StateSemenBankList, StateSemenBankForms::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }

    override fun setVariables() {

    }

    override fun setObservers() {
        viewModel.statesemenBankResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        nodalOfficerList.clear()

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
                    nodalOfficerList.addAll(userResponseModel._result.data)
                    implementingAdapter.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvStateSemenLabView?.showView()
                } else {
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvStateSemenLabView?.hideView()
                }
            }
        }
    }
    private fun swipeForRefreshImplementingAgency() {
        mBinding?.srlImplementingAgency?.setOnRefreshListener {
            stateSemenBankAPICall(paginate = false, loader = true)
            mBinding?.srlImplementingAgency?.isRefreshing = false
        }
    }
    inner class ClickActions {
        fun backPress(view: View){
            onBackPressed()
        }
        fun filter(view: View) {
            val intent = Intent(
                this@StateSemenBankList,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 34)
            startActivity(intent)
        }

    }
    private fun stateSemenBankAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getStateSemenBankApi(
            this, loader, StateSemenBankRequest(
                getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id,
                getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
                getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                10,
                currentPage
            )
        )
    }
    private fun implementingAgency() {
        implementingAdapter = StateSemenAdapter(nodalOfficerList,2,Utility.getPreferenceString(this, AppConstants.ROLE_NAME))
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvStateSemenLabView.layoutManager = layoutManager
        mBinding!!.rvStateSemenLabView.adapter = implementingAdapter
        mBinding?.rvStateSemenLabView?.addOnScrollListener(recyclerScrollListener)
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
                                stateSemenBankAPICall(paginate = true, loader = true)
                            }
                        }
                    }
                }
            }
        }
}