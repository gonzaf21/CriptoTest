package com.gonx.criptotest.trade.ui.buy.model

import com.gonx.criptotest.trade.ui.common.model.UiTradeCoinItem
import org.jetbrains.compose.resources.StringResource

data class TradeState(
    val isLoading: Boolean = false,
    val error: StringResource? = null,
    val availableAmount: String = "",
    val amount: String = "",
    val coin: UiTradeCoinItem? = null,
)
