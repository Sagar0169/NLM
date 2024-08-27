package mission.vatsalya.utilities

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import mission.vatsalya.model.PojoMediaData
import mission.vatsalya.utilities.ImagePicker
import java.io.File

class ImagePickerHelper(activity: AppCompatActivity, observerSnackBarInt: ObservableInt?) : ImagePicker(
    activity!!, observerSnackBarInt!!) {
    var mediaPageFlag = 0
        private set
    val onMediaDataGet = MutableLiveData<PojoMediaData>()
    fun showImageMediaDialog(pageFlag: Int) {
        mediaPageFlag = pageFlag
        showImageMediaDialog()
    }

    fun showVideoMediaDialog(pageFlag: Int) {
        mediaPageFlag = pageFlag
        showVideoMediaDialog()
    }

    fun selectMediaDialog(pageFlag: Int) {
        mediaPageFlag = pageFlag
        selectMediaDialog()
    }

    fun selectMediaType(pageFlag: Int, which: Int) {
        mediaPageFlag = pageFlag
        selectMediaType(which)
    }

    fun attachPDF(pageDocFlag: Int) {
        mediaPageFlag = pageDocFlag
        selectMediaDocDialog()
    }

    fun clearMediaType() {
        mediaPageFlag = -1
    }

    @JvmField
    var listenerMediaData: ListenerMediaData = object : ListenerMediaData {
        override fun onMediaCapture(
            isImage: Boolean,
            mediaCompressFile: File?,
            videoThumbFile: File?
        ) {
            onMediaDataGet.value = PojoMediaData(isImage, mediaCompressFile!!, videoThumbFile)
        }

        override fun onDocCapture(docFile: File?) {
            onMediaDataGet.value = PojoMediaData(docFile!!)
        }
    }

    init {
        setListenerMediaData(listenerMediaData)
    }
}