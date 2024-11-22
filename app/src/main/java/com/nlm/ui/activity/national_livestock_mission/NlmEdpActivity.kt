package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityNlmEdpBinding
import com.nlm.model.AssistanceForEARequest
import com.nlm.model.NLMEdpRequest
import com.nlm.model.NlmEdpData
import com.nlm.model.Result
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.AssistanceForEaAdapter
import com.nlm.ui.adapter.NlmEdpAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class NlmEdpActivity : BaseActivity<ActivityNlmEdpBinding>(), CallBackDeleteAtId {
    private var mBinding: ActivityNlmEdpBinding? = null
    private var nlmEdpAdapter: NlmEdpAdapter? = null
    private var nlmEdpList = ArrayList<NlmEdpData>()
    private var layoutManager: LinearLayoutManager? = null
    private val viewModel = ViewModel()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true

    override val layoutId: Int
        get() = R.layout.activity_nlm_edp

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        nlmEdpAdapter()
        swipeForRefreshNlmEdp()
    }

    override fun onResume() {
        super.onResume()
        nlmEdpAPICall(paginate = false, loader = true)
    }

    private fun nlmEdpAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getNlmEdp(
            this, loader, NLMEdpRequest(
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
                                nlmEdpAPICall(paginate = true, loader = true)
                            }
                        }
                    }
                }
            }
        }

    private fun swipeForRefreshNlmEdp() {
        mBinding?.srlNlmEdp?.setOnRefreshListener {
            nlmEdpAPICall(paginate = false, loader = true)
            mBinding?.srlNlmEdp?.isRefreshing = false
        }
    }

    private fun nlmEdpAdapter() {
        nlmEdpAdapter = NlmEdpAdapter(this, nlmEdpList, this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding?.rvNlmEdp?.layoutManager = layoutManager
        mBinding?.rvNlmEdp?.adapter = nlmEdpAdapter
        mBinding?.rvNlmEdp?.addOnScrollListener(recyclerScrollListener)
    }


    override fun setVariables() {
    }

    override fun setObservers() {

        viewModel.nlmEdpResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        nlmEdpList.clear()

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
                    nlmEdpList.addAll(userResponseModel._result.data)
                    nlmEdpAdapter?.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvNlmEdp?.showView()
                } else {
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvNlmEdp?.hideView()
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
                this@NlmEdpActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 17)
            startActivity(intent)
        }
        fun add(view: View) {
            startActivity(
                Intent(
                    this@NlmEdpActivity,
                    AddNlmFpForestLandActivity::class.java
                )
            )
        }
    }

    override fun onClickItem(ID: Int?, position: Int,isFrom:Int) {
    }
}