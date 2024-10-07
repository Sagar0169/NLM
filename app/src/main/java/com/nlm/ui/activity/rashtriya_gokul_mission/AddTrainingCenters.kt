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
import com.nlm.databinding.ActivityAddTrainingCentersBinding
import com.nlm.model.FacultyMembersTrainingCenter
import com.nlm.model.MaitrisTrainingCenter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.ui.adapter.rgm.AddMoreFacultyMembersTrainingAdapter
import com.nlm.ui.adapter.rgm.AddMoreMaitrisTrainingAdapter
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import com.nlm.utilities.toast

class AddTrainingCenters : BaseActivity<ActivityAddTrainingCentersBinding>() {
    private var mBinding: ActivityAddTrainingCentersBinding? = null
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: StateAdapter
    private lateinit var addMoreAdapterFaculty: AddMoreFacultyMembersTrainingAdapter
    private lateinit var addMoreMaitrisAdapterFaculty: AddMoreMaitrisTrainingAdapter
    private val facultyMembersList = mutableListOf<FacultyMembersTrainingCenter>()
    private val MaitrisList = mutableListOf<MaitrisTrainingCenter>()
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
        get() = R.layout.activity_add_training_centers

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        facultyMembersList.add(FacultyMembersTrainingCenter("", "", "", ""))
        MaitrisList.add(MaitrisTrainingCenter("", "", "", "",""))

        addMoreFacultyAdapter()
        addMoreMaitrisAdapter()
        mBinding!!.etState.setOnClickListener { showBottomSheetDialog("State")
            mBinding!!.ivArrowUpDState.hideView()
        }
        mBinding!!.etDistrict.setOnClickListener { showBottomSheetDialog("District")
            mBinding!!.ivArrowUpDIstrict.hideView()

        }
        mBinding!!.etControllingAgency.setOnClickListener { showBottomSheetDialog("Agency")
            mBinding!!.ivArrowC.hideView()

        }

        // Add More button
        mBinding!!.tvAddMoreFaculty.setOnClickListener {

//            addMoreAdapterFaculty.addItem()
            facultyMembersList.add(FacultyMembersTrainingCenter("","","",""))
            addMoreAdapterFaculty.notifyItemInserted(facultyMembersList.size - 1)


            checkRemoveButtonVisibility()

        }
        mBinding!!.tvAddMoreMaitris.setOnClickListener {

//            addMoreAdapterFaculty.addItem()
            MaitrisList.add(MaitrisTrainingCenter("","","","",""))
            addMoreMaitrisAdapterFaculty.notifyItemInserted(MaitrisList.size - 1)


            checkRemoveButtonMaitrisVisibility()

        }

        // Remove button
        mBinding!!.tvRemoveFaculty.setOnClickListener {
            addMoreAdapterFaculty.removeItem()
            checkRemoveButtonVisibility()
        }
        mBinding!!.tvRemoveMaitris.setOnClickListener {
            addMoreMaitrisAdapterFaculty.removeItem()
            checkRemoveButtonMaitrisVisibility()
        }

        mBinding!!.rbReadingMaterailYes.setOnClickListener {
           rbReadingMaterial()

        }
        mBinding!!.rbReadingMaterailNo.setOnClickListener {
           rbReadingMaterial()

        }



    }
    fun rbReadingMaterial(){
        if (mBinding!!.rbReadingMaterailYes.isChecked){
            mBinding!!.llMaitris.showView()
        }else{
            mBinding!!.llMaitris.hideView()

        }
    }
fun addMoreFacultyAdapter(){
    addMoreAdapterFaculty = AddMoreFacultyMembersTrainingAdapter(this, facultyMembersList)
    mBinding?.rvFacultyMembers?.layoutManager = LinearLayoutManager(this)
    mBinding?.rvFacultyMembers?.adapter = addMoreAdapterFaculty
}

    // Function to show or hide the Remove button based on the list size
    private fun checkRemoveButtonVisibility() {
        if (facultyMembersList.size > 1) {
            mBinding!!.tvRemoveFaculty.visibility = View.VISIBLE
        } else {
            mBinding!!.tvRemoveFaculty.visibility = View.GONE
        }
        }

    fun addMoreMaitrisAdapter(){
    addMoreMaitrisAdapterFaculty = AddMoreMaitrisTrainingAdapter(this, MaitrisList)
    mBinding?.rvMaitris?.layoutManager = LinearLayoutManager(this)
    mBinding?.rvMaitris?.adapter = addMoreMaitrisAdapterFaculty
}

    // Function to show or hide the Remove button based on the list size
    private fun checkRemoveButtonMaitrisVisibility() {
        if (MaitrisList.size > 1) {
            mBinding!!.tvRemoveMaitris.visibility = View.VISIBLE
        } else {
            mBinding!!.tvRemoveMaitris.visibility = View.GONE
        }
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
                selectedTextView = mBinding!!.etState
            }

            "District" -> {
                selectedList = stateList
                selectedTextView = mBinding!!.etDistrict
            }
            "Agency" -> {
                selectedList = stateList
                selectedTextView = mBinding!!.etControllingAgency
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
            toast("Milk Union Visit Report Added Successfully")
//            val intent =
//                Intent(this@AddImplementingAgency, ImplementingAgencyMasterActivity::class.java)
//            startActivity(intent)
            finish()
        }
    }
}