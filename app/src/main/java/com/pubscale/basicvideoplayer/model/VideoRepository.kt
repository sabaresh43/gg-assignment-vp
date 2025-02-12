package com.pubscale.basicvideoplayer.model

import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.pubscale.basicvideoplayer.MyApplication
import com.pubscale.basicvideoplayer.model.data.VideoModel
import com.pubscale.basicvideoplayer.utils.checkForInternet
import java.io.IOException

class VideoRepository {
    fun fetchVideo(): VideoModel? {
        return try {
            val context = MyApplication.getAppContext()
            if(!checkForInternet(context)){
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show()
                throw IOException("No internet connection")
            }

            val json = context.assets.open("video.json").bufferedReader().use { it.readText() }
            //parsing the json to model class
            Gson().fromJson(json, VideoModel::class.java)

        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
            null
        }
    }
}