package mission.vatsalya.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mission.vatsalya.R
import mission.vatsalya.databinding.ActivityEditProfileBinding
import mission.vatsalya.utilities.BaseActivity
import mission.vatsalya.utilities.showView
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

        fun save(view: View) {
            Toast.makeText(
                this@EditProfile,
                "Details Saved Successfully.",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(this@EditProfile,DashboardActivity::class.java)
            startActivity(intent)
        }

        fun uploadDocument(view: View) {

        }

        fun edit(view: View) {
            mBinding?.tvEdit?.setOnClickListener {
                Toast.makeText(
                    this@EditProfile,
                    "You can now edit highlighted fields.",
                    Toast.LENGTH_SHORT
                ).show()
                mBinding?.tvWelcome?.text = "Edit Profile"

                mBinding?.etName?.apply {
                    isEnabled = true
                    requestFocus()
                    setSelection(text.length) // Move cursor to the end of the text
                }
                mBinding?.etEmail?.apply {
                    isEnabled = true
                    setSelection(text.length) // Move cursor to the end of the text
                }
                mBinding?.etphoneNumber?.apply {
                    isEnabled = true
                    setSelection(text.length) // Move cursor to the end of the text
                }

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(mBinding?.etName, InputMethodManager.SHOW_IMPLICIT)

                mBinding?.rbMale?.isEnabled = true
                mBinding?.rbFemale?.isEnabled = true
                mBinding!!.tvSendOtp.showView()
                mBinding?.etName?.background =
                    ContextCompat.getDrawable(this@EditProfile, R.drawable.curve_all_corner_black)
                mBinding?.etEmail?.background =
                    ContextCompat.getDrawable(this@EditProfile, R.drawable.curve_all_corner_black)
                mBinding?.etphoneNumber?.background =
                    ContextCompat.getDrawable(this@EditProfile, R.drawable.curve_all_corner_black)

            }
        }

        fun dob(view: View) {

        }

        fun profileCamera(view: View) {

        }

        fun uploadProfile(view: View) {

        }

        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}