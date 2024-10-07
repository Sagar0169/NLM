package com.nlm.ui.activity.national_dairy_development

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nlm.R
import com.nlm.databinding.ActivityDcsCenterVisitNddBinding
import com.nlm.model.DcsCenterVisit
import com.nlm.ui.activity.FilterStateActivity
import com.nlm.ui.adapter.ndd.DCSCenterVisitAdapter
import com.nlm.utilities.BaseActivity

class DCSCenterVisitNDDActivity : BaseActivity<ActivityDcsCenterVisitNddBinding>() {
    private var mBinding: ActivityDcsCenterVisitNddBinding? = null
    private lateinit var adapter: DCSCenterVisitAdapter
    private lateinit var list: List<DcsCenterVisit>
    private var layoutManager: LinearLayoutManager? = null

    override val layoutId: Int
        get() = R.layout.activity_dcs_center_visit_ndd


    inner class ClickActions {
        fun backPress(view: View) {
            finish()
        }
        fun filter(view: View) {
            val intent = Intent(
                this@DCSCenterVisitNDDActivity,
                FilterStateActivity::class.java
            ).putExtra("isFrom", 27)
            startActivity(intent)
        }
    }


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        list = listOf(

            DcsCenterVisit(
                "8",
                "19-Nov-1992",
                "Jason Padilla",
                "MADHYA PRADESH",
                "ANUPPUR",
                "2024-08-20"

                ),
            	   DcsCenterVisit(
                "8",
                "19-Nov-1992",
                "Jason Padilla",
                "MADHYA PRADESH",
                "ANUPPUR",
                "2024-08-20"

                ),
            	   DcsCenterVisit(
                "8",
                "19-Nov-1992",
                "Jason Padilla",
                "MADHYA PRADESH",
                "ANUPPUR",
                "2024-08-20"

                ),
            	   DcsCenterVisit(
                "8",
                "19-Nov-1992",
                "Jason Padilla",
                "MADHYA PRADESH",
                "ANUPPUR",
                "2024-08-20"

                ),
            	   DcsCenterVisit(
                "8",
                "19-Nov-1992",
                "Jason Padilla",
                "MADHYA PRADESH",
                "ANUPPUR",
                "2024-08-20"

                ),
            	   DcsCenterVisit(
                "8",
                "19-Nov-1992",
                "Jason Padilla",
                "MADHYA PRADESH",
                "ANUPPUR",
                "2024-08-20"

                ),
            	   DcsCenterVisit(
                "8",
                "19-Nov-1992",
                "Jason Padilla",
                "MADHYA PRADESH",
                "ANUPPUR",
                "2024-08-20"

                ),


        )
        implementingAgency()
//        setupWebView()
        mBinding!!.fabAdd.setOnClickListener{
            val intent = Intent(this@DCSCenterVisitNDDActivity, AddDCSCenterVisit::class.java).putExtra("isFrom",1)
            startActivity(intent)
        }
    }

    private fun implementingAgency() {
        adapter = DCSCenterVisitAdapter(list)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.recyclerView.layoutManager = layoutManager
        mBinding!!.recyclerView.adapter = adapter
    }

    override fun setVariables() {
    }

    override fun setObservers() {
    }
//    override fun onBackPressed() {
//        if (mBinding?.webView?.canGoBack() == true) {
//            mBinding?.webView?.goBack()
//        } else {
//            super.onBackPressed()
//        }
//    }
//    private fun setupWebView() {
//        // Enable JavaScript (if needed)
//        mBinding?.webView?.settings?.javaScriptEnabled = true
//
//        // Set WebViewClient to open URLs within the WebView
//        mBinding?.webView?.webViewClient = WebViewClient()
//
//        // Load a URL in the WebView
//        mBinding?.webView?.loadUrl("http://134.209.222.136/nlm/admin/dairy-plant-visit-report/add")
//    }
}