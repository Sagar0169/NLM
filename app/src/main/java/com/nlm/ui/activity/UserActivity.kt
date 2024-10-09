package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityUserBinding
import com.nlm.model.All_Users
import com.nlm.ui.adapter.ALL_Users_Adapter
import com.nlm.utilities.BaseActivity

class UserActivity : BaseActivity<ActivityUserBinding>() {

    private var mBinding: ActivityUserBinding? = null

    override val layoutId: Int
        get() = R.layout.activity_user
    private lateinit var implementingAdapter: ALL_Users_Adapter
    private var layoutManager: LinearLayoutManager? = null
    private lateinit var nodalOfficerList: List<All_Users>

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction=ClickActions()
        nodalOfficerList = listOf(
            All_Users(
                "NA",
                "NA",
                "NA",
                "NA",
                "NA",
                "NA",
                "NA",
                "NA",
            ),)


        implementingAgency()

        mBinding!!.fabAddAgency.setOnClickListener{
            val intent = Intent(this@UserActivity,UserFormActivity::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }

    inner class ClickActions {
        fun backPress(view: View){
            onBackPressedDispatcher.onBackPressed()
        }
    }
    private fun implementingAgency() {
        implementingAdapter = ALL_Users_Adapter(nodalOfficerList,0)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvArtificialInsemination.layoutManager = layoutManager
        mBinding!!.rvArtificialInsemination.adapter = implementingAdapter
    }
}