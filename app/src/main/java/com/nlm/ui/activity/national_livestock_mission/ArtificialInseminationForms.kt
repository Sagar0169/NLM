package com.nlm.ui.activity.national_livestock_mission

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.ActivityArtificialInseminationBinding
import com.nlm.databinding.ItemAddDocumentDialogBinding
import com.nlm.model.DocumentData
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.ObservationAIAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Utility.showSnackbar

class ArtificialInseminationForms : BaseActivity<ActivityArtificialInseminationBinding>(){
    override val layoutId: Int
        get() = R.layout.activity_artificial_insemination
    private var mBinding: ActivityArtificialInseminationBinding? = null
    private lateinit var bottomSheetAdapter: StateAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ObservationAIAdapter
    private lateinit var programmeList: MutableList<Array<String>>
    private var DialogDocName:TextView?=null
    private val state = listOf(
        "Left Artificial", "Right Artificial", "Left Squint", "Right Squint", "Others"
    )

    private val district = listOf(
        "Black", "Brown", "Blue", "Reddish", "Green", "Other"
    )
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        recyclerView = mBinding?.rvObservationByNlm!!
        recyclerView.layoutManager = LinearLayoutManager(this)

        programmeList = mutableListOf()
        programmeList.add(arrayOf("", "", "",""))

        adapter = ObservationAIAdapter(programmeList)
        recyclerView.adapter = adapter
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }

    inner class ClickActions {
        fun state(view: View){showBottomSheetDialog("state")}
        fun district(view: View){showBottomSheetDialog("district")}
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
        fun addDocDialog(view: View){
//            AddDocumentDialog(this@ArtificialInseminationForms)
        }
    }
//    private fun AddDocumentDialog(context: Context) {
//        val bindingDialog: ItemAddDocumentDialogBinding = DataBindingUtil.inflate(
//            layoutInflater,
//            R.layout.item_add_document_dialog,
//            null,
//            false
//        )
//        val dialog = Dialog(context, android.R.style.Theme_Translucent_NoTitleBar)
//        dialog.setCancelable(true)
//        dialog.setCanceledOnTouchOutside(true)
//        dialog.setContentView(bindingDialog.root)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.window!!.setLayout(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//        dialog.window!!.setGravity(Gravity.CENTER)
//        DialogDocName=bindingDialog.etDoc
//        bindingDialog.tvChooseFile.setOnClickListener {
//            openOnlyPdfAccordingToPosition()
//        }
//
//        bindingDialog.tvSubmit.setOnClickListener {
//            if (bindingDialog.etDescription.text.toString().isNotEmpty())
//            {
//
//                DocumentList.add(DocumentData(bindingDialog.etDescription.text.toString(),DocumentName))
//
//                DocumentList.size.minus(1).let {
//                    AddDocumentAdapter?.notifyItemInserted(it)
//                    dialog.dismiss()
////
//                }
//            }
//
//
//            else {
//                showSnackbar(mBinding!!.clParent, getString(R.string.please_enter_atleast_one_field))
//            }
//        }
//        dialog.show()
//    }

    private fun showBottomSheetDialog(type: String) {
        bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_state, null)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val rvBottomSheet = view.findViewById<RecyclerView>(R.id.rvBottomSheet)
        val close = view.findViewById<TextView>(R.id.tvClose)

        close.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        // Define a variable for the selected list and TextView
        val selectedList: List<String>
        val selectedTextView: TextView

        // Initialize based on type
        when (type) {


            "state" -> {
                selectedList = state
                selectedTextView = mBinding!!.etState
            }

            "district" -> {
                selectedList = district
                selectedTextView = mBinding!!.etDistrict
            }



            else -> return
        }
        bottomSheetAdapter = StateAdapter(selectedList,this) { selectedItem ->
            selectedTextView.text = selectedItem
            bottomSheetDialog.dismiss()
        }

        rvBottomSheet.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvBottomSheet.adapter = bottomSheetAdapter
        bottomSheetDialog.setContentView(view)

        bottomSheetDialog.show()
    }

}