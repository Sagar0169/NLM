package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityNationalLiveStockIaBinding
import com.nlm.model.DataImplementingAgency
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.ImplementingAgencyRequest
import com.nlm.model.Result
import com.nlm.ui.activity.FilterStateActivity

import com.nlm.ui.adapter.NationalLiveStockMissionIAAdapter
import com.nlm.ui.fragment.national_livestock_mission_fragments.NLSIAInfrastructureSheepGoat
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences
import com.nlm.utilities.Preferences.getPreferenceOfScheme

import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class NationalLiveStockMissionIAList : BaseActivity<ActivityNationalLiveStockIaBinding>(),CallBackDeleteAtId {

    private var viewModel = ViewModel()
    private lateinit var implementingAdapter: NationalLiveStockMissionIAAdapter
    private var mBinding: ActivityNationalLiveStockIaBinding? = null
    private var implementingAgencyList = ArrayList<DataImplementingAgency>()
    private var layoutManager: LinearLayoutManager? = null
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    private var itemPosition : Int ?= null
    var stateId: Int = 0
    var nameOfLocation: String = ""

    companion object {
        const val FILTER_REQUEST_CODE = 1001
    }

    override val layoutId: Int
        get() = R.layout.activity_national_live_stock_ia

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        implementingAgencyAdapter()

        swipeForRefreshImplementingAgency()
    }

    private fun implementingAgencyAdapter() {
        implementingAdapter = NationalLiveStockMissionIAAdapter(this,
            implementingAgencyList,
            1,
            Utility.getPreferenceString(this, AppConstants.ROLE_NAME),
            this
        )
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding?.rvImplementingAgency?.layoutManager = layoutManager
        mBinding?.rvImplementingAgency?.adapter = implementingAdapter
        mBinding?.rvImplementingAgency?.addOnScrollListener(recyclerScrollListener)
    }

    override fun setVariables() {

    }

    override fun setObservers() {

        viewModel.implementingAgencyResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result?.data != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        implementingAgencyList.clear()

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
                    implementingAgencyList.addAll(userResponseModel._result.data)
                    implementingAdapter.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvImplementingAgency?.showView()
                } else {
                    if (userResponseModel._result.is_add) {
                        mBinding?.fabAddAgency?.showView()
                    } else {
                        mBinding?.fabAddAgency?.hideView()
                    }
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvImplementingAgency?.hideView()
                }
            }
        }
        viewModel.implementingAgencyAddResult.observe(this){
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

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun addImplementingAgency(view: View) {
            startActivity(
                Intent(
                    this@NationalLiveStockMissionIAList,
                    NLMIAForm::class.java
                ).putExtra("isFrom", 1)
            )
        }

        fun filter(view: View) {
            val intent =
                Intent(this@NationalLiveStockMissionIAList, FilterStateActivity::class.java)
            intent.putExtra("isFrom", 0)
            intent.putExtra("selectedStateId", stateId) // previously selected state ID
            intent.putExtra("selectedLocation", nameOfLocation)
            startActivityForResult(intent, FILTER_REQUEST_CODE)
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the data passed from FilterStateActivity
             stateId = data?.getIntExtra("stateId", 0)!!
             nameOfLocation = data.getStringExtra("nameLocation").toString()
            // Log the data
            implementingAgencyAPICall(paginate = false, loader = true, nameOfLocation)

        }
    }


    private fun swipeForRefreshImplementingAgency() {
        mBinding?.srlImplementingAgency?.setOnRefreshListener {
            implementingAgencyAPICall(paginate = false, loader = true, nameOfLocation)
            mBinding?.srlImplementingAgency?.isRefreshing = false
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
                                implementingAgencyAPICall(paginate = true, loader = true, "")
                            }
                        }
                    }
                }
            }
        }

    private fun implementingAgencyAPICall(paginate: Boolean, loader: Boolean, location: String) {
        if (paginate) {
            currentPage++
        }
        viewModel.getImplementingAgencyApi(
            this, loader, ImplementingAgencyRequest(
                getPreferenceOfScheme(
                    this@NationalLiveStockMissionIAList,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.role_id,
                getPreferenceOfScheme(
                    this@NationalLiveStockMissionIAList,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.state_code,
                getPreferenceOfScheme(
                    this@NationalLiveStockMissionIAList,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id,
                location,
                10,
                currentPage
            )
        )
    }

    override fun onClickItem(ID: Int?, position: Int) {
        viewModel.getImplementingAgencyAddApi(this,true,
            ImplementingAgencyAddRequest(
                user_id = getPreferenceOfScheme(this,AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
                part = "part1",
              id= ID, is_deleted = 1
                )
        )
        itemPosition = position
    }
    override fun onResume() {
        super.onResume()
        implementingAgencyAPICall(paginate = false, loader = true, "")
    }
}