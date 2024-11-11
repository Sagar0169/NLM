package com.nlm.callBack

import androidx.fragment.app.Fragment
import com.nlm.model.IdAndDetails
import com.nlm.model.ImplementingAgencyAdvisoryCommittee
import com.nlm.model.ImplementingAgencyFundsReceived
import com.nlm.model.ImplementingAgencyProjectMonitoring

//interface SingleCallBackItem {
//    fun onClickItem(selectedItem: IdAndName)
//}
interface CallBackItemTypeIACompositionListEdit {
    fun onClickItem(selectedItem: IdAndDetails)
}
//interface CallBackIdType {
//    fun onClickItem(ID: Int?,position:Int)
//}

interface DeleteItemCallBack {
    fun onClickItem(documentId: Int,position:Int)
}
interface DialogCallback {
    fun onYes()

}
interface CallBackDeleteAtId {
    fun onClickItem(ID: Int?,position:Int)
}
interface SwitchFragmentCallBack {
    fun onClickItem(fragment:Fragment,Tab_id:Int)
}
interface AddItemCallBackProjectMonitoring {
    fun onClickItem2(list:MutableList<ImplementingAgencyProjectMonitoring>)
}
interface AddItemCallBackFundsRecieved {
    fun onClickItem(list:MutableList<ImplementingAgencyFundsReceived>)
}
interface OnBackSaveAsDraft {
    fun onSaveAsDraft()
}
interface OnNextButtonClickListener {
    fun onNextButtonClick()
    fun onNavigateToFirstFragment()
}