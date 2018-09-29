package com.nasibov.fakhri.neurelia.viewModel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.nasibov.fakhri.neurelia.R
import com.nasibov.fakhri.neurelia.base.BaseViewModel
import com.nasibov.fakhri.neurelia.model.photo.Photo
import com.nasibov.fakhri.neurelia.model.photo.PhotoDao
import com.nasibov.fakhri.neurelia.repository.network.NeureliaAPI
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject


class PhotoViewModel(private val photoDao: PhotoDao) : BaseViewModel() {

    @Inject
    lateinit var neureliaAPI: NeureliaAPI

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val snackbarMessage: MutableLiveData<Int> = MutableLiveData()
    val allPhotos: MutableLiveData<List<Photo>> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    init {
        getAllPhotos()
    }

    private fun getAllPhotos() {
        val subscription = photoDao.loadAllPhotos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrievePostListStart() }
                .doOnTerminate { onRetrievePostListFinish() }
                .subscribe(
                        { result -> onRetrievePostListSuccess(result) },
                        { onRetrievePostListError() }
                )
        compositeDisposable.add(subscription)
    }

    fun savePhoto(currentImage: File) {
        val photo = Photo(fileName = currentImage.name, filePath = currentImage.absolutePath)
        Single.fromCallable { photoDao.insert(photo) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    private fun onRetrievePostListStart() {
        Log.i("PhotoViewModel", "onRetrievePostListStart")
        loadingVisibility.value = View.VISIBLE
    }

    private fun onRetrievePostListFinish() {
        Log.i("PhotoViewModel", "onRetrievePostListFinish")
    }

    private fun onRetrievePostListSuccess(result: List<Photo>) {
        Log.i("PhotoViewModel", "list of size ${result.size}")
        allPhotos.value = result
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListError() {
        snackbarMessage.value = R.string.message_error
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

}