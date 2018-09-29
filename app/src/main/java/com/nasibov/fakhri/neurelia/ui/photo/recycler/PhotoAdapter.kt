package com.nasibov.fakhri.neurelia.ui.photo.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.nasibov.fakhri.neurelia.R
import com.nasibov.fakhri.neurelia.model.photo.Photo
import com.nasibov.fakhri.neurelia.util.GlideApp
import java.io.File


class PhotoAdapter(private var photos: List<Photo>, private val context: Context, private val faceDetector: FirebaseVisionFaceDetector) : RecyclerView.Adapter<PhotoHolder>() {

    fun updatePhotoList(newPhotos: List<Photo>) {
        photos = newPhotos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        return PhotoHolder(LayoutInflater.from(context).inflate(R.layout.recycler_photo_item, parent, false))
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {

        val file = File(photos[position].filePath)
        GlideApp.with(context)
                .load(file)
                .override(1280, 720)
                .into(holder.photoView)


    }
}