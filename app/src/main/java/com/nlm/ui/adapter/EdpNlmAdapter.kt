package com.nlm.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
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
import com.nlm.databinding.ItemNlmedpIaBinding
import com.nlm.databinding.ItemNlmedpNlmBinding
import kotlin.coroutines.coroutineContext

class EdpNlmAdapter(
    private val programmeList: MutableList<Array<String>>,
    private val context: Context,

) : RecyclerView.Adapter<EdpNlmAdapter.AvailabilityOfEquipmentViewHolder>() {
    private lateinit var stateAdapter: StateAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private val projectFinancing = listOf(
        "Subsidy Loan", "Self Finance"
    )
    private val tvAnimals = listOf(
        "Yes", "No"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailabilityOfEquipmentViewHolder {

        val binding = ItemNlmedpNlmBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return   AvailabilityOfEquipmentViewHolder(binding)

    }

    override fun onBindViewHolder(holder: AvailabilityOfEquipmentViewHolder, position: Int) {


        // Handle visibility of add/delete buttons
        handleButtonVisibility(holder.binding.btnAdd, holder.binding.btnDelete, position)


        holder.binding.tvAnimals.setOnClickListener { showBottomSheetDialog("Animals",holder.binding.tvAnimals) }
        holder.binding.tvProjectFinancing.setOnClickListener { showBottomSheetDialog("projectFinancing",holder.binding.tvProjectFinancing) }
        // Add new row
        holder.binding.btnAdd.setOnClickListener {
            programmeList.add(arrayOf("", "",""))
            notifyItemInserted(programmeList.size - 1)
            notifyItemChanged(position)
        }
        // Delete row
        holder.binding.btnDelete.setOnClickListener {
            if (programmeList.size > 1) {
                programmeList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, programmeList.size)
            }
        }
    }

    override fun getItemCount(): Int = programmeList.size
    // Helper method to manage button visibility
    private fun handleButtonVisibility(btnAdd: ImageButton, btnDelete: ImageButton, position: Int) {
        if (position == programmeList.size - 1) {
            // Last item, show Add button, hide Delete button
            btnAdd.visibility = View.VISIBLE
            btnDelete.visibility = View.GONE
        } else {
            // Hide Add button, show Delete button for other items
            btnAdd.visibility = View.GONE
            btnDelete.visibility = View.VISIBLE
        }
    }

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
                selectedList = projectFinancing
                selectedTextView = textView
            }
            "Animals" -> {
                selectedList = tvAnimals
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
    inner class AvailabilityOfEquipmentViewHolder(val binding: ItemNlmedpNlmBinding) :
        RecyclerView.ViewHolder(binding.root)
}
