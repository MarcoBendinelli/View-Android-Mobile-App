package com.mvrc.viewapp.core

import android.content.res.Configuration
import com.mvrc.viewapp.core.ScreenUtils.Companion.r
import com.mvrc.viewapp.core.ScreenUtils.Companion.rT
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * These tests validate the behavior of utility functions in the [ScreenUtils] class,
 * including screen dimension calculations and orientation setting.
 */
class ScreenUtilsTest {

    private lateinit var mockConfiguration: Configuration

    @Before
    fun setUp() {
        mockConfiguration = mockk {
            screenHeightDp = 800
            screenWidthDp = 400
        }
    }

    @Test
    fun `setDimensions set current device screen dimensions correctly`() {
        ScreenUtils.setDimensions(mockConfiguration)

        assertEquals(400, ScreenUtils.currentWidth)
        assertEquals(800, ScreenUtils.currentHeight)
    }

    @Test
    fun `setOrientation set current device screen orientation correctly`() {
        ScreenUtils.setOrientation(Configuration.ORIENTATION_LANDSCAPE)

        assertEquals(Configuration.ORIENTATION_LANDSCAPE, ScreenUtils.orientation)
    }

    @Test
    fun `getNormalizedHeight calculate normalized height correctly in portrait mode`() {
        ScreenUtils.setOrientation(Configuration.ORIENTATION_PORTRAIT)
        ScreenUtils.currentHeight = 800
        ScreenUtils.prototypeHeight = 1000.0

        val result = ScreenUtils.getNormalizedHeight()

        assertEquals(800.0 / 1000.0, result, 0.01)
    }

    @Test
    fun `getNormalizedWidth calculate normalized width correctly in portrait mode`() {
        ScreenUtils.setOrientation(Configuration.ORIENTATION_PORTRAIT)
        ScreenUtils.currentWidth = 400
        ScreenUtils.prototypeWidth = 500.0

        val result = ScreenUtils.getNormalizedWidth()

        assertEquals(400.0 / 500.0, result, 0.01)
    }

    @Test
    fun `getNormalizedHeight calculate normalized height correctly in landscape mode`() {
        ScreenUtils.setOrientation(Configuration.ORIENTATION_LANDSCAPE)
        ScreenUtils.currentWidth = 400
        ScreenUtils.prototypeWidth = 500.0

        val result = ScreenUtils.getNormalizedHeight()

        assertEquals(400.0 / 500.0, result, 0.01)
    }

    @Test
    fun `getNormalizedWidth calculate normalized width correctly in landscape mode`() {
        ScreenUtils.setOrientation(Configuration.ORIENTATION_LANDSCAPE)
        ScreenUtils.currentHeight = 800
        ScreenUtils.prototypeHeight = 1000.0

        val result = ScreenUtils.getNormalizedWidth()

        assertEquals(800.0 / 1000.0, result, 0.01)
    }

    @Test
    fun `getNormalizedSmallestSize calculate normalized smallest size correctly`() {
        ScreenUtils.currentWidth = 300
        ScreenUtils.prototypeWidth = 600.0

        val result = ScreenUtils.getNormalizedSmallestSize()

        assertEquals(300.0 / 600.0, result, 0.01)
    }

    @Test
    fun `extension function rT for Int convert to TextUnit correctly`() {
        ScreenUtils.currentWidth = 300
        ScreenUtils.prototypeWidth = 600.0

        val result = 20.rT()

        assertEquals((300.0 / 600.0) * 20, result.value.toDouble(), 0.01)
    }

    @Test
    fun `extension function rT for Double convert to TextUnit correctly`() {
        ScreenUtils.currentWidth = 300
        ScreenUtils.prototypeWidth = 600.0

        val result = 0.5.rT()

        assertEquals((300 / 600.0) * 0.5, result.value.toDouble(), 0.01)
    }

    @Test
    fun `extension function r for Double convert to Dp correctly`() {
        ScreenUtils.currentWidth = 300
        ScreenUtils.prototypeWidth = 600.0

        val result = 0.5.r()

        assertEquals((300 / 600.0) * 0.5, result.value.toDouble(), 0.01)
    }

    @Test
    fun `extension function r for Int should to Dp correctly`() {
        ScreenUtils.currentWidth = 300
        ScreenUtils.prototypeWidth = 600.0

        val result = 20.r()

        assertEquals((300 / 600.0) * 20.0, result.value.toDouble(), 0.01)
    }

    @Test
    fun `extension function rh for Double convert to Dp based on screen height correctly`() {
        ScreenUtils.setOrientation(Configuration.ORIENTATION_PORTRAIT)
        ScreenUtils.currentHeight = 800
        ScreenUtils.prototypeHeight = 1000.0

        val result = 0.5.rh()

        assertEquals(0.5 * (800.0 / 1000.0), result.value.toDouble(), 0.01)
    }

    @Test
    fun `extension function rh for Int convert to Dp based on screen height correctly`() {
        ScreenUtils.setOrientation(Configuration.ORIENTATION_PORTRAIT)
        ScreenUtils.currentHeight = 800
        ScreenUtils.prototypeHeight = 1000.0

        val result = 20.rh()

        assertEquals(20.0 * (800.0 / 1000.0), result.value.toDouble(), 0.01)
    }

    @Test
    fun `extension function rw for Double convert to Dp based on screen width correctly`() {
        ScreenUtils.setOrientation(Configuration.ORIENTATION_PORTRAIT)
        ScreenUtils.currentWidth = 400
        ScreenUtils.prototypeWidth = 500.0

        val result = 0.5.rw()

        assertEquals(0.5 * (400.0 / 500.0), result.value.toDouble(), 0.01)
    }

    @Test
    fun `extension function rw for Int convert to Dp based on screen width correctly`() {
        ScreenUtils.setOrientation(Configuration.ORIENTATION_PORTRAIT)
        ScreenUtils.currentWidth = 400
        ScreenUtils.prototypeWidth = 500.0

        val result = 20.rw()

        assertEquals(20.0 * (400.0 / 500.0), result.value.toDouble(), 0.01)
    }
}