package com.nasibov.fakhri.neurelia.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nasibov.fakhri.neurelia.R
import com.nasibov.fakhri.neurelia.ui.photo.PhotoFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainBottomAppBar)
        if (savedInstanceState == null) {
            val photoFragment = PhotoFragment.newInstance()
            photoFab.setOnClickListener { photoFragment.takePhoto() }
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, photoFragment)
                    .commitNow()
        }
    }
}
