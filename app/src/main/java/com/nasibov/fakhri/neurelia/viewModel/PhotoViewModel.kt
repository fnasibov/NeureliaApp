package com.nasibov.fakhri.neurelia.viewModel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.nasibov.fakhri.neurelia.base.BaseViewModel
import com.nasibov.fakhri.neurelia.repository.network.NeureliaAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PhotoViewModel : BaseViewModel() {

    @Inject
    lateinit var neureliaAPI: NeureliaAPI

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    init {
        loadPosts()
    }

    private lateinit var subscription: Disposable

    private fun loadPosts() {
        subscription = neureliaAPI.getPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrievePostListStart() }
                .doOnTerminate { onRetrievePostListFinish() }
                .subscribe(
                        { onRetrievePostListSuccess() },
                        { onRetrievePostListError(it) }
                )
    }

    private fun onRetrievePostListStart() {
        Log.i("PhotoViewModel", "onRetrievePostListStart")
        loadingVisibility.value = View.VISIBLE
    }

    private fun onRetrievePostListFinish() {
        Log.i("PhotoViewModel", "onRetrievePostListFinish")
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess() {
        Log.i("PhotoViewModel", "onRetrievePostListSuccess")
    }

    private fun onRetrievePostListError(throwable: Throwable) {
        Log.e("PhotoViewModel", "onRetrievePostListError: ${throwable.stackTrace}")
    }
}