package com.nasibov.fakhri.neurelia.model.photo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Photo(val fileName: String,
                 val filePath: String){
    @field:PrimaryKey(autoGenerate = true)
    var id: Long = 0
}