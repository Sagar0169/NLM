package com.nlm.ui.activity.livestock_health_disease.mobile_veterinary_units

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityStateMobileVeterinaryBinding
import com.nlm.model.MobileVeterinaryUnitsListData
import com.nlm.model.MobileVeterinaryUnitsListRequest
import com.nlm.model.Result
import com.nlm.model.VaccinationProgrammerListData
import com.nlm.model.VaccinationProgrammerListRequest
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.MobileVeterinaryAdapter
import com.nlm.ui.adapter.VaccinationProgrammerAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class MobileVeterinaryListActivity : BaseActivity<ActivityStateMobileVeterinaryBinding>(),CallBackDeleteAtId {
    private var mBinding: ActivityStateMobileVeterinaryBinding? = null
    private var mobileVeterinaryAdapter: MobileVeterinaryAdapter?= null
    private var stateMobileVeterinaryList= ArrayList<MobileVeterinaryUnitsListData>()
    private var districtMobileVeterinaryList= ArrayList<MobileVeterinaryUnitsListData>()
    private var blockMobileVeterinaryList= ArrayList<MobileVeterinaryUnitsListData>()
    private var farmerMobileVeterinaryList= ArrayList<MobileVeterinaryUnitsListData>()
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: String ?= null
    private val viewModel= ViewModel()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true

    override val layoutId: Int
        get() = R.layout.activity_state_mobile_veterinary

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        isFrom = intent?.extras?.getString(AppConstants.IS_FROM)
        when (isFrom) {
            getString(R.string.state) -> {
                mobileVeterinaryAdapter(stateMobileVeterinaryList)
            }
            getString(R.string.district) -> {
                mobileVeterinaryAdapter(districtMobileVeterinaryList)
            }
            getString(R.string.block_level) -> {
                mobileVeterinaryAdapter(blockMobileVeterinaryList)
            }
            getString(R.string.farmer_level) -> {
                mobileVeterinaryAdapter(farmerMobileVeterinaryList)
            }
        }
        swipeForRefreshMobileVeterinary()
    }

    override fun onResume() {
        super.onResume()
        when (isFrom) {
            getString(R.string.state) -> {
                stateMobileVeterinaryAPICall(paginate = false, loader = true)
            }
            getString(R.string.district) -> {
                districtMobileVeterinaryAPICall(paginate = false, loader = true)
            }
            getString(R.string.block_level) -> {
                blockMobileVeterinaryAPICall(paginate = false, loader = true)
            }
            getString(R.string.farmer_level) -> {
                farmerMobileVeterinaryAPICall(paginate = false, loader = true)
            }
        }
    }

    private fun swipeForRefreshMobileVeterinary() {
        mBinding?.srlMobileVeterinary?.setOnRefreshListener {
            when (isFrom) {
                getString(R.string.state) -> {
                    stateMobileVeterinaryAPICall(paginate = false, loader = true)
                }
                getString(R.string.district) -> {
                    districtMobileVeterinaryAPICall(paginate = false, loader = true)
                }
                getString(R.string.block_level) -> {
                    blockMobileVeterinaryAPICall(paginate = false, loader = true)
                }
                getString(R.string.farmer_level) -> {
                    farmerMobileVeterinaryAPICall(paginate = false, loader = true)
                }
            }
            mBinding?.srlMobileVeterinary?.isRefreshing = false
        }
    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun filter(view: View) {
            startActivity(Intent(
                this@MobileVeterinaryListActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", isFrom))
        }

        fun add(view: View){
            when (isFrom) {
                getString(R.string.state) -> {
                    startActivity(Intent(this@MobileVeterinaryListActivity, AddNewMobileVeterinaryUnit::class.java).putExtra("isFrom", isFrom))
                }
                getString(R.string.district) -> {
                }
                getString(R.string.block_level) -> {
                }
                getString(R.string.farmer_level) -> {
                }
            }
        }
    }

    private fun mobileVeterinaryAdapter(list: ArrayList<MobileVeterinaryUnitsListData>) {
        mobileVeterinaryAdapter = MobileVeterinaryAdapter(this,list,isFrom,this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding?.rvMobileVeterinary?.layoutManager = layoutManager
        mBinding?.rvMobileVeterinary?.adapter = mobileVeterinaryAdapter
        mBinding?.rvMobileVeterinary?.addOnScrollListener(recyclerScrollListener)
    }
    private fun stateMobileVeterinaryAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getStateMobileVeterinaryUnitsList(
            this, loader, MobileVeterinaryUnitsListRequest(
                role_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id,
                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                state_code = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
                page = currentPage,
                limit = 10,
            )
        )
    }
    private fun districtMobileVeterinaryAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getDistrictMobileVeterinaryUnitsList(
            this, loader, MobileVeterinaryUnitsListRequest(
                role_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id,
                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                state_code = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
                page = currentPage,
                limit = 10,
            )
        )
    }
    private fun blockMobileVeterinaryAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getBlockMobileVeterinaryUnitsList(
            this, loader, MobileVeterinaryUnitsListRequest(
                role_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id,
                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                state_code = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
                page = currentPage,
                limit = 10,
            )
        )
    }
    private fun farmerMobileVeterinaryAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getFarmerMobileVeterinaryUnitsList(
            this, loader, MobileVeterinaryUnitsListRequest(
                role_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id,
                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                state_code = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
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
                                        stateMobileVeterinaryAPICall(paginate = true, loader = true)
                                    }
                                    getString(R.string.district) -> {
                                        districtMobileVeterinaryAPICall(paginate = true, loader = true)
                                    }
                                    getString(R.string.block_level) -> {
                                        blockMobileVeterinaryAPICall(paginate = true, loader = true)
                                    }
                                    getString(R.string.farmer_level) -> {
                                        farmerMobileVeterinaryAPICall(paginate = true, loader = true)
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

        viewModel.stateMobileVeterinaryUnitsListResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result?.data != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        stateMobileVeterinaryList.clear()

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
                    stateMobileVeterinaryList.addAll(userResponseModel._result.data)
                    mobileVeterinaryAdapter?.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvMobileVeterinary?.showView()
                } else {
                    if (userResponseModel._result.is_add) {
                        mBinding?.fabAddAgency?.showView()
                    } else {
                        mBinding?.fabAddAgency?.hideView()
                    }
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvMobileVeterinary?.hideView()
                }
            }
        }

        viewModel.districtMobileVeterinaryUnitsListResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result?.data != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        districtMobileVeterinaryList.clear()

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
                    districtMobileVeterinaryList.addAll(userResponseModel._result.data)
                    mobileVeterinaryAdapter?.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvMobileVeterinary?.showView()
                } else {
                    if (userResponseModel._result.is_add) {
                        mBinding?.fabAddAgency?.showView()
                    } else {
                        mBinding?.fabAddAgency?.hideView()
                    }
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvMobileVeterinary?.hideView()
                }
            }
        }

        viewModel.blockMobileVeterinaryUnitsListResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result?.data != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        blockMobileVeterinaryList.clear()

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
                    blockMobileVeterinaryList.addAll(userResponseModel._result.data)
                    mobileVeterinaryAdapter?.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvMobileVeterinary?.showView()
                } else {
                    if (userResponseModel._result.is_add) {
                        mBinding?.fabAddAgency?.showView()
                    } else {
                        mBinding?.fabAddAgency?.hideView()
                    }
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvMobileVeterinary?.hideView()
                }
            }
        }

        viewModel.farmerMobileVeterinaryUnitsListResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result?.data != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        farmerMobileVeterinaryList.clear()

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
                    farmerMobileVeterinaryList.addAll(userResponseModel._result.data)
                    mobileVeterinaryAdapter?.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvMobileVeterinary?.showView()
                } else {
                    if (userResponseModel._result.is_add) {
                        mBinding?.fabAddAgency?.showView()
                    } else {
                        mBinding?.fabAddAgency?.hideView()
                    }
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvMobileVeterinary?.hideView()
                }
            }
        }
    }

    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
    }
}