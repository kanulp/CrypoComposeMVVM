package com.karangajjar.cryptolist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.karangajjar.cryptolist.feature.exchangelist.exchangeListScreen
import com.karangajjar.cryptolist.ratelist.rateListScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination:String = "rate_list_route"
){
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        rateListScreen(onCryptoItemClick = navController::popBackStack, navController = navController)
        exchangeListScreen(
            onExchangeItemClick = Unit::toString,
            navController = navController
        )
    }
}