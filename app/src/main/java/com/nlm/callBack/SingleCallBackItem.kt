package com.nlm.callBack

import com.nlm.model.ImplementingAgencyAdvisoryCommittee
import com.nlm.model.ImplementingAgencyFundsReceived
import com.nlm.model.ImplementingAgencyProjectMonitoring

//interface SingleCallBackItem {
//    fun onClickItem(selectedItem: IdAndName)
//}
//interface CallBackItemType {
//    fun onClickItem(selectedItem: IdAndName)
//}
//interface CallBackIdType {
//    fun onClickItem(ID: Int?,position:Int)
//}

interface DeleteItemCallBack {
    fun onClickItem(documentId: Int,position:Int)
}
interface AddItemCallBack {
    fun onClickItem(list:MutableList<ImplementingAgencyAdvisoryCommittee>)
}
interface AddItemCallBackProjectMonitoring {
    fun onClickItem2(list:MutableList<ImplementingAgencyProjectMonitoring>)
}
interface AddItemCallBackFundsRecieved {
    fun onClickItem(list:MutableList<ImplementingAgencyFundsReceived>)
}
interface SizeItemCallBack {
    fun onClickItemSize(size: Int)
}