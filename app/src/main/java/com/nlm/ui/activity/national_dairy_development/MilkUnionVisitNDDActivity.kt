package com.nlm.ui.activity.national_dairy_development

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityMilkUnionVisitNddBinding
import com.nlm.model.NDDMilkUnionListData
import com.nlm.model.NDDMilkUnionListRequest
import com.nlm.model.Result
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockMissionIAList
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockMissionIAList.Companion.FILTER_REQUEST_CODE
import com.nlm.ui.adapter.ndd.MilkUnionVisitAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class MilkUnionVisitNDDActivity : BaseActivity<ActivityMilkUnionVisitNddBinding>(),
    CallBackDeleteAtId {
    private var mBinding: ActivityMilkUnionVisitNddBinding? = null
    private lateinit var nlmComponentBAdapter: MilkUnionVisitAdapter
    private lateinit var nlmComponentBList: ArrayList<NDDMilkUnionListData>
    private var layoutManager: LinearLayoutManager? = null
    private var viewModel = ViewModel()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    private var itemPosition : Int ?= null
    var stateId: Int = 0
    var districtId: Int = 0
    var districtName: String = ""
    var phoneNo: String = ""
    var nameOfAgency: String = ""
    override val layoutId: Int
        get() = R.layout.activity_milk_union_visit_ndd


    inner class ClickActions {
        fun backPress(view: View){
            onBackPressed()
        }
        fun filter(view: View) {
            val intent =
                Intent(this@MilkUnionVisitNDDActivity, FilterStateActivity::class.java)
            intent.putExtra("isFrom", 25)
            intent.putExtra("selectedStateId", stateId) // previously selected state ID
            intent.putExtra("districtId", districtId) // previously selected state ID
            intent.putExtra("districtName", districtName)
            intent.putExtra("nameOfAgency", nameOfAgency)
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
            districtName = data.getStringExtra("districtName").toString()
            nameOfAgency = data.getStringExtra("nameOfAgency").toString()
            //Need to add nameOfAgency also
            // Log the data
            componentBListApiCall(paginate = false, loader = true,districtId,phoneNo,nameOfAgency)
            Log.d("FilterResult", "Received data from FilterStateActivity: $districtId")
            Log.d("FilterResult", "Received data from FilterStateActivity: $nameOfAgency")
        }
    }


    override fun initView() {
        mBinding = viewDataBinding
        viewModel.init()
        mBinding?.clickAction=ClickActions()
        nlmComponentBList= arrayListOf()
        implementingAgency()
        swipeForRefreshAscad()
        mBinding!!.fabAdd.setOnClickListener{
            val intent = Intent(this@MilkUnionVisitNDDActivity, AddMilkUnionVisit::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }
    private fun swipeForRefreshAscad() {
        mBinding?.srlAscad?.setOnRefreshListener {
            currentPage = 1
            componentBListApiCall(paginate = false, loader = true,districtId,phoneNo,nameOfAgency)
            mBinding?.srlAscad?.isRefreshing = false
        }
    }
    private fun implementingAgency() {
        nlmComponentBAdapter = MilkUnionVisitAdapter(this,nlmComponentBList,this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.recyclerView.layoutManager = layoutManager
        mBinding!!.recyclerView.adapter = nlmComponentBAdapter
        mBinding?.recyclerView?.addOnScrollListener(recyclerScrollListener)
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
                                componentBListApiCall(paginate = true, loader = true,districtId,phoneNo,nameOfAgency)
                            }
                        }
                    }
                }
            }
        }
    override fun setVariables() {
    }

    override fun setObservers() {
        viewModel.milkUnionListResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result?.data!= null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        nlmComponentBList.clear()

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
                        mBinding?.fabAdd?.showView()
                    } else {
                        mBinding?.fabAdd?.hideView()
                    }
                    nlmComponentBList.addAll(userResponseModel._result.data)
                    nlmComponentBAdapter?.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.recyclerView?.showView()
                } else {
                    if (userResponseModel._result.is_add) {
                        mBinding?.fabAdd?.showView()
                    } else {
                        mBinding?.fabAdd?.hideView()
                    }
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.recyclerView?.hideView()
                }
            }
        }

    }

    private fun componentBListApiCall(paginate: Boolean, loader: Boolean,district:Int,phone:String,nameOfAgency:String) {
        if (paginate) {
            currentPage++
        }
        viewModel.getMilkUnionList(
            this, loader, NDDMilkUnionListRequest(
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
                district,
                nameOfAgency,
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
    override fun onResume() {
        super.onResume()
        currentPage = 1
        componentBListApiCall(paginate = false, loader = true,districtId,phoneNo,nameOfAgency)
    }

    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
        TODO("Not yet implemented")
    }
}