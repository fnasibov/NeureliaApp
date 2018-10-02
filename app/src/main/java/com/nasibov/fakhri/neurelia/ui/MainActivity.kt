package com.nasibov.fakhri.neurelia.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.nasibov.fakhri.neurelia.R
import com.nasibov.fakhri.neurelia.ui.photo.PhotoFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainBottomAppBar as Toolbar?)
        if (savedInstanceState == null) {
            val photoFragment = PhotoFragment.newInstance()
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, photoFragment)
                    .commitNow()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
