package com.perusu.coroutineapp.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.perusu.coroutineapp.data.model.Dog
import com.perusu.coroutineapp.data.model.GeneralResult
import com.perusu.coroutineapp.data.model.ResultOf
import com.perusu.coroutineapp.data.remote.IRepo
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class CoroutineViewModel(
    private val iRepo: IRepo
) : ViewModel(), CoroutineScope {

    private val parentJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + parentJob

    val obTopTwoDogs = MutableLiveData<ResultOf<List<Dog>>>()
    val obDogList = MutableLiveData<ResultOf<List<Dog>>>()
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

    fun getDogList() {
        parentJob.cancelChildren()
        launch {
            obDogList.value = ResultOf.Progress(true)
            val result = runCatching { iRepo.getDogList() }
            result.onSuccess {
                obDogList.value = it
            }.onFailure {
                obDogList.value = ResultOf.Failure(it.message, it)
            }
            obDogList.value = ResultOf.Progress(false)
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

    private fun getTimeDifference(start: Long): String {
        val difference = System.currentTimeMillis() - start
        return buildString {
            append(difference / 1000)
            append(" seconds")
        }
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancelChildren()
    }

}