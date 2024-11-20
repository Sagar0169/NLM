package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityStateSemenBankListBinding
import com.nlm.model.ArtificialInseminationRequest
import com.nlm.model.DataSemen
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.Result
import com.nlm.model.StateSemenBankNLMRequest
import com.nlm.model.StateSemenBankRequest
import com.nlm.model.State_Semen_Bank

import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockMissionIAList.Companion.FILTER_REQUEST_CODE
import com.nlm.ui.adapter.StateSemenAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class StateSemenBankList : BaseActivity<ActivityStateSemenBankListBinding>(), CallBackDeleteAtId {
    private var mBinding: ActivityStateSemenBankListBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_state_semen_bank_list
    private lateinit var implementingAdapter: StateSemenAdapter
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    private var itemPosition : Int ?= null
    private var viewModel = ViewModel()
    private lateinit var nodalOfficerList: ArrayList<DataSemen>
    private var layoutManager: LinearLayoutManager? = null
    var stateId: Int = 0
    var districtId: Int = 0
    var nameOfLocation: String = ""
    var districtName: String = ""
    var phoneNo: String = ""
    var year: String = ""
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
        stateSemenBankAPICall(paginate = false, loader = true,nameOfLocation,districtId,phoneNo,year)
        swipeForRefreshImplementingAgency()
        mBinding!!.fabAddAgency.setOnClickListener{
            val intent = Intent(this@StateSemenBankList, StateSemenBankForms::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }
//don
    override fun setVariables() {

    }

    override fun setObservers() {
        viewModel.statesemenBankResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result?.data != null && userResponseModel._result.data.isNotEmpty()) {
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
                    if (userResponseModel._result.is_add) {
                        mBinding?.fabAddAgency?.showView()
                    } else {
                        mBinding?.fabAddAgency?.hideView()
                    }
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvStateSemenLabView?.hideView()
                }
            }
        }

        viewModel.stateSemenBankAddResult.observe(this) {
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

                    itemPosition?.let { it1 -> implementingAdapter.onDeleteButtonClick(it1) }
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }
            }
        }
    }
    private fun swipeForRefreshImplementingAgency() {
        mBinding?.srlImplementingAgency?.setOnRefreshListener {
            stateSemenBankAPICall(paginate = false, loader = true,nameOfLocation,districtId,phoneNo,year)
            mBinding?.srlImplementingAgency?.isRefreshing = false
        }
    }
    inner class ClickActions {
        fun backPress(view: View){
            onBackPressed()
        }
        fun filter(view: View) {
            val intent =
                Intent(this@StateSemenBankList, FilterStateActivity::class.java)
            intent.putExtra("isFrom", 34)
            intent.putExtra("selectedStateId", stateId) // previously selected state ID
            intent.putExtra("districtId", districtId) // previously selected state ID
            intent.putExtra("selectedLocation", nameOfLocation)
            intent.putExtra("phoneNo", phoneNo)
            intent.putExtra("districtName", districtName)
            intent.putExtra("year", year)
            startActivityForResult(intent, FILTER_REQUEST_CODE)
        }

    }
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the data passed from FilterStateActivity
            districtId = data?.getIntExtra("districtId", 0)!!
            stateId = data.getIntExtra("stateId", 0)
            nameOfLocation = data.getStringExtra("nameLocation").toString()
            phoneNo = data.getStringExtra("etPhoneno").toString()
            year = data.getStringExtra("year").toString()
            districtName = data.getStringExtra("districtName").toString()
            //Need to add year also
            // Log the data
            stateSemenBankAPICall(paginate = false, loader = true, nameOfLocation,districtId,phoneNo,year)
            Log.d("FilterResult", "Received data from FilterStateActivity: $nameOfLocation")
            Log.d("FilterResult", "Received data from FilterStateActivity: $districtId")
            Log.d("FilterResult", "Received data from FilterStateActivity: $year")
        }
    }
    private fun stateSemenBankAPICall(paginate: Boolean, loader: Boolean,location:String,district:Int,phone:String,year:String) {
        if (paginate) {
            currentPage++
        }
        viewModel.getStateSemenBankApi(
            this, loader, StateSemenBankRequest(
                getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id,
                getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
                getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                location,
                phone,
                year,
                district,
                10,
                currentPage
            )
        )
    }
    private fun implementingAgency() {
        implementingAdapter = StateSemenAdapter(this,nodalOfficerList,2,Utility.getPreferenceString(this, AppConstants.ROLE_NAME),this)
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
                                stateSemenBankAPICall(paginate = true, loader = true,nameOfLocation,districtId,phoneNo,year)
                            }
                        }
                    }
                }
            }
        }

    override fun onClickItem(ID: Int?, position: Int) {
        viewModel.getStateSemenAddBankApi(
            this, true,
            StateSemenBankNLMRequest(
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
    }

}