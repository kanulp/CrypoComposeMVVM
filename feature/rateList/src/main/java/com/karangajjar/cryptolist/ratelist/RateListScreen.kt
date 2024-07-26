package com.karangajjar.cryptolist.ratelist

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.karangajjar.cryptolist.core.model.cryptolistmodel.Data


@Composable
internal fun RateListRoute(
    viewModel : RateListViewModel = hiltViewModel(),
    onRateItemClick:()->Unit,
    navController: NavController
){
    val filteredRateListUiState by viewModel.filteredRateList.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    RateListScreen(
        rateListUiSate = filteredRateListUiState,
        onRateItemClick = onRateItemClick,
        searchQuery = searchQuery,
        onSearchQueryChanged = viewModel::updateSearchQuery,
        onSortOrderToggle = viewModel::toggleSortOrder,
        navController =  navController
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateListScreen(
    rateListUiSate: RateListUiState,
    onRateItemClick:()->Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSortOrderToggle: () -> Unit,
    navController: NavController

){
    var localSearchQuery by remember { mutableStateOf(searchQuery) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Rate List") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("exchange_list_route")
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "Clear"
                    )
                }
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                TextField(
                    value = localSearchQuery,
                    onValueChange = {
                        localSearchQuery = it
                        onSearchQueryChanged(it)
                    },                    placeholder = { Text(text = "Search :") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    ),
                    trailingIcon = {
                        if (localSearchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                localSearchQuery = ""
                                onSearchQueryChanged("")
                            }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Clear")
                            }
                        }
                    }
                )
                IconButton(onClick = onSortOrderToggle) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_sort_by_size), // Replace with your sort icon resource
                        contentDescription = "Sort"
                    )
                }
            }
            when(rateListUiSate){
                is RateListUiState.Error -> {
                    Text(
                        text = rateListUiSate.message,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                is RateListUiState.Loading ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                is RateListUiState.RateListEmpty -> {
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
                is RateListUiState.Success -> {
                    LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(items = rateListUiSate.rateList) { rateItem ->
                                RateListItem(
                                    rateItem = rateItem,
                                    onItemClick = onRateItemClick
                                )
                            }
                        }
                }
            }
        }
    }
}

@Composable
fun RateListItem(
    modifier: Modifier = Modifier,
    rateItem: Data,
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
                    text = getInitials(rateItem.id),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = convertId(rateItem.id),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 32.dp)
                )
                Text(
                    text = "${rateItem.symbol} ${rateItem.currencySymbol ?: ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "$${formatRate(rateItem.rateUsd)}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = rateItem.type,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray // Change color based on positive/negative change
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