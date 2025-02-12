package com.pubscale.basicvideoplayer

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private lateinit var instance: MyApplication
        fun getAppContext(): Context {
            return instance.applicationContext
        }
    }
}