package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityRsplabBinding
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.ImplementingAgencyRequest
import com.nlm.model.RSPLabListData
import com.nlm.model.Result
import com.nlm.model.RspLabListRequest
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.RSPLABListAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class RSPLabList : BaseActivity<ActivityRsplabBinding>(), CallBackDeleteAtId {
    override val layoutId: Int
        get() = R.layout.activity_rsplab

    private var viewModel = ViewModel()
    private var mBinding: ActivityRsplabBinding? = null
    private var rSPLABListAdapter: RSPLABListAdapter ?= null
    private var rSPLabList = ArrayList<RSPLabListData>()
    private var layoutManager: LinearLayoutManager? = null
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    private var itemPosition : Int ?= null

    override fun initView() {
        mBinding = viewDataBinding
        viewModel.init()
        mBinding?.clickAction = ClickActions()
        rSPLABListAdapter()
        swipeForRefreshSrlRSPLab()
    }
    companion object {
        const val FILTER_REQUEST_CODE = 1002
    }
    private fun rSPLABListAdapter() {
        rSPLABListAdapter = RSPLABListAdapter(this,rSPLabList,this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding?.rvRspLabView?.layoutManager = layoutManager
        mBinding?.rvRspLabView?.adapter = rSPLABListAdapter
        mBinding?.rvRspLabView?.addOnScrollListener(recyclerScrollListener)
    }

    private fun swipeForRefreshSrlRSPLab() {
        mBinding?.SrlRSPLab?.setOnRefreshListener {
            implementingAgencyAPICall(paginate = false, loader = true)
            mBinding?.SrlRSPLab?.isRefreshing = false
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

    override fun setVariables() {}

    override fun setObservers() {
        viewModel.rspLabListResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        rSPLabList.clear()

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
                    rSPLabList.addAll(userResponseModel._result.data)
                    rSPLABListAdapter?.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvRspLabView?.showView()
                } else {
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvRspLabView?.hideView()
                }
            }
        }
    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun add(view: View) {
            startActivity(Intent(this@RSPLabList, RspLabSemenForms::class.java).putExtra("isFrom",1))
        }

        fun filter(view: View) {
            startActivity(Intent(this@RSPLabList, FilterStateActivity::class.java))
        }
    }

    private fun implementingAgencyAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getRspLabListApi(
            this, loader, RspLabListRequest(
                getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id,
                getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
                10,
                currentPage
            )
        )
    }

    override fun onClickItem(ID: Int?, position: Int) {
//        viewModel.getImplementingAgencyAddApi(this,true,
//            ImplementingAgencyAddRequest(
//                user_id = getPreferenceOfScheme(this,AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
//                part = "part1",
//                id= ID, is_deleted = 1
//            )
//        )
//        itemPosition = position
    }

    override fun onResume() {
        super.onResume()
        implementingAgencyAPICall(paginate = false, loader = true)
    }
}