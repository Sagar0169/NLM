package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityNlmAssistanceForQfspactivityBinding
import com.nlm.model.ArtificialInseminationAddRequest
import com.nlm.model.Format6AssistanceForQspAddEdit
import com.nlm.model.ImportExocticGoatRequest
import com.nlm.model.NlmAssistanceForQFSPData
import com.nlm.model.NlmAssistanceForQFSPListRequest
import com.nlm.model.NlmFpForest
import com.nlm.model.Result
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.NlmAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class NlmAssistanceForQFSPActivity : BaseActivity<ActivityNlmAssistanceForQfspactivityBinding>(),
    CallBackDeleteAtId {
    private var mBinding: ActivityNlmAssistanceForQfspactivityBinding? = null
    private var nlmAssistanceForQFSPAdapter: NlmAdapter?= null
    private var nlmAssistanceForQFSPList = ArrayList<NlmAssistanceForQFSPData>()
    private var layoutManager: LinearLayoutManager? = null
    private val viewModel= ViewModel()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    private var itemPosition : Int ?= null
    override val layoutId: Int
        get() = R.layout.activity_nlm_assistance_for_qfspactivity

    override fun initView() {
        mBinding = viewDataBinding
        viewModel.init()
        mBinding?.clickAction = ClickActions()
        nlmAssistanceForQFSPAdapter()
        swipeForRefreshNlmAssistanceForQFSP()
    }

    override fun onResume() {
        super.onResume()
        nlmAssistanceForQFSPAPICall(paginate = false, loader = true)
    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
        fun filter(view: View) {
            val intent = Intent(
                this@NlmAssistanceForQFSPActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 13)
            startActivity(intent)
        }
        fun add(view: View) {
            startActivity(Intent(this@NlmAssistanceForQFSPActivity, AddNlmAssistanceForQFSPActivity::class.java))
        }
    }

    private fun nlmAssistanceForQFSPAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getAssistanceForQfspList(
            this, loader, NlmAssistanceForQFSPListRequest(
                role_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id,
                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                state_code = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
                page = currentPage,
                limit = 10
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
                                nlmAssistanceForQFSPAPICall(paginate = true, loader = true)
                            }
                        }
                    }
                }
            }
        }

    private fun swipeForRefreshNlmAssistanceForQFSP() {
        mBinding?.srlNlmAssistanceForQFSP?.setOnRefreshListener {
            currentPage = 1
            nlmAssistanceForQFSPAPICall(paginate = false, loader = true)
            mBinding?.srlNlmAssistanceForQFSP?.isRefreshing = false
        }
    }

    private fun nlmAssistanceForQFSPAdapter() {
        nlmAssistanceForQFSPAdapter = NlmAdapter(this,nlmAssistanceForQFSPList,this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding?.rvNlmAssistanceForQFSP?.layoutManager = layoutManager
        mBinding?.rvNlmAssistanceForQFSP?.adapter = nlmAssistanceForQFSPAdapter
        mBinding?.rvNlmAssistanceForQFSP?.addOnScrollListener(recyclerScrollListener)
    }

    override fun setVariables() {
    }

    override fun setObservers() {
        viewModel.nlmAssistanceForQFSPResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result?.data != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        nlmAssistanceForQFSPList.clear()

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
                    nlmAssistanceForQFSPList.addAll(userResponseModel._result.data)
                    nlmAssistanceForQFSPAdapter?.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvNlmAssistanceForQFSP?.showView()
                } else {
                    if (userResponseModel._result.is_add) {
                        mBinding?.fabAddAgency?.showView()
                    } else {
                        mBinding?.fabAddAgency?.hideView()
                    }
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvNlmAssistanceForQFSP?.hideView()
                }
            }
        }
        viewModel.foramt6AssistanceForQspAddEditResult.observe(this){
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
                    itemPosition?.let { it1 -> nlmAssistanceForQFSPAdapter?.onDeleteButtonClick(it1) }
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }
            }
        }
    }

    override fun onClickItem(ID: Int?, position: Int,isFrom:Int) {
        viewModel.getAssistanceForQfspAddEdit(this,true,
            Format6AssistanceForQspAddEdit(
                state_code = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.state_code,
                user_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.user_id,
                role_id = getPreferenceOfScheme(this, AppConstants.SCHEME, Result::class.java)?.role_id,
                id = ID,
                is_deleted = 1,
            )
        )
        itemPosition = position
    }
}