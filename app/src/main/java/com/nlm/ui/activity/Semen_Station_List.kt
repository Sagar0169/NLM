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
import com.nlm.databinding.ActivityBullOfMothersListBinding
import com.nlm.databinding.ActivitySemenStationListBinding
import com.nlm.model.Bull_Mothers
import com.nlm.ui.adapter.Bull_Of_Mothers_Adapter
import com.nlm.utilities.BaseActivity
import com.nlm.utilities.BaseFragment

class Semen_Station_List : BaseActivity<ActivitySemenStationListBinding>(){
    private var mBinding: ActivitySemenStationListBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_semen_station_list

    private lateinit var implementingAdapter: Bull_Of_Mothers_Adapter
    private var layoutManager: LinearLayoutManager? = null
    private lateinit var nodalOfficerList: List<Bull_Mothers>

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction=ClickActions()
        nodalOfficerList = listOf(
            Bull_Mothers(
                "LADAKH",
                "Perspiciatis esse nihil ullam dolor sit duis velit tempora occaecat cupiditate deserunt dolorem est a esse",
                "Submitted",
                "04-09-2024",
                "Done"
            ),
            Bull_Mothers(
                "UTTAR PRADESH",
                "Eiusmod aut ea soluta iusto quidem",
                "Submitted",
                "03-09-2024",
                "Done"),

        )
        implementingAgency()

        mBinding!!.fabAddAgency.setOnClickListener{
            val intent = Intent(this@Semen_Station_List,SemenStation::class.java).putExtra("isFrom",1)
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
        fun filter(view: View) {
            val intent = Intent(
                this@Semen_Station_List,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 34)
            startActivity(intent)
        }
    }
    private fun implementingAgency() {
        implementingAdapter = Bull_Of_Mothers_Adapter(nodalOfficerList,0)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvArtificialInsemination.layoutManager = layoutManager
        mBinding!!.rvArtificialInsemination.adapter = implementingAdapter
    }
}