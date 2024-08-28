package mission.vatsalya.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mission.vatsalya.R
import mission.vatsalya.databinding.ActivityChildMissingBinding
import mission.vatsalya.databinding.FragmentBasicDetailsBinding
import mission.vatsalya.databinding.FragmentFacialAttributesBinding
import mission.vatsalya.databinding.FragmentFirDetailsBinding
import mission.vatsalya.databinding.FragmentPhysicalAttributesBinding
import mission.vatsalya.ui.activity.OtpActivity
import mission.vatsalya.ui.activity.RegistrationActivity
import mission.vatsalya.utilities.BaseFragment

class FirDetailsFragment : BaseFragment<FragmentFirDetailsBinding>(){
    private var mBinding: FragmentFirDetailsBinding?=null
    private var listener: OnNextButtonClickListener? = null



    override val layoutId: Int
        get() = R.layout.fragment_fir_details

    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }

    interface OnNextButtonClickListener {
        fun onNextButtonClick()
    }
    inner class ClickActions {

        fun login(view: View) {


        }
        fun register(view: View) {

        }

        fun backPress(view: View) {

        }
        fun next(view: View) {
            listener?.onNextButtonClick()

        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnNextButtonClickListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

}