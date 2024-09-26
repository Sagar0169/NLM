package com.nlm.utilities


import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.media.ExifInterface
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.databinding.ObservableInt
import com.nlm.utilities.Constants.CARD_IMAGE_PATH
import com.nlm.utilities.Constants.DOC_PATH
import com.nlm.utilities.Constants.DOC_PATH_ORIGIONAL
import com.nlm.utilities.Constants.FILE_PATH_COMPRESSED
import com.nlm.utilities.Constants.FILE_PATH_THUMBNAIL
import com.nlm.utilities.Constants.FILE_SEPERATOR
import com.nlm.utilities.Constants.VALID_IMAGE_FILE_SIZE_KB
import com.nlm.utilities.Constants.VALID_VIDEO_FILE_SIZE_KB
import com.nlm.R
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URI
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileUtils {
    // get path of File for grter than kitkat
    fun getPath(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    val type = split[0]
                    return if ("primary".equals(type, ignoreCase = true)) {
                        Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    } else {
                        "/storage/" + split[0] + "/" + split[1]
                    }
                } else if (isDownloadsDocument(uri)) {
                    val fileName = getFilePath(context, uri)
                    if (fileName != null) {
                        return Environment.getExternalStorageDirectory()
                            .toString() + "/Download/" + fileName
                    }
                    var id = DocumentsContract.getDocumentId(uri)
                    if (id.startsWith("raw:")) {
                        id = id.replaceFirst("raw:".toRegex(), "")
                        val file = File(id)
                        if (file.exists()) return id
                    }
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )
                    return getDataColumn(context, contentUri, null, null)

                    /*final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));*/
//                    return  getDataColumn(context, contentUri, null, null);
                } else if (isGoogleDocs(uri)) {
                    return getFileFromDoc(context, uri)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(
                        split[1]
                    )
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                return getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }
        }
        return null
    }

    fun getFilePath(context: Context, uri: Uri?): String? {
        var cursor: Cursor? = null
        val projection = arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, null, null,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun getFileFromDoc(context: Context, returnUri: Uri): String {
        var tmppath = ""
        val returnCursor = context.contentResolver.query(returnUri, null, null, null, null)
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val dir: File =
            File(CARD_IMAGE_PATH + DOC_PATH + DOC_PATH_ORIGIONAL)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        try {
            val input = context.contentResolver.openInputStream(returnUri)
            val tmpfile = File(dir, returnCursor.getString(nameIndex))
            val output: OutputStream = FileOutputStream(tmpfile)
            try {
                try {
                    val buffer = ByteArray(4 * 1024) // or other buffer size
                    var read: Int
                    while (input!!.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                    }
                    output.flush()
                } finally {
                    returnCursor.close()
                    output.close()
                }
                input!!.close()
                tmppath = tmpfile.absolutePath
            } catch (e: Exception) {
                e.printStackTrace() // handle exception, define IOException and others
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return tmppath
    }

    private fun isGoogleDocs(uri: Uri): Boolean {
        return "com.google.android.apps.docs.storage" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    @JvmStatic
    fun checkFileExists(path: String?): Boolean {
        val f = File(path)
        return checkFileExists(f)
    }

    @JvmStatic
    fun checkFileExists(file: File?): Boolean {
        return file != null && file.exists() && file.length() > 0
    }

    fun setUpSIze(length: Long, attach: Int): Boolean {
        val sizeKb = length / 1024.0
        //video
        /*else if (attach == 3 && sizeKb <= 5120) {//doc
            return true;
        }*/return if (attach == 1 && sizeKb <= VALID_IMAGE_FILE_SIZE_KB) { //image
            true
        } else attach == 2 && sizeKb <= VALID_VIDEO_FILE_SIZE_KB
    }

    @JvmStatic
    fun validFileType(filePath: String, validExtension: Array<String?>): Boolean {
        var isMatch = false
        val extension = filePath.substring(filePath.lastIndexOf('.'))
        if (!TextUtils.isEmpty(extension)) {
            for (type in validExtension) {
                if (extension.equals(type, ignoreCase = true)) {
                    isMatch = true
                    break
                }
            }
        }
        return isMatch
    }

    fun checkOtherFileType(filePath: String): BooleanArray {
        var isValid = false
        var isImage = false
        if (!TextUtils.isEmpty(filePath)) {
            val filePathInLowerCase = filePath.lowercase(Locale.getDefault())
            if (filePathInLowerCase.endsWith(".mp4")) {
                isValid = true
            } else if (filePathInLowerCase.endsWith(".jpg")
                || filePathInLowerCase.endsWith(".jpeg") || filePathInLowerCase.endsWith(".png")
            ) {
                isValid = true
                isImage = true
            }
        }
        return booleanArrayOf(isValid, isImage)
    }

    fun checkFileType(filePath: String): Boolean {
        if (!TextUtils.isEmpty(filePath)) {
            val filePathInLowerCase = filePath.lowercase(Locale.getDefault())
            return filePathInLowerCase.endsWith(".pdf")
        }
        return false
    }

    /*public static String[] getDocPath(Uri uri, Context mContext, ObservableInt observerSnackBarInt) {
        String path = "";
        String image = "0";

        if (mContext.getContentResolver() != null) {
            Cursor returnCursor = mContext.getContentResolver().query(uri, null, null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();
                if (nameIndex >= 0 && sizeIndex >= 0) {
                    String fileName = returnCursor.getString(nameIndex);
                    boolean[] fileTypeData = checkOtherFileType(fileName);
                    //long fileSize = returnCursor.getLong(sizeIndex);
                    if (fileTypeData[0]) {
                        image = fileTypeData[1] ? "1" : "0";
                        path = fileName;
                        */
    /*if (setUpSIze(fileSize, (fileTypeData[1] ? 1 : 3))) {
                            image = fileTypeData[1] ? "1" : "0";
                            path = fileName;
                        } else {
                            observerSnackBarInt.set(R.string.error_document_validation);
                        }*/
    /*
                    } else {
                        observerSnackBarInt.set(R.string.error_valid_medai_format);
                    }
                }
            } else {
                File file = new File(URI.create(uri.toString()));
                String fType = getMimeType(mContext, uri);
                boolean[] fileTypeData = checkMimeType(fType);
                if (fileTypeData[0]) {
                    image = fileTypeData[1] ? "1" : "0";
                    path = file.getAbsolutePath();
                    */
    /*if (checkValidFileSize(file.getAbsolutePath(), (fileTypeData[1] ? 1 : 3))) {
                        image = fileTypeData[1] ? "1" : "0";
                        path = file.getAbsolutePath();
                    } else {
                        observerSnackBarInt.set(R.string.error_document_validation);
                    }*/
    /*
                } else {
                    observerSnackBarInt.set(R.string.error_valid_medai_format);
                }
            }
        }
        return new String[]{path, image};
    }*/
    fun getDocPath(uri: Uri, mContext: Context, observerSnackBarInt: ObservableInt): Array<String> {
        var path = ""
        var image = "0"
        if (mContext.contentResolver != null) {
            val returnCursor = mContext.contentResolver.query(uri, null, null, null, null)
            if (returnCursor != null) {
                val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
                returnCursor.moveToFirst()
                if (nameIndex >= 0 && sizeIndex >= 0) {
                    val fileName = returnCursor.getString(nameIndex)
                    val fileTypeData = checkOtherFileType(fileName)
                    //long fileSize = returnCursor.getLong(sizeIndex);
                    if (fileTypeData[0]) {
                        image = if (fileTypeData[1]) "1" else "0"
                        path = fileName
                        /*if (setUpSIze(fileSize, (fileTypeData[1] ? 1 : 3))) {
                            image = fileTypeData[1] ? "1" : "0";
                            path = fileName;
                        } else {
                            observerSnackBarInt.set(R.string.error_document_validation);
                        }*/
                    } else {
                        observerSnackBarInt.set(R.string.error_valid_medai_format)
                    }
                }
            } else {
                val file = File(URI.create(uri.toString()))
                val fType = getMimeType(mContext, uri)
                val fileTypeData = checkMimeType(fType)
                if (fileTypeData[0]) {
                    image = if (fileTypeData[1]) "1" else "0"
                    path = file.absolutePath
                    /*if (checkValidFileSize(file.getAbsolutePath(), (fileTypeData[1] ? 1 : 3))) {
                        image = fileTypeData[1] ? "1" : "0";
                        path = file.getAbsolutePath();
                    } else {
                        observerSnackBarInt.set(R.string.error_document_validation);
                    }*/
                } else {
                    observerSnackBarInt.set(R.string.error_valid_medai_format)
                }
            }
        }
        return arrayOf(path, image)
    }

    fun saveDocFile(context: Context, sourceUri: Uri?, fileName: String, isImage: Boolean): File? {
        val f: File
        val mImagePath: String
        //File outputFile = null;
        if (isImage) {
            f = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "")
            mImagePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + FILE_SEPERATOR + fileName
        } else {
            f = File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES), "")
            mImagePath = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
                .toString() + FILE_SEPERATOR + fileName
        }
        if (!f.exists()) {
            f.mkdirs()
        }

        /*try {
            outputFile = File.createTempFile(fileName, (isImage ? ".jpg" : ".pdf"), f);
        } catch (IOException e) {
            e.printStackTrace();
            outputFile = null;
        }*/if (sourceUri != null) { //&& outputFile != null
            var fos: FileOutputStream? = null
            var fis: InputStream? = null
            try {
                fis = context.contentResolver.openInputStream(sourceUri)
                fos = FileOutputStream(mImagePath, false)
                //fos = new FileOutputStream(outputFile, false);
                val buf = ByteArray(1024)
                var len: Int
                while (fis!!.read(buf).also { len = it } != -1) {
                    fos.write(buf, 0, len)
                }
                fos.close()
                fis.close()
                return File(mImagePath)
                //return outputFile;
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    fos?.close()
                    fis?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    fun checkMediaIsImage(filePath: String): Boolean {
        var isImage = false
        if (!TextUtils.isEmpty(filePath)) {
            val filePathInLowerCase = filePath.lowercase(Locale.getDefault())
            if (filePathInLowerCase.endsWith(".jpg")
                || filePathInLowerCase.endsWith(".jpeg")
            ) {
                isImage = true
            }
        }
        return isImage
    }

    fun checkMimeType(mimeType: String?): BooleanArray {
        var isValid = false
        var isImage = false
        if (!TextUtils.isEmpty(mimeType)) {
            val mimeTypeInLowerCase = mimeType!!.lowercase(Locale.getDefault())
            if (TextUtils.equals(
                    "mp4",
                    mimeTypeInLowerCase
                ) || mimeTypeInLowerCase.lowercase(Locale.getDefault()).contains("mp4")
            ) {
                isValid = true
            } else if (TextUtils.equals(
                    "jpg",
                    mimeTypeInLowerCase
                ) || mimeTypeInLowerCase.lowercase(
                    Locale.getDefault()
                ).contains("jpg")
            ) {
                isValid = true
                isImage = true
            } else if (TextUtils.equals(
                    "jpeg",
                    mimeTypeInLowerCase
                ) || mimeTypeInLowerCase.lowercase(
                    Locale.getDefault()
                ).contains("jpeg")
            ) {
                isValid = true
                isImage = true
            }
        }
        return booleanArrayOf(isValid, isImage)
    }

    fun getMimeType(context: Context, uri: Uri?): String? {
        var extension: String? = ""
        if (uri != null) {
            extension = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
                val mime = MimeTypeMap.getSingleton()
                mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
            } else {
                MimeTypeMap.getFileExtensionFromUrl(
                    Uri.fromFile(File(uri.path)).toString()
                )
            }
        }
        return extension
    }

    fun checkValidFileSize(path: String?, isDocument: Int): Boolean {
        val f = File(path)
        return checkFileExists(f) && setUpSIze(f.length(), isDocument)
    }

    @JvmStatic
    fun compressFullImage(context: Context, photoFile: File, maxSize: Long): File {
        val file: File
        file = if (checkFileExists(photoFile)) {
            val sizeKb = photoFile.length() / 1024.0
            return if (sizeKb > maxSize) {
                compressImageSize(
                    context,
                    photoFile,
                    maxSize / sizeKb,
                    false
                )
            } else {
                compressImageSize(context, photoFile, 0.0, true)
            }
        } else {
            photoFile
        }
        return file
    }

    private fun compressImageSize(
        context: Context,
        photoFile: File,
        ratio: Double,
        isActual: Boolean
    ): File {
        val file: File
        var bmp: Bitmap? = null
        var scaledBitmap: Bitmap? = null
        var input: InputStream? = null
        var input1: InputStream? = null
        val cr = context.contentResolver
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            input = cr.openInputStream(Uri.fromFile(photoFile))
            BitmapFactory.decodeStream(input, null, options)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        var actualWidth = options.outWidth
        var actualHeight = options.outHeight
        if (actualHeight <= 0) {
            actualHeight = 1
        }
        if (actualWidth <= 0) {
            actualWidth = 1
        }
        val maxHeight: Float
        val maxWidth: Float
        if (isActual) {
            maxHeight = actualHeight.toFloat()
            maxWidth = actualWidth.toFloat()
        } else {
            maxHeight = Math.round(actualHeight * ratio).toFloat()
            maxWidth = Math.round(actualWidth * ratio).toFloat()
        }
        var imgRatio = actualWidth.toFloat() / actualHeight
        val maxRatio = maxWidth / maxHeight
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try {
            input1 = cr.openInputStream(Uri.fromFile(photoFile))
            bmp = BitmapFactory.decodeStream(input1, null, options)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (input1 != null) {
                try {
                    input1.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp!!, middleX - bmp.width / 2f, middleY - bmp.height / 2f,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )
        val exif: ExifInterface
        try {
            exif = ExifInterface(photoFile.absolutePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            /*if (exif != null) {
                    timeLatLong = FetchGPSDataFromImages(exif);
                }*/
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
            } else if (orientation == 3) {
                matrix.postRotate(180f)
            } else if (orientation == 8) {
                matrix.postRotate(270f)
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap, 0, 0, scaledBitmap.width,
                scaledBitmap.height, matrix, true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var outStream: OutputStream? = null
        file = getFile(getFinalImagePath(context, FILE_PATH_COMPRESSED), ".jpg")
        try {
            outStream = FileOutputStream(file)
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 84, outStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (outStream != null) {
                try {
                    outStream.flush()
                    outStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            scaledBitmap?.recycle()
            if (bmp != null) {
                bmp.recycle()
            }
        }
        return file
    }

    fun getFinalImagePath(context: Context, dir: String): String {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            .toString() + FILE_SEPERATOR + dir
    }

    @JvmStatic
    fun getCompressVideoFile(context: Context): File {
        return getFile(
            context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
                .toString() + FILE_SEPERATOR + FILE_PATH_COMPRESSED, ".mp4"
        )
    }

    fun getCompressVideoThumbnailFile(context: Context): File {
        return getFile(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + FILE_SEPERATOR + FILE_PATH_THUMBNAIL, ".png"
        )
    }

    fun getFile(filePath: String?, extension: String): File {
        val path = File(filePath)
        if (!path.exists()) {
            path.mkdirs()
        }
        return File(path, System.currentTimeMillis().toString() + extension)
    }

    @JvmStatic
    fun deleteFile(file: File) {
        if (checkFileExists(file)) {
            file.delete()
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int, reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).
            val totalPixels = (width * height).toFloat()
            // Anything more than 2x the requested pixels we'll sample down further
            val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++
            }
        }
        return inSampleSize
    }

    @JvmStatic
    fun getVideoDuration(context: Context?, uri: Uri?): Float {
        var duration: Long = -1
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(context, uri)
            duration =
                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!
                    .toLong()
        } catch (e: Exception) {
            //Logger.("Exception getVideoDuration -" + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                try {
                    mediaMetadataRetriever.release()
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
        }
        return duration / 1000f
    }

    @JvmStatic
    fun validFileSize(length: Long, validSizeKb: Long): Boolean {
        val sizeKb = length / 1024
        return sizeKb <= validSizeKb
    }

    @JvmStatic
    fun getVideoThumbnailImage(context: Context, videoPath: String?): File? {
        var thumbImage: File? = null
        val bitmap = getVideoThumbnail(videoPath)
        if (bitmap != null) {
            var outStream: FileOutputStream? = null
            try {
                thumbImage = getCompressVideoThumbnailFile(context)
                outStream = FileOutputStream(thumbImage)
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, outStream)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                bitmap.recycle()
                try {
                    if (outStream != null) {
                        outStream.flush()
                        outStream.close()
                    }
                } catch (e: Exception) {
                }
            }
        }
        return thumbImage
    }

    fun getVideoThumbnail(path: String?): Bitmap? {
        var bitmap: Bitmap? = null
        var clazz: Class<*>? = null
        var instance: Any? = null
        try {
            clazz = Class.forName("android.media.MediaMetadataRetriever")
            instance = clazz.newInstance()
            val method = clazz.getMethod("setDataSource", String::class.java)
            method.invoke(instance, path)
            // The method name changes between API Level 9 and 10.
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD) {
                bitmap = clazz.getMethod("captureFrame").invoke(instance) as Bitmap
            } else {
                val data = clazz.getMethod("getEmbeddedPicture").invoke(instance) as ByteArray
                if (data != null) {
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                }
                if (bitmap == null) {
                    bitmap = clazz.getMethod("getFrameAtTime").invoke(instance) as Bitmap
                }
            }
        } catch (e: Exception) {
            bitmap = null
        } finally {
            try {
                if (instance != null) {
                    clazz!!.getMethod("release").invoke(instance)
                }
            } catch (ignored: Exception) {
            }
        }
        return bitmap
    }

    @Throws(IOException::class)
    fun createTempCameraFile(context: Context, isImage: Boolean): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val cameraFileName: String
        val storageDir: File?
        val extension: String
        if (isImage) {
            cameraFileName = "JPEG_$timeStamp"
            storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            extension = ".jpg"
        } else {
            cameraFileName = "MP4_$timeStamp"
            storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
            extension = ".mp4"
        }
        return File.createTempFile(
            cameraFileName,  /* prefix */
            extension,  /* suffix */
            storageDir /* directory */
        )
    }

    @Throws(IOException::class)
    fun createAudioFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val cameraFileName: String
        val storageDir: File?
        val extension: String
        cameraFileName = "AUDIO_$timeStamp"
        storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        extension = ".mp3"
        return File.createTempFile(
            cameraFileName,  /* prefix */
            extension,  /* suffix */
            storageDir /* directory */
        )
    }

    fun getAudioFile(context: Context): File? {
        var fileTemporary: File? = null
        try {
            fileTemporary = createAudioFile(context)
        } catch (exception: IOException) {
            //   Logger.e(context.getClass().getSimpleName() + " getAudioFile", exception.toString());
        }
        return fileTemporary
    }

    @JvmStatic
    fun getCameraFile(context: Context, isImage: Boolean): File? {
        var fileTemporary: File? = null
        try {
            fileTemporary = createTempCameraFile(context, isImage)
        } catch (exception: IOException) {
            // Logger.d(context.getClass().getSimpleName() + " openCamera", exception.toString());
        }
        return fileTemporary
    }

    @JvmStatic
    fun getTempCameraFileUri(context: Context?, file: File?): Uri {
        return FileProvider.getUriForFile(
            context!!,
             "mission.vatsalya.provider",
            file!!
        )
    }

    @SuppressLint("SuspiciousIndentation")
    fun checkForValidImage(context: Context, tempFilePath: String): File {
        val filename = tempFilePath.substring(tempFilePath.lastIndexOf("/") + 1)
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + FILE_SEPERATOR + filename
        )
             return    file
    }

 /*   public static File checkForValidVideo(Context context, String tempFilePath) {
        String filename = tempFilePath.substring(tempFilePath.lastIndexOf("/") + 1);
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES) + FILE_SEPERATOR + filename);
        if (checkFileExists(file)) {
            return file;
        } else {
            return null;
        }
    }*/

/*
    public static File checkForValidVideo(Context context, String tempFilePath) {
        String filename = tempFilePath.substring(tempFilePath.lastIndexOf("/") + 1);
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES) + FILE_SEPERATOR + filename);
        if (checkFileExists(file)) {
            return file;
        } else {
            return null;
        }
    }*/

    fun checkForValidVideo(context: Context, tempFilePath: String): File? {
        val filename = tempFilePath.substring(tempFilePath.lastIndexOf("/") + 1)
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES), filename)
        return if (checkFileExists(file)) {
            file
        } else {
            null
        }
    }

    fun getPDFPath(uri: Uri, mContext: Context, observerSnackBarInt: ObservableInt): String {
        val returnCursor = mContext.contentResolver.query(uri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
            returnCursor.moveToFirst()
            if (nameIndex >= 0 && sizeIndex >= 0) {
                val fileName = returnCursor.getString(nameIndex)
                val isValidFile = checkFileType(fileName)
                //long fileSize = returnCursor.getLong(sizeIndex);
                if (isValidFile) {
                    return fileName
                    /*if (setUpSIze(fileSize, 3)) {
                        return fileName;
                    } else {
                        observerSnackBarInt.set(R.string.error_document_validation);
                    }*/
                } else {
                    observerSnackBarInt.set(R.string.error_valid_pdf_format)
                }
            }
        } else {
            val file = File(URI.create(uri.toString()))
            val fType = getMimeType(mContext, uri)
            if (TextUtils.equals("pdf", fType!!.lowercase(Locale.getDefault())) || fType.lowercase(
                    Locale.getDefault()
                ).contains("pdf")
            ) {
                return file.name
                /*if (checkValidFileSize(file.getAbsolutePath(), 3)) {
                    return file.getName();
                } else {
                    observerSnackBarInt.set(R.string.error_document_validation);
                }*/
            } else {
                observerSnackBarInt.set(R.string.error_valid_pdf_format)
            }
        }
        return ""
    }

    fun savePdfFile(sourceUri: Uri?, fileName: String, context: Context): File? {
        val f = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString() + FILE_SEPERATOR
        )
        if (!f.exists()) f.mkdirs()
        val mImagePath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            .toString() + FILE_SEPERATOR + fileName
        if (sourceUri != null) {
            var fos: FileOutputStream? = null
            var fis: InputStream? = null
            try {
                fis = context.contentResolver.openInputStream(sourceUri)
                fos = FileOutputStream(mImagePath, true)
                val buf = ByteArray(1024)
                var len: Int
                while (fis!!.read(buf).also { len = it } != -1) {
                    fos.write(buf, 0, len)
                }
                fos.close()
                fis.close()
                return File(mImagePath)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    fos?.close()
                    fis?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }
}