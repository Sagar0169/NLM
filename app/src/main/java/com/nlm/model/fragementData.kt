package com.nlm.model

import android.net.Uri
import java.io.Serializable


data class NodalOfficer(
    val state: String,
    val agencyName: String,
    val nodalOfficerName: String,
    val nodalOfficerEmail: String,
    val created: String,
    val mobileNumber: String,    // New field
    val designation: String      // New field
) : Serializable

data class Indicator(
    val indicatorNumber: String,
    val indicatorText: String
) : Serializable

data class RGMVitro(
    val state: String,
    val created: String,
    val status: String? = null,
) : Serializable

data class RgmAi(
    val name: String,
    val state: String,
    val district: String,
    val location: String,
    val dateOfVisit: String,
    val submit: String? = null,
    val created: String,
    val createdBy: String? = null,


    ) : Serializable


data class OnlyCreated(
    val state: String,
    val created: String,
    val district: String? = null,
    val block: String? = null,
    val village: String? = null,
    val createdBy: String? = "Super Admin",
    ) : Serializable

data class NLMIA_data(
    val state: String,
    val created: String,
    val Name_Location: String? = null,
    val IA_Status: String? = null,


    ) : Serializable
data class State_Semen_Bank(
    val state: String,
    val District: String,
    val created: String,
    val Establishment_Year: String? = null,
    val NLM_Status: String? = null,
    ) : Serializable

data class ArtificialInsemenation(
    val state: String,
    val created: String,
    val district: String? = null,
    val liquid_nitrogen: String? = null,
    val frozen_semen_straws: String? = null,
    val cryocans: String? = null
) : Serializable


data class ImportOfGoat(
    val State: String,
    val Created_By: String,
    val Created: String,
    val Status_IA: String,
    val Status_NLM: String,
    ) : Serializable
data class Bull_Mothers(
    val Name_of_The_Farm: String,
    val State_Name: String,
    val District_Name: String,
    val Locatiob: String,
    val IA_Status: String,
    val NLM_Status: String,
    val Created_On: String,
    ) : Serializable
data class Semen_Station(
    val State_Name: String,
    val District_Name: String,
    val NLM_visit_date: String,
    val IA_Status: String,
    val NLM_Status: String,
    val Created_On: String,
) : Serializable

data class NLM_CompB(
    val Name_Of_The_DCS: String,
    val State_Name: String,
    val District: String,
    val Tehsil: String,
    val Village: String,
    val Created_At: String,


    ) : Serializable
data class RGM_IA(
    val Agency_Name: String,
    val State: String,
    val District: String,
    val Status_IA: String,
    val Status_NLM: String,
    val Created: String,
    val Visit: String,
    val Remarks_of_nlm: String,
    ) : Serializable
data class All_Users(

    val State: String,
    val Name: String,
    val Username: String,
    val Email: String,
    val Groups: String,
    val Role: String,
    val Status: String,
    val Created: String,

    ) : Serializable
data class NlmEdp(
    val comment: String,
    val created: String,
    val createdBy: String? = "Super Admin",

    ) : Serializable

data class NlmFpForest(
    val state: String,
    val district: String,
    val location: String,
    val agencyName: String,
    val areaCovered: String,
    val created: String,
    val organogram: String = "O",
    val technicalCompetence: String = "O",
    val createdBy: String? = "Super Admin",
    ) : Serializable


data class MilkUnionVisit(
    val state: String,
    val nameOfMilkUnion: String,
    val district: String,
    val createdBy: String,
    val createdDate: String,
) : Serializable
data class BreedMultiplication(
    val nameOfBeneficiary: String,

    val state: String,
    val district: String,
    val nlmStatus: String,
    val iaStatus: String,
    val created: String,
) : Serializable
data class NLMComponentA(
    val state: String,
    val district: String,
    val npdd: String,
    val year: String,
    val submit: String,
    val created: String,
) : Serializable
data class TrainingCenters(
    val state: String,
    val district: String,

    val village: String,
    val submit: String,
    val created: String,
) : Serializable
data class MilkProcessing(
    val nameOfProcessingPlant: String,
    val nameOfMilkUnion: String,
    val state: String,
    val district: String,
    val created: String,
): Serializable
data class MilkProductMarketing(
    val nameOfRetailShop: String,
    val nameOfMilkUnion: String,
    val dateOfInspection: String,
    val state: String,
    val district: String,
    val created: String,
): Serializable
data class ProductivityEnhancementServices(
    val dcs: String,
    val tehsil: String,
    val revenue: String,
    val state: String,
    val district: String,
    val created: String,
): Serializable
data class StateCenterVisit(
    val state: String,
    val district: String,
    val created: String,
    val location: String,
): Serializable
data class DairyPlantVisit(
    val state: String,
    val fssaiLicenseNo: String,
    val district: String,
    val created: String,
    val location: String,
) : Serializable
data class DcsCenterVisit(
    val fssai: String,
    val dateOfValidity: String,
    val nameOfDCS: String,
    val state: String,
    val district: String,
    val created: String,
): Serializable


data class SightedChildData(
    var dateOfSighting: String? = "DD/MM/YYYY",
    var year: String? = null,
    var month: String? = null,
    var feet: String? = null,
    var inches: String? = null,
    var relationship: String? = "Please Select",
    var gender: String? = null,  // Add this
    var name: String? = null,  // Add this
    var disabilityDetails: String? = null,  // Add this
    var description: String? = null,  // Add this
    var differentlyAbled: Boolean? = false,  // Add this
    var mentallyAbled: Boolean? = false  // Add this
)
data class details_Semen_Station(
    var State: String? = "DD/MM/YYYY",
    var District: String? = null,
    var Location: String? = null,
    var Address: String? = null,
    var Pin_code: String? = null,
    var  Phone_No: String? = "Please Select",
    var Grading: String? = null,  // Add this
    var Area_under: String? = null,  // Add this
    var Area_fodder: String? = null,  // Add this
    var ISO_9002: Boolean? = false,  // Add this
    var Cmugrading: Boolean? = false  // Add this

)


data class FacialAttributeData(
    var hariLength: String? = "Please Select",
    var hairColor: String? = "Please Select",
    var eyeType: String? = "Please Select",
    var eyeColor: String? = "Please Select",
    var earsType: String? = "Please Select",
    var earsSize: String? = "Please Select",
    var lipsType: String? = "Please Select",
    var lipsColor: String? = "Please Select",
    var frontTeeth: String? = "Please Select",
    var spectacleTypes: String? = "Please Select",
    var spectacleColor: String? = "Please Select",

    )

data class PhysicalAttributesData(
    var complexion: String? = "Please Select",
    var build: String? = "Please Select",
    var neckType: String? = "Please Select",
    var topWear: String? = "Please Select",
    var topWearColor: String? = "Please Select",
    var bottomWear: String? = "Please Select",
    var bottomWearColor: String? = "Please Select",
    var footWear: String? = "Please Select",
    var footWearColor: String? = "Please Select",
    var identification: String? = null

)

data class BackgroundData(
    var primaryLang: String? = "Please Select",
    var otherLang: String? = "Please Select",


    )

data class LocationData(
    var state: String? = "Please Select",
    var district: String? = "Please Select",
    var pin: String? = null,
    var address: String? = null,
)


data class UploadDocumentResponse(
    val _result: UploadDocumentsData,
    val _resultflag: Int,
    val message: String,
    val statuscode: Int
) : Serializable

data class UploadDocumentsData(
    val document_name: String?,
    val documents_url: String?,
    val id: Int?,
) : Serializable


data class FileItem(val fileName: String, val fileUri: Uri?)
data class FacultyMembersTrainingCenter(
    var sanctionedPost: String?,
    var noOfFilledPosts: String?,
    var postFilledOnContractual: String?,
    var vacantPosts: String?
)
data class MaitrisTrainingCenter(
    var name: String?,
    var duration: String?,
    var _2022: String?,
    var _2023: String?,
    var _2024: String?,
)


