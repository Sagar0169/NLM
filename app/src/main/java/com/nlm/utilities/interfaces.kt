package com.nlm.utilities


interface OnItemClickListenerTwoArguments{
   fun onItemPositionClicked(position:Int,id:Int)
}

interface OnItemClickListenerThreeArguments{
   fun onItemPositionClicked(amount:String,name:String,id:String)
}

interface OnAuthorizedListener{
   fun onAuthorized(from:String,id:String,name:String,email:String,gender:String,url:String)
}

interface DialogCallback {
   fun onYes()

}

interface DialogCallbackSub {
   fun onYes()
}

interface BlockUnblockEventListener{
//  fun callUnblockApi(userData: UnblockDataX)
}

interface DialogCallbackCameraGallery {
   fun onYes(from: String)
}
interface OnSwipeListener{
   fun onAccept()
   fun onReject()
}

interface DbCallback {
   fun onSuccess()
   fun onFail(e: Exception)
}

