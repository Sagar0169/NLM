package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.ActivityUserFormBinding
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.BaseActivity

class UserFormActivity : BaseActivity<ActivityUserFormBinding>() {

    private var mBinding: ActivityUserFormBinding? = null
    private lateinit var bottomSheetAdapter: StateAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private val group = listOf(
        "Short", "Medium", "Long", "No Hair"
    )

    private val role = listOf(
        "Black", "Brown", "Gray"
    )

    private val state = listOf(
        "Left Artificial", "Right Artificial", "Left Squint", "Right Squint", "Others"
    )

    private val district = listOf(
        "Black", "Brown", "Blue", "Reddish", "Green", "Other"
    )

    private val designation = listOf(
        "Folded", "Normal", "Other"
    )

    private val organisation = listOf(
        "Large", "Normal", "Small"
    )

    override val layoutId: Int
        get() = R.layout.activity_user_form

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }

    inner class ClickActions {
        fun backPress(view: View){
            onBackPressedDispatcher.onBackPressed()
        }

        fun save(view: View){
            startActivity(Intent(this@UserFormActivity, UserActivity::class.java))
        }
        fun group(view: View){
            showBottomSheetDialog("group")
        }
        fun role(view: View){showBottomSheetDialog("role")}
        fun state(view: View){showBottomSheetDialog("state")}
        fun district(view: View){showBottomSheetDialog("district")}
        fun designation(view: View){showBottomSheetDialog("designation")}
        fun organisation(view: View){showBottomSheetDialog("organisation")}
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
            "hairLength" -> {
                selectedList = group
                selectedTextView = mBinding!!.etGroup
            }

            "hairColor" -> {
                selectedList = role
                selectedTextView = mBinding!!.etRole
            }

            "eyeType" -> {
                selectedList = state
                selectedTextView = mBinding!!.etState
            }

            "eyeColor" -> {
                selectedList = district
                selectedTextView = mBinding!!.etDistrict
            }

            "earsType" -> {
                selectedList = designation
                selectedTextView = mBinding!!.etDesignation
            }

            "earsSize" -> {
                selectedList = organisation
                selectedTextView = mBinding!!.etOrganisation
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