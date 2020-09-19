package com.perusu.coroutineapp.common

import android.app.Application

private var application: CoroutineApp? = null

class CoroutineApp : Application(){

    companion object{
        fun getApplicationContext(): CoroutineApp {
            return application!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }

}