package com.nlm.ui.fragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nlm.R
import com.nlm.databinding.FragmentFacialAttributesBinding
import com.nlm.ui.adapter.RelationshipAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.BaseFragment

class FacialAttributesFragment : BaseFragment<FragmentFacialAttributesBinding>() {
    private var mBinding: FragmentFacialAttributesBinding? = null
    private var isSelected: Boolean? = false
    private lateinit var relationAdapter: RelationshipAdapter
    private var layoutManager: LinearLayoutManager? = null
    private var listener: OnNextButtonClickListener? = null
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: StateAdapter

    interface OnNextButtonClickListener {
        fun onNextButtonClick()
    }

    private val hairLength = listOf(
        "Short", "Medium", "Long", "No Hair"
    )

    private val hairColor = listOf(
        "Black", "Brown", "Gray"
    )

    private val eyeType = listOf(
        "Left Artificial", "Right Artificial", "Left Squint", "Right Squint", "Others"
    )

    private val eyeColor = listOf(
        "Black", "Brown", "Blue", "Reddish", "Green", "Other"
    )

    private val earsType = listOf(
        "Folded", "Normal", "Other"
    )

    private val earsSize = listOf(
        "Large", "Normal", "Small"
    )

    private val lipsType = listOf(
        "Wrinkled", "Drooping", "Thin", "Uneven", "Flat Upper lip", "Full lips", "Cleft Lips"
    )

    private val lipsColor = listOf(
        "Pink", "Purple", "Red", "Black"
    )

    private val frontTeeth = listOf(
        "Normal", "Broken", "One missing", "Two missing", "No teeth"
    )
    private val spectaclesType = listOf(
        "Round Frame", "Rectangle Frame", "No Frame"
    )
    private val spectaclesColor = listOf(
        "Black", "Blue", "Brown",
        "Cyan", "Gray", "Green",
        "Magenta", "Orange", "Pink",
        "Purple", "Red", "Violet",
        "White", "Yellow", "Other",
    )


    override val layoutId: Int
        get() = R.layout.fragment_facial_attributes

    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()

        // Set click listeners for each TextView
        mBinding!!.tvHairLength.setOnClickListener { showBottomSheetDialog("hairLength") }
        mBinding!!.tvHairColor.setOnClickListener { showBottomSheetDialog("hairColor") }
        mBinding!!.tvEyeType.setOnClickListener { showBottomSheetDialog("eyeType") }
        mBinding!!.tvEyeColor.setOnClickListener { showBottomSheetDialog("eyeColor") }
        mBinding!!.tvEarsType.setOnClickListener { showBottomSheetDialog("earsType") }
        mBinding!!.tvEarsSize.setOnClickListener { showBottomSheetDialog("earsSize") }
        mBinding!!.tvLipsType.setOnClickListener { showBottomSheetDialog("lipsType") }
        mBinding!!.tvLipsColor.setOnClickListener { showBottomSheetDialog("lipsColor") }
        mBinding!!.tvFrontTeeth.setOnClickListener { showBottomSheetDialog("frontTeeth") }
        mBinding!!.tvSpectaclesType.setOnClickListener { showBottomSheetDialog("spectaclesType") }
        mBinding!!.tvSpectaclesColor.setOnClickListener { showBottomSheetDialog("spectaclesColor") }
    }

    private fun showBottomSheetDialog(type: String) {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_state, null)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val rvBottomSheet = view.findViewById<RecyclerView>(R.id.rvBottomSheet)
        val close = view.findViewById<TextView>(R.id.tvClose)

        close.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        // Define a variable for the selected list and TextView
        val selectedList: List<String>
        val selectedTextView: TextView

        // Initialize based on type
        when (type) {
            "hairLength" -> {
                selectedList = hairLength
                selectedTextView = mBinding!!.tvHairLength
            }

            "hairColor" -> {
                selectedList = hairColor
                selectedTextView = mBinding!!.tvHairColor
            }

            "eyeType" -> {
                selectedList = eyeType
                selectedTextView = mBinding!!.tvEyeType
            }

            "eyeColor" -> {
                selectedList = eyeColor
                selectedTextView = mBinding!!.tvEyeColor
            }

            "earsType" -> {
                selectedList = earsType
                selectedTextView = mBinding!!.tvEarsType
            }

            "earsSize" -> {
                selectedList = earsSize
                selectedTextView = mBinding!!.tvEarsSize
            }

            "lipsType" -> {
                selectedList = lipsType
                selectedTextView = mBinding!!.tvLipsType
            }

            "lipsColor" -> {
                selectedList = lipsColor
                selectedTextView = mBinding!!.tvLipsColor
            }

            "frontTeeth" -> {
                selectedList = frontTeeth
                selectedTextView = mBinding!!.tvFrontTeeth
            }

            "spectaclesType" -> {
                selectedList = spectaclesType
                selectedTextView = mBinding!!.tvSpectaclesType
            }

            "spectaclesColor" -> {
                selectedList = spectaclesColor
                selectedTextView = mBinding!!.tvSpectaclesColor
            }

            else -> return
        }

        // Set up the adapter
        stateAdapter = StateAdapter(selectedList, requireContext()) { selectedItem ->
            // Handle state item click
            selectedTextView.text = selectedItem
            selectedTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            bottomSheetDialog.dismiss()
        }

        rvBottomSheet.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvBottomSheet.adapter = stateAdapter
        bottomSheetDialog.setContentView(view)

        // Rotate drawable
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down)
        var rotatedDrawable = rotateDrawable(drawable, 180f)
        selectedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, rotatedDrawable, null)

        // Set a dismiss listener to reset the view visibility
        bottomSheetDialog.setOnDismissListener {
            rotatedDrawable = rotateDrawable(drawable, 0f)
            selectedTextView.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                rotatedDrawable,
                null
            )
        }

        // Show the bottom sheet
        bottomSheetDialog.show()
    }


    private fun rotateDrawable(drawable: Drawable?, angle: Float): Drawable? {
        drawable?.mutate() // Mutate the drawable to avoid affecting other instances

        val rotateDrawable = RotateDrawable()
        rotateDrawable.drawable = drawable
        rotateDrawable.fromDegrees = 0f
        rotateDrawable.toDegrees = angle
        rotateDrawable.level = 10000 // Needed to apply the rotation

        return rotateDrawable
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