package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityImportOfExoticGoatListBinding
import com.nlm.model.ArtificialInseminationRequest
import com.nlm.model.DataIE
import com.nlm.model.ImportExocticGoatRequest
import com.nlm.model.ImportExoticGoatAddEditRequest
import com.nlm.model.ImportOfGoat
import com.nlm.model.Result
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.activity.national_livestock_mission.NationalLiveStockMissionIAList.Companion.FILTER_REQUEST_CODE
import com.nlm.ui.adapter.Import_Of_Goat_Adapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.PrefEntities
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class ImportOfExoticGoatList : BaseActivity<ActivityImportOfExoticGoatListBinding>(),
    CallBackDeleteAtId {
    override val layoutId: Int
        get() = R.layout.activity_import_of_exotic_goat_list
    private var mBinding: ActivityImportOfExoticGoatListBinding? = null
    private lateinit var implementingAdapter: Import_Of_Goat_Adapter
    private var layoutManager: LinearLayoutManager? = null
    private  var ImportExocticGoatList= ArrayList<DataIE>()
    private val viewModel= ViewModel()
    private var currentPage = 1
    private var itemPosition : Int ?= null
    private var totalPage = 1
    var NoFarmers: String ?=null
    private var loading = true
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction=ClickActions()
        viewModel.init()

        implementingAgency()
        swipeForRefreshImplementingAgency()
    }

    override fun onResume() {
        super.onResume()
        exocticGoatAPICall(paginate = false, loader = true,NoFarmers)
    }
    override fun setVariables() {

    }
    private fun swipeForRefreshImplementingAgency() {
        mBinding?.srlImportOfExoticGoat?.setOnRefreshListener {
            exocticGoatAPICall(paginate = false, loader = true,NoFarmers)
            mBinding?.srlImportOfExoticGoat?.isRefreshing = false
        }
    }

    override fun setObservers() {
        viewModel.importExocticGoatResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        ImportExocticGoatList.clear()

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
                    ImportExocticGoatList.addAll(userResponseModel._result.data)
                    implementingAdapter.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvArtificialInsemination?.showView()
                } else {
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvArtificialInsemination?.hideView()
                }
            }
        }
        viewModel.importExoticGoatAddEditResult.observe(this){
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
    inner class ClickActions {
        fun backPress(view: View){
            onBackPressed()
        }
        fun add(view: View){
            val intent = Intent(this@ImportOfExoticGoatList, ImportOfExoticGoatForms::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
        fun filter(view: View){
            val intent =
                Intent(this@ImportOfExoticGoatList, FilterStateActivity::class.java)
            intent.putExtra("isFrom", 37)
            intent.putExtra("NumberOfFarmers", NoFarmers) // Add selected data to intent
            startActivityForResult(intent, FILTER_REQUEST_CODE)
        }
    }
    private fun implementingAgency() {
        implementingAdapter = Import_Of_Goat_Adapter(this,this

                ,ImportExocticGoatList,
            Utility.getPreferenceString(this, AppConstants.ROLE_NAME))
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding?.rvArtificialInsemination?.layoutManager = layoutManager
        mBinding?.rvArtificialInsemination?.adapter = implementingAdapter
        mBinding?.rvArtificialInsemination?.addOnScrollListener(recyclerScrollListener)
    }
    private fun exocticGoatAPICall(paginate: Boolean, loader: Boolean,NoFarmers:String?) {
        if (paginate) {
            currentPage++
        }
        viewModel.getImportExocticGoatList(
            this, loader, ImportExocticGoatRequest(
                role_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id.toString(),
                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
                state_code = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code.toString(),
                page = currentPage,
                limit = 10,
                number_of_farmers_benefited=NoFarmers


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
                                exocticGoatAPICall(paginate = true, loader = true,NoFarmers)
                            }
                        }
                    }
                }
            }
        }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the data passed from FilterStateActivity

            if (data != null) {
                NoFarmers=data.getStringExtra("NumberOfFarmers")
            }
            exocticGoatAPICall(paginate = false, loader = true,NoFarmers)


        }
    }

    override fun onClickItem(ID: Int?, position: Int) {
        viewModel.getImportExoticGoatAdd(this@ImportOfExoticGoatList,true,
            ImportExoticGoatAddEditRequest(
                comment_by_nlm_whether = null,
                import_of_exotic_goat_detail_import = null,
                import_of_exotic_goat_achievement = null,
                import_of_exotic_goat_verified_nlm = null,
                state_code = getPreferenceOfScheme(this@ImportOfExoticGoatList, AppConstants.SCHEME, Result::class.java)?.state_code,
                user_id = getPreferenceOfScheme(
                    this@ImportOfExoticGoatList,
                    AppConstants.SCHEME,
                    Result::class.java
                )?.user_id.toString(),
                role_id = getPreferenceOfScheme(this@ImportOfExoticGoatList, AppConstants.SCHEME, Result::class.java)?.role_id,
                is_type = null,
                id = ID,
                is_draft=null,
                import_of_exotic_goat_document = null,
                is_deleted = 1,
                number_of_farmers_benefited=null
            )
        )
        itemPosition = position
    }

}