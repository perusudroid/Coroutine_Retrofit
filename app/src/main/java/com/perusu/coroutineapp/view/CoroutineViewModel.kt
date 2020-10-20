package com.perusu.coroutineapp.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.perusu.coroutineapp.data.domain.IRepo
import com.perusu.coroutineapp.data.model.Dog
import com.perusu.coroutineapp.data.model.GeneralResult
import com.perusu.coroutineapp.data.model.ResultOf
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
open class CoroutineViewModel(
    private val iRepo: IRepo
) : ViewModel(), CoroutineScope {

    private val parentJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + parentJob

    val mutableStateFlow = MutableStateFlow(0)

    val obTopTwoDogs = MutableLiveData<ResultOf<List<Dog>>>()
    val obDogList = MutableLiveData<ResultOf<List<Dog>>>()


    fun setMutableStateValue(){
        launch{
            (1..5).forEach {
                delay(500)
                mutableStateFlow.value = it
            }
        }
    }

    fun flowWithMap() : Flow<Int> = flow{
        (1..5).forEach {
            delay(500)
           emit(it)
        }
    }.map {it * it}


    val obFlowDogList: LiveData<ResultOf<List<Dog>>> = liveData {
        emit(ResultOf.Progress(true))
        val start = System.currentTimeMillis()
        iRepo.getDogListFlow()
            .catch { emit(ResultOf.Failure(it.message, it)) }
            .collect {
                emit(it)
                Log.d("CoroutineViewModel", "getDogListByFlow: ${getTimeDifference(start)}")
            }
        emit(ResultOf.Progress(false))
    }

    val flowWithBuilder: LiveData<Dog> = liveData {
        flowSample()
            .collect {
                emit(it)
            }
    }

    val flowWithoutLiveData: LiveData<Dog> = liveData {
        flowSample()
            .collect {
                emit(it)
            }
    }

    val obRandomTwoDogs = liveData {
        emit(GeneralResult.Progress(true))
        emitSource(getRandomTwoDogs())
    }

    private fun getRandomTwoDogs(): LiveData<GeneralResult> = liveData {
        while (true) {
            delay(10000)
            val topTwoDogsResult = iRepo.getRandomTwoDogs()
            emit(topTwoDogsResult)
        }
    }

    fun getTopDogList() {
        parentJob.cancelChildren()
        launch {
            obTopTwoDogs.value = ResultOf.Progress(true)
            val result = runCatching { iRepo.getTopTwoDogs() }
            result.onSuccess {
                obTopTwoDogs.value = it
            }.onFailure {
                obTopTwoDogs.value = ResultOf.Failure(it.message, it)
            }
            obTopTwoDogs.value = ResultOf.Progress(false)
        }
    }

    fun getDogList() {
        parentJob.cancelChildren()
        launch {
            obDogList.value = ResultOf.Progress(true)
            val start = System.currentTimeMillis()
            val result = runCatching { iRepo.getDogList() }
            result.onSuccess {
                obDogList.value = it
                Log.d("CoroutineViewModel", "getDogList: ${getTimeDifference(start)}")
            }.onFailure {
                obDogList.value = ResultOf.Failure(it.message, it)
            }
            obDogList.value = ResultOf.Progress(false)
        }
    }

    fun getDogListByFlow() {
        parentJob.cancelChildren()
        launch {
            val start = System.currentTimeMillis()
            iRepo.getDogListFlow()
                .catch { obDogList.value = ResultOf.Failure(it.message, it) }
                .collect {
                    obDogList.value = it
                    Log.d("CoroutineViewModel", "getDogListByFlow: ${getTimeDifference(start)}")
                }
            obDogList.value = ResultOf.Progress(false)
        }
    }

    private fun getTimeDifference(start: Long): String {
        val difference = System.currentTimeMillis() - start
        return buildString {
            append(difference / 1000)
            append(" seconds")
        }
    }


    fun flowSample(): Flow<Dog> = flow {
        emit(execute("German shepard"))
        emit(execute("Laprador"))
        emit(execute("Dobber man"))
    }.flowOn(Dispatchers.IO)

    private suspend fun execute(text: String): Dog {
        delay(500)
        return Dog(text, null)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancelChildren()
    }

}