package com.nlm.ui.activity.livestock_health_disease.vaccination_programme

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityVaccinationProgrammerListBinding
import com.nlm.model.Result
import com.nlm.model.VaccinationProgrammerListData
import com.nlm.model.VaccinationProgrammerListRequest
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.activity.livestock_health_disease.mobile_veterinary_units.AddNewMobileVeterinaryUnitState
import com.nlm.ui.adapter.VaccinationProgrammerAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class VaccinationProgrammerListActivity : BaseActivity<ActivityVaccinationProgrammerListBinding>(),CallBackDeleteAtId {
    private var mBinding: ActivityVaccinationProgrammerListBinding? = null
    private var vaccinationProgrammerAdapter: VaccinationProgrammerAdapter ?= null
    private var stateVaccinationProgrammerList= ArrayList<VaccinationProgrammerListData>()
    private var districtVaccinationProgrammerList= ArrayList<VaccinationProgrammerListData>()
    private var farmerVaccinationProgrammerList= ArrayList<VaccinationProgrammerListData>()
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: String ?= null
    private val viewModel= ViewModel()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true

    override val layoutId: Int
        get() = R.layout.activity_vaccination_programmer_list

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        isFrom = intent?.extras?.getString(AppConstants.IS_FROM)
        when (isFrom) {
            getString(R.string.state) -> {
                vaccinationProgrammerAdapter(stateVaccinationProgrammerList)
            }
            getString(R.string.district) -> {
                vaccinationProgrammerAdapter(districtVaccinationProgrammerList)
            }
            getString(R.string.farmer_level) -> {
                vaccinationProgrammerAdapter(farmerVaccinationProgrammerList)
            }
        }
        swipeForRefreshVaccinationProgrammer()
    }

    override fun onResume() {
        super.onResume()
        when (isFrom) {
            getString(R.string.state) -> {
                stateVaccinationProgrammerAPICall(paginate = false, loader = true)
            }
            getString(R.string.district) -> {
                districtVaccinationProgrammerAPICall(paginate = false, loader = true)
            }
            getString(R.string.farmer_level) -> {
                farmerVaccinationProgrammerAPICall(paginate = false, loader = true)
            }
        }
    }

    private fun swipeForRefreshVaccinationProgrammer() {
        mBinding?.srlVaccinationProgrammer?.setOnRefreshListener {
            when (isFrom) {
                getString(R.string.state) -> {
                    stateVaccinationProgrammerAPICall(paginate = false, loader = true)
                }
                getString(R.string.district) -> {
                    districtVaccinationProgrammerAPICall(paginate = false, loader = true)
                }
                getString(R.string.farmer_level) -> {
                    farmerVaccinationProgrammerAPICall(paginate = false, loader = true)
                }
            }
            mBinding?.srlVaccinationProgrammer?.isRefreshing = false
        }
    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun filter(view: View) {
            startActivity(
                Intent(
                    this@VaccinationProgrammerListActivity,
                    FilterStateActivity::class.java).putExtra("isFrom", isFrom))
        }
        fun add(view: View){
            startActivity(Intent(this@VaccinationProgrammerListActivity, AddNewMobileVeterinaryUnitState::class.java)
                .putExtra("isFrom", isFrom)
            )
        }
    }


    private fun vaccinationProgrammerAdapter(list: ArrayList<VaccinationProgrammerListData>) {
        vaccinationProgrammerAdapter = VaccinationProgrammerAdapter(this,list,isFrom,this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding?.rvVaccinationProgrammer?.layoutManager = layoutManager
        mBinding?.rvVaccinationProgrammer?.adapter = vaccinationProgrammerAdapter
        mBinding?.rvVaccinationProgrammer?.addOnScrollListener(recyclerScrollListener)
    }

    private fun stateVaccinationProgrammerAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getStateVaccinationProgrammerList(
            this, loader, VaccinationProgrammerListRequest(
                role_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id,
                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                state_code = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
                page = currentPage,
                limit = 10,
            )
        )
    }
    private fun districtVaccinationProgrammerAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getDistrictVaccinationProgrammerList(
            this, loader, VaccinationProgrammerListRequest(
                role_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id,
                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                state_code = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
                page = currentPage,
                limit = 10,
            )
        )
    }
    private fun farmerVaccinationProgrammerAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getFarmerVaccinationProgrammerList(
            this, loader, VaccinationProgrammerListRequest(
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
                                        stateVaccinationProgrammerAPICall(paginate = true, loader = true)
                                    }
                                    getString(R.string.district) -> {
                                        districtVaccinationProgrammerAPICall(paginate = true, loader = true)
                                    }
                                    getString(R.string.farmer_level) -> {
                                        farmerVaccinationProgrammerAPICall(paginate = true, loader = true)
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

        viewModel.stateVaccinationProgrammerListResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result?.data != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        stateVaccinationProgrammerList.clear()

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
                    stateVaccinationProgrammerList.addAll(userResponseModel._result.data)
                    vaccinationProgrammerAdapter?.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvVaccinationProgrammer?.showView()
                } else {
                    if (userResponseModel._result.is_add) {
                        mBinding?.fabAddAgency?.showView()
                    } else {
                        mBinding?.fabAddAgency?.hideView()
                    }
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvVaccinationProgrammer?.hideView()
                }
            }
        }

        viewModel.districtVaccinationProgrammerListResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result?.data != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        districtVaccinationProgrammerList.clear()

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
                    districtVaccinationProgrammerList.addAll(userResponseModel._result.data)
                    vaccinationProgrammerAdapter?.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvVaccinationProgrammer?.showView()
                } else {
                    if (userResponseModel._result.is_add) {
                        mBinding?.fabAddAgency?.showView()
                    } else {
                        mBinding?.fabAddAgency?.hideView()
                    }
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvVaccinationProgrammer?.hideView()
                }
            }
        }

        viewModel.farmerVaccinationProgrammerListResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result?.data != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        farmerVaccinationProgrammerList.clear()

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
                    farmerVaccinationProgrammerList.addAll(userResponseModel._result.data)
                    vaccinationProgrammerAdapter?.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvVaccinationProgrammer?.showView()
                } else {
                    if (userResponseModel._result.is_add) {
                        mBinding?.fabAddAgency?.showView()
                    } else {
                        mBinding?.fabAddAgency?.hideView()
                    }
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvVaccinationProgrammer?.hideView()
                }
            }
        }
    }

    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {

    }
}