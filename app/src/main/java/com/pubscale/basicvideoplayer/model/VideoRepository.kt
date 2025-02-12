package com.pubscale.basicvideoplayer.model

import android.util.Log
import com.google.gson.Gson
import com.pubscale.basicvideoplayer.MyApplication
import com.pubscale.basicvideoplayer.model.data.VideoModel

class VideoRepository {
    fun fetchVideo(): VideoModel? {
        return try {
            val context = MyApplication.getAppContext()
            val json = context.assets.open("video.json").bufferedReader().use { it.readText() }
            //parsing the json to model class
            Gson().fromJson(json, VideoModel::class.java)

        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
            null
        }
    }
}