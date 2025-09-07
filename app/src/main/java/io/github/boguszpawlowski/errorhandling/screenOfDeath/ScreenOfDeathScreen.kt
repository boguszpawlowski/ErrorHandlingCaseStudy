package io.github.boguszpawlowski.errorhandling.screenOfDeath

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.boguszpawlowski.errorhandling.R
import io.github.boguszpawlowski.errorhandling.ui.theme.ErrorHandlingCaseStudyTheme

@Composable
internal fun ScreenOfDeathScreen(
    threadName: String,
    exceptionName: String,
    exceptionStackTrace: String,
    appVersionName: String,
    onShareButtonClicked: () -> Unit,
    onAppRestartClicked: () -> Unit,
) {
    ErrorHandlingCaseStudyTheme {
        Scaffold(
            bottomBar = {
                BottomBar(
                    onShareButtonClicked = onShareButtonClicked,
                    onAppRestartClicked = onAppRestartClicked,
                )
            },
        ) { padding ->
            ExceptionContent(
                padding = padding,
                threadName = threadName,
                exceptionName = exceptionName,
                appVersionName = appVersionName,
                exceptionStackTrace = exceptionStackTrace,
            )
        }
    }
}

@Composable
private fun BottomBar(
    onShareButtonClicked: () -> Unit,
    onAppRestartClicked: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(onClick = onShareButtonClicked) { Text(text = stringResource(R.string.share)) }
        Button(onClick = onAppRestartClicked) { Text(text = stringResource(R.string.restart_app)) }
    }
}

@Composable
private fun ExceptionContent(
    padding: PaddingValues,
    threadName: String,
    exceptionName: String,
    appVersionName: String,
    exceptionStackTrace: String,
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        ContentText(
            text = stringResource(R.string.app_crashed_in_thread, threadName),
            textStyle = MaterialTheme.typography.headlineLarge,
        )

        ContentText(
            text = stringResource(R.string.exception, exceptionName),
            textStyle = MaterialTheme.typography.bodyMedium,
        )

        ContentText(
            text = stringResource(R.string.app_version, appVersionName),
            textStyle = MaterialTheme.typography.bodyMedium,
        )

        ContentText(
            text = exceptionStackTrace,
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.horizontalScroll(rememberScrollState()),
        )
    }
}

@Composable
private fun ContentText(
    text: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = Modifier.Companion
            .padding(8.dp)
            .then(modifier),
        style = textStyle,
    )
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Full Preview", showSystemUi = true)
@Composable
private fun ScreenOfDeathScreenPreview() {
    val stackTrace = Result
        .runCatching { throw TestException() }
        .exceptionOrNull()
        ?.stackTraceToString()
        .orEmpty()

    ScreenOfDeathScreen(
        threadName = "main",
        exceptionName = "java.lang.RuntimeException",
        exceptionStackTrace = stackTrace,
        appVersionName = "52.0.0",
        onShareButtonClicked = {},
        onAppRestartClicked = {},
    )
}

private class TestException : RuntimeException("Test exception")

