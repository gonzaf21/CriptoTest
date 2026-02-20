package com.gonx.criptotest.trade.ui.buy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gonx.criptotest.trade.ui.buy.viewmodel.BuyViewModel
import com.gonx.criptotest.trade.ui.common.TradeScreen
import com.gonx.criptotest.trade.ui.common.TradeType
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BuyScreen(
    coinId: String,
    navigateToPortfolio: () -> Unit,
) {
    val viewModel = koinViewModel<BuyViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    //TODO: handle coinId

    TradeScreen(
        state = state,
        tradeType = TradeType.BUY,
        onAmountChange = viewModel::onAmountChange,
        onSubmitClick = viewModel::onBuyClick,
    )
}