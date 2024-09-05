package com.nlm.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityListOfRgmIaBinding
import com.nlm.databinding.ActivitySemenStationBinding
import com.nlm.model.Bull_Mothers
import com.nlm.model.RGM_IA
import com.nlm.ui.adapter.Bull_Of_Mothers_Adapter
import com.nlm.ui.adapter.RGM_IA_Adapter
import com.nlm.utilities.BaseActivity

class List_of_RGM_IA : BaseActivity<ActivityListOfRgmIaBinding>() {
    private var mBinding: ActivityListOfRgmIaBinding? = null
    override val layoutId: Int
        get() = R.layout.activity_list_of_rgm_ia
    private lateinit var implementingAdapter: RGM_IA_Adapter
    private var layoutManager: LinearLayoutManager? = null
    private lateinit var nodalOfficerList: List<RGM_IA>
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction=ClickActions()
        nodalOfficerList = listOf(
            RGM_IA(
                "NA",
                "NA",
                "NA",
                "NA",
            ),
        )
        implementingAgency()

        mBinding!!.fabAddAgency.setOnClickListener{
            val intent = Intent(this@List_of_RGM_IA,RGM_State_Implementing_Agency::class.java).putExtra("isFrom",1)
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
        implementingAdapter = RGM_IA_Adapter(nodalOfficerList,1)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvArtificialInsemination.layoutManager = layoutManager
        mBinding!!.rvArtificialInsemination.adapter = implementingAdapter
    }
}