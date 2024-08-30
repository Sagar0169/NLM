package com.nlm.utilities

object Constants {

    const val REQUEST_PERMISSION_RECORD_AUDIO = 121
    const val TIME_HANDLER_BANNER = 3000
    const val REQUEST_WRITE_STORAGE_PERMISSION_DOC_PDF = 107
    const val REQUEST_WRITE_STORAGE_PERMISSION_DOC = 105
    const val REQUEST_CODE_DOC_PDF = 21
    const val REQUEST_CODE_DOC = 20
    const val MEDIA_PAGE_FLAG_REGISTER_PROFILE: Int = 16
    const val REQUEST_CAMERA_PERMISSION_MEDIA = 106
    const   val VIDEO_MIN_SIZE_DURATION = 1
    const val REQUEST_CAMERA_PERMISSION = 102
    const val MEDIA_PAGE_FLAG_FragmentEditProfile_1 = 11

    const val REQUEST_CAMERA_IMAGE_RESULT = 30
    const val REQUEST_CAMERA_VIDEO_RESULT = 31
    const val VALID_IMAGE_FILE_SIZE_KB = (1024 * 5 //2MB
            ).toLong()
    const val VALID_VIDEO_FILE_SIZE_KB = (1024 * 10 //4MB
            ).toLong()
    const val FILE_SEPERATOR = "/"
    const val FILE_PATH_COMPRESSED = "Compress/"
    const val FILE_PATH_THUMBNAIL = "Thumbnail/"
    const  val CARD_IMAGE_PATH = ( "/DDNews/.ddnews/")
    const val DOC_PATH = "Document/"
    const val DOC_PATH_ORIGIONAL = "Original/"


    const val LANGUAGE_CODE_ENGLISH = "en"
    const val LANGUAGE_CODE_ARABIC = "ar"
    const val AUTH_TOKEN = "AUTH_TOKEN"
    const val USER_NAME = "USER_NAME"
    const val USER_IMAGE = "USER_IMAGE"

    const val USER_ID = "USER_ID"
    const val USER_DATA = "USER_DATA"
    const val USER_TYPE = "USER_TYPE"
    const val BASE_URL="http://51.211.170.77/primeIntegration/api/"



    const val STORAGE_STORAGE_REQUEST_CODE = 100
    const val NOTIFICATION_VIEW_TYPE_TEXT = 1
    const val NOTIFICATION_VIEW_TYPE_DAY = 2
    const val NOTIFICATION_VIEW_TYPE_TIME = 3

    const val LANGUAGE = "LANGUAGE"

    const val NOTIFICATION_TYPE_MUSIC = 201
    const val AUDIO_STATE_START = 101
    const val AUDIO_STATE_STOP = 100
    const val AUDIO_STATE_FINISH = 102
    const val AUDIO_STATE_NEXT = 110
    const val AUDIO_STATE_PREVIOUS = 120
    const val AUDIO_INIT_DURATION = 103
    const val AUDIO_DURATION = 104
    const val AUDIO_SHOW_PROGRESS_DURATION = 105
    const val AUDIO_STATE_GO_FINISH = 106
    const val AUDIO_ERROR = 107
    const val AUDIO_OFF_PROGRESS = 108
    const val AUDIO_SHOW_PROGRESS_PLAY = 109
    const val PLAY_AUDIO_MUSIC = 110


    const val REQUEST_CODE_RECOVER_PLAY_SERVICES = 200

    const val LOCATION_QUICK_UPDATE_INTERVAL = (20 * 1000).toLong()
    const val LOCATION_QUICK_FASTEST_INTERVAL = (5 * 1000).toLong()
    var LOCATION_PAGE_FLAG_ActivityTempleInfoList = 11
    const val REQUEST_PERMISSION_FINE_LOCATION = 144

    const val REQUEST_CHECK_SETTINGS = 0x1


    const val  CHANNEL_ID_MUSIC="KYTMusic"
    const val  CHANNEL_NAME_MUSIC="Know your Temple"

    const val Broadcast_PLAY_NEW_AUDIO = "com.knowyourtemple.audioplayer.PlayNewAudio"
//    const val Broadcast_PLAY_PAUSE = "com.knowyourtemple.audioplayer.PlayPauseAudio"
    const val ACTION_NOTIFY = "com.knowyourtemple.audioplayer.NOTIFY"
    const val ACTION_PLAY = "com.knowyourtemple.audioplayer.ACTION_PLAY"
    const val ACTION_PAUSE = "com.knowyourtemple.audioplayer.ACTION_PAUSE"
    const val ACTION_PREVIOUS = "com.knowyourtemple.audioplayer.ACTION_PREVIOUS"
    const val ACTION_NEXT = "com.knowyourtemple.audioplayer.ACTION_NEXT"
    const val ACTION_STOP = "com.knowyourtemple.audioplayer.ACTION_STOP"


}