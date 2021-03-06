package com.nasibov.fakhri.neurelia.viewModel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.nasibov.fakhri.neurelia.R
import com.nasibov.fakhri.neurelia.base.BaseViewModel
import com.nasibov.fakhri.neurelia.model.photo.Photo
import com.nasibov.fakhri.neurelia.model.photo.PhotoDao
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class PhotoViewModel(private val photoDao: PhotoDao) : BaseViewModel() {


    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val snackbarMessage: MutableLiveData<Int> = MutableLiveData()
    val allPhotos: MutableLiveData<List<Photo>> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    init {
        getAllPhotos()
    }

    private fun getAllPhotos() {
        compositeDisposable.add(
                photoDao.loadAllPhotos()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { onRetrievePostListStart() }
                        .doOnTerminate { onRetrievePostListFinish() }
                        .subscribe(
                                { result -> onRetrievePostListSuccess(result) },
                                { onRetrievePostListError() }
                        )
        )
    }

    @Suppress("SimpleDateFormat")
    fun createImageFile(externalFilesDir: File?): File {
        val date = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "JPEG_$date"

        return externalFilesDir?.let { File.createTempFile(fileName, ".jpg", it) }!!
    }

    fun savePhoto(currentImage: File) {
        val photo = Photo(fileName = currentImage.name, filePath = currentImage.absolutePath)
        compositeDisposable.add(
                Single.fromCallable { photoDao.insert(photo) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe()
        )
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