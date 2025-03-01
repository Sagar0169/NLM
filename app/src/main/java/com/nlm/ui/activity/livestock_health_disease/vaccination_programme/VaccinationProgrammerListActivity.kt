package com.nlm.ui.activity.livestock_health_disease.vaccination_programme

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtIdString
import com.nlm.databinding.ActivityVaccinationProgrammerListBinding
import com.nlm.model.DistrictVaccinationProgrammeAddRequest
import com.nlm.model.FarmerVaccinationProgrammeAddRequest
import com.nlm.model.Result
import com.nlm.model.StateVaccinationProgrammeAddRequest
import com.nlm.model.VaccinationProgrammerListData
import com.nlm.model.VaccinationProgrammerListRequest
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockMissionIAList
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockMissionIAList.Companion.FILTER_REQUEST_CODE
import com.nlm.ui.adapter.VaccinationProgrammerAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class VaccinationProgrammerListActivity : BaseActivity<ActivityVaccinationProgrammerListBinding>(),
    CallBackDeleteAtIdString {
    private var mBinding: ActivityVaccinationProgrammerListBinding? = null
    private var vaccinationProgrammerAdapter: VaccinationProgrammerAdapter? = null
    private var stateVaccinationProgrammerList = ArrayList<VaccinationProgrammerListData>()
    private var districtVaccinationProgrammerList = ArrayList<VaccinationProgrammerListData>()
    private var farmerVaccinationProgrammerList = ArrayList<VaccinationProgrammerListData>()
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: String? = null
    private val viewModel = ViewModel()
    private var currentPage = 1
    private var itemPosition: Int? = null
    private var totalPage = 1
    private var loading = true
    var block: String = ""
    var village: String = ""
    var stateId: Int = 0
    var districtName: String = ""
    var disctrictId: Int = 0
    private var isFromList: Int = 0

    override val layoutId: Int
        get() = R.layout.activity_vaccination_programmer_list

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        isFrom = intent?.extras?.getString(AppConstants.IS_FROM)
        when (isFrom) {
            getString(R.string.state) -> {
                mBinding?.tvHeading?.text = getString(R.string.list_of_state_vaccination_programme)
                vaccinationProgrammerAdapter(stateVaccinationProgrammerList)
                isFromList = 41
            }

            getString(R.string.district) -> {
                mBinding?.tvHeading?.text =
                    getString(R.string.list_of_district_vaccination_programme)
                vaccinationProgrammerAdapter(districtVaccinationProgrammerList)
                isFromList = 42
            }

            getString(R.string.farmer_level) -> {
                mBinding?.tvHeading?.text =
                    getString(R.string.list_of_beneficiary_farmer_vaccination_programme)
                vaccinationProgrammerAdapter(farmerVaccinationProgrammerList)
                isFromList = 53
            }
        }
        swipeForRefreshVaccinationProgrammer()
    }

    override fun onResume() {
        super.onResume()
        currentPage = 1
        when (isFrom) {
            getString(R.string.state) -> {
                stateVaccinationProgrammerAPICall(paginate = false, loader = true,disctrictId,
                    block,
                    village)
            }

            getString(R.string.district) -> {
                districtVaccinationProgrammerAPICall(paginate = false, loader = true,disctrictId,
                    block,
                    village)
            }

            getString(R.string.farmer_level) -> {
                farmerVaccinationProgrammerAPICall(paginate = false, loader = true,getPreferenceOfScheme(
                    this,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code ,   disctrictId,
                    block,
                    village)

            }
        }
    }

    private fun swipeForRefreshVaccinationProgrammer() {
        mBinding?.srlVaccinationProgrammer?.setOnRefreshListener {
            currentPage = 1
            when (isFrom) {
                getString(R.string.state) -> {
                    stateVaccinationProgrammerAPICall(paginate = false, loader = true,disctrictId,
                        block,
                        village)
                }

                getString(R.string.district) -> {
                    districtVaccinationProgrammerAPICall(paginate = false, loader = true,disctrictId,
                        block,
                        village)
                }

                getString(R.string.farmer_level) -> {
                    farmerVaccinationProgrammerAPICall(paginate = false, loader = true,getPreferenceOfScheme(
                        this,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_code,disctrictId,
                        block,
                        village)
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

            val intent =
                Intent(this@VaccinationProgrammerListActivity, FilterStateActivity::class.java)
            intent.putExtra("isFrom", isFromList)
            intent.putExtra("selectedStateId", stateId) // previously selected state ID
            intent.putExtra("districtId", disctrictId) // previously selected state ID
            intent.putExtra("block", block)
            intent.putExtra("districtName", districtName)
            intent.putExtra("village", village)
            startActivityForResult(intent, FILTER_REQUEST_CODE)
        }

        fun add(view: View) {
            when (isFrom) {
                getString(R.string.state) -> {
                    startActivity(
                        Intent(
                            this@VaccinationProgrammerListActivity,
                            AddVaccinationProgrammeStateLevel::class.java
                        )
                            .putExtra("isFrom", isFrom)
                    )
                }

                getString(R.string.district) -> {
                    startActivity(
                        Intent(
                            this@VaccinationProgrammerListActivity,
                            AddVaccinationProgrammeDistrictLevel::class.java
                        )
                            .putExtra("isFrom", isFrom)
                    )
                }

                getString(R.string.farmer_level) -> {
                    startActivity(
                        Intent(
                            this@VaccinationProgrammerListActivity,
                            AddVaccinationProgrammeFarmerLevel::class.java
                        )
                            .putExtra("isFrom", isFrom)
                    )
                }
            }
        }
    }

    private fun vaccinationProgrammerAdapter(list: ArrayList<VaccinationProgrammerListData>) {
        vaccinationProgrammerAdapter = VaccinationProgrammerAdapter(this, list, isFrom, this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding?.rvVaccinationProgrammer?.layoutManager = layoutManager
        mBinding?.rvVaccinationProgrammer?.adapter = vaccinationProgrammerAdapter
        mBinding?.rvVaccinationProgrammer?.addOnScrollListener(recyclerScrollListener)
    }

    private fun stateVaccinationProgrammerAPICall(paginate: Boolean, loader: Boolean, district: Int,
                                                  block: String,
                                                  village: String) {
        if (paginate) {
            currentPage++
        }
        viewModel.getStateVaccinationProgrammerList(
            this, loader, VaccinationProgrammerListRequest(
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
                village = village,
                limit = 10,
            )
        )
    }

    private fun districtVaccinationProgrammerAPICall(paginate: Boolean, loader: Boolean  ,district: Int,
                                                     block: String,
                                                     village: String) {
        if (paginate) {
            currentPage++
        }
        viewModel.getDistrictVaccinationProgrammerList(
            this, loader, VaccinationProgrammerListRequest(
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
                village = village,
                district_code=district,
                limit = 10,
            )
        )
    }

    private fun farmerVaccinationProgrammerAPICall(paginate: Boolean, loader: Boolean, Stateid:Int?,  district: Int, block: String, village: String) {
        if (paginate) {
            currentPage++
        }
        viewModel.getFarmerVaccinationProgrammerList(
            this, loader, VaccinationProgrammerListRequest(
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
                state_code =Stateid,
                district_code = district,
                page = currentPage,
                village=village,
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
                                        stateVaccinationProgrammerAPICall(
                                            paginate = true,
                                            loader = true,disctrictId,
                                            block,
                                            village
                                        )
                                    }

                                    getString(R.string.district) -> {
                                        districtVaccinationProgrammerAPICall(
                                            paginate = true,
                                            loader = true,disctrictId,
                                            block,
                                            village
                                        )
                                    }

                                    getString(R.string.farmer_level) -> {
                                        farmerVaccinationProgrammerAPICall(
                                            paginate = true,
                                            loader = true,
                                            getPreferenceOfScheme(
                                                this@VaccinationProgrammerListActivity,
                                                AppConstants.SCHEME,
                                                Result::class.java
                                            )?.state_code,disctrictId,
                                            block,
                                            village
                                        )
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
        viewModel.stateVaccinationProgrammerAddResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {
                    mBinding?.clParent?.let { it1 -> showSnackbar(it1, userResponseModel.message) }
                } else {
                    itemPosition?.let { it1 -> vaccinationProgrammerAdapter?.onDeleteButtonClick(it1) }
                    mBinding?.clParent?.let { it1 -> showSnackbar(it1, userResponseModel.message) }
                }
            }
        }
        viewModel.districtVaccinationProgrammerAddResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {
                    mBinding?.clParent?.let { it1 -> showSnackbar(it1, userResponseModel.message) }
                } else {
                    itemPosition?.let { it1 -> vaccinationProgrammerAdapter?.onDeleteButtonClick(it1) }
                    mBinding?.clParent?.let { it1 -> showSnackbar(it1, userResponseModel.message) }
                }
            }
        }
        viewModel.farmerVaccinationProgrammerAddResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {
                    mBinding?.clParent?.let { it1 -> showSnackbar(it1, userResponseModel.message) }
                } else {
                    itemPosition?.let { it1 -> vaccinationProgrammerAdapter?.onDeleteButtonClick(it1) }
                    mBinding?.clParent?.let { it1 -> showSnackbar(it1, userResponseModel.message) }
                }
            }
        }
    }


    override fun onClickItem(ID: Int?, position: Int, isFrom: String) {
        when (isFrom) {
            getString(R.string.state) -> {
                viewModel.getStateVaccinationProgrammeAdd(
                    this@VaccinationProgrammerListActivity, true,
                    StateVaccinationProgrammeAddRequest(
                        id = ID,
                        role_id = getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.role_id,
                        state_code = getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.state_code,
                        user_id = getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.user_id,
                        is_deleted = 1
                    )
                )
                itemPosition = position
            }

            getString(R.string.district) -> {
                viewModel.getDistrictVaccinationProgrammeAdd(
                    this@VaccinationProgrammerListActivity, true,
                    DistrictVaccinationProgrammeAddRequest(
                        id = ID,
                        role_id = getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.role_id,
                        state_code = getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.state_code,
                        user_id = getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.user_id,
                        is_deleted = 1
                    )
                )
                itemPosition = position
            }

            getString(R.string.farmer_level) -> {
                viewModel.getFarmerVaccinationProgrammeAdd(
                    this@VaccinationProgrammerListActivity, true,
                    FarmerVaccinationProgrammeAddRequest(
                        id = ID,
                        role_id = getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.role_id,
                        state_code = getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.state_code,
                        user_id = getPreferenceOfScheme(
                            this,
                            AppConstants.SCHEME,
                            Result::class.java
                        )?.user_id,
                        is_deleted = 1
                    )
                )
                itemPosition = position
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the data passed from FilterStateActivity
            disctrictId = data?.getIntExtra("districtId", 0)!!
            stateId = data.getIntExtra("stateId", 0)
            block = data.getStringExtra("block").toString()
            village = data.getStringExtra("village").toString()
            districtName = data.getStringExtra("districtName").toString()
            //Need to add year also
            // Log the data
            when (isFrom) {
                getString(R.string.state) -> {
                    stateVaccinationProgrammerAPICall(
                        paginate = false,
                        loader = true,
                        disctrictId,
                        block,
                        village
                    )
                }

                getString(R.string.district) -> {
                    districtVaccinationProgrammerAPICall(
                        paginate = false,
                        loader = true, disctrictId, block, village
                    )
                }

                getString(R.string.farmer_level) -> {
                    getPreferenceOfScheme(
                        this@VaccinationProgrammerListActivity,
                        AppConstants.SCHEME,
                        Result::class.java
                    )?.state_code?.let {
                        farmerVaccinationProgrammerAPICall(
                            paginate = false,
                            loader = true,
                            disctrictId,
                            it,
                            block,
                            village
                        )
                    }
                }


            }
//            Log.d("FilterResult", "Received data from FilterStateActivity: $d")
//            Log.d("FilterResult", "Received data from FilterStateActivity: $block")
        }
    }
}