package io.github.boguszpawlowski.errorhandling.screenOfDeath

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kotlin.system.exitProcess

internal class ScreenOfDeathActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()

        val threadName = intent.getStringExtra(THREAD_NAME_EXTRA).orEmpty()
        val exceptionName = intent.getStringExtra(EXCEPTION_NAME_EXTRA).orEmpty()
        val exceptionStackTrace = intent.getStringExtra(EXCEPTION_STACK_TRACE_EXTRA).orEmpty()
        val appVersionName = intent.getStringExtra(APP_VERSION_NAME_EXTRA).orEmpty()

        setContent {
            ScreenOfDeathScreen(
                threadName = threadName,
                exceptionName = exceptionName,
                exceptionStackTrace = exceptionStackTrace,
                appVersionName = appVersionName,
                onShareButtonClicked = {
                    shareException(
                        threadName = threadName,
                        exceptionName = exceptionName,
                        exceptionStackTrace = exceptionStackTrace,
                        appVersionName = appVersionName,
                    )
                },
                onAppRestartClicked = ::onAppRestartClicked,
            )
        }
    }

    private fun shareException(
        threadName: String,
        exceptionName: String,
        exceptionStackTrace: String,
        appVersionName: String,
    ) {
        val message = "Thread: $threadName\n" +
                "App Version: $appVersionName\n" +
                "Exception: $exceptionName\n" +
                "StackTrace:\n$exceptionStackTrace"
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        this.startActivity(Intent.createChooser(shareIntent, null))
    }

    private fun onAppRestartClicked() {
        packageManager.getLaunchIntentForPackage(packageName)?.let {
            it.addFlags(FLAG_ACTIVITY_NEW_TASK)
            it.addFlags(FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(it)

            exitProcess(0)
        }
    }

    companion object {
        const val THREAD_NAME_EXTRA = "RedScreenOfDeathActivity.THREAD_NAME_EXTRA"
        const val EXCEPTION_NAME_EXTRA = "RedScreenOfDeathActivity.EXCEPTION_NAME_EXTRA"
        const val EXCEPTION_STACK_TRACE_EXTRA = "RedScreenOfDeathActivity.EXCEPTION_STACK_TRACE_EXTRA"
        const val APP_VERSION_NAME_EXTRA = "RedScreenOfDeathActivity.APP_VERSION_NAME_EXTRA"

        fun createIntent(
            context: Context,
            threadName: String,
            exceptionName: String,
            exceptionStackTrace: String,
            appVersionName: String,
        ): Intent =
            Intent(context, ScreenOfDeathActivity::class.java).apply {
                putExtra(THREAD_NAME_EXTRA, threadName)
                putExtra(EXCEPTION_NAME_EXTRA, exceptionName)
                putExtra(EXCEPTION_STACK_TRACE_EXTRA, exceptionStackTrace)
                putExtra(APP_VERSION_NAME_EXTRA, appVersionName)
                flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NO_ANIMATION
            }
    }
}

fun Activity.setScreenOrientation() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

