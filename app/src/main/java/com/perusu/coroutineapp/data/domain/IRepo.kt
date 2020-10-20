package com.perusu.coroutineapp.data.domain

import com.perusu.coroutineapp.data.model.Dog
import com.perusu.coroutineapp.data.model.GeneralResult
import com.perusu.coroutineapp.data.model.ResultOf
import kotlinx.coroutines.flow.Flow

interface IRepo {

    suspend fun getRandomTwoDogs(): GeneralResult

    suspend fun getTopTwoDogs(): ResultOf<List<Dog>>

    suspend fun getDogList() : ResultOf<List<Dog>>

    suspend fun getDogListFlow() : Flow<ResultOf<List<Dog>>>

}