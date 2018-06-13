package net.rmitsolutions.libcamlibrary

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import net.rmitsolutions.libcam.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private val LIB_CAMERA_RESULT = 999


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startLibCamActivity(view: View) {
        startActivityForResult(Intent(this, LibCameraActivity::class.java), LIB_CAMERA_RESULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            LIB_CAMERA_RESULT -> {
                Log.d("Data", "Length ${data?.getStringExtra(ImageData.Constants.IMAGE_LENGTH)}")
                Log.d("Data", "Width ${data?.getStringExtra(ImageData.Constants.IMAGE_WIDTH)}")
                Log.d("Data", "Date Stamp ${data?.getStringExtra(ImageData.Constants.IMAGE_TIME_STAMP)}")
                Log.d("Data", "Latitude ${data?.getStringExtra(ImageData.Constants.IMAGE_LATITUDE)}")
                Log.d("Data", "Longitude ${data?.getStringExtra(ImageData.Constants.IMAGE_LONGITUDE)}")
                Log.d("Data", "Time take photo ${data?.getStringExtra(ImageData.Constants.IMAGE_TIME_STAMP)}")
                val base64String = data?.getStringExtra(ImageData.Constants.IMAGE_STRING_BASE_64)
                Log.d("Data", "Base 64 - $base64String")
            }
        }
    }

}
