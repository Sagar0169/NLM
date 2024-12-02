package com.nlm.ui.activity.livestock_health_disease.vaccination_programme

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nlm.R
import com.nlm.databinding.ActivityAddVaccinationProgrammeDistrictLevelBinding
import com.nlm.databinding.ActivityAddVaccinationProgrammeStateLevelBinding
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.Utility.showSnackbar

class AddVaccinationProgrammeDistrictLevel : BaseActivity<ActivityAddVaccinationProgrammeDistrictLevelBinding>()  {
    override val layoutId: Int
        get() = R.layout.activity_add_vaccination_programme_district_level

    private var mBinding: ActivityAddVaccinationProgrammeDistrictLevelBinding? = null
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
        return !(mBinding?.etInput5?.text.toString().isEmpty()&&mBinding?.etInput4?.text.toString().isEmpty()&&mBinding?.etInput3?.text.toString().isEmpty()&&mBinding?.etInput2?.text.toString().isEmpty()&&mBinding?.etInput1?.text.toString().isEmpty()&&mBinding?.etRemarks1?.text.toString().isEmpty()&&mBinding?.etRemark5?.text.toString().isEmpty()&&mBinding?.etRemark4?.text.toString().isEmpty()&&mBinding?.etRemark3?.text.toString().isEmpty()&&mBinding?.etRemark2?.text.toString().isEmpty()&&mBinding?.etChooseFile2?.text.toString().isEmpty()&&mBinding?.etChooseFile3?.text.toString().isEmpty()&&mBinding?.etChooseFile4?.text.toString().isEmpty()&&mBinding?.etChooseFile5?.text.toString().isEmpty()&&mBinding?.etChooseFile1?.text.toString().isEmpty())

    }
}
