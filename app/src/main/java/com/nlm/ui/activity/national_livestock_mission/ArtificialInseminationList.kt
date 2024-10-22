package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.databinding.ActivityArtificialInseminationListBinding
import com.nlm.model.ArtificialInsemenation
import com.nlm.model.ArtificialInseminationRequest
import com.nlm.model.DataArtificialInsemination
import com.nlm.model.DataImplementingAgency
import com.nlm.model.ImplementingAgencyRequest
import com.nlm.model.Result
import com.nlm.ui.adapter.rgm.Artificial_Insemination_adapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.PrefEntities
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class ArtificialInseminationList : BaseActivity<ActivityArtificialInseminationListBinding>() {
    private var mBinding: ActivityArtificialInseminationListBinding? = null
    private var viewModel = ViewModel()
    private var artificialInseminationList = ArrayList<DataArtificialInsemination>()
    private var layoutManager: LinearLayoutManager? = null
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true

    override val layoutId: Int
        get() = R.layout.activity_artificial_insemination_list
    private lateinit var implementingAdapter: Artificial_Insemination_adapter

    private lateinit var nodalOfficerList: List<ArtificialInsemenation>
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
        implementingAgencyAPICall(paginate = false, loader = true)
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

    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressed()
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
            implementingAgencyAPICall(paginate = false, loader = true)
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
                                implementingAgencyAPICall(paginate = true, loader = true)
                            }
                        }
                    }
                }
            }
        }


    private fun implementingAgencyAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getArtificialInseminationApi(
            this, loader, ArtificialInseminationRequest(
                getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id,
                getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
                getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                10,
                currentPage
            )
        )
    }

    private fun implementingAgency() {
        implementingAdapter = Artificial_Insemination_adapter(
            artificialInseminationList,
            Utility.getPreferenceString(this, AppConstants.ROLE_NAME)
        )
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvArtificialInsemination.layoutManager = layoutManager
        mBinding!!.rvArtificialInsemination.adapter = implementingAdapter
        mBinding?.rvArtificialInsemination?.addOnScrollListener(recyclerScrollListener)

    }
}