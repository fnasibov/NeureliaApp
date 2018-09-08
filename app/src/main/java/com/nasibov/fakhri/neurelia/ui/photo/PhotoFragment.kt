package com.nasibov.fakhri.neurelia.ui.photo


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.nasibov.fakhri.neurelia.R

class PhotoFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }


    companion object {

        @JvmStatic
        fun newInstance() =
                PhotoFragment()
    }
}
