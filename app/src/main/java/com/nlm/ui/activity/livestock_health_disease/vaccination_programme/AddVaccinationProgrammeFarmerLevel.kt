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
import com.nlm.utilities.Utility.showSnackbar

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
            if (vaild())
            {
                showSnackbar(mBinding!!.main, "Data Saved")
            }
            else{
                showSnackbar(mBinding!!.main, "Please fill all the fields")
            }
        }
        fun saveAsDraft(view: View) {
            if (vaild())
            {
                showSnackbar(mBinding!!.main, "Data Saved")
            }
            else{
                showSnackbar(mBinding!!.main, "Please fill all the fields")
            }
        }
    }
    private fun vaild(): Boolean {
        return !(mBinding?.etInputs1?.text.toString().isEmpty()&&mBinding?.etInputs4?.text.toString().isEmpty()&&mBinding?.etInputs3?.text.toString().isEmpty()&&mBinding?.etInputs2?.text.toString().isEmpty()&&mBinding?.etInputs1?.text.toString().isEmpty()&&mBinding?.etRemarks1?.text.toString().isEmpty()&&mBinding?.etRemarks5?.text.toString().isEmpty()&&mBinding?.etRemarks4?.text.toString().isEmpty()&&mBinding?.etRemarks3?.text.toString().isEmpty()&&mBinding?.etRemarks2?.text.toString().isEmpty()&&mBinding?.etChooseFile2?.text.toString().isEmpty()&&mBinding?.etChooseFile3?.text.toString().isEmpty()&&mBinding?.etChoosefile4?.text.toString().isEmpty()&&mBinding?.etChoosefile5?.text.toString().isEmpty()&&mBinding?.etChooseFile1?.text.toString().isEmpty())

    }

}
