package com.huantansheng.jmedia.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.huantansheng.jmedia.mediaModel.MediaModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    public fun initAlbum(view:View) {
        MediaModel.initAlbum(this)
    }
}
