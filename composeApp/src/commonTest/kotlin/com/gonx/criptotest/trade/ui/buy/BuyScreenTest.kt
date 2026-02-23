package com.gonx.criptotest.trade.ui.buy

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.gonx.criptotest.trade.ui.common.COIN_NAME_TEST_TAG
import com.gonx.criptotest.trade.ui.common.ERROR_TEST_TAG
import com.gonx.criptotest.trade.ui.common.TradeScreen
import com.gonx.criptotest.trade.ui.common.TradeType
import com.gonx.criptotest.trade.ui.common.model.TradeState
import com.gonx.criptotest.trade.ui.common.model.UiTradeCoinItem
import criptotest.composeapp.generated.resources.Res
import criptotest.composeapp.generated.resources.error_unknown
import kotlin.test.Test

class BuyScreenTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun checkSubmitButtonLabelChangesWithTradeType() = runComposeUiTest {
        val state = TradeState(
            coin = UiTradeCoinItem(
                id = "bitcoin",
                name = "Bitcoin",
                symbol = "BTC",
                iconUrl = "url",
                price = 50000.0,
            )
        )

        setContent {
            TradeScreen(
                state = state,
                tradeType = TradeType.BUY,
                onAmountChange = {},
                onSubmitClick = {},
            )
        }

        onNodeWithText("Sell Now").assertDoesNotExist()
        onNodeWithText("Buy Now").assertExists()
        onNodeWithText("Buy Now").assertIsDisplayed()

        setContent {
            TradeScreen(
                state = state,
                tradeType = TradeType.SELL,
                onAmountChange = {},
                onSubmitClick = {},
            )
        }

        onNodeWithText("Buy Now").assertDoesNotExist()
        onNodeWithText("Sell Now").assertExists()
        onNodeWithText("Sell Now").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun checkIfCoinNameShownProperlyInBuy() = runComposeUiTest {
        val state = TradeState(
            coin = UiTradeCoinItem(
                id = "bitcoin",
                name = "Bitcoin",
                symbol = "BTC",
                iconUrl = "url",
                price = 50000.0,
            )
        )

        setContent {
            TradeScreen(
                state = state,
                tradeType = TradeType.BUY,
                onAmountChange = {},
                onSubmitClick = {},
            )
        }

        onNodeWithTag(COIN_NAME_TEST_TAG).assertExists()
        onNodeWithTag(COIN_NAME_TEST_TAG).assertTextEquals("Bitcoin")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun checkIfErrorIsShownProperly() = runComposeUiTest {
        val state = TradeState(
            coin = UiTradeCoinItem(
                id = "bitcoin",
                name = "Bitcoin",
                symbol = "BTC",
                iconUrl = "url",
                price = 50000.0,
            ),
            error = Res.string.error_unknown
        )

        setContent {
            TradeScreen(
                state = state,
                tradeType = TradeType.BUY,
                onAmountChange = {},
                onSubmitClick = {},
            )
        }

        onNodeWithTag(ERROR_TEST_TAG).assertExists()
        onNodeWithTag(ERROR_TEST_TAG).assertIsDisplayed()
    }
}