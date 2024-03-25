package com.liningkang.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import androidx.annotation.RequiresApi
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException

object ImageUtil {
    const val CROP_REQUEST_CODE = 100
    const val OPEN_PHOTO_REQUEST_CODE = 0

    @Throws(FileNotFoundException::class, IOException::class)
    fun getBitmapFormUri(context: Context, uri: Uri?): Bitmap? {
        var input = context!!.contentResolver.openInputStream(uri!!)

        //这一段代码是不加载文件到内存中也得到bitmap的真是宽高，主要是设置inJustDecodeBounds为true
        val onlyBoundsOptions = BitmapFactory.Options()
        onlyBoundsOptions.inJustDecodeBounds = true //不加载到内存
        onlyBoundsOptions.inDither = true //optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565 //optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
        input!!.close()
        val originalWidth = onlyBoundsOptions.outWidth
        val originalHeight = onlyBoundsOptions.outHeight
        if (originalWidth == -1 || originalHeight == -1) return null

        //图片分辨率以480x800为标准
        val hh = 256//这里设置高度为800f
        val ww = 256 //这里设置宽度为480f
        //缩放比，由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        var be = 1 //be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) { //如果宽度大的话根据宽度固定大小缩放
            be = (originalWidth / ww).toInt()
        } else if (originalWidth < originalHeight && originalHeight > hh) { //如果高度高的话根据宽度固定大小缩放
            be = (originalHeight / hh).toInt()
        }
        if (be <= 0) be = 1
        //比例压缩
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = be //设置缩放比例
        bitmapOptions.inDither = true
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
        input = context!!.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
        input!!.close()
        return compressImage(bitmap!!) //再进行质量压缩
    }

    fun compressImage(image: Bitmap): Bitmap? {
        val maxSize = 100 * 1024 // 将 KB 转换为字节
        var compressedBitmap = image

        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream) // 先质量压缩，保留原始 Bitmap

        while (stream.toByteArray().size > maxSize) {
            compressedBitmap = Bitmap.createScaledBitmap(compressedBitmap, (compressedBitmap.width * 0.9).toInt(), (compressedBitmap.height * 0.9).toInt(), true)
            stream.reset()
            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        }

        return compressedBitmap
    }

    fun getRoundedBitmap(source: Bitmap?): Bitmap? {
        if (source == null) return source
// 创建一个新的Bitmap对象，用于存储裁剪后的圆形图片
        var circleBitmap = Bitmap.createBitmap(source.width, source.width, Bitmap.Config.ARGB_8888);

        // 初始化Canvas对象，并将新的Bitmap对象设置为画布
        var canvas = Canvas(circleBitmap)

        // 在画布上绘制一个圆形路径
        val path = Path()
        val centerX = source.width / 2
        val centerY = source.width / 2
        val radius = centerX.coerceAtMost(centerY)
        val rectF = RectF(
            (centerX - radius).toFloat(),
            (centerY - radius).toFloat(), (centerX + radius).toFloat(), (centerY + radius).toFloat()
        );
        path.addArc(rectF, 0f, 360f)

        // 使用Paint对象进行裁剪，只保留圆形路径内的部分
        val paint = Paint()
        paint.isAntiAlias = true;
        paint.style = Paint.Style.FILL;
        canvas.drawPath(path, paint);

        // 将裁剪后的图片保存到新的Bitmap对象中
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        canvas.drawBitmap(source, 0f, 0f, paint);

        return circleBitmap
    }


    // 将 Drawable 转换为 Bitmap
    @RequiresApi(Build.VERSION_CODES.O)
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        } else if (drawable is VectorDrawable || drawable is AdaptiveIconDrawable) {
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
        throw IllegalArgumentException("Unsupported Drawable type")
    }

    // 将 Bitmap 转换为 Drawable
    fun bitmapToDrawable(resources: Resources, bitmap: Bitmap): Drawable {
        return BitmapDrawable(resources, bitmap)
    }


    // 将 Drawable 转换为 Base64 字符串
    fun drawableToBase64(drawable: Drawable): String {
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            return bitmapToBase64(bitmap)
        } else {
            // 将 Drawable 转换为 Bitmap
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmapToBase64(bitmap)
        }
    }

    // 将 Bitmap 转换为 Base64 字符串
    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    // 将 Base64 字符串转换为 Drawable
    fun base64ToDrawable(resources: Resources, base64: String): Drawable? {
        val byteArray = Base64.decode(base64, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

        if (bitmap != null) {
            return BitmapDrawable(resources, bitmap)
        } else {
            return null
        }
    }

    fun base64ToBitmap(resources: Resources, base64: String): Bitmap? {
        val byteArray = Base64.decode(base64, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        return bitmap
    }

    fun openCropActivity(context: Activity, imageUri: Uri?) {
        try {
            val intent1 = Intent("com.android.camera.action.CROP")
            intent1.setDataAndType(imageUri, "image/*")
            intent1.putExtra("crop", "true")
            intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri) //
            intent1.putExtra("aspectX", 1)
            intent1.putExtra("aspectY", 1)
            intent1.putExtra("outputFormat", Bitmap.CompressFormat.JPEG)
            intent1.putExtra("outputX", 256)
            intent1.putExtra("outputY", 256)
            intent1.putExtra("scale", true)
            intent1.putExtra("scaleUpIfNeeded", true)
            intent1.putExtra("return-data", true)
            context.startActivityForResult(intent1, CROP_REQUEST_CODE)
        } catch (e: Exception) {
            throw e
        }

    }

}