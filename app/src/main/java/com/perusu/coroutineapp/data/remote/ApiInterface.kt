package com.perusu.coroutineapp.data.remote

import LoginResponse
import com.perusu.coroutineapp.data.model.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface{

    // with call
    @GET("breeds/list/all")
    fun getBreedsListAsync(): Call<ApiResponse<Map<String, List<String>>>>

    @GET("breed/{breedName}/images/random")
    fun getImageByUrlAsync(@Path("breedName") breedName: String): Call<ApiResponse<String>>

    // without call
    @GET("breeds/list/all")
    suspend fun getBreedsList(): ApiResponse<Map<String, List<String>>>

    @GET("breed/{breedName}/images/random")
    suspend fun getImageByUrl(@Path("breedName") breedName: String): ApiResponse<String>

    //flow
    @GET("breeds/list/all")
    suspend fun getBreedsListFlow(): ApiResponse<Map<String, List<String>>>

    @POST("auth/token")
    suspend fun login(@Body request: HashMap<String, String?>?): LoginResponse
}