package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ActivityImportOfExoticGoatListBinding
import com.nlm.model.ArtificialInseminationRequest
import com.nlm.model.DataIE
import com.nlm.model.ImportExocticGoatRequest
import com.nlm.model.ImportOfGoat
import com.nlm.model.Result
import com.nlm.ui.adapter.Import_Of_Goat_Adapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.PrefEntities
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class ImportOfExoticGoatList : BaseActivity<ActivityImportOfExoticGoatListBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_import_of_exotic_goat_list
    private var mBinding: ActivityImportOfExoticGoatListBinding? = null
    private lateinit var implementingAdapter: Import_Of_Goat_Adapter
    private var layoutManager: LinearLayoutManager? = null
    private lateinit var ImportExocticGoatList: ArrayList<DataIE>
    private val viewModel= ViewModel()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction=ClickActions()
        viewModel.init()
        if(Utility.getPreferenceString(this, AppConstants.ROLE_NAME)=="Super Admin")
        {
            mBinding!!.fabAddAgency.hideView()
        }
        ImportExocticGoatList = arrayListOf(
            DataIE(
                "NA",
                null,
                null,
                "NA",
                "NA",
                null,
                null,
                null,
                null,
                "NA",
                "NA",

        ))
        exocticGoatAPICall(paginate = false, loader = true)
        implementingAgency()
        swipeForRefreshImplementingAgency()

        mBinding!!.fabAddAgency.setOnClickListener{
            val intent = Intent(this@ImportOfExoticGoatList, ImportOfExoticGoatForms::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }

    override fun setVariables() {

    }
    private fun swipeForRefreshImplementingAgency() {
        mBinding?.srlImplementingAgency?.setOnRefreshListener {
            exocticGoatAPICall(paginate = false, loader = true)
            mBinding?.srlImplementingAgency?.isRefreshing = false
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
    }
    inner class ClickActions {
        fun backPress(view: View){
            onBackPressed()
        }
    }
    private fun implementingAgency() {
        implementingAdapter = Import_Of_Goat_Adapter(ImportExocticGoatList,
            Utility.getPreferenceString(this, AppConstants.ROLE_NAME))
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvArtificialInsemination.layoutManager = layoutManager
        mBinding!!.rvArtificialInsemination.adapter = implementingAdapter
        mBinding?.rvArtificialInsemination?.addOnScrollListener(recyclerScrollListener)
    }
    private fun exocticGoatAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getImportExocticGoatList(
            this, loader, ImportExocticGoatRequest(
                role_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id.toString(),
                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id.toString(),
                state_code = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code.toString(),
                page = currentPage.toString(),
                limit = 10.toString(),
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
                                exocticGoatAPICall(paginate = true, loader = true)
                            }
                        }
                    }
                }
            }
        }
}