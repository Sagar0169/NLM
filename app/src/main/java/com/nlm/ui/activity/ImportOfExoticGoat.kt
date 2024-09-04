package com.nlm.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nlm.R
import com.nlm.databinding.ActivityArtificialInseminationBinding
import com.nlm.databinding.ActivityImportOfExoticGoatBinding
import com.nlm.utilities.BaseActivity

class ImportOfExoticGoat : BaseActivity<ActivityImportOfExoticGoatBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_import_of_exotic_goat
    private var mBinding: ActivityImportOfExoticGoatBinding? = null
    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {

        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}