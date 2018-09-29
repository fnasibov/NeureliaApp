package com.nasibov.fakhri.neurelia.ui.photo


import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.nasibov.fakhri.neurelia.R
import com.nasibov.fakhri.neurelia.injection.ViewModelFactory
import com.nasibov.fakhri.neurelia.ui.photo.recycler.PhotoAdapter
import com.nasibov.fakhri.neurelia.util.rotate
import com.nasibov.fakhri.neurelia.viewModel.PhotoViewModel
import kotlinx.android.synthetic.main.fragment_photo.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PhotoFragment : Fragment() {

    private lateinit var mViewModel: PhotoViewModel
    private lateinit var mCurrentImage: File
    private lateinit var mFaceDetector: FirebaseVisionFaceDetector

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_photo, container, false)
        mViewModel = ViewModelProviders.of(this, ViewModelFactory(activity as AppCompatActivity)).get(PhotoViewModel::class.java)
        mViewModel.snackbarMessage.observe(this, Observer { errorMessage -> if (errorMessage != null) showSnackbar(errorMessage) })

        FirebaseApp.initializeApp(activity?.applicationContext)

        val faceDetectorOptions = FirebaseVisionFaceDetectorOptions.Builder()
                .setModeType(FirebaseVisionFaceDetectorOptions.FAST_MODE)
                .setLandmarkType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .setTrackingEnabled(false)
                .build()

        mFaceDetector = FirebaseVision.getInstance().getVisionFaceDetector(faceDetectorOptions)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.loadingVisibility.observe(this, Observer { photoProgressBar.visibility = it })
        recyclerPhoto.layoutManager = context?.let { GridLayoutManager(it, 3) }
        val photoAdapter = context?.let { PhotoAdapter(listOf(), it, mFaceDetector) }
        recyclerPhoto.adapter = photoAdapter
        mViewModel.allPhotos.observe(this, Observer { photoList -> photoAdapter?.updatePhotoList(photoList) })
        activity?.findViewById<FloatingActionButton>(R.id.mainFab)?.apply {
            setImageDrawable(resources.getDrawable(R.drawable.ic_camera_alt_24px, activity?.applicationContext?.theme))
            setOnClickListener { takePhoto() }
        }
    }

    private fun showSnackbar(@StringRes errorMessage: Int) {
        Log.i("PhotoFragment", "Snackbar showed!")
        val marginBottom = resources.getDimension(R.dimen.snackbar_bottom_margin).toInt()
        val snackbar = Snackbar.make(photoListContainer, errorMessage, Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        val layoutParams = snackbarView.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.apply {
            setMargins(
                    leftMargin,
                    topMargin,
                    rightMargin,
                    bottomMargin + marginBottom
            )
        }
        snackbar.show()
    }

    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity?.packageManager!!).also {
                val photoFile: File = try {
                    createImageFile()
                } catch (e: IOException) {
                    error { e }
                }

                val photoURI: Uri = FileProvider.getUriForFile(activity?.applicationContext!!,
                        "com.nasibov.fakhri.neurelia.fileprovider",
                        photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, PhotoFragment.REQUEST_TAKE_PHOTO)
            }

        }
    }

    @Suppress("SimpleDateFormat")
    private fun createImageFile(): File {
        val date = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "JPEG_$date"
        val filesDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(fileName, ".jpg", filesDir)

        mCurrentImage = image
        return mCurrentImage
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            val absolutePath = mCurrentImage.absolutePath
            val exifInterface = ExifInterface(absolutePath)
            val rotation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
            val angle: Float = when (rotation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90F
                ExifInterface.ORIENTATION_ROTATE_180 -> 180F
                ExifInterface.ORIENTATION_ROTATE_270 -> 270F
                ExifInterface.ORIENTATION_NORMAL -> 0F
                else -> 0F
            }

            val bitmap = when (angle) {
                0F -> BitmapFactory.decodeFile(absolutePath)
                else -> BitmapFactory.decodeFile(absolutePath).rotate(angle)
            }

            val visionImage = bitmap?.let { FirebaseVisionImage.fromBitmap(it) }

            visionImage?.let {
                mFaceDetector.detectInImage(it)
                        .addOnSuccessListener { mutableList: MutableList<FirebaseVisionFace>? ->
                            Toast.makeText(context, "Faces found: ${mutableList?.size}", Toast.LENGTH_SHORT).show()
                        }
            }

            mViewModel.savePhoto(mCurrentImage)
        }
    }

    companion object {

        const val REQUEST_TAKE_PHOTO = 1

        @JvmStatic
        fun newInstance() = PhotoFragment()
    }
}
