package com.mvrc.viewapp.core

import android.content.res.Configuration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Utility class for handling screen dimension-related conversions
 * to obtain a responsive design, where sizes can be adjusted dynamically
 * based on the device's characteristics.
 */
class ScreenUtils {
    companion object {

        // Dimensions of the current device
        var currentWidth: Int = 0
        var currentHeight: Int = 0

        // Dimensions inside the View design
        var prototypeWidth: Double = 375.0
        var prototypeHeight: Double = 734.0

        // Device orientation
        var orientation: Int = Configuration.ORIENTATION_PORTRAIT
            private set

        /**
         * Set current device screen dimensions based on the provided [Configuration].
         *
         * @param configuration The configuration containing screen dimensions.
         */
        fun setDimensions(configuration: Configuration) {
            val tempWidth = configuration.screenWidthDp
            val tempHeight = configuration.screenHeightDp

            currentWidth = if (tempHeight > tempWidth) tempWidth else tempHeight
            currentHeight = if (tempHeight > tempWidth) tempHeight else tempWidth

            orientation = configuration.orientation
        }

        /**
         * Set current device screen orientation.
         *
         * @param newOrientation The new orientation to be set.
         */
        fun setOrientation(newOrientation: Int) {
            orientation = newOrientation
        }

        /**
         * Helper function that provides a normalized height value based on the screen orientation.
         * In portrait mode, it considers the height ratio, and in landscape mode,
         * it considers the width ratio.
         */
        fun getNormalizedHeight(): Double {
            return if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                currentHeight / prototypeHeight
            } else {
                currentWidth / prototypeWidth
            }
        }

        /**
         * Helper function that provides a normalized width value based on the screen orientation.
         * In portrait mode, it considers the width ratio, and in landscape mode,
         * it considers the height ratio.
         */
        fun getNormalizedWidth(): Double {
            return if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                currentWidth / prototypeWidth
            } else {
                currentHeight / prototypeHeight
            }
        }

        /**
         * Helper function that provides a normalized width value without taking into account
         * the orientation of the screen. It always consider the smallest size of the device.
         */
        fun getNormalizedSmallestSize(): Double = currentWidth / prototypeWidth

        // Extension functions to adapter font

        fun Int.rT(): TextUnit = (getNormalizedSmallestSize() * this).sp

        fun Double.rT(): TextUnit = (getNormalizedSmallestSize() * this).sp

        // Extension functions to adapt according to the smallest dimension

        fun Double.r(): Dp = (getNormalizedSmallestSize() * this).dp

        fun Int.r(): Dp = (getNormalizedSmallestSize() * this).dp

        // Extension functions to adapt according the screen height

        fun Double.rh(): Dp = (getNormalizedHeight() * this).dp

        fun Int.rh(): Dp = (getNormalizedHeight() * this).dp

        // Extension functions to adapt according the screen width

        fun Double.rw(): Dp = (getNormalizedWidth() * this).dp

        fun Int.rw(): Dp = (getNormalizedWidth() * this).dp
    }
}