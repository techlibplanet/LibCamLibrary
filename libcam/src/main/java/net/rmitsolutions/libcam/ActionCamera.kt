package net.rmitsolutions.libcam

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Base64
import com.theartofdev.edmodo.cropper.CropImage
import net.rmitsolutions.libcam.Constants.logD
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.ArrayList

internal class ActionCamera(private val activity: Activity) {

    private val TAG = Constants.tag(activity)

    companion object {
        private val savePhoto = SavePhoto()
        private val pictureUtils = PictureUtils()

    }

    fun takePhoto() {
        activity.startActivityForResult(pickImageChooserIntent, Constants.TAKE_PHOTO)
    }

    private val pickImageChooserIntent: Intent
        get() {
            val outputFileUri = captureImageOutputUri

            val allIntents = ArrayList<Intent>()
            val packageManager = activity.packageManager
            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val listCam = packageManager.queryIntentActivities(captureIntent, 0)
            for (res in listCam) {
                val intent = Intent(captureIntent)
                intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
                intent.`package` = res.activityInfo.packageName
                if (outputFileUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
                }
                allIntents.add(intent)
            }
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "image/*"
            val listGallery = packageManager.queryIntentActivities(galleryIntent, 0)
            for (res in listGallery) {
                val intent = Intent(galleryIntent)
                intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
                intent.`package` = res.activityInfo.packageName
                allIntents.add(intent)
            }
            var mainIntent = allIntents[allIntents.size - 1]
            for (intent in allIntents) {
                if (intent.component!!.className == "com.android.documentsui.DocumentsActivity") {
                    mainIntent = intent
                    break
                }
            }
            allIntents.remove(mainIntent)
            val chooserIntent = Intent.createChooser(mainIntent, "Select source")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toTypedArray<Parcelable>())

            return chooserIntent
        }


    private val captureImageOutputUri: Uri?
        get() {
            var outputFileUri: Uri? = null
            val getImage = activity.externalCacheDir
            if (getImage != null) {
                outputFileUri = Uri.fromFile(File(getImage.path, "pickImageResult.jpeg"))
            }
            return outputFileUri
        }

    fun getPickImageResultUri(data: Intent?): Uri? {
        var isCamera = true
        if (data != null && data.data != null) {
            val action = data.action
            isCamera = action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
        }
        return if (isCamera) captureImageOutputUri else data!!.data
    }

    fun cropImage(uri: Uri) {
        CropImage.activity(uri).start(activity)
    }

    fun cropImageActivityResult(requestCode: Int, resultCode: Int, data: Intent): Uri? {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val uri = result.uri
                return uri
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                logD(TAG, "Error : $error")
                return null
            }
        }
        return null
    }

    fun getBitmapFromUri(uri: Uri): Bitmap? {
        return MediaStore.Images.Media.getBitmap(activity.contentResolver, uri);
    }

    fun getUriFromBitmap(bitmap: Bitmap): Uri? {
        val path = MediaStore.Images.Media.insertImage(activity.contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    // Get bitmap from byte array
    fun getByteArrayFromBitmap(bitmap: Bitmap, compressQuality: Int): ByteArray? {
        val byteArrayStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, byteArrayStream)
        return byteArrayStream.toByteArray()
    }

    // Get byte array from bitmap
    fun getBitmapFromByteArray(byteArray: ByteArray): Bitmap? {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        return if (bitmap != null) {
            bitmap
        } else {
            null
        }
    }

    fun getUriFromByteArray(byteArray: ByteArray): Uri? {
        val bitmap = getBitmapFromByteArray(byteArray)
        return getUriFromBitmap(bitmap!!)
    }

    fun getByteArrayFromUri(uri: Uri, compressQuality: Int): ByteArray? {
        val bitmap = getBitmapFromUri(uri)
        return getByteArrayFromBitmap(bitmap!!, compressQuality)
    }

    // Get base 64 string from bitmap with image quality
    fun getBase64StringFromBitmap(bitmap: Bitmap, quality: Int): String? {
        val byteArrayStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayStream)
        val byteArray = byteArrayStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    //Get bitmap from base 64 string
    fun getBitmapFromBase64String(base64String: String): Bitmap? {
        val bitmapArray = Base64.decode(base64String, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
        return if (bitmap != null) {
            bitmap
        } else {
            null
        }
    }

    fun getBase64StringFromUri(uri: Uri, compressQuality: Int): String? {
        val bitmap = getBitmapFromUri(uri)
        return getBase64StringFromBitmap(bitmap!!, compressQuality)
    }

    fun getUriFromBase64String(base64String: String): Uri? {
        val bitmap = getBitmapFromBase64String(base64String)
        return getUriFromBitmap(bitmap!!)
    }

    fun savePhotoInDeviceMemory(bitmap: Bitmap, imagePrefix: String, autoConcatenateNameByDate: Boolean): String? {
        return savePhoto.writePhotoFile(bitmap, imagePrefix, Constants.DEFAULT_DIRECTORY_NAME, Constants.DEFAULT_BITMAP_FORMAT, autoConcatenateNameByDate, activity)
    }

    fun savePhotoInDeviceMemory(bitmap: Bitmap, imagePrefix: String, format: Bitmap.CompressFormat, autoConcatenateNameByDate: Boolean): String? {
        return savePhoto.writePhotoFile(bitmap, imagePrefix, Constants.DEFAULT_DIRECTORY_NAME, format, autoConcatenateNameByDate, activity)
    }

    fun savePhotoInDeviceMemory(bitmap: Bitmap, imagePrefix: String, directoryName: String, autoConcatenateNameByDate: Boolean): String? {
        return savePhoto.writePhotoFile(bitmap, imagePrefix, directoryName, Constants.DEFAULT_BITMAP_FORMAT, autoConcatenateNameByDate, activity)
    }

    fun savePhotoInDeviceMemory(bitmap: Bitmap, imagePrefix: String, directoryName: String, format: Bitmap.CompressFormat, autoConcatenateNameByDate: Boolean): String? {
        return savePhoto.writePhotoFile(bitmap, imagePrefix, directoryName, format, autoConcatenateNameByDate, activity)
    }

    fun savePhotoInDeviceMemory(uri: Uri, imagePrefix: String, autoConcatenateNameByDate: Boolean): String? {
        val bitmap = getBitmapFromUri(uri)
        return savePhoto.writePhotoFile(bitmap, imagePrefix, Constants.DEFAULT_DIRECTORY_NAME, Constants.DEFAULT_BITMAP_FORMAT, autoConcatenateNameByDate, activity)
    }

    fun savePhotoInDeviceMemory(uri: Uri, imagePrefix: String, format: Bitmap.CompressFormat, autoConcatenateNameByDate: Boolean): String? {
        val bitmap = getBitmapFromUri(uri)
        return savePhoto.writePhotoFile(bitmap, imagePrefix, Constants.DEFAULT_DIRECTORY_NAME, format, autoConcatenateNameByDate, activity)
    }

    fun savePhotoInDeviceMemory(uri: Uri, imagePrefix: String, directoryName: String, autoConcatenateNameByDate: Boolean): String? {
        val bitmap = getBitmapFromUri(uri)
        return savePhoto.writePhotoFile(bitmap, imagePrefix, directoryName, Constants.DEFAULT_BITMAP_FORMAT, autoConcatenateNameByDate, activity)
    }

    fun savePhotoInDeviceMemory(uri: Uri, imagePrefix: String, directoryName: String, format: Bitmap.CompressFormat, autoConcatenateNameByDate: Boolean): String? {
        val bitmap = getBitmapFromUri(uri)
        return savePhoto.writePhotoFile(bitmap, imagePrefix, directoryName, format, autoConcatenateNameByDate, activity)
    }

    fun rotatePicture(bitmap: Bitmap, rotate: Int) {
        if (bitmap != null) {
            pictureUtils.rotateImage(bitmap, rotate.toFloat())
        }
    }

    fun createScaledBitmap(bitmap: Bitmap, width: Int, height: Int, filter: Boolean): Bitmap? {
        return pictureUtils.createScaledBitmap(bitmap, width, height, filter)
    }
}