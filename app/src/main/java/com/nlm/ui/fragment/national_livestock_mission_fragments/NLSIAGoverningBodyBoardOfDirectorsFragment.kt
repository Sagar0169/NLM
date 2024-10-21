package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.AddItemCallBack
import com.nlm.callBack.AddItemCallBackProjectMonitoring
import com.nlm.databinding.FragmentNLSIAGoverningBodyBoardOfDirectorsBinding
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.ImplementingAgencyAdvisoryCommittee
import com.nlm.model.ImplementingAgencyProjectMonitoring
import com.nlm.model.Result
import com.nlm.ui.adapter.NlmIACompositionOFGoverningAdapter
import com.nlm.ui.adapter.NlmIAProjectMonitoringCommitteeAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.viewModel.ViewModel


class NLSIAGoverningBodyBoardOfDirectorsFragment : BaseFragment<FragmentNLSIAGoverningBodyBoardOfDirectorsBinding>(),AddItemCallBack,AddItemCallBackProjectMonitoring{
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__governing_body__board__of__directors
    val viewModel = ViewModel()
    private var mBinding: FragmentNLSIAGoverningBodyBoardOfDirectorsBinding?=null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NlmIACompositionOFGoverningAdapter
    private var AdvisoryCommitteeList: MutableList<ImplementingAgencyAdvisoryCommittee> = mutableListOf()
    private var ProjectMonitoringCommitteeList: MutableList<ImplementingAgencyProjectMonitoring> = mutableListOf()
    private lateinit var programmeList: MutableList<ImplementingAgencyAdvisoryCommittee>
    private lateinit var adapter2: NlmIAProjectMonitoringCommitteeAdapter
    private lateinit var programmeList2: MutableList<ImplementingAgencyProjectMonitoring>


    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        recyclerView = mBinding?.recyclerView2!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.init()
        programmeList = mutableListOf()
        programmeList.add(ImplementingAgencyAdvisoryCommittee(null,null,null,null,null))

        adapter = NlmIACompositionOFGoverningAdapter(programmeList,this)
        recyclerView.adapter = adapter

        mBinding?.recyclerView1?.layoutManager = LinearLayoutManager(requireContext())

        programmeList2 = mutableListOf()
        programmeList2.add(ImplementingAgencyProjectMonitoring(null,null,null,null))

        adapter2 = NlmIAProjectMonitoringCommitteeAdapter(programmeList2,this)
        mBinding?.recyclerView1?.adapter = adapter2

    }

    override fun setVariables() {

    }

    override fun setObservers() {
        viewModel.implementingAgencyAddResult.observe(viewLifecycleOwner){
            val userResponseModel = it
            if (userResponseModel!=null)
            {
                if(userResponseModel._resultflag==0){
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }
                else{
                    showSnackbar(mBinding!!.clParent, userResponseModel.message)
                }
            }
        }
    }
    inner class ClickActions {
   fun save2(view: View){
       viewModel.getImplementingAgencyAddApi(requireContext(),true,
           ImplementingAgencyAddRequest(
               getPreferenceOfScheme(requireContext(), AppConstants.SCHEME, Result::class.java)?.state_code,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               AdvisoryCommitteeList,
               ProjectMonitoringCommitteeList,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,

               )
       )
   }



    }

    override fun onClickItem(list: MutableList<ImplementingAgencyAdvisoryCommittee>) {
       AdvisoryCommitteeList.clear() // Clear previous data if needed
        AdvisoryCommitteeList.addAll(list) // Copy new items
        Log.d("ListData", "onClickItem: $list")
    }

    override fun onClickItem2(list: MutableList<ImplementingAgencyProjectMonitoring>) {
        ProjectMonitoringCommitteeList.clear() // Clear previous data if needed
        ProjectMonitoringCommitteeList.addAll(list) // Copy new items

    }
}