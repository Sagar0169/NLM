package com.nlm.ui.activity.national_livestock_mission

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityArtificialInseminationListBinding
import com.nlm.model.ArtificialInsemenation
import com.nlm.ui.adapter.rgm.Artificial_Insemination_adapter
import com.nlm.utilities.AppConstants
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.PrefEntities
import com.nlm.utilities.Utility
import com.nlm.utilities.hideView

class ArtificialInseminationList : BaseActivity<ActivityArtificialInseminationListBinding> (){
    private var mBinding: ActivityArtificialInseminationListBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_artificial_insemination_list
    private lateinit var implementingAdapter: Artificial_Insemination_adapter

    private lateinit var nodalOfficerList: List<ArtificialInsemenation>
    private var layoutManager: LinearLayoutManager? = null
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction=ClickActions()
        if(Utility.getPreferenceString(this, AppConstants.ROLE_NAME)=="Super Admin")
        {
            mBinding!!.fabAddAgency.hideView()
        }
        nodalOfficerList = listOf(
            ArtificialInsemenation(
                "BIHAR",
                "2024-08-23",
                "BHAGALPUR",
                "test",
                "234242342432","1977"

            ),
            ArtificialInsemenation(
                "HARYANA",
                "2024-08-23",
                "GURUGRAM",
                "test",
                "895353543535453","2000"

            ),
            ArtificialInsemenation(
                "N/A",
                "2024-08-21",
                "N/A",
                "test",
                "9996543218","2017"

            ),
            ArtificialInsemenation(
                "N/A",
                "2024-08-20",
                "N/A",
                "",
                "9990157283","1947"

            ),
            ArtificialInsemenation(
                "N/A",
                "1970-01-01",
                "N/A",
                "",
                "9990157283","2001"

            ),

            )
        implementingAgency()

        mBinding!!.fabAddAgency.setOnClickListener{
            val intent = Intent(this@ArtificialInseminationList, ArtificialInseminationForms::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }

    inner class ClickActions {
        fun backPress(view: View){
            onBackPressed()
        }
    }
    private fun implementingAgency() {
        implementingAdapter = Artificial_Insemination_adapter(nodalOfficerList,Utility.getPreferenceString(this, AppConstants.ROLE_NAME))
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvArtificialInsemination.layoutManager = layoutManager
        mBinding!!.rvArtificialInsemination.adapter = implementingAdapter
    }
}