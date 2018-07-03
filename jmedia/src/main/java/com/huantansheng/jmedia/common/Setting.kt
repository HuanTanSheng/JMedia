package com.huantansheng.jmedia.common

import android.view.View
import com.huantansheng.jmedia.extensionInterface.ImageEngine
import com.huantansheng.jmedia.mediaModel.entity.MediaFile
import java.lang.ref.WeakReference
import java.util.*

object Setting {
    var minWidth = 1
    var minHeight = 1
    var minSize: Long = 1
    var count = 1
    var photosAdView: WeakReference<View>? = null
    var albumItemsAdView: WeakReference<View>? = null
    var photoAdIsOk = false
    var albumItemsAdIsOk = false
    var selectedMedias = ArrayList<MediaFile>()
    var showOriginalMenu = false
    var originalMenuUsable = false
    var originalMenuUnusableHint = ""
    var selectedOriginal = false
    var fileProviderAuthority: String? = null
    var isShowCamera = false
    var onlyStartCamera = false
    var showPuzzleMenu = true
    var showGif = true
    var imageEngine: ImageEngine? = null
    var mediaType: MediaType = MediaType.ALL

    fun clear() {
        minWidth = 1
        minHeight = 1
        minSize = 1
        count = 1
        photosAdView = null
        albumItemsAdView = null
        photoAdIsOk = false
        albumItemsAdIsOk = false
        selectedMedias.clear()
        showOriginalMenu = false
        originalMenuUsable = false
        originalMenuUnusableHint = ""
        selectedOriginal = false
        isShowCamera = false
        onlyStartCamera = false
        showPuzzleMenu = true
        showGif = true
        mediaType = MediaType.IMAGE
    }

    fun hasPhotosAd(): Boolean {
        return photosAdView != null && photosAdView!!.get() != null
    }

    fun hasAlbumItemsAd(): Boolean {
        return albumItemsAdView != null && albumItemsAdView!!.get() != null
    }
}