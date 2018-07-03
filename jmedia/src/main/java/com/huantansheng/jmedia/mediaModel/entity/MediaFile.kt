package com.huantansheng.jmedia.mediaModel.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 媒体文件
 *
 * @param name  名称
 * @param path  路径地址
 * @param isImage  是否是图片，否既是视频
 * @param isVideo  是否是视频，否既是图片
 * @param type  类型
 * @param width  宽
 * @param height  高
 * @param size  文件大小，单位：Bytes
 * @param createTime  文件被创建的时间戳,单位：毫秒
 * @param selected  是否被选中,内部使用,无需关心
 * @param selectedOriginal  用户选择时是否选择了原图选项
 *
 */
@Parcelize
data class MediaFile(val name: String,
                     val path: String,
                     val isImage: Boolean,
                     val isVideo: Boolean,
                     val type: String,
                     var width: Int,
                     var height: Int,
                     val size: Long,
                     val createTime: Long,
                     var selected: Boolean = false,
                     var selectedOriginal: Boolean = false) : Parcelable