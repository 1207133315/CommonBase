package com.liningkang.utils

import android.content.Context
import android.os.Environment
import android.os.StatFs
import android.text.TextUtils
import java.io.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

object FileUtils {
    private val TAG = "FileUtil"
    private val DEFAULT_BUFFER_SIZE = 8 * 1024

    fun changeFileMode(file: File?, mode: String): Boolean {
        try {
            if (file == null || !file.exists()) {
                return false
            }
            val command = "chmod " + mode + " " + file.absolutePath
            val runtime = Runtime.getRuntime()
            runtime.exec(command)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun changeFilePermission755(file: File?): Boolean {
        try {
            if (file == null || !file.exists()) {
                return false
            }
            file.setReadable(true, false)
            file.setExecutable(true, false)
            file.setWritable(true, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    //TODO 需要做版本适配。4.X以上系统无此函数
    //5.0.2r1 有此函数
    //public static int setPermissions(File path, int mode, int uid, int gid)
    //http://www.grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/5.0.2_r1/android/os/FileUtils.java#FileUtils
    //4.4.4r1 有此函数
    //public static int setPermissions(File path, int mode, int uid, int gid)
    //http://www.grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.4.4_r1/android/os/FileUtils.java#FileUtils.setPermissions%28java.io.File%2Cint%2Cint%2Cint%29
    //4.2.2r1 有此函数
    //public static native int setPermissions(String file, int mode, int uid, int gid);
    //http://www.grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.2.2_r1/android/os/FileUtils.java#FileUtils.setPermissions%28java.lang.String%2Cint%2Cint%2Cint%29
    fun setPermissions(path: String?, paramInt1: Int, paramInt2: Int, paramInt3: Int): Int {
        try {
            return Class.forName("android.os.FileUtils")
                .getDeclaredMethod(
                    "setPermissions",
                    Integer.TYPE,
                    Integer.TYPE,
                    Integer.TYPE,
                    Integer.TYPE
                )
                .invoke(null, path, paramInt1, paramInt2, paramInt3) as Int
        } catch (e: Exception) {
            try {
                val LibCore = Class.forName("libcore.io.Libcore")
                val field = LibCore.getDeclaredField("os")
                if (!field.isAccessible) {
                    field.isAccessible = true
                }
                val os = field[null]
                val chmod = os.javaClass.getDeclaredMethod(
                    "chmod",
                    String::class.java,
                    Int::class.javaPrimitiveType
                )
                chmod.invoke(os, path, paramInt1)
                return 0
            } catch (e1: Exception) {
                e1.printStackTrace()
                e.printStackTrace()
                return -1
            }
        }
    }

    fun makeDir(dir: String?): Boolean {
        if (TextUtils.isEmpty(dir)) {
            return false
        }
        val f = File(dir)
        return if (!f.exists()) {
            f.mkdirs()
        } else true
    }

    fun formatFileSize(fileS: Long): String? { // Convert file size
        if (fileS < 0) {
            return "未知大小"
        }
        val df = DecimalFormat("0.0")
        val fileSizeString: String
        if (fileS < 1024) {
            fileSizeString = df.format(Rounding(fileS.toDouble())) + "B"
        } else if (fileS < 1048576) {
            fileSizeString = df.format(Rounding(fileS.toDouble() / 1024)) + "K"
        } else if (fileS < 1073741824) {
            fileSizeString = df.format(Rounding(fileS.toDouble() / 1048576)) + "M"
        } else {
            fileSizeString = df.format(Rounding(fileS.toDouble() / 1073741824)) + "G"
        }
        return fileSizeString
    }

    private fun Rounding(d: Double): Double {
        val bd = BigDecimal(d)
        bd.setScale(1, RoundingMode.HALF_UP)
        return bd.toFloat().toDouble()
    }

    fun IsFileExist(file: String?): Boolean {
        if (TextUtils.isEmpty(file)) {
            return false
        }
        val f = File(file)
        return f.exists()
    }

    // storage/emulated/0/360Download/file.txt  ->  file.txt
    fun getFileName(path: String?): String? {
        return File(path).name
    }

    // storage/emulated/0/360Download/file.txt  ->  file
    fun getFileNameEx(file: String): String? {
        var file = file
        var nPos = file.lastIndexOf(".")
        if (nPos > 0) {
            file = file.substring(0, nPos)
            nPos = file.lastIndexOf("/")
            if (nPos > 0) {
                file = file.substring(nPos + 1)
            }
        }
        return file
    }

    fun getFileLen(file: String?): Long {
        if (file == null) return 0
        val f = File(file)
        return f.length()
    }

    // 获得一个目录下 所有文件的大小 包括子目录
    fun getDirectorySize(filePath: String?): Long {
        if (filePath == null) return 0
        val file = File(filePath)
        if (!file.isDirectory) return 0
        var size: Long = 0
        val list = file.listFiles()
        if (list != null) {
            for (i in list.indices) {
                if (list[i].isDirectory) {
                    size += getDirectorySize(list[i].absolutePath)
                } else {
                    size += list[i].length()
                }
            }
        }
        return size
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    private fun makesureParentExist(file_: File?) {
        if (file_ == null) {
            return
        }
        val parent = file_.parentFile
        if (parent != null && !parent.exists()) makeDir(parent.path)
    }

    private fun makesureFileExist(file: File?) {
        if (file == null) return
        if (!file.exists()) {
            makesureParentExist(file)
            createNewFile(file)
        }
    }

    private fun createNewFile(file_: File?) {
        if (file_ == null) {
            return
        }
        if (!__createNewFile(file_)) throw RuntimeException(
            file_.absolutePath
                    + " doesn't be created!"
        )
    }

    private fun __createNewFile(file_: File?): Boolean {
        if (file_ == null) {
            return false
        }
        makesureParentExist(file_)
        if (file_.exists()) delete(file_)
        try {
            return file_.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    private fun delete(f: File?) {
        if ((f != null) && (f.exists()) && (!(f.delete()))) {
            throw RuntimeException(
                (f.absolutePath
                        + " doesn't be deleted!")
            )
        }
    }

    fun makesureFileExist(filePath_: String?) {
        if (filePath_ == null) return
        makesureFileExist(File(filePath_))
    }



    // data/data/com.qihoo.appstore
    fun getAvailableBytesOfDataDir(): Long {
        return getAvailableBytes(Environment.getDataDirectory())
    }

    // 只要有根目录就可以非空
    /*
    * getExistDir(new File("360Download/a"));
        getExistDir(new File(PathUtils.getSDCardPath() + "/360Download/a"));
        getExistDir(new File(PathUtils.getSDCardPath() + "/360Download/a/b/c"));
        getExistDir(new File(PathUtils.getSDCardPath() + "/360Download/a/b/c/file.txt"));
        getExistDir(new File(PathUtils.getSDCardPath() + "/360Download/file.txt"));
        getExistDir(new File("360Download/file.txt"));*/
    fun getExistDir(file: File): File {
        var file = file
        if (file != null) {
            while (true) {
                if (file.isDirectory && file.exists()) {
                    break
                } else {
                    file = file.parentFile
                    if (file == null) { //  getExistDir(new File("360Download/a"));
                        break
                    }
                }
            }
        }
        return file
    }

    fun getAvailableBytes(root: File?): Long {
        var root = root ?: return 0
        root = getExistDir(root)
        if (root != null && root.exists()) {
            val stat: StatFs
            try {
                stat = StatFs(root.path)
            } catch (e: IllegalArgumentException) {
//                java.lang.IllegalArgumentException
//                at android.os.StatFs.native_setup(Native Method)
//                at android.os.StatFs.<init>(StatFs.java:32)
//                at FileUtils.getAvailableBytes(AppStore:309)
//                at FileUtils.checkPathWithSize(AppStore:318)
//                at com.qihoo.downloadmanager.CheckDownloadCondition.checkDisk(AppStore:104)
//                at com.qihoo.appstore.download.DownloadSuccessAction.doApkInstall(AppStore:229)
//                at com.qihoo.appstore.download.DownloadSuccessAction.onDownloadSuccess(AppStore:104)
//                at com.qihoo.downloadservice.DownloadServiceDelegate.onNotifyDownloadInfo(AppStore:92)
//                at com.android.downloader.core.DownloadServiceListener$1.run(AppStore:36)
//                at android.os.Handler.handleCallback(Handler.java:615)
//                at android.os.Handler.dispatchMessage(Handler.java:92)
//                at android.os.Looper.loop(Looper.java:137)
//                at android.app.ActivityThread.main(ActivityThread.java:4914)
//                at java.lang.reflect.Method.invokeNative(Native Method)
//                at java.lang.reflect.Method.invoke(Method.java:511)
//                at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:808)
//                at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:575)
//                at dalvik.system.NativeStart.main(Native Method)
                if (LogUtils.isDebug) {
                    e.printStackTrace()
                }
                return 0
            }
            val availableBlocks = stat.availableBlocks.toLong() - 4L
            return stat.blockSize * availableBlocks
        } else {
            return 0
        }
    }

    fun checkPathWithSize(root: File?, needSize: Long): Boolean {
        val avilableBytes = getAvailableBytes(root)
        return avilableBytes > needSize
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 是否在目录层级内
     *
     * @param root      根目彿
     * @param path      根目录中的子目录
     * @param hierarchy 层级数，假娀昿1则永远返回true＿
     */
    fun isInHierarchy(root: File, path: File, hierarchy: Int): Boolean {
        if (hierarchy < 0) {
            return true
        } else {
            val nodes = path.path.substring(root.path.length).split(File.separator).toTypedArray()
            return nodes.size - 1 <= hierarchy
        }
    }

    fun moveFile(srcFileName: String?, destFileName: String?): Boolean {
        val srcFile = File(srcFileName)
        if (!srcFile.exists() || !srcFile.isFile) return false
        val destFile = File(destFileName)
        if (!destFile.parentFile.exists()) destFile.parentFile.mkdirs()
        return srcFile.renameTo(destFile)
    }

    fun copyFile(oldPath: String?, newPath: String): File? {
        if (TextUtils.isEmpty(oldPath) || TextUtils.isEmpty(newPath)) {
            return null
        }
        val oldFile = File(oldPath)
        var newFile = File(newPath)
        var byteread: Int
        var `in`: InputStream? = null
        var out: FileOutputStream? = null
        try {
            if (newFile.exists()) {
                var newName = ""
                var suffix = ""
                val pos = newPath.lastIndexOf(".")
                if (pos > 0) {
                    newName = newPath.substring(0, pos)
                    suffix = newPath.substring(pos, newPath.length)
                } else {
                    newName = newPath
                }
                var index = 0
                do {
                    index++
                    newFile = File("$newName ($index)$suffix")
                } while (newFile.exists())
                if (!newFile.parentFile.exists()) {
                    newFile.parentFile.mkdirs()
                }
            }
            synchronized(newFile) {
                if (!newFile.getParentFile().exists()) {
                    newFile.getParentFile().mkdirs()
                }
            }
            if (oldFile.exists()) {
                `in` = FileInputStream(oldPath)
                out = FileOutputStream(newFile)
                val buffer = ByteArray(1024)
                while ((`in`.read(buffer).also { byteread = it }) != -1) {
                    out.write(buffer, 0, byteread)
                }
                `in`.close()
                newFile.setLastModified(oldFile.lastModified())
            }
            return newFile
        } catch (e: Exception) {
            LogUtils.e("FileUtil", e.toString())
        } finally {
            try {
                `in`?.close()
                out?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }


    /**
     * 拷贝文件
     *
     * @param sourceFile 源文仿
     * @param destFile   目标文件
     * @return 是否拷贝成功
     */
    fun copyFile(sourceFile: File, destFile: File): Boolean {
        var isCopyOk = false
        val buffer: ByteArray
        var bis: BufferedInputStream? = null
        var bos: BufferedOutputStream? = null
        // 如果此时没有文件夹目录就创建
        var canonicalPath = ""
        try {
            canonicalPath = destFile.canonicalPath
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (!destFile.exists()) {
            if (canonicalPath.lastIndexOf(File.separator) >= 0) {
                canonicalPath =
                    canonicalPath.substring(0, canonicalPath.lastIndexOf(File.separator))
                isCopyOk = makeDir(canonicalPath)
            }
        }
        if (!isCopyOk) {
            return false
        }
        try {
            bis = BufferedInputStream(FileInputStream(sourceFile), DEFAULT_BUFFER_SIZE)
            bos = BufferedOutputStream(FileOutputStream(destFile), DEFAULT_BUFFER_SIZE)
            buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var len: Int
            while ((bis.read(buffer, 0, DEFAULT_BUFFER_SIZE).also { len = it }) != -1) {
                bos.write(buffer, 0, len)
            }
            bos.flush()
            isCopyOk = sourceFile.length() == destFile.length()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bos?.close()
                bis?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return isCopyOk
    }

    fun copyDir(src: File, dst: File) {
        if (!dst.exists()) {
            dst.mkdirs()
        }
        val files = src.listFiles()
        if (files != null && files.size > 0) {
            for (file: File in files) {
                if (file.isDirectory) {
                    // 先判断剩余空间是否不足.如果不足则退出
                    try {
                        val fs = StatFs(dst.path)
                        val freeSize = fs.blockSize.toLong() * fs.availableBlocks.toLong()
                        if (1024 >= freeSize) {
                            throw RuntimeException("NO_ENOUGH_SPACE")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    copyDir(file, File(dst, file.name))
                } else {
                    try {
                        val fs = StatFs(dst.path)
                        val freeSize = fs.blockSize.toLong() * fs.availableBlocks.toLong()
                        if (file.length() + 1024 >= freeSize) {
                            throw RuntimeException("NO_ENOUGH_SPACE")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    val destFile = File(dst, file.name)
                    if (destFile.exists()) {
                        LogUtils.d(TAG, "file exist")
                    } else {
                        if (file.canRead() && !copyFile(file, destFile)) {
                            throw RuntimeException("Copy file fail")
                        }
                    }
                }
            }
        }
    }

    /**
     * 拷贝asets下的文件到指定文件
     *
     * @param context
     * @param fileName
     * @param file
     * @return
     */
    fun copyAssetsFile(context: Context, fileName: String?, file: File): Boolean {
        val parent = file.parentFile
        if (!parent.exists()) {
            parent.mkdirs()
        }
        if (!parent.exists()) {
            return false
        }
        val temp = File("$file.temp")
        var bis: BufferedInputStream? = null
        var bos: BufferedOutputStream? = null
        try {
            bis = BufferedInputStream(context.assets.open((fileName)!!))
            bos = BufferedOutputStream(FileOutputStream(temp))
            val buffer = ByteArray(8 * 1024)
            var len: Int
            while ((bis.read(buffer).also { len = it }) != -1) {
                bos.write(buffer, 0, len)
            }
            bos.flush()
            if (temp.renameTo(file)) {
                return true
            } else {
                temp.delete()
                file.delete()
            }
        } catch (e: FileNotFoundException) {
            if (LogUtils.isDebug) {
                e.printStackTrace()
            }
        } catch (e: IOException) {
            if (LogUtils.isDebug) {
                e.printStackTrace()
            }
        } finally {
            safeClose(bis)
            safeClose(bos)
        }
        return false
    }

    /**
     * 安全关闭流对象
     *
     * @param closeable
     */
    private fun safeClose(closeable: Closeable?) {
        try {
            closeable?.close()
        } catch (e: IOException) {
            if (LogUtils.isDebug) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 读取文件内容到字节数绿
     */
    fun readFileToBytes(file: File): ByteArray? {
        var bytes: ByteArray? = null
        if (file.exists()) {
            val buffer: ByteArray
            var bis: BufferedInputStream? = null
            var bos: BufferedOutputStream? = null
            var baos: ByteArrayOutputStream? = null
            try {
                bis = BufferedInputStream(FileInputStream(file), DEFAULT_BUFFER_SIZE)
                baos = ByteArrayOutputStream()
                bos = BufferedOutputStream(baos, DEFAULT_BUFFER_SIZE)
                buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var len: Int
                while ((bis.read(buffer, 0, DEFAULT_BUFFER_SIZE).also { len = it }) != -1) {
                    bos.write(buffer, 0, len)
                }
                bos.flush()
                bytes = baos.toByteArray()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    bos?.close()
                    baos?.close()
                    bis?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return bytes
    }

    fun readFileToBytes(file: File, offset: Long, len: Long): ByteArray? {
        var bytes: ByteArray? = null
        if (file.exists() && (offset >= 0) && (len > offset) && (offset < file.length())) {
            var raf: RandomAccessFile? = null
            var bos: ByteArrayOutputStream? = null
            try {
                raf = RandomAccessFile(file, "r")
                raf.seek(offset)
                bos = ByteArrayOutputStream()
                var b: Int
                var count: Long = 0
                while ((raf.read().also { b = it }) != -1 && count < len) {
                    bos.write(b)
                    count++
                }
                bos.flush()
                bytes = bos.toByteArray()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    raf?.close()
                    bos?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return bytes
    }

    fun writeBytesToFile(file: File, bytes: ByteArray?, offset: Long, byteCount: Int): Boolean {
        var isOk = false
        if (!file.exists()) {
            try {
                isOk = file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (file.exists() && (bytes != null) && (offset >= 0)) {
            var raf: RandomAccessFile? = null
            try {
                raf = RandomAccessFile(file, "rw")
                raf.seek(offset)
                raf.write(bytes, 0, byteCount)
                isOk = true
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    raf?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return isOk
    }

    /**
     * 读取文件内容到字符串
     */
    fun readFileToString(file: File): String? {
        return readFileToString(file, null)
    }

    /**
     * 读取文件内容到字符串
     */
    fun readFileToString(file: File, encoding: String?): String? {
        var result: String? = null
        if (file.exists()) {
            val buffer: CharArray
            var br: BufferedReader? = null
            var isr: InputStreamReader? = null
            var bw: BufferedWriter? = null
            val sw = StringWriter()
            try {
                isr =
                    if (encoding == null) InputStreamReader(FileInputStream(file)) else InputStreamReader(
                        FileInputStream(file), encoding
                    )
                br = BufferedReader(isr)
                bw = BufferedWriter(sw)
                buffer = CharArray(DEFAULT_BUFFER_SIZE)
                var len: Int
                while ((br.read(buffer, 0, DEFAULT_BUFFER_SIZE).also { len = it }) != -1) {
                    bw.write(buffer, 0, len)
                }
                bw.flush()
                result = sw.toString()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    bw?.close()
                    br?.close()
                    isr?.close()
                    sw.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return result
    }

    /**
     * 写字符串到文件，文件父目录娀果不存在，会自动创建
     */
    fun writeStringToFile(file: File, content: String): Boolean {
        return writeStringToFile(file, content, false)
    }

    /**
     * 写字符串到文件，文件父目录娀果不存在，会自动创建
     */
    fun writeStringToFile(file: File, content: String, isAppend: Boolean): Boolean {
        var isWriteOk = false
        val buffer: CharArray
        var count = 0
        var br: BufferedReader? = null
        var bw: BufferedWriter? = null
        try {
            if (!file.exists()) {
                createNewFileAndParentDir(file)
            }
            if (file.exists()) {
                br = BufferedReader(StringReader(content))
                bw = BufferedWriter(FileWriter(file, isAppend))
                buffer = CharArray(DEFAULT_BUFFER_SIZE)
                var len: Int
                while ((br.read(buffer, 0, DEFAULT_BUFFER_SIZE).also { len = it }) != -1) {
                    bw.write(buffer, 0, len)
                    count += len
                }
                bw.flush()
            }
            isWriteOk = content.length == count
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bw?.close()
                br?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return isWriteOk
    }

    /**
     * 写字节数组到文件，文件父目录如果不存在，会自动创廿
     */
    fun writeBytesToFile(file: File, bytes: ByteArray): Boolean {
        return writeBytesToFile(file, bytes, false)
    }

    /**
     * 写字节数组到文件，文件父目录如果不存在，会自动创廿
     */
    fun writeBytesToFile(file: File, bytes: ByteArray, isAppend: Boolean): Boolean {
        var isWriteOk = false
        val buffer: ByteArray
        var count = 0
        var bais: ByteArrayInputStream? = null
        var bis: BufferedInputStream? = null
        var bos: BufferedOutputStream? = null
        try {
            if (!file.exists()) {
                createNewFileAndParentDir(file)
            }
            if (file.exists()) {
                bos = BufferedOutputStream(FileOutputStream(file, isAppend), DEFAULT_BUFFER_SIZE)
                bais = ByteArrayInputStream(bytes)
                bis = BufferedInputStream(bais, DEFAULT_BUFFER_SIZE)
                buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var len: Int
                while ((bis.read(buffer, 0, DEFAULT_BUFFER_SIZE).also { len = it }) != -1) {
                    bos.write(buffer, 0, len)
                    count += len
                }
                bos.flush()
            }
            isWriteOk = bytes.size == count
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bos?.close()
                bis?.close()
                bais?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return isWriteOk
    }

    /**
     * 创建文件父目彿
     */
    fun createParentDir(file: File): Boolean {
        var isMkdirs = true
        if (!file.exists()) {
            val dir = file.parentFile
            if (!dir.exists()) {
                isMkdirs = dir.mkdirs()
            }
        }
        return isMkdirs
    }

    /**
     * 创建文件及其父目彿
     */
    fun createNewFileAndParentDir(file: File): Boolean {
        var isCreateNewFileOk: Boolean
        isCreateNewFileOk = createParentDir(file)
        // 创建父目录失败，直接返回false，不再创建子文件
        if (isCreateNewFileOk) {
            if (!file.exists()) {
                try {
                    isCreateNewFileOk = file.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                    isCreateNewFileOk = false
                }
            }
        }
        return isCreateNewFileOk
    }


    /**
     * 根据文件名称获取文件的后缿͗符串
     *
     * @param filename 文件的名秿可能带路徿
     * @return 文件的后缿͗符串
     */
    fun getFileExtension(filename: String): String? {
        if (!TextUtils.isEmpty(filename)) {
            val dotPos = filename.lastIndexOf('.')
            if (0 <= dotPos) {
                return filename.substring(dotPos + 1)
            }
        }
        return ""
    }

    // /a/b/cccc/t.txt  => /a/b/cccc/t
    fun getPathExcludeExtension(filename: String): String? {
        if (!TextUtils.isEmpty(filename)) {
            val dotPos = filename.lastIndexOf('.')
            return if (0 <= dotPos) {
                filename.substring(0, dotPos)
            } else {
                filename
            }
        }
        return ""
    }

    fun modifyFileExtension(file: String, newExtension: String): String? {
        val nPos1 = file.lastIndexOf(".")
        return if (nPos1 > 0) {
            file.substring(0, nPos1) + newExtension
        } else ""
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与吿
     *
     * @param sPath 要删除的目录或文仿
     * @return 删除成功返回 true，否则返囿false〿
     */
    fun deleteFileOrDirectory(sPath: String): Boolean {
        val file = File(sPath)
        // 判断目录或文件是否存圿
        if (!file.exists()) { // 不存在返囿false
            return false
        } else {
            // 判断是否为文仿
            return if (file.isFile) { // 为文件时调用删除文件方法
                deleteFile(sPath)
            } else { // 为目录时调用删除目录方法
                deleteDirectory(sPath)
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件吿
     * @return 单个文件删除成功返回true，否则返回false
     */
    fun deleteFile(sPath: String?): Boolean {
        if (TextUtils.isEmpty(sPath)) {
            return false
        }
        var flag = false
        val file = File(sPath)
        // 路径为文件且不为空则进行删除
        if (file.isFile && file.exists()) {
            flag = file.delete()
        }
        return flag
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    fun deleteDirectory(sPath: String): Boolean {
        return deleteDirectoryImp(sPath, null)
    }

    interface IDeleteFileFilter {
        fun isDelete(file: File?): Boolean
    }

    fun deleteDirectoryImp(sPath: String, deleteFileFilter: IDeleteFileFilter?): Boolean {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔笿
        var sPath = sPath
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator
        }
        val dirFile = File(sPath)
        // 如果dir对应的文件不存在，或者不是一个目录，则ꀥǿ
        if (!dirFile.exists() || !dirFile.isDirectory) {
            return false
        }
        var flag = true
        // 删除文件夹下的所有文仿包括子目彿
        val files = dirFile.listFiles() ?: return false
        for (i in files.indices) {
            // 删除子文仿
            if (files[i].isFile) {
                if (deleteFileFilter == null || deleteFileFilter.isDelete(files[i])) {
                    flag = deleteFile(files[i].absolutePath)
                    if (!flag) {
                        break
                    }
                }
            } // 删除子目彿
            else {
                flag = deleteDirectory(files[i].absolutePath)
                if (!flag) {
                    break
                }
            }
        }
        return if (!flag) {
            false
        } else dirFile.delete()
        // 删除当前目录
    }

    fun renameFile(newFileName: String?, oldFileName: String?): Boolean {
        val oldfile = File(oldFileName)
        val newfile = File(newFileName)
        if (!oldfile.exists()) {
            return false
        }
        val renameResult = oldfile.renameTo(newfile)
        oldfile.delete()
        return renameResult
    }

    fun getUniqueFilename(rootPath: String, filename: String, extension: String?): String? {
        var ext: String? = null
        var name: String? = null
        try {
            if (extension == null) {
                ext = filename.substring(filename.lastIndexOf("."), filename.length)
                name = filename.substring(0, filename.lastIndexOf("."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (TextUtils.isEmpty(ext)) {
            name = filename
            ext = extension
        }
        var fullFilename: String = "$rootPath/$name$ext"
        if (!File(fullFilename).exists()) {
            return fullFilename
        }
        for (magnitude in 1..999999999) {
            fullFilename = "$rootPath/$name ($magnitude)$ext"
            if (!File(fullFilename).exists()) {
                return fullFilename
            }
        }
        return null
    }


    fun pathFileExist(sPath: String?): Boolean {
        if (TextUtils.isEmpty(sPath)) {
            return false
        }
        val dirFile: File = File(sPath)
        return dirFile != null && dirFile.exists()
    }

    fun closeQuietly(`in`: Closeable?) {
        if (`in` != null) {
            try {
                `in`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Error) {
                e.printStackTrace()
            }
        }
    }

     class ScanFileUtils() {
        private val mLock: AsyncToSync = AsyncToSync()
        @JvmOverloads
        fun scanAllDir(
            fileDir: File,
            filter: FileFilter?,
            hierarchy: Int = -1,
            maxFiles: Int = -1
        ) {
            val list: MutableList<File> = ArrayList()
            list.add(fileDir)
            scanAllDir(list, filter, hierarchy, maxFiles)
        }

        /**
         * 非ꀥݒ方式扫描目录；
         */
        @JvmOverloads
        fun scanAllDir(
            fileDirs: MutableList<File>?,
            filter: FileFilter?,
            hierarchy: Int = -1,
            maxFiles: Int = -1
        ) {
            if (fileDirs != null) {
                for (fileDir: File in fileDirs) {
                    if (mLock.isExit()) {
                        break
                    } else {
                        mLock.callWait()
                    }
                    val linkedList = LinkedList<File>()
                    linkedList.addLast(fileDir)
                    while (!linkedList.isEmpty()) {
                        if (mLock.isExit()) {
                            break
                        } else {
                            mLock.callWait()
                        }
                        var files: Array<File>?
                        try {
                            files = listFiles(linkedList.removeFirst(), filter, maxFiles)
                        } catch (e: Throwable) {
                            e.printStackTrace()
                            continue
                        }
                        if (files != null) {
                            for (file: File in files) {
                                if (mLock.isExit()) {
                                    break
                                } else {
                                    mLock.callWait()
                                }
                                if (file.isDirectory && isInHierarchy(fileDir, file, hierarchy)) {
                                    linkedList.addLast(file)
                                }
                            }
                        }
                    }
                    linkedList.clear()
                }
                fileDirs.clear()
            }
        }

        private fun listFiles(f: File, filter: FileFilter?, maxFiles: Int): Array<File>? {
            if (filter == null || maxFiles > 0) {
                val files = f.listFiles()
                if (filter == null || files == null) {
                    return files
                }
                if (maxFiles > 0 && files.size > maxFiles) {
                    return null
                }
            }
            return listFiles(f, filter)
        }

        /**
         * 实现listFiles功能，遍历过程中进行锁判斿
         */
        private fun listFiles(f: File?, filter: FileFilter?): Array<File>? {
            if (f == null) {
                return null
            }
            val files = f.listFiles()
            if (filter == null || files == null) {
                return files
            }
            val result: MutableList<File> = ArrayList(files.size)
            for (file: File in files) {
                if (mLock.isExit()) {
                    break
                } else {
                    mLock.callWait()
                }
                if (filter.accept(file)) {
                    result.add(file)
                }
            }
            return result.toTypedArray()
        }

        /**
         * 非方式获取目录的占用空间＿
         */
        fun getDirSize(dir: File, filter: FileFilter?): Long {
            return getDirSize(dir, filter, true)
        }

        /**
         * 非方式获取目录的占用空间
         *
         * @param isComputeDirSize 是否计算目录占用大小
         */
        fun getDirSize(dir: File, filter: FileFilter?, isComputeDirSize: Boolean): Long {
            var size: Long = 0
            val linkedList = LinkedList<File>()
            linkedList.addLast(dir)
            while (!linkedList.isEmpty()) {
                if (mLock.isExit()) {
                    break
                } else {
                    mLock.callWait()
                }
                var files: Array<File>?
                try {
                    files = listFiles(linkedList.removeFirst(), filter, -1)
                } catch (e: Throwable) {
                    e.printStackTrace()
                    continue
                }
                if (files != null) {
                    for (file: File in files) {
                        if (mLock.isExit()) {
                            break
                        } else {
                            mLock.callWait()
                        }
                        if (file.isDirectory) {
                            linkedList.addLast(file)
                            if (isComputeDirSize) {
                                size += file.length()
                            }
                        } else {
                            size += file.length()
                        }
                    }
                }
            }
            if (isComputeDirSize) {
                size += dir.length()
            }
            linkedList.clear()
            return size
        }

        /**
         * 非递归方式删除文件目录
         *
         * @param dir 文件目录
         */
        fun deleteDir(dir: File, filter: FileFilter?) {
            if (dir.exists() && dir.isDirectory) {
                val linkedList = LinkedList<File>()
                linkedList.addLast(dir)
                while (!linkedList.isEmpty()) {
                    if (mLock.isExit()) {
                        break
                    } else {
                        mLock.callWait()
                    }
                    val tmp = linkedList.removeLast()
                    val files = listFiles(tmp, filter)
                    if (files == null || files.size == 0) {
                        tmp.delete()
                    } else {
                        for (file: File in files) {
                            if (mLock.isExit()) {
                                break
                            } else {
                                mLock.callWait()
                            }
                            if (file.isDirectory) {
                                linkedList.addLast(file)
                            } else {
                                file.delete()
                            }
                        }
                    }
                }
            }
        }

        /**
         * 递归方式删除文件目录
         *
         * @param dir 文件目录
         */
        fun deleteDirRecursive(dir: File, filter: FileFilter?) {
            if (dir.exists() && dir.isDirectory) {
                if (mLock.isExit()) {
                    return
                } else {
                    mLock.callWait()
                }
                var files = listFiles(dir, filter)
                if (files != null && files.size > 0) {
                    for (file: File in files) {
                        if (mLock.isExit()) {
                            return
                        } else {
                            mLock.callWait()
                        }
                        if (file.isDirectory) {
                            deleteDirRecursive(file, filter)
                        } else {
                            file.delete()
                        }
                    }
                }
                files = dir.listFiles()
                if (files == null || files.size == 0) {
                    dir.delete()
                }
            }
        }

        fun pause() {
            mLock.pause()
        }

        fun resume() {
            mLock.resume()
        }

        fun destroy() {
            mLock.exit()
        }
    }


    fun enumFilesUnderDir(dir: String?, setFiles: MutableSet<String?>?) {
        if (setFiles == null) {
            throw RuntimeException("")
        }
        val directory = File(dir)
        if (directory.isDirectory) {
            val files = directory.listFiles()
            if (files != null) {
                for (file: File in files) {
                    if (file.isDirectory) {
                        enumFilesUnderDir(dir, setFiles)
                    } else if (file.isFile) {
                        setFiles.add(file.absolutePath)
                    }
                }
            }
        }
    }

    fun getFileModifyTime(file: String?): Long {
        if (TextUtils.isEmpty(file)) {
            return 0
        }
        val f = File(file)
        return if (f.exists() && f.isFile) {
            f.lastModified()
        } else 0
    }

    fun getExceptionWhenCreate(file: String?): String? {
        try {
            File(file).createNewFile()
        } catch (e1: IOException) {
            e1.printStackTrace()
            return "exception: " + e1.message + " " + e1.cause + " " +pathFileExist(
                file
            )
        } catch (e1: Throwable) {
            return "throwable: " + e1.message + " " + e1.cause + " " + pathFileExist(
                file
            )
        }
        return ""
    }

    /**
     * 将InputStream转换成byte数组
     *
     * @param in InputStream
     * @return byte[]
     * @throws IOException
     */
    val BUFFER_SIZE = 4096

    @Throws(IOException::class)
    fun InputStreamTOByte(`in`: InputStream): ByteArray? {
        val outStream = ByteArrayOutputStream()
        var data: ByteArray? = ByteArray(BUFFER_SIZE)
        var count = -1
        while ((`in`.read(data, 0, BUFFER_SIZE).also { count = it }) != -1) outStream.write(
            data,
            0,
            count
        )
        data = null
        return outStream.toByteArray()
    }

    /**
     * 读取assets目录文件
     *
     * @param context
     * @param fileName
     * @return
     */
    fun readAssetFile(context: Context, fileName: String?): String? {
        val reader: BufferedReader
        try {
            reader = BufferedReader(InputStreamReader(context.assets.open((fileName)!!)))
            val builder = StringBuilder()
            var buffer: String?
            while ((reader.readLine().also { buffer = it }) != null) {
                builder.append(buffer)
            }
            return builder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }
}