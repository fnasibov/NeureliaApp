package com.nasibov.fakhri.neurelia.injection

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.nasibov.fakhri.neurelia.repository.db.NeureliaDatabase
import com.nasibov.fakhri.neurelia.viewModel.PhotoViewModel

class ViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoViewModel::class.java)) {
            val db = Room.databaseBuilder(activity.applicationContext, NeureliaDatabase::class.java, "photos").build()
            @Suppress("UNCHECKED_CAST")
            return PhotoViewModel(db.photoDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}