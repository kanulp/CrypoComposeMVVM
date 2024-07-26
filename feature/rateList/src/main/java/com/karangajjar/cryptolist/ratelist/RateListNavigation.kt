package com.karangajjar.cryptolist.ratelist

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val RATE_LIST_ROUTE = "rate_list_route"

fun NavController.navigateToRateList(navOptions: NavOptions? = null) = navigate(RATE_LIST_ROUTE, navOptions)


public fun NavGraphBuilder.rateListScreen(
    onCryptoItemClick: () -> Unit,
    navController: NavController
) {

    composable(route = RATE_LIST_ROUTE) {
        RateListRoute(
            onRateItemClick = onCryptoItemClick,
            navController = navController
        )
    }
}
