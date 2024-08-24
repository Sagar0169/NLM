package mission.vatsalya.ui.fragment

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import mission.vatsalya.R
import mission.vatsalya.databinding.FragmentSightedConfirmationBinding
import mission.vatsalya.databinding.FragmentSightedLocationDetialsFragementBinding
import mission.vatsalya.databinding.FragmentSightedUploadBinding
import mission.vatsalya.ui.activity.DashboardActivity
import mission.vatsalya.ui.adapter.RelationshipAdapter
import mission.vatsalya.utilities.BaseFragment


class SightedConfirmationFragment : BaseFragment<FragmentSightedConfirmationBinding>() {
    private var mBinding: FragmentSightedConfirmationBinding? = null
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
        get() = R.layout.fragment_sighted_confirmation

    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }

    inner class ClickActions {

        fun submit(view: View) {
            val intent = Intent(requireContext(),DashboardActivity::class.java)
            startActivity(intent)
            Toast.makeText(requireContext(),"Form Filled Successfully",Toast.LENGTH_SHORT).show()
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