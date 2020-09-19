package com.perusu.coroutineapp.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: T
//    @SerializedName("message") val message: Map<String,List<String>>
)