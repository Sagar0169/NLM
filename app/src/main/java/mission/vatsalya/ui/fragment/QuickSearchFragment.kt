package mission.vatsalya.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import mission.vatsalya.R
import mission.vatsalya.databinding.FragmentQuickSearchBinding
import mission.vatsalya.utilities.BaseFragment
import mission.vatsalya.utilities.hideView
import mission.vatsalya.utilities.showView


class QuickSearchFragment() : BaseFragment<FragmentQuickSearchBinding>(){
    override val layoutId: Int
        get() = R.layout.fragment_quick_search

    private var mBinding:FragmentQuickSearchBinding?=null
    override fun init() {
        mBinding=viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {

        fun FirDetail(view: View) {
            if (mBinding?.llFir?.isVisible == true)
            {
                mBinding?.llFir?.hideView()
            }
            else{
                mBinding?.llFir?.showView()
            }
        }
        fun register(view: View) {

        }

        fun backPress(view: View) {

        }
    }
}