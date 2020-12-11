package com.cwl.common.util

import android.content.Context
import android.os.Environment
import timber.log.Timber
import java.io.*
import java.util.*


object FileUtil {

    /**
     * 外置卡缓存目录或APP缓存目录
     */
    fun getDiskCachePath(context: Context): String {
        val cachePath: String
        if ((Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) && context.externalCacheDir != null) {
            // /storage/emulated/0/Android/data/包名/cache
            cachePath = context.externalCacheDir!!.path
        } else {
            // /data/data/包名/cache
            cachePath = context.cacheDir.path
        }
        return cachePath
    }

    /**
     * 创建文件，若文件夹不存在则自动创建文件夹，若文件存在则删除旧文件
     * @param path :待创建文件路径
     */
    fun createNewFile(path: String): File {
        val file = File(path)
        try {
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
        } catch (e: IOException) {
            Timber.e(e)
        }

        return file
    }

    /**
     * 创建文件夹
     */
    fun createDir(path:String){
        val file=File(path)
        try {
            if(file.isDirectory && !file.exists()){
                file.mkdirs()
            }
        }catch (e:Exception){
            Timber.e(e)
        }
    }

    /**
     * 将文件输入流写入文件
     */
    fun writeFileFromInputStream(inStream: InputStream, path: String): Boolean {
        var result = true
        try {
            val file = createNewFile(path)
            val out = FileOutputStream(file)
            var data = ByteArray(1024)
            var num = 0
            do {
                num = inStream.read(data, 0, data.size)
                if(num!=-1) out.write(data, 0, num)
            }while (num!=-1)
            out.close()
        } catch (e: Exception) {
            result = false
            Timber.e(e)
        }

        return result
    }

    /**
     * 获取文件输入流
     */
    fun readFileToInputStream(path: String): InputStream? {
        var inputStream: InputStream? = null
        try {
            val file = File(path)
            inputStream = FileInputStream(file)
        } catch (e: IOException) {
            Timber.e(e)
        }

        return inputStream
    }




    /**
     * 根据给出路径自动选择复制文件或整个文件夹
     * @param src :源文件或文件夹路径
     * @param dest :目标文件或文件夹路径
     */
    fun copyFiles(src: String, dest: String) {
        val srcFile = File(src)
        if (srcFile.exists()) {
            if (srcFile.isFile) {
                writeFileFromInputStream(readFileToInputStream(src)!!, dest)
            } else {
                val subFiles = srcFile.listFiles()
                if (subFiles!!.size == 0) {
                    val subDir = File(dest)
                    subDir.mkdirs()
                } else {
                    for (subFile in subFiles) {
                        val subDirPath = dest + System.getProperty("file.separator") + subFile.name
                        copyFiles(subFile.absolutePath, subDirPath)
                    }
                }
            }
        }
    }

    /**
     * 根据给出路径自动选择删除文件或整个文件夹
     * @param path :文件或文件夹路径
     */
    fun deleteFiles(path: String) {
        val file = File(path)
        if (!file.exists()) {
            return
        }
        if (file.isFile) {
            file.delete()// 删除文件
        } else {
            val subFiles = file.listFiles()
            for (subfile in subFiles!!) {
                deleteFiles(subfile.absolutePath)// 删除当前目录下的子目录
            }
            file.delete()// 删除当前目录
        }
    }


    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    fun getFileByPath(filePath: String): File? {
        return if (isSpace(filePath)) null else File(filePath)
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    fun isFileExists(filePath: String): Boolean {
        return isFileExists(getFileByPath(filePath))
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    fun isFileExists(file: File?): Boolean {
        return file != null && file.exists()
    }

    /**
     * 重命名文件
     *
     * @param filePath 文件路径
     * @param newName  新名称
     * @return `true`: 重命名成功<br></br>`false`: 重命名失败
     */
    fun rename(filePath: String, newName: String): Boolean {
        return rename(getFileByPath(filePath), newName)
    }

    /**
     * 重命名文件
     *
     * @param file    文件
     * @param newName 新名称
     * @return `true`: 重命名成功<br></br>`false`: 重命名失败
     */
    fun rename(file: File?, newName: String): Boolean {
        // 文件为空返回false
        if (file == null) return false
        // 文件不存在返回false
        if (!file.exists()) return false
        // 新的文件名为空返回false
        if (isSpace(newName)) return false
        // 如果文件名没有改变返回true
        if (newName == file.name) return true
        val newFile = File(file.parent + File.separator + newName)
        // 如果重命名的文件已存在返回false
        return !newFile.exists() && file.renameTo(newFile)
    }

    /**
     * 判断是否是目录
     *
     * @param dirPath 目录路径
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isDir(dirPath: String): Boolean {
        return isDir(getFileByPath(dirPath))
    }

    /**
     * 判断是否是目录
     *
     * @param file 文件
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isDir(file: File?): Boolean {
        return isFileExists(file) && file!!.isDirectory
    }

    /**
     * 判断是否是文件
     *
     * @param filePath 文件路径
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isFile(filePath: String): Boolean {
        return isFile(getFileByPath(filePath))
    }

    /**
     * 判断是否是文件
     *
     * @param file 文件
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isFile(file: File?): Boolean {
        return isFileExists(file) && file!!.isFile
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param dirPath 目录路径
     * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
     */
    fun createOrExistsDir(dirPath: String): Boolean {
        return createOrExistsDir(getFileByPath(dirPath))
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
     */
    fun createOrExistsDir(file: File?): Boolean {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param filePath 文件路径
     * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
     */
    fun createOrExistsFile(filePath: String): Boolean {
        return createOrExistsFile(getFileByPath(filePath))
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
     */
    fun createOrExistsFile(file: File?): Boolean {
        if (file == null) return false
        // 如果存在，是文件则返回true，是目录则返回false
        if (file.exists()) return file.isFile
        if (!createOrExistsDir(file.parentFile)) return false
        try {
            return file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

    }

    /**
     * 判断文件是否存在，存在则在创建之前删除
     *
     * @param filePath 文件路径
     * @return `true`: 创建成功<br></br>`false`: 创建失败
     */
    fun createFileByDeleteOldFile(filePath: String): Boolean {
        return createFileByDeleteOldFile(getFileByPath(filePath))
    }

    /**
     * 判断文件是否存在，存在则在创建之前删除
     *
     * @param file 文件
     * @return `true`: 创建成功<br></br>`false`: 创建失败
     */
    fun createFileByDeleteOldFile(file: File?): Boolean {
        if (file == null) return false
        // 文件存在并且删除失败返回false
        if (file.exists() && file.isFile && !file.delete()) return false
        // 创建目录失败返回false
        if (!createOrExistsDir(file.parentFile)) return false
        try {
            return file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

    }


    /**
     * 删除目录
     *
     * @param dirPath 目录路径
     * @return `true`: 删除成功<br></br>`false`: 删除失败
     */
    fun deleteDir(dirPath: String): Boolean {
        return deleteDir(getFileByPath(dirPath))
    }

    /**
     * 删除目录
     *
     * @param dir 目录
     * @return `true`: 删除成功<br></br>`false`: 删除失败
     */
    fun deleteDir(dir: File?): Boolean {
        if (dir == null) return false
        // 目录不存在返回true
        if (!dir.exists()) return true
        // 不是目录返回false
        if (!dir.isDirectory) return false
        // 现在文件存在且是文件夹
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (file.isFile) {
                    if (!deleteFile(file)) return false
                } else if (file.isDirectory) {
                    if (!deleteDir(file)) return false
                }
            }
        }
        return dir.delete()
    }

    /**
     * 删除文件
     *
     * @param srcFilePath 文件路径
     * @return `true`: 删除成功<br></br>`false`: 删除失败
     */
    fun deleteFile(srcFilePath: String): Boolean {
        return deleteFile(getFileByPath(srcFilePath))
    }

    /**
     * 删除文件
     *
     * @param file 文件
     * @return `true`: 删除成功<br></br>`false`: 删除失败
     */
    fun deleteFile(file: File?): Boolean {
        return file != null && (!file.exists() || file.isFile && file.delete())
    }

    /**
     * 删除目录下的所有文件
     *
     * @param dirPath 目录路径
     * @return `true`: 删除成功<br></br>`false`: 删除失败
     */
    fun deleteFilesInDir(dirPath: String): Boolean {
        return deleteFilesInDir(getFileByPath(dirPath))
    }

    /**
     * 删除目录下的所有文件
     *
     * @param dir 目录
     * @return `true`: 删除成功<br></br>`false`: 删除失败
     */
    fun deleteFilesInDir(dir: File?): Boolean {
        if (dir == null) return false
        // 目录不存在返回true
        if (!dir.exists()) return true
        // 不是目录返回false
        if (!dir.isDirectory) return false
        // 现在文件存在且是文件夹
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (file.isFile) {
                    if (!deleteFile(file)) return false
                } else if (file.isDirectory) {
                    if (!deleteDir(file)) return false
                }
            }
        }
        return true
    }

    /**
     * 获取目录下所有文件
     *
     * @param dirPath     目录路径
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    fun listFilesInDir(dirPath: String, isRecursive: Boolean): List<File>? {
        return listFilesInDir(getFileByPath(dirPath), isRecursive)
    }

    /**
     * 获取目录下所有文件
     *
     * @param dir         目录
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    fun listFilesInDir(dir: File?, isRecursive: Boolean): List<File>? {
        if (!isDir(dir)) return null
        if (isRecursive) return listFilesInDir(dir)
        val list = ArrayList<File>()
        val files = dir!!.listFiles()
        if (files != null && files.size != 0) {
            Collections.addAll(list, *files)
        }
        return list
    }

    /**
     * 获取目录下所有文件包括子目录
     *
     * @param dirPath 目录路径
     * @return 文件链表
     */
    fun listFilesInDir(dirPath: String): List<File>? {
        return listFilesInDir(getFileByPath(dirPath))
    }

    /**
     * 获取目录下所有文件包括子目录
     *
     * @param dir 目录
     * @return 文件链表
     */
    fun listFilesInDir(dir: File?): List<File>? {
        if (!isDir(dir)) return null
        val list = ArrayList<File>()
        val files = dir!!.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                list.add(file)
                if (file.isDirectory) {
                    val fileList = listFilesInDir(file)
                    if (fileList != null) {
                        list.addAll(fileList)
                    }
                }
            }
        }
        return list
    }

    /**
     * 获取目录下所有后缀名为suffix的文件
     *
     * 大小写忽略
     *
     * @param dirPath     目录路径
     * @param suffix      后缀名
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    fun listFilesInDirWithFilter(dirPath: String, suffix: String, isRecursive: Boolean): List<File>? {
        return listFilesInDirWithFilter(getFileByPath(dirPath), suffix, isRecursive)
    }

    /**
     * 获取目录下所有后缀名为suffix的文件
     *
     * 大小写忽略
     *
     * @param dir         目录
     * @param suffix      后缀名
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    fun listFilesInDirWithFilter(dir: File?, suffix: String, isRecursive: Boolean): List<File>? {
        if (isRecursive) return listFilesInDirWithFilter(dir, suffix)
        if (dir == null || !isDir(dir)) return null
        val list = ArrayList<File>()
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (file.name.toUpperCase().endsWith(suffix.toUpperCase())) {
                    list.add(file)
                }
            }
        }
        return list
    }

    /**
     * 获取目录下所有后缀名为suffix的文件包括子目录
     *
     * 大小写忽略
     *
     * @param dirPath 目录路径
     * @param suffix  后缀名
     * @return 文件链表
     */
    fun listFilesInDirWithFilter(dirPath: String, suffix: String): List<File>? {
        return listFilesInDirWithFilter(getFileByPath(dirPath), suffix)
    }

    /**
     * 获取目录下所有后缀名为suffix的文件包括子目录
     *
     * 大小写忽略
     *
     * @param dir    目录
     * @param suffix 后缀名
     * @return 文件链表
     */
    fun listFilesInDirWithFilter(dir: File?, suffix: String): List<File>? {
        if (dir == null || !isDir(dir)) return null
        val list = ArrayList<File>()
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (file.name.toUpperCase().endsWith(suffix.toUpperCase())) {
                    list.add(file)
                }
                if (file.isDirectory) {
                    list.addAll(listFilesInDirWithFilter(file, suffix)!!)
                }
            }
        }
        return list
    }

    /**
     * 获取目录下所有符合filter的文件
     *
     * @param dirPath     目录路径
     * @param filter      过滤器
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    fun listFilesInDirWithFilter(dirPath: String, filter: FilenameFilter, isRecursive: Boolean): List<File>? {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter, isRecursive)
    }

    /**
     * 获取目录下所有符合filter的文件
     *
     * @param dir         目录
     * @param filter      过滤器
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    fun listFilesInDirWithFilter(dir: File?, filter: FilenameFilter, isRecursive: Boolean): List<File>? {
        if (isRecursive) return listFilesInDirWithFilter(dir, filter)
        if (dir == null || !isDir(dir)) return null
        val list = ArrayList<File>()
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (filter.accept(file.parentFile, file.name)) {
                    list.add(file)
                }
            }
        }
        return list
    }

    /**
     * 获取目录下所有符合filter的文件包括子目录
     *
     * @param dirPath 目录路径
     * @param filter  过滤器
     * @return 文件链表
     */
    fun listFilesInDirWithFilter(dirPath: String, filter: FilenameFilter): List<File>? {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter)
    }

    /**
     * 获取目录下所有符合filter的文件包括子目录
     *
     * @param dir    目录
     * @param filter 过滤器
     * @return 文件链表
     */
    fun listFilesInDirWithFilter(dir: File?, filter: FilenameFilter): List<File>? {
        if (dir == null || !isDir(dir)) return null
        val list = ArrayList<File>()
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (filter.accept(file.parentFile, file.name)) {
                    list.add(file)
                }
                if (file.isDirectory) {
                    list.addAll(listFilesInDirWithFilter(file, filter)!!)
                }
            }
        }
        return list
    }

    /**
     * 获取目录下指定文件名的文件包括子目录
     *
     * 大小写忽略
     *
     * @param dirPath  目录路径
     * @param fileName 文件名
     * @return 文件链表
     */
    fun searchFileInDir(dirPath: String, fileName: String): List<File>? {
        return searchFileInDir(getFileByPath(dirPath), fileName)
    }

    /**
     * 获取目录下指定文件名的文件包括子目录
     *
     * 大小写忽略
     *
     * @param dir      目录
     * @param fileName 文件名
     * @return 文件链表
     */
    fun searchFileInDir(dir: File?, fileName: String): List<File>? {
        if (dir == null || !isDir(dir)) return null
        val list = ArrayList<File>()
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (file.name.toUpperCase() == fileName.toUpperCase()) {
                    list.add(file)
                }
                if (file.isDirectory) {
                    list.addAll(searchFileInDir(file, fileName)!!)
                }
            }
        }
        return list
    }


    /**
     * 获取文件最后修改的毫秒时间戳
     *
     * @param filePath 文件路径
     * @return 文件最后修改的毫秒时间戳
     */
    fun getFileLastModified(filePath: String): Long {
        return getFileLastModified(getFileByPath(filePath))
    }

    /**
     * 获取文件最后修改的毫秒时间戳
     *
     * @param file 文件
     * @return 文件最后修改的毫秒时间戳
     */
    fun getFileLastModified(file: File?): Long {
        return file?.lastModified() ?: -1
    }


    /**
     * 获取目录长度
     *
     * @param dirPath 目录路径
     * @return 目录长度
     */
    fun getDirLength(dirPath: String): Long {
        return getDirLength(getFileByPath(dirPath))
    }

    /**
     * 获取目录长度
     *
     * @param dir 目录
     * @return 目录长度
     */
    fun getDirLength(dir: File?): Long {
        if (!isDir(dir)) return -1
        var len: Long = 0
        val files = dir!!.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (file.isDirectory) {
                    len += getDirLength(file)
                } else {
                    len += file.length()
                }
            }
        }
        return len
    }

    /**
     * 获取文件长度
     *
     * @param filePath 文件路径
     * @return 文件长度
     */
    fun getFileLength(filePath: String): Long {
        return getFileLength(getFileByPath(filePath))
    }

    /**
     * 获取文件长度
     *
     * @param file 文件
     * @return 文件长度
     */
    fun getFileLength(file: File?): Long {
        return if (!isFile(file)) -1 else file!!.length()
    }


    /**
     * 获取全路径中的最长目录
     *
     * @param file 文件
     * @return filePath最长目录
     */
    fun getDirName(file: File?): String? {
        return if (file == null) null else getDirName(file.path)
    }

    /**
     * 获取全路径中的最长目录
     *
     * @param filePath 文件路径
     * @return filePath最长目录
     */
    fun getDirName(filePath: String): String? {
        if (isSpace(filePath)) return filePath
        val lastSep = filePath.lastIndexOf(File.separator)
        return if (lastSep == -1) "" else filePath.substring(0, lastSep + 1)
    }

    /**
     * 获取全路径中的文件名
     *
     * @param file 文件
     * @return 文件名
     */
    fun getFileName(file: File?): String? {
        return if (file == null) null else getFileName(file.path)
    }

    /**
     * 获取全路径中的文件名
     *
     * @param filePath 文件路径
     * @return 文件名
     */
    fun getFileName(filePath: String): String? {
        if (isSpace(filePath)) return filePath
        val lastSep = filePath.lastIndexOf(File.separator)
        return if (lastSep == -1) filePath else filePath.substring(lastSep + 1)
    }

    /**
     * 获取全路径中的不带拓展名的文件名
     *
     * @param file 文件
     * @return 不带拓展名的文件名
     */
    fun getFileNameNoExtension(file: File?): String? {
        return if (file == null) null else getFileNameNoExtension(file.path)
    }

    /**
     * 获取全路径中的不带拓展名的文件名
     *
     * @param filePath 文件路径
     * @return 不带拓展名的文件名
     */
    fun getFileNameNoExtension(filePath: String): String? {
        if (isSpace(filePath)) return filePath
        val lastPoi = filePath.lastIndexOf('.')
        val lastSep = filePath.lastIndexOf(File.separator)
        if (lastSep == -1) {
            return if (lastPoi == -1) filePath else filePath.substring(0, lastPoi)
        }
        return if (lastPoi == -1 || lastSep > lastPoi) {
            filePath.substring(lastSep + 1)
        } else filePath.substring(lastSep + 1, lastPoi)
    }

    /**
     * 获取全路径中的文件拓展名
     *
     * @param file 文件
     * @return 文件拓展名
     */
    fun getFileExtension(file: File?): String? {
        return if (file == null) null else getFileExtension(file.path)
    }

    /**
     * 获取全路径中的文件拓展名
     *
     * @param filePath 文件路径
     * @return 文件拓展名
     */
    fun getFileExtension(filePath: String): String? {
        if (isSpace(filePath)) return filePath
        val lastPoi = filePath.lastIndexOf('.')
        val lastSep = filePath.lastIndexOf(File.separator)
        return if (lastPoi == -1 || lastSep >= lastPoi) "" else filePath.substring(lastPoi + 1)
    }


    private fun isSpace(s: String?): Boolean {
        if (s == null) return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }
}