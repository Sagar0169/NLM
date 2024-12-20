package com.nlm.callBack

import androidx.fragment.app.Fragment
import com.nlm.model.AhidfFormatForNlm
import com.nlm.model.AhidfMonitoring
import com.nlm.model.AssistanceForEaTrainingInstitute
import com.nlm.model.AssistanceForQfspFinancialProgres
import com.nlm.model.DairyPlantVisitReportNpddScheme
import com.nlm.model.FpFromForestLandFilledByNlm
import com.nlm.model.FpFromNonForestFilledByNlmTeam
import com.nlm.model.FspPlantStorageCommentsOfNlm
import com.nlm.model.IdAndDetails
import com.nlm.model.ImplementingAgencyAdvisoryCommittee
import com.nlm.model.ImplementingAgencyDocument
import com.nlm.model.ImplementingAgencyFundsReceived
import com.nlm.model.ImplementingAgencyInvolvedDistrictWise
import com.nlm.model.ImplementingAgencyProjectMonitoring
import com.nlm.model.ImportOfExoticGoatAchievement
import com.nlm.model.ImportOfExoticGoatDetailImport
import com.nlm.model.ImportOfExoticGoatVerifiedNlm
import com.nlm.model.NlmEdpFormatForNlm
import com.nlm.model.NlmEdpMonitoring
import com.nlm.model.RspAddAverage
import com.nlm.model.RspAddBucksList
import com.nlm.model.RspAddEquipment
import com.nlm.model.StateSemenBankOtherAddManpower
import com.nlm.model.StateSemenInfraGoat

//interface SingleCallBackItem {
//    fun onClickItem(selectedItem: IdAndName)
//}
interface CallBackItemTypeIACompositionListEdit {
    fun onClickItem(selectedItem: IdAndDetails,position:Int,isFrom:Int)
}
interface CallBackItemUploadDocEdit {
    fun onClickItemEditDoc(selectedItem: ImplementingAgencyDocument, position:Int)
}
interface CallBackItemImportExoticAchivementEdit {
    fun onClickItem(selectedItem: ImportOfExoticGoatAchievement, position:Int, isFrom:Int)

}
interface CallBackItemFormat6Edit {
    fun onClickItem(selectedItem: AssistanceForQfspFinancialProgres, position:Int, isFrom:Int)}
interface CallBackItemFormat9Edit {
    fun onClickItem(selectedItem: FpFromForestLandFilledByNlm, position:Int, isFrom:Int)}

interface CallBackItemImportExoticVerifiedByNlm {
    fun onClickItem(selectedItem: ImportOfExoticGoatVerifiedNlm, position:Int, isFrom:Int)
}
interface CallBackItemImportExoticDetailtEdit {
    fun onClickItemDetail(selectedItem: ImportOfExoticGoatDetailImport, position:Int, isFrom:Int)
}
interface CallBackItemManPower {
    fun onClickItem(selectedItem: StateSemenBankOtherAddManpower, position:Int, isFrom:Int)
}
interface CallBackAvilabilityEquipment {
    fun onClickItem(selectedItem: RspAddEquipment, position:Int, isFrom:Int)
}
interface CallBackSemenDoseAvg {
    fun onClickItem(selectedItem: RspAddBucksList, position:Int, isFrom:Int)
}

interface CallBackNddScheme {
    fun onClickItem(selectedItem: DairyPlantVisitReportNpddScheme, position:Int, isFrom:Int)
}
interface CallBackSemenDose {
    fun onClickItem(selectedItem: RspAddAverage, position:Int, isFrom:Int)
}
interface CallBackFspCommentNlm {
    fun onClickItem(selectedItem: FspPlantStorageCommentsOfNlm, position:Int, isFrom:Int)
}

interface CallBackFspNonNlm {
    fun onClickItem(selectedItem: FpFromNonForestFilledByNlmTeam, position:Int, isFrom:Int)
}
interface CallBackAssistanceEANlm {
    fun onClickItem(selectedItem: AssistanceForEaTrainingInstitute, position:Int, isFrom:Int)
}
interface CallBackNlmEdpMonitor {
    fun onClickItem(selectedItem: NlmEdpMonitoring, position:Int, isFrom:Int)
}
interface CallBackNlmAnimalMonitor {
    fun onClickItem(selectedItem: AhidfMonitoring, position:Int, isFrom:Int)
}
interface CallBackNlmEdpFormat {
    fun onClickItem(selectedItem: NlmEdpFormatForNlm, position:Int, isFrom:Int)
}

interface CallBackAnimalFund {
    fun onClickItem(selectedItem: AhidfFormatForNlm, position:Int, isFrom:Int)
}
interface CallBackItemGoatSemen {
    fun onClickItem(selectedItem: StateSemenInfraGoat, position:Int, isFrom:Int)
}
interface CallBackItemFundsReceivedListEdit {
    fun onClickItem(selectedItem: ImplementingAgencyFundsReceived,position:Int)
}
interface CallBackItemNLMDistrictWiseListEdit {
    fun onClickItem(selectedItem: ImplementingAgencyInvolvedDistrictWise, position:Int)
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
    fun onClickItem(ID: Int?,position:Int,isFrom: Int)
}
interface CallBackDeleteAtIdString {
    fun onClickItem(ID: Int?,position:Int,isFrom: String)
}
interface CallBackDeleteFSPAtId {
    fun onClickItemDelete(ID: Int?,position:Int)
}
interface CallBackDeleteFormatAtId {
    fun onClickItemFormatDelete(ID: Int?,position:Int)
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