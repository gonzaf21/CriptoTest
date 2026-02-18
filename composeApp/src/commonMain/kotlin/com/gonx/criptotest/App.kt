package com.gonx.criptotest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gonx.criptotest.coins.ui.CoinsListScreen
import com.gonx.criptotest.portfolio.ui.PortfolioScreen
import com.gonx.criptotest.theme.CriptoTestTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
    KoinContext {
        CriptoTestTheme {
            Scaffold { paddingValues ->
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                    //CoinsListScreen()
                    PortfolioScreen(
                        onCoinItemClick = {},
                        onDiscoverCoinsClick = {}
                    )
                }
            }
        }
    }
}