package com.nlm.ui.activity.livestock_health_disease.mobile_veterinary_units

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackDeleteAtIdString
import com.nlm.databinding.ActivityStateMobileVeterinaryBinding
import com.nlm.model.BlockMobileVeterinaryUnitAddRequest
import com.nlm.model.DistrictMobileVeterinaryUnitAddRequest
import com.nlm.model.FarmerMobileVeterinaryUnitsAddRequest
import com.nlm.model.MobileVeterinaryUnitsListData
import com.nlm.model.MobileVeterinaryUnitsListRequest
import com.nlm.model.RSPAddRequest
import com.nlm.model.Result
import com.nlm.model.StateMobileVeterinaryUnitAddRequest
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockMissionIAList
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockMissionIAList.Companion.FILTER_REQUEST_CODE
import com.nlm.ui.adapter.MobileVeterinaryAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class MobileVeterinaryListActivity : BaseActivity<ActivityStateMobileVeterinaryBinding>(),
    CallBackDeleteAtIdString {
    private var mBinding: ActivityStateMobileVeterinaryBinding? = null
    private var mobileVeterinaryAdapter: MobileVeterinaryAdapter? = null
    private var stateMobileVeterinaryList = ArrayList<MobileVeterinaryUnitsListData>()
    private var districtMobileVeterinaryList = ArrayList<MobileVeterinaryUnitsListData>()
    private var blockMobileVeterinaryList = ArrayList<MobileVeterinaryUnitsListData>()
    private var farmerMobileVeterinaryList = ArrayList<MobileVeterinaryUnitsListData>()
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: String? = null
    private var isFromList: Int = 0
    private val viewModel = ViewModel()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    private var itemPosition: Int? = null
    var stateId: Int = 0
    var districtId: Int = 0
    var districtName: String = ""
    var block: String = ""
    var village: String = ""
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
                isFromList = 41
            }

            getString(R.string.district) -> {
                mobileVeterinaryAdapter(districtMobileVeterinaryList)
                isFromList = 42
            }

            getString(R.string.block_level) -> {
                mobileVeterinaryAdapter(blockMobileVeterinaryList)
                isFromList = 43
            }

            getString(R.string.farmer_level) -> {
                mobileVeterinaryAdapter(farmerMobileVeterinaryList)
                isFromList = 44
            }
        }
        swipeForRefreshMobileVeterinary()
    }

    override fun onResume() {
        super.onResume()
        when (isFrom) {
            getString(R.string.state) -> {
                stateMobileVeterinaryAPICall(
                    paginate = false,
                    loader = true,
                    districtId,
                    block,
                    village
                )
            }

            getString(R.string.district) -> {
                districtMobileVeterinaryAPICall(
                    paginate = false,
                    loader = true,
                    districtId,
                    block,
                    village
                )
            }

            getString(R.string.block_level) -> {
                blockMobileVeterinaryAPICall(
                    paginate = false,
                    loader = true,
                    districtId,
                    block,
                    village
                )
            }

            getString(R.string.farmer_level) -> {
                farmerMobileVeterinaryAPICall(
                    paginate = false,
                    loader = true,
                    districtId,
                    block,
                    village
                )
            }
        }
    }

    private fun swipeForRefreshMobileVeterinary() {
        mBinding?.srlMobileVeterinary?.setOnRefreshListener {
            when (isFrom) {
                getString(R.string.state) -> {
                    stateMobileVeterinaryAPICall(
                        paginate = false,
                        loader = true,
                        districtId,
                        block,
                        village
                    )
                }

                getString(R.string.district) -> {
                    districtMobileVeterinaryAPICall(
                        paginate = false,
                        loader = true,
                        districtId,
                        block,
                        village
                    )
                }

                getString(R.string.block_level) -> {
                    blockMobileVeterinaryAPICall(
                        paginate = false,
                        loader = true,
                        districtId,
                        block,
                        village
                    )
                }

                getString(R.string.farmer_level) -> {
                    farmerMobileVeterinaryAPICall(
                        paginate = false,
                        loader = true,
                        districtId,
                        block,
                        village
                    )
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
            val intent =
                Intent(this@MobileVeterinaryListActivity, FilterStateActivity::class.java)
            intent.putExtra("isFrom", isFromList)
            intent.putExtra("selectedStateId", stateId) // previously selected state ID
            intent.putExtra("districtId", districtId) // previously selected state ID
            intent.putExtra("block", block)
            intent.putExtra("districtName", districtName)
            intent.putExtra("village", village)
            startActivityForResult(intent, NationalLiveStockMissionIAList.FILTER_REQUEST_CODE)
        }

        fun add(view: View) {
            when (isFrom) {
                getString(R.string.state) -> {
                    startActivity(
                        Intent(
                            this@MobileVeterinaryListActivity,
                            AddNewMobileVeterinaryUnitState::class.java
                        ).putExtra("isFrom", isFrom)
                    )
                }

                getString(R.string.district) -> {
                    startActivity(
                        Intent(
                            this@MobileVeterinaryListActivity,
                            AddNewMobileVeterinaryUnitDistrict::class.java
                        ).putExtra("isFrom", isFrom)
                    )
                }

                getString(R.string.block_level) -> {
                    startActivity(
                        Intent(
                            this@MobileVeterinaryListActivity,
                            AddNewMobileVeterinaryUnitBlock::class.java
                        ).putExtra("isFrom", isFrom)
                    )

                }

                getString(R.string.farmer_level) -> {
                    startActivity(
                        Intent(
                            this@MobileVeterinaryListActivity,
                            AddNewMobileVeterinaryUnitVillage::class.java
                        ).putExtra("isFrom", isFrom)
                    )

                }
            }
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the data passed from FilterStateActivity
            districtId = data?.getIntExtra("districtId", 0)!!
            stateId = data.getIntExtra("stateId", 0)
            block = data.getStringExtra("block").toString()
            village = data.getStringExtra("village").toString()
            districtName = data.getStringExtra("districtName").toString()
            //Need to add year also
            // Log the data
            when (isFrom) {
                getString(R.string.state) -> {
                    stateMobileVeterinaryAPICall(
                        paginate = false,
                        loader = true,
                        districtId,
                        block,
                        village
                    )
                }

                getString(R.string.district) -> {
                    districtMobileVeterinaryAPICall(
                        paginate = false,
                        loader = true, districtId, block, village
                    )
                }

                getString(R.string.block_level) -> {
                    blockMobileVeterinaryAPICall(
                        paginate = false,
                        loader = true,
                        districtId,
                        block,
                        village
                    )
                }

                getString(R.string.farmer_level) -> {
                    farmerMobileVeterinaryAPICall(
                        paginate = false,
                        loader = true, districtId, block, village
                    )
                }
            }
            Log.d("FilterResult", "Received data from FilterStateActivity: $districtId")
            Log.d("FilterResult", "Received data from FilterStateActivity: $block")
        }
    }

    private fun mobileVeterinaryAdapter(list: ArrayList<MobileVeterinaryUnitsListData>) {
        mobileVeterinaryAdapter = MobileVeterinaryAdapter(this, list, isFrom, this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding?.rvMobileVeterinary?.layoutManager = layoutManager
        mBinding?.rvMobileVeterinary?.adapter = mobileVeterinaryAdapter
        mBinding?.rvMobileVeterinary?.addOnScrollListener(recyclerScrollListener)
    }

    private fun stateMobileVeterinaryAPICall(
        paginate: Boolean,
        loader: Boolean,
        district: Int,
        block: String,
        village: String
    ) {
        if (paginate) {
            currentPage++
        }
        viewModel.getStateMobileVeterinaryUnitsList(
            this, loader, MobileVeterinaryUnitsListRequest(
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

    private fun districtMobileVeterinaryAPICall(
        paginate: Boolean,
        loader: Boolean,
        district: Int,
        block: String,
        village: String
    ) {
        if (paginate) {
            currentPage++
        }
        viewModel.getDistrictMobileVeterinaryUnitsList(
            this, loader, MobileVeterinaryUnitsListRequest(
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
                district_code = district
            )
        )
    }

    private fun blockMobileVeterinaryAPICall(
        paginate: Boolean,
        loader: Boolean,
        district: Int,
        block: String,
        village: String
    ) {
        if (paginate) {
            currentPage++
        }
        viewModel.getBlockMobileVeterinaryUnitsList(
            this, loader, MobileVeterinaryUnitsListRequest(
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
                district_code = district,
                block_name = block

            )
        )
    }

    private fun farmerMobileVeterinaryAPICall(
        paginate: Boolean,
        loader: Boolean,
        district: Int,
        block: String,
        village: String
    ) {
        if (paginate) {
            currentPage++
        }
        viewModel.getFarmerMobileVeterinaryUnitsList(
            this, loader, MobileVeterinaryUnitsListRequest(
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
                district_code = district,
                block_name = block,
                village_name = village
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
                                        stateMobileVeterinaryAPICall(
                                            paginate = true,
                                            loader = true,
                                            districtId,
                                            block,
                                            village
                                        )
                                    }

                                    getString(R.string.district) -> {
                                        districtMobileVeterinaryAPICall(
                                            paginate = true,
                                            loader = true, districtId, block, village
                                        )
                                    }

                                    getString(R.string.block_level) -> {
                                        blockMobileVeterinaryAPICall(
                                            paginate = true,
                                            loader = true,
                                            districtId,
                                            block,
                                            village
                                        )
                                    }

                                    getString(R.string.farmer_level) -> {
                                        farmerMobileVeterinaryAPICall(
                                            paginate = true,
                                            loader = true, districtId, block, village
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

        viewModel.stateMobileVeterinaryUnitsAddResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {

                    showSnackbar(mBinding!!.clParent, userResponseModel.message)

                } else {

                    itemPosition?.let { it1 -> mobileVeterinaryAdapter?.onDeleteButtonClick(it1) }
//                    rSPLABListAdapter?.notifyDataSetChanged()
//                    implementingAgencyAPICall(paginate = true, loader = true)
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }
            }
        }

        viewModel.districtMobileVeterinaryUnitsAddResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {

                    showSnackbar(mBinding!!.clParent, userResponseModel.message)

                } else {

                    itemPosition?.let { it1 -> mobileVeterinaryAdapter?.onDeleteButtonClick(it1) }
//                    rSPLABListAdapter?.notifyDataSetChanged()
//                    implementingAgencyAPICall(paginate = true, loader = true)
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }
            }
        }

        viewModel.blockMobileVeterinaryUnitsAddResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {

                    showSnackbar(mBinding!!.clParent, userResponseModel.message)

                } else {

                    itemPosition?.let { it1 -> mobileVeterinaryAdapter?.onDeleteButtonClick(it1) }
//                    rSPLABListAdapter?.notifyDataSetChanged()
//                    implementingAgencyAPICall(paginate = true, loader = true)
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }
            }
        }

        viewModel.farmerMobileVeterinaryUnitsAddResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {

                    showSnackbar(mBinding!!.clParent, userResponseModel.message)

                } else {

                    itemPosition?.let { it1 -> mobileVeterinaryAdapter?.onDeleteButtonClick(it1) }
//                    rSPLABListAdapter?.notifyDataSetChanged()
//                    implementingAgencyAPICall(paginate = true, loader = true)
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }
            }
        }
    }


    override fun onClickItem(ID: Int?, position: Int, isFrom: String) {
        when (isFrom) {
            getString(R.string.state) -> {
                viewModel.getStateMobileVeterinaryUnitsAdd(
                    this@MobileVeterinaryListActivity, true,
                    StateMobileVeterinaryUnitAddRequest(
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
                viewModel.getDistrictMobileVeterinaryUnitsAdd(
                    this@MobileVeterinaryListActivity, true,
                    DistrictMobileVeterinaryUnitAddRequest(
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

            getString(R.string.block_level) -> {
                viewModel.getBlockMobileVeterinaryUnitsAdd(
                    this@MobileVeterinaryListActivity, true,
                    BlockMobileVeterinaryUnitAddRequest(
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
                viewModel.getFarmerMobileVeterinaryUnitsAdd(
                    this@MobileVeterinaryListActivity, true,
                    FarmerMobileVeterinaryUnitsAddRequest(
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
}