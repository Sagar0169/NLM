package mission.vatsalya.ui.fragment

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import mission.vatsalya.R
import mission.vatsalya.databinding.FragmentSightedLocationDetialsFragementBinding
import mission.vatsalya.databinding.FragmentSightedUploadBinding
import mission.vatsalya.ui.adapter.RelationshipAdapter
import mission.vatsalya.utilities.BaseFragment


class SightedUploadFragment : BaseFragment<FragmentSightedUploadBinding>() {
    private var mBinding: FragmentSightedUploadBinding? = null
    private var isSelected: Boolean? = false
    private lateinit var relationAdapter: RelationshipAdapter
    private var layoutManager: LinearLayoutManager? = null
    private var listener: OnNextButtonClickListener? = null

    interface OnNextButtonClickListener {
        fun onNextButtonClick()
    }

    private val relationList = listOf(
        "Parent", "Legal Guardian", "Other",
    )

    override val layoutId: Int
        get() = R.layout.fragment_sighted_upload

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