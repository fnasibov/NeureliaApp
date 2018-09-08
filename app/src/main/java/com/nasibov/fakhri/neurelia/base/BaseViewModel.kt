package com.nasibov.fakhri.neurelia.base

import androidx.lifecycle.ViewModel
import com.nasibov.fakhri.neurelia.injection.component.DaggerNeureliaInjector
import com.nasibov.fakhri.neurelia.injection.component.NeureliaInjector
import com.nasibov.fakhri.neurelia.injection.module.NetworkModule
import com.nasibov.fakhri.neurelia.viewModel.PhotoViewModel
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    private val injector: NeureliaInjector = DaggerNeureliaInjector
            .builder()
            .networkModule(NetworkModule)
            .build()

    init {
        inject()
    }

    private lateinit var subscription: Disposable

    private fun inject() {
        when (this) {
            is PhotoViewModel -> injector.inject(this)
        }
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}