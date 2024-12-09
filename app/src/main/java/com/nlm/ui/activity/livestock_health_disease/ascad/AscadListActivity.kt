package com.nlm.ui.activity.livestock_health_disease.ascad

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackDeleteAtIdString
import com.nlm.databinding.ActivityAscadListBinding
import com.nlm.model.AscadListData
import com.nlm.model.AscadListRequest
import com.nlm.model.BlockMobileVeterinaryUnitAddRequest
import com.nlm.model.DistrictAscadAddRequest
import com.nlm.model.DistrictMobileVeterinaryUnitAddRequest
import com.nlm.model.FarmerMobileVeterinaryUnitsAddRequest
import com.nlm.model.Result
import com.nlm.model.StateAscadAddRequest
import com.nlm.model.StateMobileVeterinaryUnitAddRequest
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.activity.livestock_health_disease.mobile_veterinary_units.AddNewMobileVeterinaryUnitState
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockMissionIAList
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockMissionIAList.Companion.FILTER_REQUEST_CODE
import com.nlm.ui.adapter.AscadAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class AscadListActivity : BaseActivity<ActivityAscadListBinding>(), CallBackDeleteAtIdString {
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
    private var isFromList: Int = 0
    var stateId: Int = 0
    var districtId: Int = 0
    var districtName: String = ""
    private var itemPosition: Int? = null

    override val layoutId: Int
        get() = R.layout.activity_ascad_list

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        isFrom = intent?.extras?.getString(AppConstants.IS_FROM)
        when (isFrom) {
            getString(R.string.state) -> {
                mBinding?.tvHeading?.text= "Ascad State"
                ascadAdapter(stateAscadList)
                isFromList = 41
            }

            getString(R.string.district) -> {
                mBinding?.tvHeading?.text= "Ascad District"
                ascadAdapter(districtAscadList)
                isFromList = 42
            }
        }
        swipeForRefreshAscad()
    }

    override fun onResume() {
        super.onResume()
        currentPage = 1
        when (isFrom) {
            getString(R.string.state) -> {
                stateAscadAPICall(paginate = false, loader = true, districtId)
            }

            getString(R.string.district) -> {
                districtAscadAPICall(paginate = false, loader = true, districtId)
            }
        }
    }

    private fun swipeForRefreshAscad() {
        mBinding?.srlAscad?.setOnRefreshListener {
            currentPage = 1
            when (isFrom) {
                getString(R.string.state) -> {
                    stateAscadAPICall(paginate = false, loader = true, districtId)
                }

                getString(R.string.district) -> {
                    districtAscadAPICall(paginate = false, loader = true, districtId)
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
            val intent =
                Intent(this@AscadListActivity, FilterStateActivity::class.java)
            intent.putExtra("isFrom", isFromList)
            intent.putExtra("selectedStateId", stateId) // previously selected state ID
            intent.putExtra("districtId", districtId) // previously selected state ID
            intent.putExtra("districtName", districtName)
            startActivityForResult(intent, NationalLiveStockMissionIAList.FILTER_REQUEST_CODE)
        }


        fun add(view: View) {
            if(isFrom == getString(R.string.state))
                startActivity(
                    Intent(
                        this@AscadListActivity,
                        AddAscadStateActivity::class.java
                    ).putExtra("isFrom", isFrom)
                )
            else if (isFrom == getString(R.string.district)){
                startActivity(
                    Intent(
                        this@AscadListActivity,
                        AddAscadDistrictActivity::class.java
                    ).putExtra("isFrom", isFrom)
                )
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the data passed from FilterStateActivity
            districtId = data?.getIntExtra("districtId", 0)!!
            stateId = data.getIntExtra("stateId", 0)
            districtName = data.getStringExtra("districtName").toString()
            //Need to add year also
            // Log the data
            when (isFrom) {
                getString(R.string.state) -> {
                    stateAscadAPICall(
                        paginate = false,
                        loader = true,
                       stateId

                    )
                }

                getString(R.string.district) -> {
                    districtAscadAPICall(
                        paginate = false,
                        loader = true, districtId
                    )
                }
            }
            Log.d("FilterResult", "Received data from FilterStateActivity: $districtId")
        }
    }

    private fun ascadAdapter(list: ArrayList<AscadListData>) {
        ascadAdapter = AscadAdapter(this, list, isFrom, this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding?.rvAscad?.layoutManager = layoutManager
        mBinding?.rvAscad?.adapter = ascadAdapter
        mBinding?.rvAscad?.addOnScrollListener(recyclerScrollListener)
    }

    private fun stateAscadAPICall(paginate: Boolean, loader: Boolean,district: Int,) {
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

    private fun districtAscadAPICall(paginate: Boolean, loader: Boolean,  district: Int,) {
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
                district_code=district
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
                                        stateAscadAPICall(paginate = true, loader = true, districtId)
                                    }

                                    getString(R.string.district) -> {
                                        districtAscadAPICall(paginate = true, loader = true, districtId)
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

        viewModel.stateAscadAddResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {

                    showSnackbar(mBinding!!.clParent, userResponseModel.message)

                } else {

                    itemPosition?.let { it1 -> ascadAdapter?.onDeleteButtonClick(it1) }
//                    rSPLABListAdapter?.notifyDataSetChanged()
//                    implementingAgencyAPICall(paginate = true, loader = true)
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }
            }
        }

        viewModel.districtAscadAddResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {

                    showSnackbar(mBinding!!.clParent, userResponseModel.message)

                } else {

                    itemPosition?.let { it1 -> ascadAdapter?.onDeleteButtonClick(it1) }
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
                viewModel.getStateAscadAdd(
                    this@AscadListActivity, true,
                    StateAscadAddRequest(
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
                viewModel.getDistrictAscadAdd(
                    this@AscadListActivity, true,
                    DistrictAscadAddRequest(
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