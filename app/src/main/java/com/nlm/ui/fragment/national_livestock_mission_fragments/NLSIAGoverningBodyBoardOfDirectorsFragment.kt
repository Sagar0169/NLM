package com.nlm.ui.fragment.national_livestock_mission_fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.AddItemCallBack
import com.nlm.callBack.AddItemCallBackProjectMonitoring
import com.nlm.databinding.FragmentNLSIAGoverningBodyBoardOfDirectorsBinding
import com.nlm.databinding.ItemCompositionOfGoverningNlmIaBinding
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
import com.nlm.utilities.hideView
import com.nlm.utilities.setSafeOnClickListener
import com.nlm.utilities.showView
import com.nlm.viewModel.ViewModel


class NLSIAGoverningBodyBoardOfDirectorsFragment : BaseFragment<FragmentNLSIAGoverningBodyBoardOfDirectorsBinding>(){
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
    private var dialog: Dialog? = null


    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        recyclerView = mBinding?.recyclerView2!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.init()
        mBinding?.tvAddMore1?.setOnClickListener {
      openCamera()
        }
        programmeList = mutableListOf()
        adapter = NlmIACompositionOFGoverningAdapter(programmeList)
        recyclerView.adapter = adapter

        mBinding?.recyclerView1?.layoutManager = LinearLayoutManager(requireContext())

        programmeList2 = mutableListOf()
        programmeList2.add(ImplementingAgencyProjectMonitoring(null,null,null,null))

        adapter2 = NlmIAProjectMonitoringCommitteeAdapter(programmeList2)
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

//    override fun onClickItem(list: MutableList<ImplementingAgencyAdvisoryCommittee>) {
//       AdvisoryCommitteeList.clear() // Clear previous data if needed
//        AdvisoryCommitteeList.addAll(list) // Copy new items
//        Log.d("ListData", "onClickItem: $list")
//    }


    private fun openCamera() {
        val binding: ItemCompositionOfGoverningNlmIaBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_composition_of_governing_nlm_ia,
            null,
            false
        )
        dialog = Dialog(requireContext())
        dialog!!.setCancelable(false)
        dialog!!.setContentView(binding.root)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog!!.window!!.setGravity(Gravity.CENTER)
       binding.btnDelete.hideView()
        binding.tvSubmit.setOnClickListener {
            if (binding.nameOfOfficial.toString().isNotEmpty()||binding.nameOfDesignation.toString().isNotEmpty()||binding.nameOfOrganization.toString().isNotEmpty())
            {
            programmeList.add(ImplementingAgencyAdvisoryCommittee(binding.nameOfOfficial.toString(),binding.nameOfDesignation.toString(),binding.nameOfOrganization.toString(),null,null))
            programmeList.size.minus(1).let {
                adapter.notifyItemInserted(it)}
            }
           dialog!!.dismiss()
        }
        dialog!!.show()
    }
}