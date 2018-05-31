package net.rmitsolutions.libcamlibrary

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Camera
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import net.rmitsolutions.libcam.*
import net.rmitsolutions.libcam.Constants.ACCESS_LOCATION
import net.rmitsolutions.libcam.Constants.CAMERA
import net.rmitsolutions.libcam.Constants.CROP_PHOTO
import net.rmitsolutions.libcam.Constants.DEFAULT_FILE_PREFIX
import net.rmitsolutions.libcam.Constants.EXTERNAL_STORAGE
import net.rmitsolutions.libcam.Constants.TAKE_PHOTO
import net.rmitsolutions.libcam.Constants.logD
import net.rmitsolutions.libcam.Constants.tag
import org.jetbrains.anko.toast
import kotlin.math.log

class MainActivity : AppCompatActivity(), SavePhotoListener {

    private val TAG = tag(this)
    private lateinit var libCamera: LibCamera

    private lateinit var libPermissions: LibPermissions
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissions = arrayOf<String>(Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)

        libPermissions = LibPermissions(this, permissions)

        val runnable = Runnable {
            logD(TAG, "All permission enabled")
        }
        libPermissions.askPermissions(runnable)

        libCamera = LibCamera(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.crop_image, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_load_image -> {
                logD(TAG, "Load button clicked")
                val runnable = Runnable {
                    libCamera.takePhoto()
                }
                libPermissions.askPermissions(runnable, CAMERA)
                return true
            }

            R.id.menu_crop_image -> {
                logD(TAG, "Crop button clicked")
                if (imageUri != null) {
                    libCamera.cropImage(imageUri!!)
                } else {
                    toast("Please capture or select an Image")
                }
                return true
            }

            R.id.menu_upload_image -> {
                logD(TAG, "Upload button clicked")
                if (imageUri != null) {
                    val path = libCamera.savePhotoInDeviceMemory(imageUri!!, DEFAULT_FILE_PREFIX, true)
                    toast(path!!)
                    val savePhotoCallback = SavePhotoCallback()
                    savePhotoCallback.setSavePhotoListener(this)
                    savePhotoCallback.onSavePhoto(path)
                } else {
                    toast("Please capture on select an Image!")
                }

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onPhotoSaved(imageInformation : ImageInformationObject) {
        if (imageInformation!= null){
            logD(TAG,"Image name ${imageInformation.imageName}")
            logD(TAG, "Date Stamp ${imageInformation.dateStamp}")
            logD(TAG, "Date time take photo ${imageInformation.dateTimeTakePhoto}")
            logD(TAG, "Image length ${imageInformation.imageLength}")
            logD(TAG, "Image Width ${imageInformation.imageWidth}")
            logD(TAG, "Model device ${imageInformation.modelDevice}")
            logD(TAG, "Make Company ${imageInformation.makeCompany}")
            logD(TAG, "Latitude ${imageInformation.latitude}")
            logD(TAG, "Longitude ${imageInformation.longitude}")
            logD(TAG, "Orientation ${imageInformation.orientation}")

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    val resultImageUri = libCamera.getPickImageResultUri(data!!)
                    imageUri = resultImageUri!!
                    Glide.with(this).load(imageUri).into(cameraImageView)
                }
            }

            CROP_PHOTO -> {
                if (data != null) {
                    imageUri = libCamera.cropImageActivityResult(requestCode, resultCode, data)!!
                    Glide.with(this).load(imageUri).into(cameraImageView)
                }
            }
        }
    }
}
