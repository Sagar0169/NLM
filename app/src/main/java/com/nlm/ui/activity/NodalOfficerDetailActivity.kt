package com.nlm.ui.activity

import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.ActivityNodalOfficerDetailBinding
import com.nlm.model.NodalOfficer
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.utilities.toast

class NodalOfficerDetailActivity : BaseActivity<ActivityNodalOfficerDetailBinding>() {
    private var binding: ActivityNodalOfficerDetailBinding? = null
    private var isEditMode = false
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: StateAdapter
    private val stateList = listOf(
        "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
        "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand",
        "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
        "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab",
        "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
        "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman and Nicobar Islands",
        "Chandigarh", "Dadra and Nagar Haveli and Daman and Diu", "Lakshadweep",
        "Delhi", "Puducherry", "Ladakh", "Lakshadweep", "Jammu and Kashmir"
    )
    override val layoutId: Int
        get() = R.layout.activity_nodal_officer_detail

    override fun initView() {
        binding = viewDataBinding
        binding?.clickAction = ClickActions()
        val isFrom = intent?.getIntExtra("isFrom", 0)

        when (isFrom) {
            1 -> {
                isEditMode = true
                binding!!.btnEditSave.hideView()
                binding!!.btnSave.showView()
                binding!!.tvState.setTextColor(ContextCompat.getColor(this, R.color.grey))
                binding!!.tvDesignation.setTextColor(ContextCompat.getColor(this, R.color.grey))

                binding!!.btnSave.showView()
            }

            2 -> {
                binding!!.btnEditSave.hideView()
            }
            3 -> {
                binding!!.btnEditSave.text ="Save"
                isEditMode = true
                updateEditModeUI()
            }
        }


        val nodalOfficer = intent.getSerializableExtra("nodalOfficer") as? NodalOfficer
        binding!!.tvState.setOnClickListener { showBottomSheetDialog("State") }
        binding!!.tvDesignation.setOnClickListener { showBottomSheetDialog("Designation") }
        // Set data to the views
        nodalOfficer?.let {
            binding!!.tvState.text = it.state
            binding!!.etAgencyName.setText(it.agencyName)
            binding!!.etNodalOfficerName.setText(it.nodalOfficerName)
            binding!!.etNodalOfficerEmail.setText(it.nodalOfficerEmail)
            binding!!.etMobileNumber.setText(it.mobileNumber)
            binding!!.tvDesignation.text = it.designation
            binding!!.etCreated.setText(it.created)
        }

        // Initialize views based on edit mode
        updateEditModeUI()

        // Set up Edit/Save button
        binding!!.btnEditSave.setOnClickListener {
            toggleEditMode()
        }
    }

    private fun toggleEditMode() {
        toast("Saved")
//        isEditMode = !isEditMode
//        updateEditModeUI()
//
//        // If saving, capture the edited data
//        if (!isEditMode) {
//            // Capture the updated data and do something with it (e.g., save to database)
//            val updatedNodalOfficer = NodalOfficer(
//                state = binding!!.tvState.text.toString(),
//                agencyName = binding!!.etAgencyName.text.toString(),
//                nodalOfficerName = binding!!.etNodalOfficerName.text.toString(),
//                nodalOfficerEmail = binding!!.etNodalOfficerEmail.text.toString(),
//                created = binding!!.etCreated.text.toString(),
//                mobileNumber = binding!!.etMobileNumber.text.toString(),
//                designation = binding!!.tvDesignation.text.toString()
//            )
//        }
    }

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
            "State" -> {
                selectedList = stateList
                selectedTextView = binding!!.tvState
            }

            "Designation" -> {
                selectedList = stateList
                selectedTextView = binding!!.tvDesignation
            }

            else -> return
        }

        // Set up the adapter
        stateAdapter = StateAdapter(selectedList, this) { selectedItem ->
            // Handle state item click
            selectedTextView.text = selectedItem
            selectedTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
            bottomSheetDialog.dismiss()
        }

        rvBottomSheet.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvBottomSheet.adapter = stateAdapter
        bottomSheetDialog.setContentView(view)

        // Rotate drawable
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_arrow_down)
        var rotatedDrawable = rotateDrawable(drawable, 180f)
        selectedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, rotatedDrawable, null)

        // Set a dismiss listener to reset the view visibility
        bottomSheetDialog.setOnDismissListener {
            rotatedDrawable = rotateDrawable(drawable, 0f)
            selectedTextView.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                rotatedDrawable,
                null
            )
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


    private fun updateEditModeUI() {
        // Enable or disable EditTexts based on the mode
        binding!!.etAgencyName.isEnabled = isEditMode
        binding!!.tvState.isEnabled = isEditMode
        binding!!.tvDesignation.isEnabled = isEditMode
        binding!!.etNodalOfficerName.isEnabled = isEditMode
        binding!!.etNodalOfficerEmail.isEnabled = isEditMode
        binding!!.etMobileNumber.isEnabled = isEditMode
        binding!!.etCreated.isEnabled = isEditMode

        // Change button text based on the mode
        binding!!.btnEditSave.text ="Save"

        // Show or hide the drawable end for the TextView
        if (isEditMode) {
            // Show the drawable end (assuming drawable is defined)
            binding!!.tvState.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_down,
                0
            )
            binding!!.tvDesignation.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_down,
                0
            )
        } else {
            // Hide the drawable end
            binding!!.tvState.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            binding!!.tvDesignation.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun submit(view: View) {
            toast("Agency Added Successfully")
//            val intent =
//                Intent(this@AddImplementingAgency, ImplementingAgencyMasterActivity::class.java)
//            startActivity(intent)
            finish()
        }
    }
}
