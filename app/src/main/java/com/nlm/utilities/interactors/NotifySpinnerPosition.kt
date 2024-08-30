package com.knowyourtemple.businesslogic.interactors

class NotifySpinnerPosition {
    private var viewFlag = 0
    private var notifyPosition: NotifyPosition? = null
    private var spinnerPosition = 0

    fun setSpinnerPosition(spinnerPosition: Int) {
        this.spinnerPosition = spinnerPosition
    }

    fun setSpinnerPositionNotify(spinnerPosition: Int) {
        if (notifyPosition != null) {
            notifyPosition!!.onChangePosition(spinnerPosition, viewFlag)
        }
    }

    fun setSpinnerPositionNotifyAll(spinnerPosition: Int) {
        if (this.spinnerPosition == spinnerPosition) return
        this.spinnerPosition = spinnerPosition
        if (notifyPosition != null) {
            notifyPosition!!.onChangePosition(spinnerPosition, viewFlag)
        }
    }

    fun getSpinnerPosition(): Int {
        return spinnerPosition
    }

    fun setNotifyPositionListener(notifyPosition: NotifyPosition?, viewFlag: Int) {
        this.notifyPosition = notifyPosition
        this.viewFlag = viewFlag
    }

    interface NotifyPosition {
        fun onChangePosition(position: Int, viewFlag: Int)
    }
}