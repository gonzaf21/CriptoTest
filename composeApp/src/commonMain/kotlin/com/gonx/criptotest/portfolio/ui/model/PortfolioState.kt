package com.gonx.criptotest.portfolio.ui.model

import org.jetbrains.compose.resources.StringResource

data class PortfolioState(
    val portfolioValue: String = "",
    val cashBalance: String = "",
    val showBuyBtn: Boolean = false,
    val isLoading: Boolean = false,
    val error: StringResource? = null,
    val coins: List<UiPortfolioCoinItem> = emptyList()
)