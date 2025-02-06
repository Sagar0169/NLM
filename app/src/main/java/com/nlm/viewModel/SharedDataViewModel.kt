package com.nlm.viewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nlm.model.RGMStateImplementingAgencyDataClass



class SharedDataViewModel : ViewModel() {
    val userData = MutableLiveData(RGMStateImplementingAgencyDataClass()) // Initialize with default fields

    init {
        userData.value = RGMStateImplementingAgencyDataClass() // Initialize with default empty fields
    }
}
