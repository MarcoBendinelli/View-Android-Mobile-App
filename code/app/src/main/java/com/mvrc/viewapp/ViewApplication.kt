package com.mvrc.viewapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom application class for the ViewApp. Extends the Android [Application] class
 * and is annotated with [HiltAndroidApp] to enable Hilt for dependency injection.
 *
 * Hilt is a dependency injection library for Android that reduces the boilerplate of
 * doing manual dependency injection inside the project. The [HiltAndroidApp] annotation
 * triggers Hilt's code generation for the application, setting up Dagger Hilt for use.
 */
@HiltAndroidApp
class ViewApplication : Application()