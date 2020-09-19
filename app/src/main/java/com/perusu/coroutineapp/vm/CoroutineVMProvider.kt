package com.perusu.coroutineapp.vm

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.perusu.coroutineapp.view.CoroutineViewModel

object CoroutineVMProvider {

    fun obtainMainViewModel(frag: Fragment): CoroutineViewModel {
        return ViewModelProvider(
            frag,
            ViewModelFactory.getInstance()
        ).get(CoroutineViewModel::class.java)
    }

}