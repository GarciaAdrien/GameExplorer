package com.example.gamesearch

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class ImagePlaceholderTransformation : BitmapTransformation() {

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        return toTransform
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        // Empty implementation to satisfy the BitmapTransformation abstract class
    }
}
