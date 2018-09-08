package com.nasibov.fakhri.neurelia.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nasibov.fakhri.neurelia.R
import com.nasibov.fakhri.neurelia.ui.photo.PhotoFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, PhotoFragment.newInstance())
                    .commitNow()
        }
    }
}
