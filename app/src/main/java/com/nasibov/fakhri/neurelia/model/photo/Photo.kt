package com.nasibov.fakhri.neurelia.model.photo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Photo(@field:PrimaryKey val id: Int,
                 val fileName: String,
                 val filePath: String)