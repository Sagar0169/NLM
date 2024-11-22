package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.databinding.ActivityNlmAnimalHidfBinding
import com.nlm.model.NLMAhidfRequest
import com.nlm.model.NlmAhidfData
import com.nlm.model.Result
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.NlmAhidfAdapter
import com.nlm.ui.adapter.NlmEdpAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel

class NlmAnimalHidfActivity : BaseActivity<ActivityNlmAnimalHidfBinding>(), CallBackDeleteAtId {
    private var mBinding: ActivityNlmAnimalHidfBinding? = null
    private var nlmAhidfAdapter: NlmAhidfAdapter? = null
    private var ahidfEdpList = ArrayList<NlmAhidfData>()
    private var layoutManager: LinearLayoutManager? = null
    private val viewModel = ViewModel()
    private var currentPage = 1
    private var totalPage = 1
    private var loading = true

    override val layoutId: Int
        get() = R.layout.activity_nlm_animal_hidf

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        nlmAhidfAdapter()
        swipeForRefreshNlmAhidf()
    }

    override fun onResume() {
        super.onResume()
        nlmAhidfAPICall(paginate = false, loader = true)
    }

    private fun nlmAhidfAPICall(paginate: Boolean, loader: Boolean) {
        if (paginate) {
            currentPage++
        }
        viewModel.getNlmAhidf(
            this, loader, NLMAhidfRequest(
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
                                nlmAhidfAPICall(paginate = true, loader = true)
                            }
                        }
                    }
                }
            }
        }

    private fun swipeForRefreshNlmAhidf() {
        mBinding?.srlNlmAhidf?.setOnRefreshListener {
            nlmAhidfAPICall(paginate = false, loader = true)
            mBinding?.srlNlmAhidf?.isRefreshing = false
        }
    }

    private fun nlmAhidfAdapter() {
        nlmAhidfAdapter = NlmAhidfAdapter(this, ahidfEdpList, this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding?.rvNlmAhidf?.layoutManager = layoutManager
        mBinding?.rvNlmAhidf?.adapter = nlmAhidfAdapter
        mBinding?.rvNlmAhidf?.addOnScrollListener(recyclerScrollListener)
    }


    override fun setVariables() {
    }

    override fun setObservers() {

        viewModel.nlmAhidfResult.observe(this) {
            val userResponseModel = it
            if (userResponseModel.statuscode == 401) {
                Utility.logout(this)
            } else {
                if (userResponseModel?._result?.data != null && userResponseModel._result.data.isNotEmpty()) {
                    if (currentPage == 1) {
                        ahidfEdpList.clear()

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
                    ahidfEdpList.addAll(userResponseModel._result.data)
                    nlmAhidfAdapter?.notifyDataSetChanged()
                    mBinding?.tvNoDataFound?.hideView()
                    mBinding?.rvNlmAhidf?.showView()
                } else {
                    if (userResponseModel._result.is_add) {
                        mBinding?.fabAddAgency?.showView()
                    } else {
                        mBinding?.fabAddAgency?.hideView()
                    }
                    mBinding?.tvNoDataFound?.showView()
                    mBinding?.rvNlmAhidf?.hideView()
                }
            }
        }
    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun filter(view: View) {
            val intent = Intent(
                this@NlmAnimalHidfActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 17)
            startActivity(intent)
        }

        fun add(view: View) {
            startActivity(
                Intent(
                    this@NlmAnimalHidfActivity,
                    AddNlmFpForestLandActivity::class.java
                )
            )
        }
    }

    override fun onClickItem(ID: Int?, position: Int,isFrom:Int) {
    }
}