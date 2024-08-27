package mission.vatsalya.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import mission.vatsalya.R
import mission.vatsalya.databinding.FragmentSightedBasicDetailsBinding
import mission.vatsalya.databinding.FragmentSightedFacialAttributesBinding
import mission.vatsalya.ui.adapter.RelationshipAdapter
import mission.vatsalya.ui.fragment.SightedBasicDetailsFragment.OnNextButtonClickListener
import mission.vatsalya.utilities.BaseFragment


class SightedFacialAttributesFragment : BaseFragment<FragmentSightedFacialAttributesBinding>() {
    private var mBinding: FragmentSightedFacialAttributesBinding? = null
    private var isSelected: Boolean? = false
    private lateinit var relationAdapter: RelationshipAdapter
    private var layoutManager: LinearLayoutManager? = null
    private var listener: OnNextButtonClickListener? = null

    interface OnNextButtonClickListener {
        fun onNextButtonClick()
    }

    private val hairLength = listOf(
        "Short", "Medium", "Long","No Hair"
    )

    private val hairColor = listOf(
        "Short", "Medium", "Long","No Hair"
    )

    override val layoutId: Int
        get() = R.layout.fragment_sighted_facial_attributes

    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }

    inner class ClickActions {

        fun login(view: View) {


        }

        fun next(view: View) {
            listener?.onNextButtonClick()

        }

        fun backPress(view: View) {

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