package com.nlm.ui.activity.livestock_health_disease.vaccination_programme

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nlm.R
import com.nlm.databinding.ActivityAddVaccinationProgrammeDistrictLevelBinding
import com.nlm.databinding.ActivityAddVaccinationProgrammeFarmerLevelBinding
import com.nlm.databinding.ActivityAddVaccinationProgrammeStateLevelBinding
import com.nlm.utilities.BaseActivity

class AddVaccinationProgrammeFarmerLevel : BaseActivity<ActivityAddVaccinationProgrammeFarmerLevelBinding>()  {
    override val layoutId: Int
        get() = R.layout.activity_add_vaccination_programme_farmer_level
    private var mBinding: ActivityAddVaccinationProgrammeFarmerLevelBinding? = null
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
        fun saveAndNext(view: View) {
//            onBackPressedDispatcher.onBackPressed()
        }
        fun saveAsDraft(view: View) {
//            onBackPressedDispatcher.onBackPressed()
        }
    }

}
