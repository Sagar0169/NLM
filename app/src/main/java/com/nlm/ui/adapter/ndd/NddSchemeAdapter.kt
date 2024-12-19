package com.nlm.ui.adapter.ndd

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.callBack.CallBackAvilabilityEquipment
import com.nlm.callBack.CallBackDeleteFSPAtId
import com.nlm.callBack.CallBackNddScheme
import com.nlm.callBack.CallBackSemenDoseAvg
import com.nlm.callBack.DialogCallback

import com.nlm.databinding.ItemNddSchemeBinding
import com.nlm.model.DairyPlantVisitReportNpddScheme
import com.nlm.model.Result
import com.nlm.model.RspAddBucksList
import com.nlm.model.RspAddEquipment
import com.nlm.utilities.AppConstants
import com.nlm.utilities.Preferences.getPreferenceOfScheme
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class NddSchemeAdapter(
    private val context: Context,
    private val programmeList: MutableList<DairyPlantVisitReportNpddScheme>,
    private val viewEdit: String?,
    private val callBackEdit: CallBackNddScheme,
    private val callBackDeleteFSPAtId: CallBackDeleteFSPAtId,
) : RecyclerView.Adapter<NddSchemeAdapter.NddSchemeAdapterViewHolder>() {

    private var goOne: Int? = null
    private var goTwo: Int? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NddSchemeAdapterViewHolder {

        val binding = ItemNddSchemeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NddSchemeAdapterViewHolder(binding)

    }

    override fun onBindViewHolder(
        holder: NddSchemeAdapterViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val currentItem = programmeList[position]
        if (viewEdit == "view" ||
            getPreferenceOfScheme(
                context,
                AppConstants.SCHEME,
                Result::class.java
            )?.role_id == 24
        ) {
            holder.binding.etNddScheme.isEnabled = false
            holder.binding.etMajor.isEnabled = false
            holder.binding.etRemark.isEnabled = false
            holder.binding.rbAvailable.isEnabled = false
            holder.binding.rbNotAvailable.isEnabled = false
            holder.binding.rbPresentOperational.isEnabled = false
            holder.binding.rbPresentNonOperational.isEnabled = false
            holder.binding.btnDelete.hideView()
            holder.binding.btnEdit.hideView()

        } else if (viewEdit == "edit") {
            holder.binding.btnEdit.showView()
        }
        holder.binding.etNddScheme.isEnabled = false
        holder.binding.etMajor.isEnabled = false
        holder.binding.etRemark.isEnabled = false
        holder.binding.rbAvailable.isEnabled = false
        holder.binding.rbNotAvailable.isEnabled = false
        holder.binding.rbPresentOperational.isEnabled = false
        holder.binding.rbPresentNonOperational.isEnabled = false

        holder.binding.etNddScheme.setText(currentItem.npdd_scheme_proj_no.toString())
        holder.binding.etMajor.setText(currentItem.major_equipments_milk_products)
        holder.binding.etRemark.setText(currentItem.npdd_scheme_remarks)

        Log.d("Listing", currentItem.npdd_scheme_available.toString())
        if (currentItem.npdd_scheme_available.toString()=="null") {
            holder.binding.rbAvailable.isChecked = false
            holder.binding.rbNotAvailable.isChecked  = false
            Log.d("Listing","1")

        }else if (currentItem.npdd_scheme_available == 1) {
            holder.binding.rbAvailable.isChecked = true
            Log.d("Listing","2")
        } else {
            holder.binding.rbNotAvailable.isChecked = true
            Log.d("Listing","3")
        }

        if (currentItem.npdd_scheme_present_status.toString()=="null") {
            holder.binding.rbPresentOperational.isChecked = false
            holder.binding.rbPresentNonOperational.isChecked  = false

        }else if (currentItem.npdd_scheme_present_status == 1) {
            holder.binding.rbPresentOperational.isChecked = true
        } else {
            holder.binding.rbPresentNonOperational.isChecked = true
        }

        holder.binding.rgAvailable.setOnCheckedChangeListener { group, checkedId ->
            goOne = when (checkedId) {
                R.id.rbAvailable -> 1
                R.id.rbNotAvailable -> 0
                else -> null
            }
        }

        holder.binding.rgPresent.setOnCheckedChangeListener { group, checkedId ->
            goTwo = when (checkedId) {
                R.id.rbPresentOperational -> 1
                R.id.rbPresentNonOperational -> 0
                else -> null
            }
        }
        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            if (context != null) {
                Utility.showConfirmationAlertDialog(
                    context,
                    object :
                        DialogCallback {
                        override fun onYes() {
                            callBackDeleteFSPAtId.onClickItemDelete(currentItem.id, position)
                        }
                    },
                    context.getString(R.string.are_you_sure_want_to_delete_your_post)
                )
            }
        }

        holder.binding.btnEdit.setOnClickListener {
            callBackEdit.onClickItem(
                DairyPlantVisitReportNpddScheme(
                    npdd_scheme_proj_no=currentItem.npdd_scheme_proj_no,
                    major_equipments_milk_products=currentItem.major_equipments_milk_products,
                    npdd_scheme_remarks=currentItem.npdd_scheme_remarks,
                    id=currentItem.id,
                    dairy_plant_visit_report_id=currentItem.dairy_plant_visit_report_id,
                    npdd_scheme_available=currentItem.npdd_scheme_available,
                    npdd_scheme_present_status=currentItem.npdd_scheme_present_status
                ), position, 2
            )
//            (context as NLSIAGoverningBodyBoardOfDirectorsFragment).compositionOfGoverningNlmIaDialog(context,1,
        }

    }

    override fun getItemCount(): Int = programmeList.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun onDeleteButtonClick(position: Int) {
        if (position >= 0 && position < programmeList.size) {
            programmeList.removeAt(position)
            notifyItemRemoved(position)

            // Notify about range changes to avoid index mismatches
            notifyItemRangeChanged(position, programmeList.size)
        } else {
            Log.e(
                "Error",
                "Invalid index: $position for programmeList of size ${programmeList.size}"
            )
        }
    }

    inner class NddSchemeAdapterViewHolder(val binding: ItemNddSchemeBinding) :
        RecyclerView.ViewHolder(binding.root)
}
