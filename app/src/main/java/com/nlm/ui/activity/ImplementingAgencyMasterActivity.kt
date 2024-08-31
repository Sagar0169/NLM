package com.nlm.ui.activity

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityImplementingAgencyMasterBinding
import com.nlm.model.NodalOfficer
import com.nlm.ui.adapter.ImplementingAgencyAdapter
import com.nlm.utilities.BaseActivity

class ImplementingAgencyMasterActivity : BaseActivity<ActivityImplementingAgencyMasterBinding>() {
    private var mBinding: ActivityImplementingAgencyMasterBinding? = null
    private lateinit var implementingAdapter: ImplementingAgencyAdapter
    private lateinit var nodalOfficerList: List<NodalOfficer>
    private var layoutManager: LinearLayoutManager? = null

    override val layoutId: Int
        get() = R.layout.activity_implementing_agency_master


    inner class ClickActions {
        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        nodalOfficerList = listOf(

            NodalOfficer(
                "WEST BENGAL",
                "West Bengal Cooperative Milk Producers Federation Ltd.",
                "Dr.Swapnendu Mondal",
                "mail@benmilk.com",
                "2024-08-20","8882135865","AD/Manager"

                ),
            NodalOfficer(
                "UTTARAKHAND",
                "Uttaranchal Cooperative Dairy Federation Ltd.",
                "R. N Tiwari",
                "ucdfltd@gmail.com",
                "2024-08-20","8882135865","AD/Manager"

                ),
            NodalOfficer(
                "UTTAR PRADESH",
                "Pradeshik Cooperative Dairy Federation",
                "Naintara Dixit",
                "pcdfmsd@yahoo.co.in",
                "2024-08-20","8882135865","AD/Manager"

                ),
            NodalOfficer(
                "TELANGANA",
                "Telangana Dairy Development Cooperative Federation Ltd.",
                "N Lakshmi Manjusha",
                "projects.tgddcf@gmail.com",
                "2024-08-20","8882135865","AD/Manager"
            ),
            NodalOfficer(
                "TAMIL NADU",
                "Tamil Nadu Co-operative Milk Producers’ Federation Ltd.",
                "Dr.G. Anbumani",
                "anbumaniaavin1970@gmail.com",
                "2024-08-20","null","null"
            ),
            NodalOfficer(
                "RAJASTHAN",
                "Rajasthan Cooperative Dairy Federation ltd.",
                "Rajesh Kumar Sanganeria",
                "rajeshsanganeria5@yahoo.co.in",
                "2024-08-20","null","null"
            ),
            NodalOfficer(
                "PUNJAB",
                "The Punjab State Cooperative Milk Producers Federation Ltd.",
                "Amrit Pal Singh",
                "gmpln.milkfed@verka.coop",
                "2024-08-20","null","null"
            ),
            NodalOfficer(
                "PUDUCHERRY",
                "The Pondicherry Cooperative Milk Producers Union Ltd.",
                "Imayavaramban Thennavan",
                "ponlaitmd@gmail.com",
                "2024-08-20","null","null"
            ),
            NodalOfficer(
                "ODISHA",
                "Orissa State Cooperative Milk Producers Federation Ltd.",
                "Biswaranjan Tripathy",
                "biswaranjantripathy@ymail.com",
                "2024-08-20","null","null"
            ),
            NodalOfficer(
                "MAHARASHTRA",
                "Maharashtra Rajya Sahakari Dugh Mahasangh Maryadit",
                "Dinesh Sawant",
                "foah.mahanand@gmail.com",
                "2024-08-20","null","null"
            ),
            NodalOfficer(
                "MADHYA PRADESH",
                "Madhya Pradesh State Cooperative Dairy Federation",
                "Aseem Nigam",
                "aseemnigam@gmail.com",
                "2024-08-20","null","null"
            ),
            NodalOfficer(
                "KERALA",
                "Kerala Cooperative Milk Marketing Federation ltd.",
                "Er.Manoj Bilssey",
                "projects@milma.com",
                "2024-08-20","null","null"
            ),
            NodalOfficer(
                "KARNATAKA",
                "The Karnataka Cooperative Milk Producers' Federation ltd.",
                "Dr.Pradeep Gouda",
                "ahykmf@yahoo.com",
                "2024-08-20","null","null"
            ),
        )
        implementingAgency()

        mBinding!!.fabAddAgency.setOnClickListener{
            val intent = Intent(this@ImplementingAgencyMasterActivity,NodalOfficerDetailActivity::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }

    private fun implementingAgency() {
        implementingAdapter = ImplementingAgencyAdapter(nodalOfficerList)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.rvImplementingaegency.layoutManager = layoutManager
        mBinding!!.rvImplementingaegency.adapter = implementingAdapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
}