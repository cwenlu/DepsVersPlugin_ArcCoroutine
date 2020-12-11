package com.cwl.common.imageloader.picasso

import android.graphics.*
import com.squareup.picasso.Transformation


class RoundTransform(var angle:Float) : Transformation {
    override fun key(): String ="RoundTransform"

    override fun transform(source: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(
            source.width,
            source.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = 0xff424242
        val paint = Paint()
        val rect = Rect(0, 0, source.width, source.height)
        val rectF = RectF(rect)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.setColor(color)
        canvas.drawRoundRect(rectF, angle, angle, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(source, rect, rect, paint)
        source.recycle()
        return output
    }
}