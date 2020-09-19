package com.perusu.coroutineapp.vm

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.perusu.coroutineapp.view.CoroutineViewModel
import com.perusu.coroutineapp.data.remote.NetworkModule
import com.perusu.coroutineapp.data.remote.DogsRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repo: DogsRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(CoroutineViewModel::class.java) -> CoroutineViewModel(repo)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance() =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(
                    DogsRepository(NetworkModule.makeApiService())
                ).also { INSTANCE = it }
            }

        fun destroyInstance() {
            INSTANCE = null
        }
    }


}
