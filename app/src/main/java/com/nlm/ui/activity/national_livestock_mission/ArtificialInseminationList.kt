package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityArtificialInseminationListBinding
import com.nlm.model.ArtificialInsemenNationAddRequest
import com.nlm.model.ArtificialInsemenation
import com.nlm.model.ArtificialInseminationRequest
import com.nlm.model.DataArtificialInsemination
import com.nlm.model.DataImplementingAgency
import com.nlm.model.ImplementingAgencyRequest
import com.nlm.model.Result
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockMissionIAList.Companion.FILTER_REQUEST_CODE
import com.nlm.ui.adapter.rgm.Artificial_Insemination_adapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.PrefEntities
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class ArtificialInseminationList : BaseActivity<ActivityArtificialInseminationListBinding>(),
    CallBackDeleteAtId {
    private var mBinding: ActivityArtificialInseminationListBinding? = null
    private var viewModel = ViewModel()
    private lateinit var implementingAdapter: Artificial_Insemination_adapter
    private lateinit var nodalOfficerList: List<ArtificialInsemenation>
    private var artificialInseminationList = ArrayList<DataArtificialInsemination>()
    private var layoutManager: LinearLayoutManager? = null
    private var currentPage = 1
    private var totalPage = 1
    private var itemPosition : Int ?= null
    private var loading = true
    var stateId: Int = 0
    var nameOfLocation: String = ""
    var LiquidNitrogen :String ?=null
    var FrozenSemen: String ?=null
    var Cryocans: String ?=null
    var DistrictCode: Int ?= null
    var DistrictName: String ?= null

    override val layoutId: Int
        get() = R.layout.activity_artificial_insemination_list
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()

        if (Utility.getPreferenceString(this, AppConstants.ROLE_NAME) == "Super Admin") {
            mBinding!!.fabAddAgency.hideView()
        }
        nodalOfficerList = listOf(
            ArtificialInsemenation(
                "NA",
                "NA",
                "NA",
                "NA",
                "NA",
                "NA",
            )
        )

        implementingAgency()
        implementingAgencyAPICall(paginate = false, loader = true,LiquidNitrogen,FrozenSemen,Cryocans,DistrictCode)
        swipeForRefreshImplementingAgency()


    }

    override fun setVariables() {

    }

    override fun setObservers() {

        viewModel.artificialInseminationResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        artificialInseminationList.clear()

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
                    artificialInseminationList.addAll(userResponseModel._result.data)
                    implementingAdapter.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvArtificialInsemination?.showView()
                } else {
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvArtificialInsemination?.hideView()
                }

            }
        }
        viewModel.artificialInseminationAddResult.observe(this){
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel!=null)
            {
                if(userResponseModel._resultflag==0){

                    showSnackbar(mBinding!!.main, userResponseModel.message)

                }
                else{

                    itemPosition?.let { it1 -> implementingAdapter.onDeleteButtonClick(it1) }
                    showSnackbar(mBinding!!.main, userResponseModel.message)
                }
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the data passed from FilterStateActivity
            stateId = data?.getIntExtra("stateId", 0)!!
            LiquidNitrogen=data.getStringExtra("LiquidNitrogen")
            FrozenSemen=data.getStringExtra("FrozenSemen")
            Cryocans=data.getStringExtra("Cryocans")
            DistrictCode=data.getIntExtra("DistrictId",0)
            DistrictName=data.getStringExtra("districtName")
            implementingAgencyAPICall(paginate = false, loader = true,LiquidNitrogen,FrozenSemen,Cryocans,DistrictCode)


        }
    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressed()
        }
        fun filter(view: View) {
            val intent =
                Intent(this@ArtificialInseminationList, FilterStateActivity::class.java)
            intent.putExtra("isFrom", 36)
            intent.putExtra("FrozenSemen", LiquidNitrogen)
            intent.putExtra("LiquidNitrogen",FrozenSemen)
            intent.putExtra("Cryocans",Cryocans)
            intent.putExtra("DistrictId", DistrictCode)
            intent.putExtra("stateId", stateId) // Add selected data to intent
            intent.putExtra("districtName", DistrictName) // Add selected data to intent
            startActivityForResult(intent, FILTER_REQUEST_CODE)
        }
        fun add(view: View) {
            val intent = Intent(
                this@ArtificialInseminationList,
                ArtificialInseminationForms::class.java
            ).putExtra("isFrom", 1)
            startActivity(intent)
        }
    }

    private fun swipeForRefreshImplementingAgency() {
        mBinding?.srlImplementingAgency?.setOnRefreshListener {
            implementingAgencyAPICall(paginate = false, loader = true,LiquidNitrogen,FrozenSemen,Cryocans,DistrictCode)
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
                                implementingAgencyAPICall(paginate = true, loader = true,LiquidNitrogen,FrozenSemen,Cryocans,DistrictCode)
                            }
                        }
                    }
                }
            }
        }


    private fun implementingAgencyAPICall(paginate: Boolean, loader: Boolean,LiquidNitrogen:String?,FrozenSemen:String?,Cryocans:String?,DistrictCode:Int?) {
        if (paginate) {
            currentPage++
        }
        viewModel.getArtificialInseminationApi(
            this, loader, ArtificialInseminationRequest(
               role_id =  getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id,
               state_code =  getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
                 user_id =    getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                limit = 10,
               page =  currentPage,
                district_code =DistrictCode,
                frozen_semen_straws = FrozenSemen,
                liquid_nitrogen = LiquidNitrogen,
                cryocans=Cryocans

            )
        )
    }

    private fun implementingAgency() {
        implementingAdapter = Artificial_Insemination_adapter(this,
            artificialInseminationList,
            Utility.getPreferenceString(this, AppConstants.ROLE_NAME),this
        )
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvArtificialInsemination.layoutManager = layoutManager
        mBinding!!.rvArtificialInsemination.adapter = implementingAdapter
        mBinding?.rvArtificialInsemination?.addOnScrollListener(recyclerScrollListener)

    }

    override fun onClickItem(ID: Int?, position: Int) {
        viewModel.getArtificialInseminationAdd(this,true,
            ArtificialInsemenNationAddRequest(
                state_code = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
                district_code = null,
                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                artificial_insemination_observation_by_nlm = null,
                artificial_insemination_document = null,
                is_deleted = 1,
                is_draft_nlm = null,
                total_sheep_goat_labs =null,
                liquid_nitrogen = null,
                frozen_semen_straws = null,
                cryocans = null,
                exotic_sheep_goat = null,
                role_id =   getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id,
                is_type = null,
                id = ID,
            )
        )
        itemPosition = position
    }
}