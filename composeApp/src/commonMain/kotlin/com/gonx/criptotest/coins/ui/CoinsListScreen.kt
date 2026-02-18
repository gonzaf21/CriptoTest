package com.gonx.criptotest.coins.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gonx.criptotest.coins.ui.component.PerformanceChart
import com.gonx.criptotest.coins.ui.model.CoinsState
import com.gonx.criptotest.coins.ui.model.UiChartState
import com.gonx.criptotest.coins.ui.model.UiCoinListItem
import com.gonx.criptotest.coins.ui.viewmodel.CoinsListViewModel
import com.gonx.criptotest.theme.LocalCriptoTestColorsPalette
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CoinsListScreen() {
    val coinsListViewModel = koinViewModel<CoinsListViewModel>()
    val state by coinsListViewModel.state.collectAsStateWithLifecycle()

    CoinsListContent(
        state = state,
        onDismiss = coinsListViewModel::onDismissChart,
        onCoinLongPress = coinsListViewModel::onCoinLongPress,
        onCoinClick = coinsListViewModel::onCoinClick,
    )
}

@Composable
fun CoinsListContent(
    state: CoinsState,
    onDismiss: () -> Unit,
    onCoinLongPress: (String) -> Unit,
    onCoinClick: (String) -> Unit,
) {
    val noData = remember(state.coins) { state.coins.isEmpty() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (state.chartState != null) {
            CoinChartDialog(
                uiChartState = state.chartState,
                onDismiss = onDismiss
            )
        }

        if (noData) state.error?.let {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(it),
                color = MaterialTheme.colorScheme.error,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
            )
        }
        else CoinsList(
            coins = state.coins,
            onCoinLongPress = onCoinLongPress,
            onCoinClick = onCoinClick
        )
    }
}

@Composable
fun CoinsList(
    coins: List<UiCoinListItem>,
    onCoinLongPress: (String) -> Unit,
    onCoinClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "🔥 Top Coins:",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                )
            }

            items(coins) { coin ->
                CoinListItem(coin, onCoinLongPress, onCoinClick)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CoinListItem(
    coin: UiCoinListItem,
    onCoinLongPress: (String) -> Unit,
    onCoinClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = { onCoinLongPress(coin.id) },
                onClick = { onCoinClick(coin.id) })
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(4.dp)
                .clip(CircleShape)
                .size(40.dp),
            model = coin.iconUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = coin.name,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = coin.symbol,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = coin.formattedPrice,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = coin.formattedChange,
                color = if (coin.isPositive) LocalCriptoTestColorsPalette.current.profitGreen else LocalCriptoTestColorsPalette.current.lossRed,
                fontSize = MaterialTheme.typography.titleSmall.fontSize
            )
        }
    }
}

@Composable
fun CoinChartDialog(
    uiChartState: UiChartState,
    onDismiss: () -> Unit,
) {
    if (uiChartState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.size(32.dp))
        }
    } else {
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = onDismiss,
            title = { Text(text = "24h Price Chart for ${uiChartState.coinName}") },
            text = {
//            if (uiChartState.isLoading) {
//                Box(
//                    modifier = Modifier.fillMaxWidth().heightIn(min = 200.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator(modifier = Modifier.size(32.dp))
//                }
//            } else {
                PerformanceChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    nodes = uiChartState.sparkLine,
                    profitColor = LocalCriptoTestColorsPalette.current.profitGreen,
                    lossColor = LocalCriptoTestColorsPalette.current.lossRed,
                )
                // }
            },
            confirmButton = {},
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text(text = "Close")
                }
            }
        )
    }
}