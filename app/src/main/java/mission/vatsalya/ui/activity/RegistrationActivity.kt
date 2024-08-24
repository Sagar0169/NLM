package mission.vatsalya.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import mission.vatsalya.R
import mission.vatsalya.databinding.ActivityOtpBinding
import mission.vatsalya.databinding.ActivityRegistrationBinding
import mission.vatsalya.ui.adapter.DistrictAdapter
import mission.vatsalya.ui.adapter.StateAdapter
import mission.vatsalya.utilities.BaseActivity
import mission.vatsalya.utilities.toast

class RegistrationActivity : BaseActivity<ActivityRegistrationBinding>() {
    private var mBinding: ActivityRegistrationBinding? = null
    private var isSelected: Boolean? = false
    private var isSelectedDistrict: Boolean? = false
    private lateinit var stateAdapter: StateAdapter
    private lateinit var districtAdapter: DistrictAdapter
    private var layoutManager: LinearLayoutManager? = null

    override val layoutId: Int
        get() = R.layout.activity_registration

    private val stateList = listOf(
        "Alabama", "Alaska", "Arizona", "Arkansas", "California",
        "Colorado", "Connecticut", "Delaware", "Florida", "Georgia"
    )

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        callAdapter()
        callAdapterDistrict()


        mBinding!!.rlDistrict.setOnClickListener {
            if (isSelectedDistrict == false) {
                isSelectedDistrict = true
                mBinding!!.ivArrowDownDistrict.isVisible = true
                mBinding?.ivArrowUpDistrict?.isVisible = false
                mBinding?.llDistrict?.isVisible = true

            } else {
                isSelectedDistrict = false
                mBinding?.ivArrowDownDistrict?.isVisible = false
                mBinding?.ivArrowUpDistrict?.isVisible = true
                mBinding?.llDistrict?.isVisible = false

            }

        }
        mBinding!!.rlSelectCategory.setOnClickListener {
            if (isSelected == false) {
                isSelected = true
                mBinding!!.ivArrowDown.isVisible = true
                mBinding?.ivArrowUp?.isVisible = false
                mBinding?.llCategory?.isVisible = true

            } else {
                isSelected = false
                mBinding?.ivArrowDown?.isVisible = false
                mBinding?.ivArrowUp?.isVisible = true
                mBinding?.llCategory?.isVisible = false

            }

        }
        mBinding!!.ivArrowUp.setOnClickListener {
            isSelected = true
            mBinding?.ivArrowDown?.isVisible = true
            mBinding?.ivArrowUp?.isVisible = false
            mBinding?.llCategory?.isVisible = true
        }
        mBinding!!.ivArrowDown.setOnClickListener {
            isSelected = false
            mBinding?.ivArrowDown?.isVisible = false
            mBinding?.ivArrowUp?.isVisible = true
            mBinding?.llCategory?.isVisible = false
        }


        mBinding!!.ivArrowUpDistrict.setOnClickListener {
            isSelected = true
            mBinding?.ivArrowDownDistrict?.isVisible = true
            mBinding?.ivArrowUpDistrict?.isVisible = false
            mBinding?.llDistrict?.isVisible = true
        }
        mBinding!!.ivArrowDownDistrict.setOnClickListener {
            isSelectedDistrict = false
            mBinding?.ivArrowDownDistrict?.isVisible = false
            mBinding?.ivArrowUpDistrict?.isVisible = true
            mBinding?.llDistrict?.isVisible = false
        }

    }


    fun onClickItem(selectedItem: String) {
        mBinding!!.tvSelectCategory.text = selectedItem
        mBinding?.llCategory?.isVisible = false
        mBinding?.ivArrowDown?.isVisible = false
        mBinding?.ivArrowUp?.isVisible = true
    }

    fun onClickItemDistrict(selectedItem: String) {
        mBinding!!.tvSelectDistrict.text = selectedItem
        mBinding?.llDistrict?.isVisible = false
        mBinding?.ivArrowDownDistrict?.isVisible = false
        mBinding?.ivArrowUpDistrict?.isVisible = true
    }

    private fun callAdapter() {
        stateAdapter = StateAdapter(stateList, this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvCategory.layoutManager = layoutManager
        mBinding!!.rvCategory.adapter = stateAdapter
    }

    private fun callAdapterDistrict() {
        districtAdapter = DistrictAdapter(stateList, this,this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvDistrict.layoutManager = layoutManager
        mBinding!!.rvDistrict.adapter = districtAdapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }

    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun login(view: View) {
            val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        fun otp(view: View) {
            val intent = Intent(this@RegistrationActivity, OtpActivity::class.java)
            startActivity(intent)
        }
    }

}