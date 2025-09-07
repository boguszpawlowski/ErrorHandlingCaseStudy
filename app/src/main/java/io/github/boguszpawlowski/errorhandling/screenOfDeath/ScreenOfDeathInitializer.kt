package io.github.boguszpawlowski.errorhandling.screenOfDeath

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Build

/**
 * Sets up a custom uncaught exception handler to display a crash screen with the stacktrace.
 * Uses a [ContentProvider] for early initialization, ensuring it runs before `Application`.
 * To avoid conflicts with Crashlytics, `initOrder` is configured in AndroidManifest.xml.
 */
internal class ScreenOfDeathExceptionHandlerInitializer : ContentProvider() {

    @Suppress("TooGenericExceptionCaught")
    override fun onCreate(): Boolean {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(
            object : Thread.UncaughtExceptionHandler {
                override fun uncaughtException(
                    thread: Thread,
                    e: Throwable,
                ) {
                    try {
                        showCrashScreen(thread, e)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    defaultHandler?.uncaughtException(thread, e)
                }
            },
        )
        return true
    }

     fun showCrashScreen(
        thread: Thread,
        e: Throwable,
    ) {
        context?.applicationContext?.let { applicationContext ->

            ScreenOfDeathActivity.createIntent(
                context = applicationContext,
                threadName = thread.name,
                exceptionName = e.javaClass.simpleName,
                exceptionStackTrace = e.stackTraceToString(),
                appVersionName = context?.packageManager?.getPackageInfo(context?.packageName.orEmpty(), 0)?.versionName.orEmpty(),

            ).let(applicationContext::startActivity)
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String?>?,
        selection: String?,
        selectionArgs: Array<out String?>?,
        sortOrder: String?,
    ): Cursor? = null

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String?>?,
    ): Int = 0

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String?>?,
    ): Int = 0
}

