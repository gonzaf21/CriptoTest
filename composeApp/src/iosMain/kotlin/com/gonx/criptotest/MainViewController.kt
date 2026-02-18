package com.gonx.criptotest

import androidx.compose.ui.window.ComposeUIViewController
import com.gonx.criptotest.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}