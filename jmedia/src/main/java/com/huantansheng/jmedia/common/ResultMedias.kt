package com.huantansheng.jmedia.common

import android.graphics.BitmapFactory
import android.os.Build
import com.huantansheng.jmedia.mediaModel.entity.MediaFile
import java.util.ArrayList

object ResultMedias {

    var mediaFiles = ArrayList<MediaFile>()

    fun addMediaFile(mediaFile: MediaFile) {
        mediaFile.selected = true
        mediaFiles.add(mediaFile)
    }

    fun removeMediaFile(mediaFile: MediaFile) {
        mediaFile.selected = false
        mediaFiles.remove(mediaFile)
    }

    fun removeMediaFile(mediaFileIndex: Int) {
        removeMediaFile(mediaFiles[mediaFileIndex])
    }

    fun removeAll() {
        val size = mediaFiles.size
        for (i in 0 until size) {
            removeMediaFile(0)
        }
    }

    fun processOriginal() {
        val isIceApi = Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
        if (Setting.showOriginalMenu) {
            if (Setting.originalMenuUsable) {
                for (mediaFile in mediaFiles) {
                    mediaFile.selectedOriginal = Setting.selectedOriginal
                    if (isIceApi && mediaFile.width == 0) {
                        val options = BitmapFactory.Options()
                        options.inJustDecodeBounds = true
                        BitmapFactory.decodeFile(mediaFile.path, options)
                        mediaFile.width = options.outWidth
                        mediaFile.height = options.outHeight
                    }
                }
            }
        }
    }

    fun clear() {
        mediaFiles.clear()
    }

    fun isEmpty(): Boolean {
        return mediaFiles.isEmpty()
    }

    fun count(): Int {
        return mediaFiles.size
    }

    /**
     * 获取选择器应该显示的数字
     *
     * @param mediaFile 当前图片
     * @return 选择器应该显示的数字
     */
    fun getSelectorNumber(mediaFile: MediaFile): String {
        return (mediaFiles.indexOf(mediaFile) + 1).toString()
    }

    fun getMediaFilePath(position: Int): String {
        return mediaFiles[position].path
    }

    fun getMediaFileType(position: Int): String {
        return mediaFiles[position].type
    }
}