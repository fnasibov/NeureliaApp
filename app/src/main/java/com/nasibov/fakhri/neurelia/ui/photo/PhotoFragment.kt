package com.nasibov.fakhri.neurelia.ui.photo


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar

import com.nasibov.fakhri.neurelia.R
import com.nasibov.fakhri.neurelia.injection.ViewModelFactory
import com.nasibov.fakhri.neurelia.viewModel.PhotoViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PhotoFragment : Fragment() {

    private var errorSnackbar: Snackbar? = null
    private lateinit var viewModel: PhotoViewModel
    private lateinit var currentImage: File

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_photo, container, false)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(activity as AppCompatActivity)).get(PhotoViewModel::class.java)
        viewModel.errorMassage.observe(this, Observer { errorMessage -> if (errorMessage != null) showError(errorMessage) else hideError() })
        return view
    }

    private fun showError(@StringRes errorMessage: Int) {
        Toast.makeText(activity?.applicationContext,
                errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }

    fun takePhoto() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity?.packageManager!!) != null) {
            val photoFile: File
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                error { e }
            }
            val photoURI = FileProvider.getUriForFile(activity?.applicationContext!!, "com.nasibov.fakhri.neurelia.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, PhotoFragment.REQUEST_TAKE_PHOTO)
        }
    }

    @Suppress("SimpleDateFormat")
    private fun createImageFile(): File {
        val date = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "JPEG_$date"
        val filesDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(fileName, ".jpg", filesDir)
        currentImage = image
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            viewModel.savePhoto(currentImage)
        }
    }

    companion object {

        const val REQUEST_TAKE_PHOTO = 1

        @JvmStatic
        fun newInstance() = PhotoFragment()
    }


}
