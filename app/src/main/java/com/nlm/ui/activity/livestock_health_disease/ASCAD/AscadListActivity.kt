package com.nlm.ui.activity.livestock_health_disease.ascad

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityAscadListBinding
import com.nlm.model.AscadListData
import com.nlm.model.AscadListRequest
import com.nlm.model.Result
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.activity.livestock_health_disease.mobile_veterinary_units.AddNewMobileVeterinaryUnitState
import com.nlm.ui.adapter.AscadAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class AscadListActivity : BaseActivity<ActivityAscadListBinding>(), CallBackDeleteAtId {
    private var mBinding: ActivityAscadListBinding? = null
    private var ascadAdapter: AscadAdapter? = null
    private var stateAscadList = ArrayList<AscadListData>()
    private var districtAscadList = ArrayList<AscadListData>()
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: String? = null
    private val viewModel = ViewModel()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true

    override val layoutId: Int
        get() = R.layout.activity_ascad_list

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        isFrom = intent?.extras?.getString(AppConstants.IS_FROM)
        when (isFrom) {
            getString(R.string.state) -> {
                ascadAdapter(stateAscadList)
            }

            getString(R.string.district) -> {
                ascadAdapter(districtAscadList)
            }
        }
        swipeForRefreshAscad()
    }

    override fun onResume() {
        super.onResume()
        when (isFrom) {
            getString(R.string.state) -> {
                stateAscadAPICall(paginate = false, loader = true)
            }

            getString(R.string.district) -> {
                districtAscadAPICall(paginate = false, loader = true)
            }
        }
    }

    private fun swipeForRefreshAscad() {
        mBinding?.srlAscad?.setOnRefreshListener {
            when (isFrom) {
                getString(R.string.state) -> {
                    stateAscadAPICall(paginate = false, loader = true)
                }

                getString(R.string.district) -> {
                    districtAscadAPICall(paginate = false, loader = true)
                }
            }
            mBinding?.srlAscad?.isRefreshing = false
        }
    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun filter(view: View) {
            startActivity(
                Intent(
                    this@AscadListActivity,
                    FilterStateActivity::class.java
                ).putExtra("isFrom", isFrom)
            )
        }

        fun add(view: View) {
            startActivity(
                Intent(
                    this@AscadListActivity,
                    AddNewMobileVeterinaryUnitState::class.java
                ).putExtra("isFrom", isFrom)
            )
        }
    }

    private fun ascadAdapter(list: ArrayList<AscadListData>) {
        ascadAdapter = AscadAdapter(this, list, isFrom, this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding?.rvAscad?.layoutManager = layoutManager
        mBinding?.rvAscad?.adapter = ascadAdapter
        mBinding?.rvAscad?.addOnScrollListener(recyclerScrollListener)
    }

    private fun stateAscadAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getStateAscadList(
            this, loader, AscadListRequest(
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
            )
        )
    }

    private fun districtAscadAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getDistrictAscadList(
            this, loader, AscadListRequest(
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
                                when (isFrom) {
                                    getString(R.string.state) -> {
                                        stateAscadAPICall(paginate = true, loader = true)
                                    }

                                    getString(R.string.district) -> {
                                        districtAscadAPICall(paginate = true, loader = true)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


    override fun setVariables() {
    }

    override fun setObservers() {

        viewModel.stateAscadListResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result?.data != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        stateAscadList.clear()

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
                    stateAscadList.addAll(userResponseModel._result.data)
                    ascadAdapter?.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvAscad?.showView()
                } else {
                    if (userResponseModel._result.is_add) {
                        mBinding?.fabAddAgency?.showView()
                    } else {
                        mBinding?.fabAddAgency?.hideView()
                    }
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvAscad?.hideView()
                }
            }
        }

        viewModel.districtAscadListResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result?.data != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        districtAscadList.clear()

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
                    districtAscadList.addAll(userResponseModel._result.data)
                    ascadAdapter?.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvAscad?.showView()
                } else {
                    if (userResponseModel._result.is_add) {
                        mBinding?.fabAddAgency?.showView()
                    } else {
                        mBinding?.fabAddAgency?.hideView()
                    }
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvAscad?.hideView()
                }
            }
        }
    }

    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
    }
}