package com.gonx.criptotest.trade.ui.sell

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gonx.criptotest.trade.ui.common.TradeScreen
import com.gonx.criptotest.trade.ui.common.TradeType
import com.gonx.criptotest.trade.ui.sell.viewmodel.SellViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SellScreen(
    coinId: String,
    navigateToPortfolio: () -> Unit,
) {
    val viewModel = koinViewModel<SellViewModel>(
        parameters = { parametersOf(coinId) }
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

    TradeScreen(
        state = state,
        tradeType = TradeType.SELL,
        onAmountChange = viewModel::onAmountChange,
        onSubmitClick = viewModel::onSellClick
    )
}