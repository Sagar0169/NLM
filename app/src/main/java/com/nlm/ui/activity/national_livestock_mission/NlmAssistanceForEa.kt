package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityNlmAssistanceforEaBinding
import com.nlm.model.AddAssistanceEARequest
import com.nlm.model.AddFspPlantStorageRequest
import com.nlm.model.AssistanceForEAData
import com.nlm.model.AssistanceForEARequest
import com.nlm.model.Result
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.AssistanceForEaAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class NlmAssistanceForEa : BaseActivity<ActivityNlmAssistanceforEaBinding>(), CallBackDeleteAtId {
    private var mBinding: ActivityNlmAssistanceforEaBinding? = null
    private var assistanceForEaAdapter: AssistanceForEaAdapter? = null
    private var assistanceForEaList = ArrayList<AssistanceForEAData>()
    private var layoutManager: LinearLayoutManager? = null
    private val viewModel = ViewModel()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true
    private var itemPosition: Int? = null

    override val layoutId: Int
        get() = R.layout.activity_nlm_assistancefor_ea


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        assistanceForEaAdapter()
        swipeForRefreshAssistanceForEa()
    }

    override fun onResume() {
        super.onResume()
        assistanceForEaAPICall(paginate = false, loader = true)
    }

    private fun assistanceForEaAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getAssistanceForEaList(
            this, loader, AssistanceForEARequest(
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
                                assistanceForEaAPICall(paginate = true, loader = true)
                            }
                        }
                    }
                }
            }
        }

    private fun swipeForRefreshAssistanceForEa() {
        mBinding?.srlAssistanceForEa?.setOnRefreshListener {
            assistanceForEaAPICall(paginate = false, loader = true)
            mBinding?.srlAssistanceForEa?.isRefreshing = false
        }
    }

    private fun assistanceForEaAdapter() {
        assistanceForEaAdapter = AssistanceForEaAdapter(this, assistanceForEaList, this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding?.rvAssistanceForEa?.layoutManager = layoutManager
        mBinding?.rvAssistanceForEa?.adapter = assistanceForEaAdapter
        mBinding?.rvAssistanceForEa?.addOnScrollListener(recyclerScrollListener)
    }


    override fun setVariables() {
    }

    override fun setObservers() {
        viewModel.assistanceForEaResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        assistanceForEaList.clear()

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
                    assistanceForEaList.addAll(userResponseModel._result.data)
                    assistanceForEaAdapter?.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvAssistanceForEa?.showView()
                } else {
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvAssistanceForEa?.hideView()
                }
            }
        }

        viewModel.assistanceForEaADDResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            }
            if (userResponseModel != null) {
                if (userResponseModel._resultflag == 0) {

                    showSnackbar(mBinding!!.clParent, userResponseModel.message)

                } else {

                    itemPosition?.let { it1 -> assistanceForEaAdapter?.onDeleteButtonClick(it1) }
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }
            }
        }
    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun filter(view: View) {
            startActivity(Intent(
                this@NlmAssistanceForEa,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 16))
        }

        fun add(view: View) {
            startActivity(
                Intent(
                    this@NlmAssistanceForEa,
                    AddNLMExtensionActivity::class.java
                )
            )
        }
    }

    override fun onClickItem(ID: Int?, position: Int,isFrom:Int) {
        viewModel.getAssistanceForEaADD(
            this, true,
            AddAssistanceEARequest(
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
                )?.user_id.toString(),
                is_deleted = 1
            )
        )
        itemPosition = position
    }
}