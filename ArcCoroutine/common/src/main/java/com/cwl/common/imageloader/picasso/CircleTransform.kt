package com.cwl.common.imageloader.picasso

import android.graphics.*
import com.squareup.picasso.Transformation
import kotlin.math.min
import android.graphics.Bitmap


class CircleTransform :Transformation {
    override fun key(): String ="CircleTransform"

    override fun transform(source: Bitmap): Bitmap {
        val size = min(source.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        val squared = Bitmap.createBitmap(source, x, y, size, size)
        if(squared!=source) source.recycle()
        val output = Bitmap.createBitmap(
            size,
            size, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val paint = Paint()
        val shader = BitmapShader(squared,
            Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true
        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        squared.recycle()
        return output

    }


}