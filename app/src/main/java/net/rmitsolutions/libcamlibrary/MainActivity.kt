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
                Log.d("Data", "Length ${data?.getStringExtra("imageLength")}")
                Log.d("Data", "Width ${data?.getStringExtra("imageWidth")}")
                Log.d("Data", "Date Stamp ${data?.getStringExtra("dateStamp")}")
                Log.d("Data", "Latitude ${data?.getStringExtra("latitude")}")
                Log.d("Data", "Longitude ${data?.getStringExtra("longitude")}")
            }
        }
    }

}
