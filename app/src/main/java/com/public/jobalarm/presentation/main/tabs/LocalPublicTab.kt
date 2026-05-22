package com.jobalarm.presentation.main.tabs

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.jobalarm.util.Constants

@Composable
fun LocalPublicTab() {
    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                loadUrl(Constants.LOCAL_INSTITUTION_URL)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
