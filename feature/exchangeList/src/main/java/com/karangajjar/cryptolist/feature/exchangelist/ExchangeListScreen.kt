package com.karangajjar.cryptolist.feature.exchangelist

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.karangajjar.cryptolist.core.model.exchangemodel.Data
import java.text.SimpleDateFormat
import java.util.Date


@Composable
internal fun ExchangeListRoute(
    viewModel: ExchangeListViewModel = hiltViewModel(),
    onExchangeItemClick: () -> Unit,
    navController: NavController
) {
    val exchangeListState by viewModel.exchangeListState.collectAsStateWithLifecycle()

    ExchangeListScreen(
        exchangeListUiState=  exchangeListState,
        onExchangeItemClick = onExchangeItemClick,
        navController = navController
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeListScreen(
    exchangeListUiState: ExchangeListUiState,
    onExchangeItemClick: () -> Unit,
    navController: NavController
) {
    BackHandler {
        navController.popBackStack()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Exchanges") }
            )
        }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when (exchangeListUiState) {
                is ExchangeListUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is ExchangeListUiState.Error -> {
                    Text(
                        text = exchangeListUiState.message,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                is ExchangeListUiState.ExchangeListEmpty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "List is Empty, Try Again!",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                is ExchangeListUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(items = exchangeListUiState.exchangeList) { item ->
                            ExchangeListItem(
                                exchangeItem = item,
                                onItemClick = onExchangeItemClick
                            )
                        }
                    }
                }

            }
        }

    }
}

@Composable
fun ExchangeListItem(
    modifier: Modifier = Modifier,
    exchangeItem: Data,
    onItemClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onItemClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .size(40.dp)
                    .background(color = Color.Gray, shape = CircleShape)
            ) {
                Text(
                    text = getInitials(exchangeItem.name),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = convertId(exchangeItem.name),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 32.dp)
                )
                Text(
                    text = "$${formatRate(exchangeItem.volumeUsd)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formatRate(exchangeItem.percentTotalVolume),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = convertTimestampToText(exchangeItem.updated),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

fun getInitials(id: String): String {
    val parts = id.split("-")
    return "${parts.first().first().uppercase()}${parts.last().first().uppercase()}"
}
fun convertId(id: String): String {
    return id.split("-").joinToString(" ") { it.capitalize() }
}
fun formatRate(rate: String): String {
    return "%.3f".format(rate.toDouble())
}

@SuppressLint("SimpleDateFormat")
fun convertTimestampToText(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = Date(timestamp)
    return dateFormat.format(date)
}