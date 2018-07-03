package com.huantansheng.jmedia.mediaModel.entity

import com.huantansheng.jmedia.mediaModel.MediaModel

/**
 * 媒体专辑项
 * @param name  名称
 * @param albumPath  专辑路径地址
 * @param coverPath  专辑封面路径
 * @param mediaFiles  专辑内媒体文件集合
 */
data class AlbumItem(val name: String, val albumPath: String, val coverPath: String, val mediaFiles: ArrayList<MediaFile> = ArrayList<MediaFile>()) {

    /**
     * 添加媒体文件
     * @param media  添加的媒体文件
     */
    fun addMedia(media: MediaFile) {
        mediaFiles.add(media)
    }

    /**
     * 添加媒体文件到指定位置
     * @param index  添加的媒体文件的位置
     * @param media  添加的媒体文件
     */
    fun addMedia(index: Int, media: MediaFile) {
        mediaFiles.add(index, media)
    }
}