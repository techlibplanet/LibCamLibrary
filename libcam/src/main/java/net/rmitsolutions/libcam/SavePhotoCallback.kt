package net.rmitsolutions.libcam

class SavePhotoCallback {
    var listener : SavePhotoListener? = null

    fun setSavePhotoListener(savePhotoListener: SavePhotoListener){
        listener = savePhotoListener
    }

    fun onSavePhoto(mCurrentPhotoPath: String) {
        if (listener!=null){
            val imageInformation = ImageInformation()
            val data =imageInformation.getImageInformation(mCurrentPhotoPath)
            listener?.onPhotoSaved(data!!)
        }
    }
}