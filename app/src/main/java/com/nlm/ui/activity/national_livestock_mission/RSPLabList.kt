package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityRsplabBinding
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.ImplementingAgencyRequest
import com.nlm.model.RSPAddRequest
import com.nlm.model.RSPLabListData
import com.nlm.model.Result
import com.nlm.model.RspLabListRequest
import com.nlm.model.StateSemenBankNLMRequest
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockMissionIAList.Companion
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockMissionIAList.Companion.FILTER_REQUEST_CODE
import com.nlm.ui.adapter.RSPLABListAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
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
    var stateId: Int = 0
    var districtId: Int = 0
    var districtName: String = ""
    var phoneNo: String = ""
    var year: String = ""

    override fun initView() {
        mBinding = viewDataBinding
        viewModel.init()
        mBinding?.clickAction = ClickActions()
        rSPLABListAdapter()
        swipeForRefreshSrlRSPLab()
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
            currentPage = 1
            implementingAgencyAPICall(paginate = false, loader = true,districtId,phoneNo,year)
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
                                implementingAgencyAPICall(paginate = true, loader = true,districtId,phoneNo,year)
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
                if (userResponseModel?._result?.data!= null && userResponseModel._result.data.isNotEmpty()) {
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
                    if (userResponseModel._result.is_add) {
                        mBinding?.fabAddAgency?.showView()
                    } else {
                        mBinding?.fabAddAgency?.hideView()
                    }
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvRspLabView?.hideView()
                }
            }
        }
        viewModel.rspLabAddResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel!=null)
            {
                if(userResponseModel._resultflag==0){

                    showSnackbar(mBinding!!.clParent, userResponseModel.message)

                }
                else{

                    itemPosition?.let { it1 -> rSPLABListAdapter?.onDeleteButtonClick(it1) }
//                    rSPLABListAdapter?.notifyDataSetChanged()
//                    implementingAgencyAPICall(paginate = true, loader = true)
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
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
            val intent =
                Intent(this@RSPLabList, FilterStateActivity::class.java)
            intent.putExtra("isFrom", 40)
            intent.putExtra("selectedStateId", stateId) // previously selected state ID
            intent.putExtra("districtId", districtId) // previously selected state ID
            intent.putExtra("phoneNo", phoneNo)
            intent.putExtra("districtName", districtName)
            intent.putExtra("year", year)
            startActivityForResult(intent, NationalLiveStockMissionIAList.FILTER_REQUEST_CODE)
        }
    }
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the data passed from FilterStateActivity
            districtId = data?.getIntExtra("districtId", 0)!!
            stateId = data.getIntExtra("stateId", 0)
            phoneNo = data.getStringExtra("etPhoneno").toString()
            year = data.getStringExtra("year").toString()
            districtName = data.getStringExtra("districtName").toString()
            //Need to add year also
            // Log the data
            implementingAgencyAPICall(paginate = false, loader = true,districtId,phoneNo,year)
            Log.d("FilterResult", "Received data from FilterStateActivity: $districtId")
            Log.d("FilterResult", "Received data from FilterStateActivity: $year")
        }
    }

    private fun implementingAgencyAPICall(paginate: Boolean, loader: Boolean,district:Int,phone:String,year:String) {
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
                phone,
                year,
                district,
                10,
                currentPage
            )
        )
    }

    override fun onClickItem(ID: Int?, position: Int,isFrom:Int) {
        viewModel.getRspLabAddApi(
            this, true,
            RSPAddRequest(
                id= ID,
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
                )?.user_id.toString(),
                is_deleted = 1
            )
        )
        itemPosition = position
        rSPLABListAdapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        currentPage = 1
        implementingAgencyAPICall(paginate = false, loader = true,districtId,phoneNo,year)
    }
}