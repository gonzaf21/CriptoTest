package com.gonx.criptotest

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.gonx.criptotest.coins.ui.CoinsListScreen
import com.gonx.criptotest.core.navigation.NavScreen
import com.gonx.criptotest.portfolio.ui.PortfolioScreen
import com.gonx.criptotest.theme.CriptoTestTheme
import com.gonx.criptotest.trade.ui.buy.BuyScreen
import com.gonx.criptotest.trade.ui.sell.SellScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
    KoinContext {
        val navController: NavHostController = rememberNavController()

        CriptoTestTheme {
            //Scaffold { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = NavScreen.Portfolio,
                modifier = Modifier
                    .fillMaxSize()
                //.padding(paddingValues)
            ) {
                composable<NavScreen.Portfolio> {
                    PortfolioScreen(
                        onCoinItemClick = { coinId ->
                            navController.navigate(
                                NavScreen.Sell(
                                    coinId
                                )
                            )
                        },
                        onDiscoverCoinsClick = { navController.navigate(NavScreen.Coins) }
                    )
                }
                composable<NavScreen.Coins> {
                    CoinsListScreen(onCoinClick = { coinId ->
                        navController.navigate(
                            NavScreen.Buy(
                                coinId
                            )
                        )
                    })
                }

                composable<NavScreen.Buy> { navBackStackEntry ->
                    val coinId: String = navBackStackEntry.toRoute<NavScreen.Buy>().coinId

                    BuyScreen(
                        coinId = coinId,
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
                    val coinId: String = navBackStackEntry.toRoute<NavScreen.Buy>().coinId

                    SellScreen(
                        coinId = coinId,
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
            //}
        }
    }
}