package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityAscadBinding
import com.nlm.databinding.ActivityMobileVeterinaryBinding
import com.nlm.databinding.ActivityVaccinationProgrammerBinding
import com.nlm.model.OnlyCreated
import com.nlm.ui.adapter.OnlyCreatedAdapter
import com.nlm.utilities.BaseActivity

class AscadActivity : BaseActivity<ActivityAscadBinding>() {
    private var mBinding: ActivityAscadBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_ascad


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }

        fun state(view: View) {
            val intent = Intent(
                this@AscadActivity,
                StateMobileVeterinaryActivity::class.java
            ).putExtra("isFrom", 8)
            startActivity(intent)
        }

        fun district(view: View) {
            val intent = Intent(
                this@AscadActivity,
                StateMobileVeterinaryActivity::class.java
            ).putExtra("isFrom", 9)
            startActivity(intent)
        }

    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()


    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}