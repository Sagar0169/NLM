package mission.vatsalya.ui.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import mission.vatsalya.R
import mission.vatsalya.databinding.ActivityOtpBinding
import mission.vatsalya.utilities.BaseActivity
import mission.vatsalya.utilities.hideView
import mission.vatsalya.utilities.showView
import mission.vatsalya.utilities.toast
import java.util.concurrent.TimeUnit

class OtpActivity : BaseActivity<ActivityOtpBinding>() {
    private var mBinding: ActivityOtpBinding? = null
    private var timer: CountDownTimer? = null
    private var millisFinished: Long = 0
    private lateinit var editTexts: Array<EditText>
    override val layoutId: Int
        get() = R.layout.activity_otp


    override fun initView() {
        mBinding = viewDataBinding
        mBinding?.clickAction = ClickActions()
        editTexts = arrayOf(
            mBinding?.etOtp1!!,
            mBinding?.etOtp2!!,
            mBinding?.etOtp3!!,
            mBinding?.etOtp4!!,
            mBinding?.etOtp5!!,
            mBinding?.etOtp6!!
        )
        setupOTPInput()

    }

    override fun setVariables() {

    }

    override fun setObservers() {

    }

    inner class ClickActions {
        fun confirm(view: View) {
            val typedOTP =
                (mBinding?.etOtp1?.text.toString() +
                        mBinding?.etOtp2?.text.toString() +
                        mBinding?.etOtp3?.text.toString() +
                        mBinding?.etOtp4?.text.toString() +
                        mBinding?.etOtp5?.text.toString() +
                        mBinding?.etOtp6?.text.toString())

            if (typedOTP.isNotEmpty()) {
                if (typedOTP.length == 6) {
//                    otpVerify(typedOTP)

                } else {
                    toast(getString(R.string.please_enter_correct_otp))
                }
            } else {
                toast(getString(R.string.please_enter_otp))
            }
        }

        fun resendOtp(view: View) {
            showTimer(mBinding?.tvTimer, 60000, mBinding?.tvResentOtp)
//            otpResend()
        }

        fun backPress(view: View) {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun showTimer(tvTimer: TextView?, remainingTime: Long?, tvResendOtp: TextView?) {
        tvResendOtp?.isEnabled = false
        timer = object : CountDownTimer(remainingTime ?: 0, 1000) {
            @SuppressLint("SetTextI18n", "DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                tvTimer?.showView()
                tvResendOtp?.isEnabled = false
                tvTimer?.text = "00:" + String.format(
                    "%02d",
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                )
                millisFinished = millisUntilFinished
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onFinish() {
                tvTimer?.hideView()
                tvResendOtp?.isEnabled = true
            }
        }
        timer!!.start()
    }

    private fun setupOTPInput() {
        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && i < editTexts.size - 1) {
                        editTexts[i + 1].requestFocus()
                    } else if (s?.isEmpty() == true && i > 0) {
                        editTexts[i - 1].requestFocus()
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Not needed
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Not needed
                }
            })

            editTexts[i].setOnKeyListener { _, keyCode, _ ->
                if (keyCode == 67 && i > 0 && editTexts[i].text.isEmpty()) {
                    editTexts[i - 1].requestFocus()
                }
                false
            }
        }
    }
}