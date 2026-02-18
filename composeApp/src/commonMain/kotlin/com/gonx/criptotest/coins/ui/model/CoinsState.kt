package com.gonx.criptotest.coins.ui.model

import androidx.compose.runtime.Stable
import com.gonx.criptotest.coins.ui.model.UiCoinListItem
import org.jetbrains.compose.resources.StringResource

@Stable
data class CoinsState(
    val error: StringResource? = null,
    val coins: List<UiCoinListItem> = emptyList(),
    val chartState: UiChartState? = null,
)

@Stable
data class UiChartState(
    val sparkLine: List<Double> = emptyList(),
    val isLoading: Boolean = false,
    val coinName: String = "",
)
