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
import com.nlm.databinding.ActivityArtificialInseminationListBinding
import com.nlm.databinding.ActivityStateSemenBankListBinding
import com.nlm.model.ArtificialInsemenation
import com.nlm.model.OnlyCreatedNlm
import com.nlm.ui.adapter.Artificial_Insemination_adapter
import com.nlm.ui.adapter.NSLP_IA_Adapter
import com.nlm.utilities.BaseActivity

class Artificial_Insemination_List : BaseActivity<ActivityArtificialInseminationListBinding> (){
    private var mBinding: ActivityArtificialInseminationListBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_artificial_insemination_list
    private lateinit var implementingAdapter: Artificial_Insemination_adapter

    private lateinit var nodalOfficerList: List<ArtificialInsemenation>
    private var layoutManager: LinearLayoutManager? = null
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction=ClickActions()
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
            val intent = Intent(this@Artificial_Insemination_List,Artificial_Insemination::class.java).putExtra("isFrom",1)
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
        implementingAdapter = Artificial_Insemination_adapter(nodalOfficerList)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvArtificialInsemination.layoutManager = layoutManager
        mBinding!!.rvArtificialInsemination.adapter = implementingAdapter
    }
}