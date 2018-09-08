package com.nasibov.fakhri.neurelia.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nasibov.fakhri.neurelia.model.photo.Photo
import com.nasibov.fakhri.neurelia.model.photo.PhotoDao

@Database(entities = arrayOf(Photo::class), version = 1)
abstract class NeureliaDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao
}