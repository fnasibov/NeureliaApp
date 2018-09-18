package com.nasibov.fakhri.neurelia.model.photo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photo")
    fun loadAllPhotos(): Flowable<List<Photo>>

    @Insert(onConflict = REPLACE)
    fun insert(photo: Photo)
}