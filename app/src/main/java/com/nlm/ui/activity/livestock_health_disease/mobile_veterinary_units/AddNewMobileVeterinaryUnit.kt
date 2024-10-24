package com.nlm.ui.activity.livestock_health_disease.mobile_veterinary_units

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
import com.nlm.databinding.ActivityAddNewMobileVeterinaryUnitBinding
import com.nlm.model.Indicator
import com.nlm.ui.adapter.AddNewMobileVeterinaryUnitAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.showView

class AddNewMobileVeterinaryUnit : BaseActivity<ActivityAddNewMobileVeterinaryUnitBinding>() {
    private var mBinding: ActivityAddNewMobileVeterinaryUnitBinding? = null
    private lateinit var addNewMobileUnit: AddNewMobileVeterinaryUnitAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: StateAdapter
    private var layoutManager: LinearLayoutManager? = null
    private var isFrom: Int = 0
    val questions = listOf(
        Indicator(
            "1.",
            "What is the mechanism for operationalization/ functioning of MVUs in the state?"
        ),
        Indicator(
            "2.",
            "How is engagement of Vets/ Para-vets/ Drivers for MVUs done as per guidelines?"
        ),
        Indicator(
            "3.",
            "Procurement procedure of medicines, accessories and other consumables/non-consumables at state level which is to be maintained at MVUs?"
        ),
        Indicator(
            "4.",
            "Supply procedure for medicines, accessories and others at state level which is to be maintained at MVUs?"
        ),
        Indicator(
            "5.",
            "Is there monitoring and supervision plan for medicine & equipment  procurement/ distribution at State-level?"
        ),
        Indicator(
            "6.",
            "Is there monitoring and supervision plan at state level for fuel arrangement for MVUs?"
        ),
        Indicator(
            "7.",
            "Call Centre"
        ),
        Indicator(
            "a)",
            "Is a service provider engaged?"
        ),
        Indicator(
            "b)",
            "Is a building provided for operation with requisite seats"
        ),
        Indicator(
            "c)",
            "Are Operators engaged?"
        ),
        Indicator(
            "d)",
            "Is an App/ CRM system in place?"
        ),
        Indicator(
            "e)",
            "Are adequate staff including Veterinarians engaged?"
        ),
        Indicator(
            "f)",
            "How and by whom data compilation and analysis done for reporting?"
        ),

        // Add more questions here...
    )

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
        get() = R.layout.activity_add_new_mobile_veterinary_unit


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        isFrom = intent?.getIntExtra("isFrom", 0)!!

        when (isFrom) {
            2 -> {
                mBinding!!.tvHeading.text = "Add New District Mobile Veterinary Unit"
                mBinding!!.llDistrict.showView()
            }

            3 -> {
                mBinding!!.tvHeading.text = "Add New Block Mobile Veterinary Unit"
                mBinding!!.llDistrict.showView()
                mBinding!!.llBlock.showView()

            }

            4 -> {
                mBinding!!.tvHeading.text = "Add New Farmer Mobile Veterinary Unit"
                mBinding!!.llDistrict.showView()
                mBinding!!.llBlock.showView()
                mBinding!!.llFarmer.showView()

            }
            5 -> {
                mBinding!!.tvHeading.text = "Add New State Vaccination Programme"

            }
            6 -> {
                mBinding!!.tvHeading.text = "Add New District Vaccination Programme"
                mBinding!!.llDistrict.showView()

            }
            7 -> {
                mBinding!!.tvHeading.text = "Add New Beneficiary/farmer Vaccination Programme"
                mBinding!!.llDistrict.showView()
                mBinding!!.llFarmer.showView()

            }
            8 -> {
                mBinding!!.tvHeading.text = "Add New ASCAD State"

            }
            9 -> {
                mBinding!!.tvHeading.text = "Add New ASCAD District"
                mBinding!!.llDistrict.showView()

            }
        }
        mBinding!!.tvState.setOnClickListener { showBottomSheetDialog("State") }
        mBinding!!.tvDistrict.setOnClickListener { showBottomSheetDialog("District") }
        addNewMobileUnitAdapter()

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
                selectedTextView = mBinding!!.tvState
            }

            "District" -> {
                selectedList = stateList
                selectedTextView = mBinding!!.tvDistrict
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

    private fun addNewMobileUnitAdapter() {
        addNewMobileUnit = AddNewMobileVeterinaryUnitAdapter(questions, isFrom)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvMobileVeterinaryUnit.layoutManager = layoutManager
        mBinding!!.rvMobileVeterinaryUnit.adapter = addNewMobileUnit
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}