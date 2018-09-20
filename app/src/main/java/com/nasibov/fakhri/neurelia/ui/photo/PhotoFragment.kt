package com.nasibov.fakhri.neurelia.ui.photo


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

import com.nasibov.fakhri.neurelia.R
import com.nasibov.fakhri.neurelia.injection.ViewModelFactory
import com.nasibov.fakhri.neurelia.ui.photo.recycler.PhotoAdapter
import com.nasibov.fakhri.neurelia.viewModel.PhotoViewModel
import kotlinx.android.synthetic.main.fragment_photo.*
import java.io.File
import java.io.IOException
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*

class PhotoFragment : Fragment() {

    private lateinit var viewModel: PhotoViewModel
    private lateinit var currentImage: File

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_photo, container, false)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(activity as AppCompatActivity)).get(PhotoViewModel::class.java)
        viewModel.SnackbarMessage.observe(this, Observer { errorMessage -> if (errorMessage != null) showSnackbar(errorMessage) })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadingVisibility.observe(this, Observer { photoProgressBar.visibility = it })
        recyclerPhoto.layoutManager = GridLayoutManager(activity?.applicationContext!!, 3)
        val photoAdapter = PhotoAdapter(listOf(), activity?.applicationContext!!)
        recyclerPhoto.adapter = photoAdapter
        viewModel.allPhotos.observe(this, Observer { photoList -> photoAdapter.updatePhotoList(photoList) })
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
