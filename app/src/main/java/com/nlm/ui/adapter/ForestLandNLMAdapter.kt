package com.nlm.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.callBack.CallBackDeleteAtId
import com.nlm.callBack.CallBackItemFormat6Edit
import com.nlm.callBack.CallBackItemFormat9Edit
import com.nlm.callBack.DialogCallback
import com.nlm.databinding.ItemForestlandNlmBinding
import com.nlm.databinding.ItemNlmedpIaBinding
import com.nlm.databinding.ItemNlmedpNlmBinding
import com.nlm.model.AssistanceForQfspFinancialProgres
import com.nlm.model.FpFromForestLandFilledByNlm
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import kotlin.coroutines.coroutineContext

class ForestLandNLMAdapter(
    private val programmeList: MutableList<FpFromForestLandFilledByNlm>,
    private val context: Context,
    private var viewEdit: String?,
    private val callBackEdit: CallBackItemFormat9Edit,
    private val callBackDeleteAtId: CallBackDeleteAtId,

    ) : RecyclerView.Adapter<ForestLandNLMAdapter.AvailabilityOfEquipmentViewHolder>() {
    private lateinit var stateAdapter: StateAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private val tvDistrictNlm = listOf(
        "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
        "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand",
        "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
        "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab",
        "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
        "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman and Nicobar Islands",
        "Chandigarh", "Dadra and Nagar Haveli and Daman and Diu", "Lakshadweep",
        "Delhi", "Puducherry", "Ladakh", "Lakshadweep", "Jammu and Kashmir"
    )
    private val tvAgencyInvolved = listOf(
        "Govt", "Outsourced Agency"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailabilityOfEquipmentViewHolder {

        val binding = ItemForestlandNlmBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return   AvailabilityOfEquipmentViewHolder(binding)

    }

    override fun onBindViewHolder(holder: AvailabilityOfEquipmentViewHolder, position: Int) {


        val items=programmeList[position]
        holder.binding.etVillageName.isEnabled=false
        holder.binding.etBlock.isEnabled=false
        holder.binding.tvAgencyInvolved.isEnabled=false
        holder.binding.tvDistrictNlm.isEnabled=false
        holder.binding.etFodderProduced.isEnabled=false
        holder.binding.etConsumerFodder.isEnabled=false
        holder.binding.etVariteryFodder.isEnabled=false
        if (viewEdit=="view")
        {
            holder.binding.btnDelete.hideView()

        }
        else if(viewEdit=="edit"){
            holder.binding.btnEdit.showView()
        }
        holder.binding.btnDelete.setOnClickListener {
            if (context != null) {
                Utility.showConfirmationAlertDialog(
                    context,
                    object :
                        DialogCallback {
                        override fun onYes() {
                            callBackDeleteAtId.onClickItem(items.id,position,2)
                        }
                    },
                    context.getString(R.string.are_you_sure_want_to_delete_your_post)
                )
            }
        }
        holder.binding.btnEdit.setOnClickListener{
            callBackEdit.onClickItem(
                FpFromForestLandFilledByNlm(
                    id = items.id,
                    agency_involved = items.agency_involved,
                    area_covered = items.area_covered,
                    block_name = items.block_name,
                    consumer_fodder = items.consumer_fodder,
                    estimated_quantity = items.estimated_quantity,
                    fp_from_forest_land_id=items.fp_from_forest_land_id,
                    village_name = items.area_covered,
                    district_code = null
                ),position,2)
        }



    }

    override fun getItemCount(): Int = programmeList.size

    private fun showBottomSheetDialog(type: String,textView: TextView) {
        // Initialize the BottomSheetDialog
        bottomSheetDialog = BottomSheetDialog(context)

        // Use LayoutInflater.from(context) to get the layout inflater
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_state, null)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Set up RecyclerView and Close button in the bottom sheet
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
            "projectFinancing" -> {
                selectedList = tvDistrictNlm
                selectedTextView = textView
            }
            "Animals" -> {
                selectedList = tvAgencyInvolved
                selectedTextView = textView
            }
            else -> return
        }

        // Set up the adapter for the bottom sheet
        stateAdapter = StateAdapter(selectedList, context) { selectedItem ->
            // Handle state item click
            selectedTextView.text = selectedItem
            selectedTextView.setTextColor(ContextCompat.getColor(context, R.color.black))
            bottomSheetDialog.dismiss()
        }

        rvBottomSheet.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvBottomSheet.adapter = stateAdapter

        bottomSheetDialog.setContentView(view)

        // Rotate drawable when the bottom sheet is shown
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_down)
        var rotatedDrawable = rotateDrawable(drawable, 180f)
        selectedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, rotatedDrawable, null)

        // Reset drawable when the bottom sheet is dismissed
        bottomSheetDialog.setOnDismissListener {
            rotatedDrawable = rotateDrawable(drawable, 0f)
            selectedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, rotatedDrawable, null)
        }

        // Show the bottom sheet
        bottomSheetDialog.show()
    }
    private fun rotateDrawable(drawable: Drawable?, angle: Float): Drawable? {
        drawable?.mutate() // Mutate the drawable to avoid affecting other instances

        val rotateDrawable = RotateDrawable()
        rotateDrawable.drawable = drawable
        rotateDrawable.fromDegrees = 0f
        rotateDrawable.toDegrees = angle
        rotateDrawable.level = 10000 // Needed to apply the rotation

        return rotateDrawable
    }
    inner class AvailabilityOfEquipmentViewHolder(val binding: ItemForestlandNlmBinding) :
        RecyclerView.ViewHolder(binding.root)
    fun onDeleteButtonClick(position: Int) {
        if (position >= 0 && position < programmeList.size) {
            programmeList.removeAt(position)
            notifyItemRemoved(position)

            // Notify about range changes to avoid index mismatches
            notifyItemRangeChanged(position, programmeList.size)
        } else {
            Log.e("Error", "Invalid index: $position for programmeList of size ${programmeList.size}")
        }
    }
}

