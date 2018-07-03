package com.huantansheng.jmedia.mediaModel.entity


/**
 * 媒体专辑
 * @param albumItems  专辑项集合
 * @param hasAlbumItems  已经添加到Album对象内的专辑项集合
 */
data class Album(val albumItems: ArrayList<AlbumItem> = ArrayList<AlbumItem>(),
                 val hasAlbumItems: LinkedHashMap<String, AlbumItem> = LinkedHashMap()) {

    /**
     * 添加媒体项(私有方法，内部用）
     * @param albumItem 添加的媒体项
     */
    private fun addAlbumItem(albumItem: AlbumItem) {
        hasAlbumItems[albumItem.name] = albumItem
        albumItems.add(albumItem)
    }

    /**
     * 添加媒体项(私有方法，内部用）
     * @param albumItem 添加的媒体项
     */
    private fun addAlbumItem(index: Int, albumItem: AlbumItem) {
        hasAlbumItems[albumItem.name] = albumItem
        albumItems.add(index, albumItem)
    }

    /**
     * 添加媒体项
     * @param name 媒体项名称
     * @param albumItemPath 媒体项路径
     * @param coverPath 媒体项封面路径
     */
    fun addAlbumItem(name: String, albumItemPath: String, coverPath: String) {
        if (null == hasAlbumItems[name])
            addAlbumItem(AlbumItem(name, albumItemPath, coverPath))
    }

    /**
     * 添加媒体项到指定位置
     * @param index 添加的位置
     * @param name 媒体项名称
     * @param albumItemPath 媒体项路径
     * @param coverPath 媒体项封面路径
     */
    fun addAlbumItem(index: Int, name: String, albumItemPath: String, coverPath: String) {
        if (null == hasAlbumItems[name])
            addAlbumItem(index, AlbumItem(name, albumItemPath, coverPath))
    }

    /**
     * 根据名称获取媒体项
     * @param name 媒体项名称
     * @return 媒体项，如果没有返回null
     */
    fun getAlbumItem(name: String): AlbumItem? {
        return hasAlbumItems[name]
    }

    /**
     * 根据角标获取媒体项
     * @param index 角标
     * @return 媒体项，如果没有返回null
     */
    fun getAlbumItem(index: Int): AlbumItem? {
        return albumItems[index]
    }

    /**
     * 媒体库是否是空的
     * @return 媒体库是否为空
     */
    fun isEmpty(): Boolean {
        return albumItems.isEmpty()
    }

    /**
     * 媒体库是否不是空的
     * @return 媒体库是否为空
     */
    fun isNotEmpty(): Boolean {
        return albumItems.isNotEmpty()
    }
}