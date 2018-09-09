package com.nasibov.fakhri.neurelia.ui.photo


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar

import com.nasibov.fakhri.neurelia.R
import com.nasibov.fakhri.neurelia.databinding.FragmentPhotoBinding
import com.nasibov.fakhri.neurelia.injection.ViewModelFactory
import com.nasibov.fakhri.neurelia.viewModel.PhotoViewModel

class PhotoFragment : Fragment() {

    private var errorSnackbar: Snackbar? = null
    private lateinit var binding:FragmentPhotoBinding
    private lateinit var viewModel:PhotoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo, container,false)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(activity as AppCompatActivity)).get(PhotoViewModel::class.java)
        val view = binding.root
        viewModel.errorMassage.observe(this, Observer { errorMessage -> if (errorMessage != null) showError(errorMessage) else hideError() })
        binding.viewModel = viewModel
        return view
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, viewModel.errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }


    companion object {

        @JvmStatic
        fun newInstance() = PhotoFragment()
    }
}
