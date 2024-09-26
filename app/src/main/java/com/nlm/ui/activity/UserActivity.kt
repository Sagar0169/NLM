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
                "Super Admin",
                "superadmin",
                "superadmin@silvertouch.com",
                "Admin",
                "superadmin","2023-12-27","Active"
            ),
            All_Users(
                "Dairy Division MP",
                "dairy.division.mp",
                "dairy.division.mp@gmail.com",
                "NA",
                "Dairy Division","2024-08-09","Active"),


        All_Users(
        "Angan Lal Nirala",
        "anganlalnirala",
        "anganlalnirala@hotmail.com",
        "NA",
        "National Level Monitor","2024-08-12","Active"),


        All_Users(
        "Col Param Vir Singh Sidhu",
        "pvsid09",
        "pvsid09@gmail.com",
        "NA",
        "National Level Monitor","2024-08-12","Active"),
        )

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