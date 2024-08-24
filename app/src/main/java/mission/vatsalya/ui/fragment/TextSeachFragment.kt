package mission.vatsalya.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mission.vatsalya.R
import mission.vatsalya.databinding.FragmentTextSeachBinding
import mission.vatsalya.utilities.BaseFragment


class TextSeachFragment() : BaseFragment<FragmentTextSeachBinding>() {



    override val layoutId: Int
        get() = R.layout.fragment_text_seach
    private var mBinding:FragmentTextSeachBinding?=null
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