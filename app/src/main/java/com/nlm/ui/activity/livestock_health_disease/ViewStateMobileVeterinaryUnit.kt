package com.nlm.ui.activity.livestock_health_disease

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityViewStateMobileVeterinaryUnitBinding
import com.nlm.model.Indicator
import com.nlm.ui.adapter.AddNewMobileVeterinaryUnitAdapter
import com.nlm.utilities.BaseActivity

class ViewStateMobileVeterinaryUnit : BaseActivity<ActivityViewStateMobileVeterinaryUnitBinding>() {
    private var mBinding: ActivityViewStateMobileVeterinaryUnitBinding? = null
    private lateinit var addNewMobileUnit: AddNewMobileVeterinaryUnitAdapter
    private var layoutManager: LinearLayoutManager? = null
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

    override val layoutId: Int
        get() = R.layout.activity_view_state_mobile_veterinary_unit


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()

        addNewMobileUnitAdapter()

    }

    private fun addNewMobileUnitAdapter() {
        addNewMobileUnit = AddNewMobileVeterinaryUnitAdapter(questions,1)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvMobileVeterinaryUnit.layoutManager = layoutManager
        mBinding!!.rvMobileVeterinaryUnit.adapter = addNewMobileUnit
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}