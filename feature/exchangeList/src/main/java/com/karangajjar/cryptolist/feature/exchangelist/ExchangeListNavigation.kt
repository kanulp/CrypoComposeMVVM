package com.karangajjar.cryptolist.feature.exchangelist

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val EXCHANGE_LIST_ROUTE = "exchange_list_route"

fun NavController.navigateToExchangeList(navOptions: NavOptions? = null) = navigate(EXCHANGE_LIST_ROUTE, navOptions)

public fun NavGraphBuilder.exchangeListScreen(
    onExchangeItemClick: () -> Unit,
    navController: NavController
) {
    composable(route = EXCHANGE_LIST_ROUTE) {
        ExchangeListRoute(
            onExchangeItemClick = onExchangeItemClick,
            navController = navController
        )
    }
}
