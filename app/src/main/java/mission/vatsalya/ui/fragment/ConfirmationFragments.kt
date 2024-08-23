package mission.vatsalya.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mission.vatsalya.R
import mission.vatsalya.databinding.ActivityChildMissingBinding
import mission.vatsalya.databinding.FragmentBasicDetailsBinding
import mission.vatsalya.databinding.FragmentConfirmationBinding
import mission.vatsalya.databinding.FragmentLocationDetailsBinding
import mission.vatsalya.databinding.FragmentUploadMediaBinding
import mission.vatsalya.ui.activity.OtpActivity
import mission.vatsalya.ui.activity.RegistrationActivity
import mission.vatsalya.utilities.BaseFragment

class ConfirmationFragments : BaseFragment<FragmentConfirmationBinding>(){
    private var mBinding: FragmentConfirmationBinding?=null


    override val layoutId: Int
        get() = R.layout.fragment_confirmation

    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
    inner class ClickActions {

        fun login(view: View) {


        }
        fun register(view: View) {

        }

        fun backPress(view: View) {

        }
    }

}