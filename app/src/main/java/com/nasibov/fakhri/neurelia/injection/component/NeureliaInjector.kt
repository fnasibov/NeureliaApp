package com.nasibov.fakhri.neurelia.injection.component

import com.nasibov.fakhri.neurelia.injection.module.NetworkModule
import com.nasibov.fakhri.neurelia.viewModel.PhotoViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface NeureliaInjector {

    fun inject(photoViewModel: PhotoViewModel)

        @Component.Builder
        interface Builder{
            fun build(): NeureliaInjector
            fun networkModule(networkModule: NetworkModule): Builder
        }
}