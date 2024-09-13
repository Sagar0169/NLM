package com.nlm.ui.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.ActivityBullMotherFarmsBinding
import com.nlm.databinding.ActivityDashboardBinding
import com.nlm.ui.adapter.BottomSheetAdapter
import com.nlm.utilities.BaseActivity

class Bull_Mother_Farms :BaseActivity<ActivityBullMotherFarmsBinding>() {
    private var mBinding: ActivityBullMotherFarmsBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_bull_mother_farms
    private lateinit var bottomSheetAdapter: BottomSheetAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private val state = listOf(
        "Left Artificial", "Right Artificial", "Left Squint", "Right Squint", "Others"
    )

    private val district = listOf(
        "Black", "Brown", "Blue", "Reddish", "Green", "Other"
    )

    override fun initView() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {
        fun state(view: View){showBottomSheetDialog("state")}
        fun Farm(view: View){showBottomSheetDialog("district")}
        fun breed(view: View){showBottomSheetDialog("district")}
        fun status(view: View){showBottomSheetDialog("district")}
        fun backPress(view: View){
            onBackPressed()
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


            "state" -> {
                selectedList = state
                selectedTextView = mBinding!!.etState
            }

            "district" -> {
                selectedList = district
                selectedTextView = mBinding!!.etFarm
            }



            else -> return
        }
        bottomSheetAdapter = BottomSheetAdapter(this,selectedList) { selectedItem ->
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