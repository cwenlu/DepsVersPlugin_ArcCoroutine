package com.cwl.common.util

import java.util.concurrent.atomic.AtomicInteger

/**
 * api17 以上才有 复制于View
 * 不要混合用，版本高可直接使用View的
 * 也可以通过ids定义得到id
 */
object GenerateViewIds {
    private val sNextGeneratedId = AtomicInteger(1)
    /**
     * Generate a value suitable for use in [.setId].
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    fun generateViewId(): Int {
        while (true) {
            val result = sNextGeneratedId.get()
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            var newValue = result + 1
            if (newValue > 0x00FFFFFF) newValue = 1 // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result
            }
        }
    }

}