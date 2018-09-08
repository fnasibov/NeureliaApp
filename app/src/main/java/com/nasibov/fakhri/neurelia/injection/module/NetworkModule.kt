package com.nasibov.fakhri.neurelia.injection.module

import com.nasibov.fakhri.neurelia.repository.network.NeureliaAPI
import com.nasibov.fakhri.neurelia.util.API_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@Suppress("unused")
object NetworkModule {

    @Provides
    @Reusable
    @JvmStatic
    internal fun providesNeureliaApi(retrofit: Retrofit): NeureliaAPI {
        return retrofit.create(NeureliaAPI::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }
}