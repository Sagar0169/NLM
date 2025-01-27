package com.nlm.ui.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemTypeRGMStateIAAgencyWiseAi
import com.nlm.callBack.CallBackItemTypeRGMStateIAAgencyWiseCalf
import com.nlm.callBack.OnBackSaveAsDraft
import com.nlm.callBack.OnNextButtonClickListener
import com.nlm.databinding.FragmentFundsReceivedRGMBinding
import com.nlm.databinding.ItemAgencyWiseBinding
import com.nlm.databinding.ItemCompositionOfGoverningBinding
import com.nlm.model.RgmImplementingAgencyAgencyWiseAiDone
import com.nlm.model.RgmImplementingAgencyAgencyWiseCalfBorn
import com.nlm.model.RgmImplementingAgencyCompositionOfGoverningBody
import com.nlm.model.RgmImplementingAgencyProjectMonitoring
import com.nlm.ui.adapter.rgm.AgencyWiseAIAdapterRGM
import com.nlm.ui.adapter.rgm.AgencyWiseAdapter
import com.nlm.ui.adapter.rgm.AgencyWiseCalfAdapterRGM
import com.nlm.ui.adapter.rgm.CompositionOFGoverningAdapter
import com.nlm.ui.adapter.rgm.ProjectMonitoringCommitteeAdapterRGM
import com.nlm.utilities.BaseFragment
import com.nlm.utilities.Utility.showSnackbar
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class FundsReceivedRGMFragment(private val viewEdit: String?,private val itemId:Int?) : BaseFragment<FragmentFundsReceivedRGMBinding>(),
    CallBackDeleteAtId,
    CallBackItemTypeRGMStateIAAgencyWiseAi {
    private var mBinding: FragmentFundsReceivedRGMBinding? = null
    override val layoutId: Int
        get() = R.layout.fragment_funds__received__r_g_m

    private lateinit var AgencyWiseAIAdapter: AgencyWiseAIAdapterRGM
    private lateinit var AgencyWiseCalfAdapter: AgencyWiseCalfAdapterRGM
    private lateinit var RGMAgencyWiseAiList: MutableList<RgmImplementingAgencyAgencyWiseAiDone>
    private lateinit var RGMAgencyWiseCalfList: MutableList<RgmImplementingAgencyAgencyWiseCalfBorn>
    private var listener: OnNextButtonClickListener? = null
    private var savedAsDraftClick: OnBackSaveAsDraft? = null
    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
        AgencyWiseAIAdapter()
        AgencyWiseCalfAdapter()


    }
    private fun AgencyWiseAIAdapter() {
        RGMAgencyWiseAiList = mutableListOf()
        AgencyWiseAIAdapter =
            AgencyWiseAIAdapterRGM(requireActivity(),RGMAgencyWiseAiList,viewEdit,this,this)
        mBinding?.rvRGMAgencyWiseAi?.adapter = AgencyWiseAIAdapter
        mBinding?.rvRGMAgencyWiseAi?.layoutManager =
            LinearLayoutManager(requireContext())
    }
    private fun AgencyWiseCalfAdapter() {
        RGMAgencyWiseCalfList = mutableListOf()
        AgencyWiseCalfAdapter =
            AgencyWiseCalfAdapterRGM(requireActivity(),RGMAgencyWiseCalfList,viewEdit,this,this)
        mBinding?.rvRGMAgencyWiseCalf?.adapter = AgencyWiseCalfAdapter
        mBinding?.rvRGMAgencyWiseCalf?.layoutManager =
            LinearLayoutManager(requireContext())
    }
    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {
        fun AgencyWiseRGMDialog(view: View) {

            AgencyWiseRGMDialog(requireContext(),1,null,null)
        }
        fun AgencyWiseRGMDialog2(view: View) {

            AgencyWiseRGMDialog(requireContext(),2,null,null)
        }
        fun SaveAndNext(view: View) {
            listener?.onNextButtonClick()

        }
        fun SaveAsDraft(view: View) {
            savedAsDraftClick?.onSaveAsDraft()
        }
    }

    fun AgencyWiseRGMDialog(context: Context, isFrom:Int, selectedItem: RgmImplementingAgencyAgencyWiseAiDone?, position: Int?) {
        val bindingDialog: ItemAgencyWiseBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_agency_wise,
            null,
            false
        )
        val dialog = Dialog(context, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.CENTER)
        val lp: WindowManager.LayoutParams = dialog.window!!.attributes
        lp.dimAmount = 0.5f
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        bindingDialog.btnDelete.hideView()
        bindingDialog.tvSubmit.showView()
        Log.d("ISFROM",isFrom.toString())
        if(selectedItem!=null
//            && isFrom == 1
            )
        {

            bindingDialog.etAgency.setText(selectedItem.agency_name)
            bindingDialog.et2022.setText(selectedItem.first_year)
            bindingDialog.et2023.setText(selectedItem.secound_year)
            bindingDialog.et2024.setText(selectedItem.third_year)
        }
//        if(selectedItem!=null && isFrom == 2)
//        { bindingDialog.etAgency.setText(selectedItem.agency_name)
//            bindingDialog.et2022.setText(selectedItem.first_year)
//            bindingDialog.et2023.setText(selectedItem.secound_year)
//            bindingDialog.et2024.setText(selectedItem.third_year)
//        }
        bindingDialog.tvSubmit.setOnClickListener {
            if (bindingDialog.etAgency.text.toString().isNotEmpty()||bindingDialog.et2023.text.toString().isNotEmpty()||bindingDialog.et2022.text.toString().isNotEmpty()|| bindingDialog.et2024.text.toString().isNotEmpty())
            {
                if(isFrom == 1) {
                    if(selectedItem!=null)
                    {
                        if (position != null) {
                            RGMAgencyWiseAiList[position] =
                                RgmImplementingAgencyAgencyWiseAiDone(
                                    agency_name = bindingDialog.etAgency.text.toString(),
                                    first_year =  bindingDialog.et2022.text.toString(),
                                    secound_year =  bindingDialog.et2023.text.toString(),
                                    third_year =  bindingDialog.et2024.text.toString(),
                                    rgm_implementing_agency_id =  selectedItem.rgm_implementing_agency_id,
                                    id= selectedItem.id
                                )
                            AgencyWiseAIAdapter.notifyItemChanged(position)
                        }

                    } else{
                        RGMAgencyWiseAiList.add(
                            RgmImplementingAgencyAgencyWiseAiDone(
                                agency_name = bindingDialog.etAgency.text.toString(),
                                first_year =  bindingDialog.et2022.text.toString(),
                                secound_year =  bindingDialog.et2023.text.toString(),
                                third_year =  bindingDialog.et2024.text.toString(),
                                id=null,
                               rgm_implementing_agency_id =  null,
                            )
                        )
                        RGMAgencyWiseAiList.size.minus(1).let {
                            AgencyWiseAIAdapter.notifyItemInserted(it)
                        }}
                    dialog.dismiss()
                }
                else if(isFrom == 2){
                    if(selectedItem!=null)
                    {
                        if (position != null) {
                            RGMAgencyWiseCalfList[position] =
                                RgmImplementingAgencyAgencyWiseCalfBorn(
                                    agency_name = bindingDialog.etAgency.text.toString(),
                                    first_year =  bindingDialog.et2022.text.toString(),
                                    secound_year =  bindingDialog.et2023.text.toString(),
                                    third_year =  bindingDialog.et2024.text.toString(),
                                    rgm_implementing_agency_id =  selectedItem.rgm_implementing_agency_id,
                                    id= selectedItem.id
                                )
                            AgencyWiseCalfAdapter.notifyItemChanged(position)
                        }

                    }
                    else{
                        RGMAgencyWiseCalfList.add(
                            RgmImplementingAgencyAgencyWiseCalfBorn(
                                agency_name = bindingDialog.etAgency.text.toString(),
                                first_year =  bindingDialog.et2022.text.toString(),
                                secound_year =  bindingDialog.et2023.text.toString(),
                                third_year =  bindingDialog.et2024.text.toString(),
                                rgm_implementing_agency_id=null,
                                id= null
                            )
                        )
                        RGMAgencyWiseCalfList.size.minus(1).let {
                            AgencyWiseCalfAdapter.notifyItemInserted(it)
                        }

                    }
                    dialog.dismiss()
                }
            }

            else {
                showSnackbar(mBinding!!.clParent, getString(R.string.please_enter_atleast_one_field))
            }
        }
        dialog.show()
    }


    override fun onClickItem(ID: Int?, position: Int, isFrom: Int) {
        if (isFrom==1){
            position.let { it1 -> AgencyWiseAIAdapter.onDeleteButtonClick(it1) }
        }
        else if(isFrom==2){
            position.let { it1 -> AgencyWiseCalfAdapter.onDeleteButtonClick(it1) }
        }
    }

    override fun onClickItem(
        selectedItem: RgmImplementingAgencyAgencyWiseAiDone,
        position: Int,
        isFrom: Int
    ) {
        AgencyWiseRGMDialog(requireContext(),isFrom,selectedItem,position)
    }//AgencyWiseAiEdit
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnNextButtonClickListener
        savedAsDraftClick = context as OnBackSaveAsDraft
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        savedAsDraftClick = null
    }

}