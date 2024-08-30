package com.nlm.model

import java.io.File

class PojoMediaData {
    var isImage: Boolean
    var isDoc = false
    var mediaCompressFile: File
    var videoThumbFile: File? = null

    constructor(mediaCompressFile: File) {
        isDoc = true
        isImage = true
        this.mediaCompressFile = mediaCompressFile
    }

    constructor(isImage: Boolean, mediaCompressFile: File) {
        this.isImage = isImage
        this.mediaCompressFile = mediaCompressFile
    }

    constructor(isImage: Boolean, mediaCompressFile: File, videoThumbFile: File?) {
        this.isImage = isImage
        this.mediaCompressFile = mediaCompressFile
        this.videoThumbFile = videoThumbFile
    }
}
