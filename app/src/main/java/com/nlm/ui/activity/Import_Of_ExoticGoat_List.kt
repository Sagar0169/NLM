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
import com.nlm.databinding.ActivityImportOfExoticGoatListBinding
import com.nlm.model.ArtificialInsemenation
import com.nlm.model.ImportOfGoat
import com.nlm.ui.adapter.Artificial_Insemination_adapter
import com.nlm.ui.adapter.Import_Of_Goat_Adapter
import com.nlm.utilities.BaseActivity

class Import_Of_ExoticGoat_List : BaseActivity<ActivityImportOfExoticGoatListBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_import_of_exotic_goat_list
    private var mBinding: ActivityImportOfExoticGoatListBinding? = null
    private lateinit var implementingAdapter: Import_Of_Goat_Adapter
    private var layoutManager: LinearLayoutManager? = null
    private lateinit var nodalOfficerList: List<ImportOfGoat>
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction=ClickActions()
        nodalOfficerList = listOf(
            ImportOfGoat(
                "805",
                "2024-08-21",
            ),
            ImportOfGoat(
                "154",
                "1970-01-01",
            ),
            ImportOfGoat(
                "643",
                "2024-08-13",
            ),
            )
        implementingAgency()

        mBinding!!.fabAddAgency.setOnClickListener{
            val intent = Intent(this@Import_Of_ExoticGoat_List,Artificial_Insemination::class.java).putExtra("isFrom",1)
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
        implementingAdapter = Import_Of_Goat_Adapter(nodalOfficerList)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvArtificialInsemination.layoutManager = layoutManager
        mBinding!!.rvArtificialInsemination.adapter = implementingAdapter
    }
}