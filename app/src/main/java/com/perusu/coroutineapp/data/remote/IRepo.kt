package com.perusu.coroutineapp.data.remote

import com.perusu.coroutineapp.data.model.Dog
import com.perusu.coroutineapp.data.model.GeneralResult
import com.perusu.coroutineapp.data.model.ResultOf

interface IRepo {

    suspend fun getRandomTwoDogs(): GeneralResult

    suspend fun getTopTwoDogs(): ResultOf<List<Dog>>

    suspend fun getDogList() : ResultOf<List<Dog>>

}