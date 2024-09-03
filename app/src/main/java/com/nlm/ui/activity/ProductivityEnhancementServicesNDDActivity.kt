package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityMilkProductMarketingNddBinding
import com.nlm.databinding.ActivityProductivityEnhancementServicesNddBinding
import com.nlm.model.MilkProductMarketing
import com.nlm.model.ProductivityEnhancementServices
import com.nlm.ui.adapter.ndd.MilkProductMarketingAdapter
import com.nlm.ui.adapter.ndd.ProductivityEnhancementServicesAdapter
import com.nlm.utilities.BaseActivity

class ProductivityEnhancementServicesNDDActivity : BaseActivity<ActivityProductivityEnhancementServicesNddBinding>() {
    private var mBinding: ActivityProductivityEnhancementServicesNddBinding? = null
    private lateinit var adapter: ProductivityEnhancementServicesAdapter
    private lateinit var list: List<ProductivityEnhancementServices>
    private var layoutManager: LinearLayoutManager? = null

    override val layoutId: Int
        get() = R.layout.activity_productivity_enhancement_services_ndd


    inner class ClickActions {
        fun backPress(view: View) {
            finish()
        }
    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        list = listOf(

            ProductivityEnhancementServices(
             "Samson Hendricks",
             "Karnal",
             "2600",
             "HARYANA",
             "AMBALA",
             "2024-08-29"
         )

        )
        implementingAgency()

        mBinding!!.fabAdd.setOnClickListener{
            val intent = Intent(this@ProductivityEnhancementServicesNDDActivity,AddProductivityEnhancementServices::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }

    private fun implementingAgency() {
        adapter = ProductivityEnhancementServicesAdapter(list)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.recyclerView.layoutManager = layoutManager
        mBinding!!.recyclerView.adapter = adapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}