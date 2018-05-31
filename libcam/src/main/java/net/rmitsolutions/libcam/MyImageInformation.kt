package net.rmitsolutions.libcam

import android.media.ExifInterface
import java.io.IOException

class MyImageInformation {

    val imageInformationObject: ImageInformationObject


    init {
        imageInformationObject = ImageInformationObject()
    }


    private fun getAllFeatures(realPath: String): ExifInterface? {
        if (realPath != "") {
            var exif: ExifInterface? = null
            try {
                exif = ExifInterface(realPath)
                return exif
            } catch (e: IOException) {
                return exif
            }

        } else {
            return null
        }
    }

    fun getImageInformation(realPath: String): ImageInformationObject? {
        try {
            val exif = getAllFeatures(realPath)
            if (exif != null) {
                val latitude : String? = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
                val longitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
                val string = "0.0"
                if (latitude == string){
                    imageInformationObject.latitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE).toFloat()
                }

                if (longitude == string){
                    imageInformationObject.longitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE).toFloat()
                }

                if (exif.getAttribute(ExifInterface.TAG_DATETIME)!= null){
                    imageInformationObject.dateTimeTakePhoto = exif.getAttribute(ExifInterface.TAG_DATETIME)
                }else {
                    imageInformationObject.dateTimeTakePhoto = exif.getAttribute(ExifInterface.TAG_DATETIME)
                }

                if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)!= null){
                    imageInformationObject.orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                }

                if (exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP)!=null){
                    imageInformationObject.dateStamp = exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP)
                }else{
                    imageInformationObject.dateStamp = exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP)
                }

                if (exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)!= null){
                    imageInformationObject.imageLength = exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)
                }else{
                    imageInformationObject.imageLength = exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)
                }


                if (exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)!=null){
                    imageInformationObject.imageWidth = exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)
                }else {
                    imageInformationObject.imageWidth = exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)
                }


                if (exif.getAttribute(ExifInterface.TAG_MODEL)!=null){
                    imageInformationObject.modelDevice = exif.getAttribute(ExifInterface.TAG_MODEL)
                }else {
                    imageInformationObject.modelDevice = exif.getAttribute(ExifInterface.TAG_MODEL)
                }

                if (exif.getAttribute(ExifInterface.TAG_MAKE)!= null){
                    imageInformationObject.makeCompany = exif.getAttribute(ExifInterface.TAG_MAKE)
                }else {
                    imageInformationObject.makeCompany = exif.getAttribute(ExifInterface.TAG_MAKE)
                }

                return imageInformationObject
            } else {
                return null
            }
        } catch (ex: Exception) {
            return null
        }

    }
}