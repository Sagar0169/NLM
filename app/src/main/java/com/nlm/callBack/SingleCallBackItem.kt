package com.nlm.callBack

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
interface SizeItemCallBack {
    fun onClickItemSize(size: Int)
}