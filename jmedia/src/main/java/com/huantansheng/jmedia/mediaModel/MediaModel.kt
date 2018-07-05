package com.huantansheng.jmedia.mediaModel


import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.huantansheng.jmedia.R
import com.huantansheng.jmedia.common.MediaType
import com.huantansheng.jmedia.common.ResultMedias
import com.huantansheng.jmedia.common.Setting
import com.huantansheng.jmedia.mediaModel.entity.Album
import com.huantansheng.jmedia.mediaModel.entity.MediaFile
import java.io.File

/**
 * 媒体的业务层
 */
object MediaModel {
    private val album = Album()//媒体专辑，内含专辑列表
    private val isNotAceSdk = Build.VERSION.SDK_INT != Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1

    private var allImageAndVideo = "" //全部图片和视频的专辑名称
    private var allImage = "" //全部图片的专辑名称
    private var allVideo = "" //全部视频的专辑名称


    /**
     * 初始化album
     * @param context 上下文，用于获取contentResolver
     */
    fun initAlbum(context: Context) {
        if (album.isNotEmpty()) return

        if (allImage.isEmpty()) {
            allImageAndVideo = context.getString(R.string.j_media_all_image_and_video)
            allImage = context.getString(R.string.j_media_all_image)
            allVideo = context.getString(R.string.j_media_all_video)
        }

        val contentResolver = context.contentResolver  //contentResolver

        initImageAlbum(contentResolver) //装载图片媒体库

        initVideoAlbum(contentResolver) //装载视频媒体库

    }

    @SuppressLint("InlinedApi")
    private fun initImageAlbum(contentResolver: ContentResolver) {
        if (Setting.mediaType == MediaType.VIDEO) return

        val imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI      //外部ImageUri
        val imageSortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC"    //拍摄日期倒叙

        val imageCursor = contentResolver.query(imageUri, getImageProjections(), null, null, imageSortOrder)

        when {
            null == imageCursor -> Log.d("JMedia", "imageCursor = null")

            imageCursor.moveToFirst() -> {

                val pathCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)
                val nameCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                val dateCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)
                val mimeType = imageCursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)
                val sizeCol = imageCursor.getColumnIndex(MediaStore.Images.Media.SIZE)
                var widthCol = 0
                var heightCol = 0
                if (isNotAceSdk) {
                    widthCol = imageCursor.getColumnIndex(MediaStore.Images.Media.WIDTH)
                    heightCol = imageCursor.getColumnIndex(MediaStore.Images.Media.HEIGHT)
                }

                do {
                    val path = imageCursor.getString(pathCol)
                    val name = imageCursor.getString(nameCol)
                    val dateTime = imageCursor.getLong(dateCol)
                    val type = imageCursor.getString(mimeType)
                    val size = imageCursor.getInt(sizeCol).toLong()
                    var width = 0
                    var height = 0
                    if (!Setting.showGif) {
                        if (path.endsWith(".gif") || type.endsWith(".gif")) {
                            continue
                        }
                    }
                    if (size < Setting.minSize) {
                        continue
                    }

                    if (isNotAceSdk) {
                        width = imageCursor.getInt(widthCol)
                        height = imageCursor.getInt(heightCol)
                        if (width < Setting.minWidth || height < Setting.minHeight) {
                            continue
                        }
                    }

                    //过滤媒体库中存在但实际不存在的文件
                    val file = File(path)
                    if (!file.exists()) {
                        continue
                    }

                    val imageItem = MediaFile(name, path, true, false, type, width, height, size, dateTime)

                    //处理默认勾选
                    Setting.selectedMedias.forEach {
                        if (path == it.path) {
                            imageItem.selectedOriginal = Setting.selectedOriginal
                            ResultMedias.addMediaFile(imageItem)
                        }
                    }

                    // 初始化“全部”专辑
                    if (album.isEmpty()) {
                        // 用第一个图片作为专辑的封面
                        album.addAlbumItem(allImage, "", path)
                    }

                    // 把图片全部放进“全部”专辑
                    album.getAlbumItem(allImage)?.addMedia(imageItem)

                    // 添加当前图片的专辑到专辑模型实体中
                    val folderPath = File(path).parentFile.absolutePath

                    val albumName = folderPath.substring(folderPath.lastIndexOf("/"), folderPath.lastIndex)
                    album.addAlbumItem(albumName, folderPath, path)
                    album.getAlbumItem(albumName)?.addMedia(imageItem)
                } while (imageCursor.moveToNext())
                imageCursor.close()
            }
        }
    }


    @SuppressLint("InlinedApi")
    private fun initVideoAlbum(contentResolver: ContentResolver) {

        if (Setting.mediaType == MediaType.IMAGE) return

        val videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI      //外部VideoUri
        val videoSortOrder = MediaStore.Video.Media.DATE_TAKEN + " DESC" //拍摄日期倒叙

        val videoCursor = contentResolver.query(videoUri, getVideoProjections(), null, null, videoSortOrder)

        when {
            null == videoCursor -> Log.d("JMedia", "videoCursor = null")

            videoCursor.moveToFirst() -> {

                val albumItemAllName = "所有视频"
                val pathCol = videoCursor.getColumnIndex(MediaStore.Video.Media.DATA)
                val nameCol = videoCursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)
                val dateCol = videoCursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN)
                val mimeType = videoCursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE)
                val sizeCol = videoCursor.getColumnIndex(MediaStore.Video.Media.SIZE)
                var widthCol = 0
                var heightCol = 0
                if (isNotAceSdk) {
                    widthCol = videoCursor.getColumnIndex(MediaStore.Video.Media.WIDTH)
                    heightCol = videoCursor.getColumnIndex(MediaStore.Video.Media.HEIGHT)
                }

                do {
                    val path = videoCursor.getString(pathCol)
                    val name = videoCursor.getString(nameCol)
                    val dateTime = videoCursor.getLong(dateCol)
                    val type = videoCursor.getString(mimeType)
                    val size = videoCursor.getInt(sizeCol).toLong()
                    var width = 0
                    var height = 0

                    if (size < Setting.minSize) {
                        continue
                    }

                    if (isNotAceSdk) {
                        width = videoCursor.getInt(widthCol)
                        height = videoCursor.getInt(heightCol)
                        if (width < Setting.minWidth || height < Setting.minHeight) {
                            continue
                        }
                    }

                    //过滤媒体库中存在但实际不存在的文件
                    val file = File(path)
                    if (!file.exists()) {
                        continue
                    }

                    val videoItem = MediaFile(name, path, false, true, type, width, height, size, dateTime)

                    //处理已默认勾选
                    Setting.selectedMedias.forEach {
                        if (path == it.path) {
                            videoItem.selectedOriginal = Setting.selectedOriginal
                            ResultMedias.addMediaFile(videoItem)
                        }
                    }

                    // 初始化“全部”专辑
                    if (album.getAlbumItem(albumItemAllName) == null) {
                        // 用第一个图片作为专辑的封面
                        album.addAlbumItem(1, albumItemAllName, "", path)

                    }

                    // 把图片全部放进“全部”专辑
                    album.getAlbumItem(albumItemAllName)!!.addMedia(videoItem)

                    // 添加当前图片的专辑到专辑模型实体中
                    val folderPath = File(path).parentFile.absolutePath

                    val albumName = folderPath.substring(folderPath.lastIndexOf("/"), folderPath.lastIndex)
                    album.addAlbumItem(albumName, folderPath, path)
                    album.getAlbumItem(albumName)!!.addMedia(videoItem)
                } while (videoCursor.moveToNext())
                videoCursor.close()
            }
        }

        if (Setting.mediaType == MediaType.ALL) {
            //所有图片和视频
            album.addAlbumItem(0, allImageAndVideo, "", album.getAlbumItem(0)?.coverPath ?: "")
            if (album.getAlbumItem(allImage) != null)
                album.getAlbumItem(0)?.mediaFiles?.addAll(album.getAlbumItem(allImage)!!.mediaFiles)
            if (album.getAlbumItem(allVideo) != null)
                album.getAlbumItem(0)?.mediaFiles?.addAll(album.getAlbumItem(allVideo)!!.mediaFiles)

            //重新给媒体项内的媒体文件排序
            resortMediaFiles()

        }

        album.albumItems.forEach {
            println(it.name)
            it.mediaFiles.forEach {
                println(it.createTime)
            }
        }
    }

    //重新给媒体项内的媒体文件排序
    private fun resortMediaFiles() {
        album.albumItems.forEach {
            it.mediaFiles.sortBy {
                it.createTime
            }
        }
    }


    //获取image的信息列表key
    private fun getImageProjections(): Array<out String>? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            arrayOf(MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_TAKEN,
                    MediaStore.Images.Media.MIME_TYPE,
                    MediaStore.Images.Media.WIDTH,
                    MediaStore.Images.Media.HEIGHT,
                    MediaStore.Images.Media.SIZE)
        } else {
            arrayOf(MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_TAKEN,
                    MediaStore.Images.Media.MIME_TYPE,
                    MediaStore.Images.Media.SIZE)
        }
    }


    //获取Video的信息列表key
    private fun getVideoProjections(): Array<out String>? {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            arrayOf(MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.DATE_TAKEN,
                    MediaStore.Video.Media.MIME_TYPE,
                    MediaStore.Video.Media.WIDTH,
                    MediaStore.Video.Media.HEIGHT,
                    MediaStore.Video.Media.SIZE)
        } else {
            arrayOf(MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.DATE_TAKEN,
                    MediaStore.Video.Media.MIME_TYPE,
                    MediaStore.Video.Media.SIZE)
        }
    }


}