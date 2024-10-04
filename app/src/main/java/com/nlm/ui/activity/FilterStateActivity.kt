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
import com.nlm.databinding.ActivityFilterStateBinding
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.showView
import com.nlm.utilities.toast

class FilterStateActivity : BaseActivity<ActivityFilterStateBinding>() {
    private var binding: ActivityFilterStateBinding? = null
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
    private val status = listOf(
        "Active", "In Active"
    )
    private val reading = listOf(
        "Yes", "No"
    )
    override val layoutId: Int
        get() = R.layout.activity_filter_state

    override fun initView() {
        binding = viewDataBinding
        binding?.clickAction = ClickActions()
        val isFrom = intent?.getIntExtra("isFrom", 0)

        when (isFrom) {
            1 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
            }

            2 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
            }

            3 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvTitleBlock.showView()
                binding!!.etBlock.showView()

            }

            4 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvTitleBlock.showView()
                binding!!.etBlock.showView()
                binding!!.tvTitleVillageName.showView()
                binding!!.etVillageName.showView()

            }

            5 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()

            }

            6 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
            }

            7 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvTitleVillageName.showView()
                binding!!.etVillageName.showView()

            }

            8 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()

            }

            9 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()

            }

            11 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvTitleLocAi.text = "Location"
                binding!!.tvTitleLocAi.showView()
                binding!!.etLocAi.showView()

            }

            12 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvTitleAiName.showView()
                binding!!.etAiName.showView()
                binding!!.tvTitleLocAi.showView()
                binding!!.etLocAi.showView()
            }

            13 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvTitleNoa.showView()
                binding!!.etNoa.showView()
                binding!!.tvTitleOrganogram.showView()
                binding!!.etOrganogram.showView()
                binding!!.tvTitleTechnicalCompetance.showView()
                binding!!.etTechnicalCompetance.showView()
            }

            14 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvTitleNoa.showView()
                binding!!.etNoa.showView()
                binding!!.tvTitleLocationAddress.showView()
                binding!!.etLocationAddress.showView()
                binding!!.tvTitleCapacityofPlant.showView()
                binding!!.etCapacityofPlant.showView()
            }

            15 -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvTitleNameofAgency.showView()
                binding!!.etNameofAgency.showView()
                binding!!.tvTitleLocationAddress.showView()
                binding!!.etLocationAddress.showView()
                binding!!.tvTitleAreaCovered.showView()
                binding!!.etAreaCovered.showView()
            }

            16 -> {
                binding!!.tvTitleReadingMaterial.showView()
                binding!!.tvReadingMaterial.showView()

            }

            17 -> {
                binding!!.tvTitleCommentProgress.showView()
                binding!!.etCommentProgress.showView()

            }
            25->{

                binding!!.tvStateTitleNDD.showView()
                binding!!.tvStateNDD.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvNameofMilkUnion.showView()
                binding!!.etNameofMilkUnion.showView()

            }
            26->{
                binding!!.tvStateTitleNDD.showView()
                binding!!.tvStateNDD.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvfssaiLicNo.showView()
                binding!!.etfssaiLicNo.showView()


            }
            27->{
                binding!!.tvStateTitleNDD.showView()
                binding!!.tvStateNDD.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvfssaiLicNo.showView()
                binding!!.etfssaiLicNo.showView()
                binding!!.tvnameOfDcs.showView()
                binding!!.etnameOfDcs.showView()
            }
            28->{
                binding!!.tvStateTitleNDD.showView()
                binding!!.tvStateNDD.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvLocationNDD.showView()
                binding!!.etLocationNDD.showView()
            }
            29->{
                binding!!.tvTitleNDD.showView()
                binding!!.etTitleNDD.showView()
                binding!!.tvStatus.showView()

            }
            30->{
                binding!!.tvStateTitleNDD.showView()
                binding!!.tvStateNDD.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.etNameofRetailShop.showView()
                binding!!.tvNameofRetailShop.showView()
                binding!!.tvNameOfMPCFederation.showView()
                binding!!.etNameOfMPCFederation.showView()
                binding!!.tvDateOfInspection.showView()
                binding!!.etDateOfInspection.showView()

            }
            31->{
                binding!!.tvTitleNDD.showView()
                binding!!.etTitleNDD.showView()
                binding!!.tvStatus.showView()

            }
            32->{
                binding!!.tvStateTitleNDD.showView()
                binding!!.tvStateNDD.showView()
                binding!!.tvTitleDistrict.showView()
                binding!!.tvDistrict.showView()
                binding!!.tvTitleVillageName.showView()
                binding!!.etVillageName.showView()
            }
            33->{
                binding!!.tvNameOfBeneficiaery.showView()
                binding!!.etNameOfBeneficiary.showView()

            }
            34->{
                binding!!.tvState.showView()
                binding!!.tvTitleState.showView()
                binding!!.tvTitleLoc.showView()
                binding!!.etLoc.showView()

            }
            35->{
                binding!!.tvTitleVillageName.showView()
                binding!!.etVillageName.showView()

            }







            else -> {
                binding!!.tvTitleState.showView()
                binding!!.tvState.showView()
                binding!!.tvTitleNodalName.showView()
                binding!!.etNodalName.showView()
                binding!!.tvTitleNodalEmail.showView()
                binding!!.etNodalOfficerEmail.showView()
            }
        }
        binding!!.tvState.setTextColor(ContextCompat.getColor(this, R.color.grey))
        binding!!.tvDistrict.setTextColor(ContextCompat.getColor(this, R.color.grey))
        binding!!.tvState.setOnClickListener { showBottomSheetDialog("State") }
        binding!!.tvStateNDD.setOnClickListener { showBottomSheetDialog("StateNDD") }
        binding!!.tvDistrict.setOnClickListener { showBottomSheetDialog("District") }
        binding!!.tvStatus.setOnClickListener { showBottomSheetDialog("Status") }
        binding!!.tvReadingMaterial.setOnClickListener { showBottomSheetDialog("Reading") }

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
            "StateNDD" -> {
                selectedList = stateList
                selectedTextView = binding!!.tvStateNDD
            }

            "District" -> {
                selectedList = stateList
                selectedTextView = binding!!.tvDistrict
            }

            "Status" -> {
                selectedList = status
                selectedTextView = binding!!.tvStatus
            }

            "Reading" -> {
                selectedList = reading
                selectedTextView = binding!!.tvReadingMaterial
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


    override fun setVariables() {
    }

    override fun setObservers() {
    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }


        fun submit(view: View) {
            toast("Filter Applied")
//            val intent =
//                Intent(this@AddImplementingAgency, ImplementingAgencyMasterActivity::class.java)
//            startActivity(intent)
            finish()
        }
    }
}
