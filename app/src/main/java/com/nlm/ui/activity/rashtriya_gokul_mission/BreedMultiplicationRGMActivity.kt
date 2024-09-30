package com.nlm.ui.activity.rashtriya_gokul_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityBreedMultiplicationRgmBinding
import com.nlm.model.BreedMultiplication
import com.nlm.model.MilkUnionVisit
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.rgm.BreedMultiplicationAdapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.PrefEntities
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView

class BreedMultiplicationRGMActivity : BaseActivity<ActivityBreedMultiplicationRgmBinding>() {
    private var mBinding: ActivityBreedMultiplicationRgmBinding? = null
    private lateinit var adapter: BreedMultiplicationAdapter
    private lateinit var list: List<BreedMultiplication>
    private var layoutManager: LinearLayoutManager? = null

    override val layoutId: Int
        get() = R.layout.activity_breed_multiplication_rgm


    inner class ClickActions {
        fun backPress(view: View) {
            finish()
        }
        fun filter(view: View) {
            val intent = Intent(
                this@BreedMultiplicationRGMActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 33)
            startActivity(intent)
        }
    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        if (Utility.getPreferenceString(this, AppConstants.ROLE_NAME)=="Super Admin") {
            mBinding!!.fabAdd.hideView()
        }
        list = listOf(


            BreedMultiplication(
                "Shri Sudhanshu Shekhar",
                "TAMIL NADU",
                "Sheila Benjamin",
                "Not filled",

                "2024-08-20"

            ),
 BreedMultiplication(
                "Shri Sudhanshu Shekhar",
                "TAMIL NADU",
                "Sheila Benjamin",
                "Not filled",

                "2024-08-20"

            ),


        )
        implementingAgency()

        mBinding!!.fabAdd.setOnClickListener{
            val intent = Intent(this@BreedMultiplicationRGMActivity, AddBreedMultiplication::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }

    private fun implementingAgency() {
        adapter = BreedMultiplicationAdapter(list,Utility.getPreferenceString(this, AppConstants.ROLE_NAME))
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.recyclerView.layoutManager = layoutManager
        mBinding!!.recyclerView.adapter = adapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}