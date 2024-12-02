package com.nlm.ui.activity.livestock_health_disease.vaccination_programme

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nlm.R
import com.nlm.databinding.ActivityAddNewMobileVeterinaryUnitBinding
import com.nlm.databinding.ActivityAddVaccinationProgrammeStateLevelBinding
import com.nlm.utilities.BaseActivity

class AddVaccinationProgrammeStateLevel : BaseActivity<ActivityAddVaccinationProgrammeStateLevelBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_add_vaccination_programme_state_level
    private var mBinding: ActivityAddVaccinationProgrammeStateLevelBinding? = null
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
    private fun vaild(): Boolean {
        return !(mBinding?.etInput1a?.text.toString().isEmpty()&&mBinding?.etInput1b?.text.toString().isEmpty()&&mBinding?.etInput1c?.text.toString().isEmpty()&&mBinding?.etInput1d?.text.toString().isEmpty()&&mBinding?.etInput1e?.text.toString().isEmpty()&&mBinding?.etRemark1a?.text.toString().isEmpty()&& mBinding?.etRemark1b?.text.toString().isEmpty() && mBinding?.etRemark1c?.text.toString().isEmpty()&&mBinding?.etRemark1d?.text.toString().isEmpty()&&mBinding?.etRemark1e?.text.toString().isEmpty()&&mBinding?.etChooseFile1a?.text.toString().isEmpty()||mBinding?.etChooseFile1b?.text.toString().isEmpty()&&mBinding?.etChooseFile1c?.text.toString().isEmpty()&&mBinding?.etChooseFile1d?.text.toString().isEmpty()&&mBinding?.etChooseFile2?.text.toString().isEmpty()&&mBinding?.etChooseFile3?.text.toString().isEmpty())

    }


}