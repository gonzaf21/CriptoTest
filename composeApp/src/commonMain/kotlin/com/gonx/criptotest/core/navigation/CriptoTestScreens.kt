package com.gonx.criptotest.core.navigation

import kotlinx.serialization.Serializable

sealed interface NavScreen {
    @Serializable
    object Portfolio

    @Serializable
    object Coins

    @Serializable
    data class Buy(val coinId: String)

    @Serializable
    data class Sell(val coinId: String)
}
