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

data class OnlyCreated(
    val state: String,
    val created: String,
    val district: String? = null,
    val block: String? = null,
    val village: String? = null,

) : Serializable


data class MilkUnionVisit(
    val state: String,
    val nameOfMilkUnion: String,
    val district: String,
    val createdBy: String,
    val createdDate: String,
) : Serializable


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


