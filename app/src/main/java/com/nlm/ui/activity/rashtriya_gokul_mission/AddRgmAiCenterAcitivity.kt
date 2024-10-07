package com.nlm.ui.activity.rashtriya_gokul_mission

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
import com.nlm.databinding.ActivityAddRgmAiCenterAcitivityBinding
import com.nlm.ui.adapter.ProgrammeAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.BaseActivity

class AddRgmAiCenterAcitivity : BaseActivity<ActivityAddRgmAiCenterAcitivityBinding>() {
    private var mBinding: ActivityAddRgmAiCenterAcitivityBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProgrammeAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: StateAdapter
    private lateinit var programmeList: MutableList<Array<String>>
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

    private val center = listOf(
        "Stationary","Mobile"
    )
    private val agency = listOf(
        "Government","COOP","NGO","MAITRI"

    )
    private val categoryWorker = listOf(
        "Veterinary","Stock Man","Lay Inseminator","MAITRI"
    )
    private val qualificationWorker = listOf(
        "B.V.Sc","Graduate","12th Std","Others"
    )
    override val layoutId: Int
        get() = R.layout.activity_add_rgm_ai_center_acitivity


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        programmeList = mutableListOf()
        programmeList.add(arrayOf("", "", ""))

        adapter = ProgrammeAdapter(programmeList)
        recyclerView.adapter = adapter

        mBinding!!.tvState.setOnClickListener { showBottomSheetDialog("State") }
        mBinding!!.tvDistrict.setOnClickListener { showBottomSheetDialog("District") }
        mBinding!!.tvCentre.setOnClickListener { showBottomSheetDialog("Center") }
        mBinding!!.tvAgency.setOnClickListener { showBottomSheetDialog("Agency") }
        mBinding!!.tvCategoryWorker.setOnClickListener { showBottomSheetDialog("CategoryWorker") }
        mBinding!!.tvQualificationWorker.setOnClickListener { showBottomSheetDialog("QualificationWorker") }

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

            "Center" -> {
                selectedList = center
                selectedTextView = mBinding!!.tvCentre
            }

            "Agency" -> {
                selectedList = agency
                selectedTextView = mBinding!!.tvAgency
            }

            "CategoryWorker" -> {
                selectedList = categoryWorker
                selectedTextView = mBinding!!.tvCategoryWorker
            }

            "QualificationWorker" -> {
                selectedList = qualificationWorker
                selectedTextView = mBinding!!.tvQualificationWorker
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

//    private fun onlyCreatedAdapter() {
//        onlyCreatedAdapter = NlmEdpAdapter(onlyCreated, isFrom)
//        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        mBinding!!.rvNlmEdp.layoutManager = layoutManager
//        mBinding!!.rvNlmEdp.adapter = onlyCreatedAdapter
//    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}