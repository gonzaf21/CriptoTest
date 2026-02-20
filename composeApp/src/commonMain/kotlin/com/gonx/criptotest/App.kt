package com.gonx.criptotest


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gonx.criptotest.coins.ui.CoinsListScreen
import com.gonx.criptotest.core.navigation.NavScreen
import com.gonx.criptotest.portfolio.ui.PortfolioScreen
import com.gonx.criptotest.theme.CriptoTestTheme
import com.gonx.criptotest.trade.ui.buy.BuyScreen
import com.gonx.criptotest.trade.ui.sell.SellScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val navController: NavHostController = rememberNavController()

    CriptoTestTheme {
        NavHost(
            navController = navController,
            startDestination = NavScreen.Portfolio,
            modifier = Modifier.fillMaxSize()
        ) {
            composable<NavScreen.Portfolio> {
                PortfolioScreen(
                    onCoinItemClick = { coinId -> navController.navigate(NavScreen.Sell) },
                    onDiscoverCoinsClick = { navController.navigate(NavScreen.Coins) }
                )
            }
            composable<NavScreen.Coins> {
                CoinsListScreen(onCoinClick = { coinId -> navController.navigate(NavScreen.Buy) })
            }

            composable<NavScreen.Buy> { navBackStackEntry ->
                BuyScreen(
                    coinId = "todo",
                    navigateToPortfolio = {
                        navController.navigate(NavScreen.Portfolio) {
                            popUpTo(NavScreen.Portfolio) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable<NavScreen.Sell> { navBackStackEntry ->
                SellScreen(
                    coinId = "todo",
                    navigateToPortfolio = {
                        navController.navigate(NavScreen.Portfolio) {
                            popUpTo(NavScreen.Portfolio) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}