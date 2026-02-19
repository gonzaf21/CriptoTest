package com.gonx.criptotest.trade.mapper

import com.gonx.criptotest.core.coin.Coin
import com.gonx.criptotest.trade.ui.common.model.UiTradeCoinItem

fun UiTradeCoinItem.toCoin() = Coin(
    id = id,
    name = name,
    symbol = symbol,
    iconUrl = iconUrl,
)
