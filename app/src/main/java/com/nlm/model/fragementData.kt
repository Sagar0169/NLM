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


data class OnlyCreated(
    val state: String,
    val created: String,
    val district: String? = null,
    val block: String? = null,
    val village: String? = null,
    ) : Serializable

data class OnlyCreatedNlm(
    val state: String,
    val created: String,
    val district: String? = null,
    val block: String? = null,
    val phone: String? = null,


    val year_of_est: String? = null,
    val name: String? = null,

    ) : Serializable

data class ArtificialInsemenation(
    val state: String,
    val created: String,
    val district: String? = null,
    val liquid_nitrogen: String? = null,
    val frozen_semen_straws: String? = null,
    val cryocans: String? = null,

    ) : Serializable




data class NlmEdp(
    val comment: String,
    val created: String,

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
    ) : Serializable


data class MilkUnionVisit(
    val state: String,
    val nameOfMilkUnion: String,
    val district: String,
    val createdBy: String,
    val createdDate: String,
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


