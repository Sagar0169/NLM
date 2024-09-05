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
import com.nlm.databinding.ActivityBullMotherFarmsBinding
import com.nlm.databinding.ActivityBullOfMothersListBinding
import com.nlm.model.Bull_Mothers
import com.nlm.model.ImportOfGoat
import com.nlm.ui.adapter.Bull_Of_Mothers_Adapter
import com.nlm.ui.adapter.Import_Of_Goat_Adapter
import com.nlm.utilities.BaseActivity

class Bull_Of_Mothers_List : BaseActivity<ActivityBullOfMothersListBinding>() {
    private var mBinding: ActivityBullOfMothersListBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_bull_of_mothers_list
    private lateinit var implementingAdapter: Bull_Of_Mothers_Adapter
    private var layoutManager: LinearLayoutManager? = null
    private lateinit var nodalOfficerList: List<Bull_Mothers>
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction=ClickActions()
        nodalOfficerList = listOf(
            Bull_Mothers(
                "GOA",
                "Ut incididunt corporis et dolore",
                "Submitted",
                "04-09-2024",
                ""
            ),
            Bull_Mothers(
                "MEGHALAYA",
                "Et cupidatat eiusmod magna quaerat quia cumque tempora veniam reprehenderit distinctio Enim quis fugiat est est soluta nihil fugit qui",
                "Submitted",
                "03-09-2024",
                ""),
            Bull_Mothers(
                "PUNJAB",
                "Voluptatum culpa laboriosam magnam nobis non quasi amet voluptate in nisi quisquam rem ut ab",
                "Submitted",
                "03-09-2024",
                ""
            ),
        )
        implementingAgency()

        mBinding!!.fabAddAgency.setOnClickListener{
            val intent = Intent(this@Bull_Of_Mothers_List,Bull_Mother_Farms::class.java).putExtra("isFrom",1)
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
        implementingAdapter = Bull_Of_Mothers_Adapter(nodalOfficerList,1)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvArtificialInsemination.layoutManager = layoutManager
        mBinding!!.rvArtificialInsemination.adapter = implementingAdapter
    }
}