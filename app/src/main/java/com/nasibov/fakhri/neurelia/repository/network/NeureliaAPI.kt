package com.nasibov.fakhri.neurelia.repository.network

import com.nasibov.fakhri.neurelia.model.post.Post
import io.reactivex.Observable
import retrofit2.http.GET

interface NeureliaAPI {

    @GET("/posts")
    fun getPosts(): Observable<List<Post>>
}