package mission.vatsalya.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mission.vatsalya.R
import mission.vatsalya.databinding.ActivityEditProfileBinding
import mission.vatsalya.utilities.BaseActivity
import okhttp3.MultipartBody

class EditProfile() : BaseActivity<ActivityEditProfileBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_edit_profile
    private var mBinding: ActivityEditProfileBinding? = null

    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }
    inner class ClickActions {

        fun save(view: View){

        }
        fun uploadDocument(view: View){

        }
        fun edit(view: View){

        }
        fun dob(view: View){

        }
        fun profileCamera(view: View){

        }

        fun uploadProfile(view: View){

        }

        fun backPress(view: View){
            onBackPressedDispatcher.onBackPressed()
        }
    }
}