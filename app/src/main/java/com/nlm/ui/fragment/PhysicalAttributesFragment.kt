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
import com.nlm.ui.adapter.RelationshipAdapter
import com.nlm.ui.adapter.StateAdapter
import com.nlm.utilities.BaseFragment
import nlm.R
import nlm.databinding.FragmentPhysicalAttributesBinding

class PhysicalAttributesFragment : BaseFragment<FragmentPhysicalAttributesBinding>(){
    private var mBinding: FragmentPhysicalAttributesBinding?=null
    private var isSelected: Boolean? = false
    private lateinit var relationAdapter: RelationshipAdapter
    private var layoutManager: LinearLayoutManager? = null
    private var listener: OnNextButtonClickListener? = null
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var stateAdapter: StateAdapter

    interface OnNextButtonClickListener {
        fun onNextButtonClick()
    }

    private val Complexion = listOf(
        "Fair", "Wheatish", "Very Fair",
        "Dark",
    )
    private val Build = listOf(
        "Heavy", "Normal", "Thin",
    )
    private val Neck_Type = listOf(
        "Thin", "Thick", "Normal", "Short", "long"
    )
    private val Top_Wear = listOf(
        "Shirt", "Top", "Vest", "Kurta", "Kurti", "Nothing", "Saree", "Half Saree", "Frock"
    )
    private val Top_Wear_Color = listOf(
        "Black",
        "Blue",
        "Brown",
        "Yellow",
        "White",
        "Nothing",
        "Red",
        "Voilet",
        "Purple",
        "Green",
        "Cyan",
        "Magenta",
        "Orange",
        "Pink"
    )
    private val Bottom_Wear = listOf(
        "Capry",
        "Lehanga",
        "Dhoti",
        "Skirt",
        "Frock",
        "Nicker",
        "Half Saree",
        "Saree",
        "School Uniform",
        "Trouser/Full Pants",
        "Jeggings",
        "Jeans",
        "Half Pants",
        "Lower",
        "Cargo"
    )
    private val Bottom_Wear_Color = listOf(
        "Black",
        "Blue",
        "Brown",
        "Yellow",
        "White",
        "Nothing",
        "Red",
        "Voilet",
        "Purple",
        "Green",
        "Cyan",
        "Magenta",
        "Orange",
        "Pink",
        "Others"
    )
    private val Foot_Wear = listOf(
        "Shoes", "Slippers", "Belly", "Sandals", "BareFoot"
    )


    override val layoutId: Int
        get() = R.layout.fragment_physical_attributes

    override fun init() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()

        mBinding!!.tvComplexion.setOnClickListener { showBottomSheetDialog("Complexion") }
        mBinding!!.tvBuild.setOnClickListener { showBottomSheetDialog("Build") }
        mBinding!!.tvNeckType.setOnClickListener { showBottomSheetDialog("NeckType") }
        mBinding!!.tvTopWear.setOnClickListener { showBottomSheetDialog("TopWear") }
        mBinding!!.tvTopWearColor.setOnClickListener { showBottomSheetDialog("TopWearColor") }
        mBinding!!.tvBottomWear.setOnClickListener { showBottomSheetDialog("BottomWear") }
        mBinding!!.tvBottomWearColor.setOnClickListener { showBottomSheetDialog("BottomWearColor") }
        mBinding!!.tvFootWear.setOnClickListener { showBottomSheetDialog("FootWear") }
        mBinding!!.tvFootWearColor.setOnClickListener { showBottomSheetDialog("FootWearColor") }


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
            "Complexion" -> {
                selectedList = Complexion
                selectedTextView = mBinding!!.tvComplexion
            }

            "Build" -> {
                selectedList = Build
                selectedTextView = mBinding!!.tvBuild
            }

            "NeckType" -> {
                selectedList = Neck_Type
                selectedTextView = mBinding!!.tvNeckType
            }

            "TopWear" -> {
                selectedList = Top_Wear
                selectedTextView = mBinding!!.tvTopWear
            }

            "TopWearColor" -> {
                selectedList = Top_Wear_Color
                selectedTextView = mBinding!!.tvTopWearColor
            }

            "BottomWear" -> {
                selectedList = Bottom_Wear
                selectedTextView = mBinding!!.tvBottomWear
            }

            "BottomWearColor" -> {
                selectedList = Bottom_Wear_Color
                selectedTextView = mBinding!!.tvBottomWearColor
            }

            "FootWear" -> {
                selectedList = Foot_Wear
                selectedTextView = mBinding!!.tvFootWear
            }

            "FootWearColor" -> {
                selectedList = Bottom_Wear_Color
                selectedTextView = mBinding!!.tvFootWearColor
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