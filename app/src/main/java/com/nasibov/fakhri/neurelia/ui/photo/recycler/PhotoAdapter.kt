package com.nasibov.fakhri.neurelia.ui.photo.recycler

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nasibov.fakhri.neurelia.R
import com.nasibov.fakhri.neurelia.model.photo.Photo
import com.squareup.picasso.Picasso
import java.io.File
import android.graphics.Bitmap


class PhotoAdapter(private var photos: List<Photo>, private val context: Context) : RecyclerView.Adapter<PhotoHolder>() {

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
        val photoView = holder.photoView
        Picasso.get()
                .load(File(photos[position].filePath))
                .resize(1280, 720)
                .into(photoView)
    }


}