package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.AddItemCallBackFundsRecieved
import com.nlm.databinding.FragmentNLSIAFundsReceivedBinding
import com.nlm.model.ImplementingAgencyAddRequest
import com.nlm.model.ImplementingAgencyFundsReceived
import com.nlm.model.ImplementingAgencyProjectMonitoring
import com.nlm.model.Result
import com.nlm.ui.adapter.NlmIADistrictWiseNoAdapter
import com.nlm.ui.adapter.NlmIAFundsRecievedAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.viewModel.ViewModel


class NLSIAFundsReceivedFragment : BaseFragment<FragmentNLSIAFundsReceivedBinding>(),AddItemCallBackFundsRecieved {
    override val layoutId: Int
        get() = R.layout.fragment_n_l_s_i_a__funds__received
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NlmIAFundsRecievedAdapter
    private lateinit var programmeList: MutableList<ImplementingAgencyFundsReceived>
    private  var fundsList: MutableList<ImplementingAgencyFundsReceived> = mutableListOf()
    private var mBinding: FragmentNLSIAFundsReceivedBinding?=null
     val viewModel=ViewModel()

    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        viewModel.init()
        recyclerView = mBinding?.recyclerView1!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        programmeList = mutableListOf()
        programmeList.add(
            ImplementingAgencyFundsReceived(
               null, // Default or empty value
               null, // Default or empty value
               null, // Default or empty value
               null, // Default or empty value
               null, // Default or empty value
               null, // Default or empty value
            )
        )
        adapter = NlmIAFundsRecievedAdapter  (programmeList,this)
        recyclerView.adapter = adapter
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
   fun Save (view:View){
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
               fundsList,
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

    override fun onClickItem(list: MutableList<ImplementingAgencyFundsReceived>) {
        fundsList.clear()
        fundsList.addAll(list)
    }
}