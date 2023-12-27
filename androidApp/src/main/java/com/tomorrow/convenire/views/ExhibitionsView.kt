package com.tomorrow.convenire.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tomorrow.convenire.R
import com.tomorrow.convenire.common.cards.DefaultCardDisplay
import com.tomorrow.convenire.common.headers.PageHeaderLayout
import com.tomorrow.convenire.feature_navigation.AppRoute
import com.tomorrow.convenire.launch.LocalNavController

@Composable
fun ExhibitionsView() = PageHeaderLayout(
    title = "Exhibitions",
    subtitle = "Explore, Connect, Discover"
) {
    val navController = LocalNavController.current

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = 24.dp)
    ) {
        DefaultCardDisplay(
            painter = painterResource(id = R.mipmap.ic_map_foreground),
            onClick = { navController.navigate(AppRoute.CompaniesByMap.generate()) },
            title = "Exhibition Map",
            subtitle = "Explore venue layout",
            isHighlighted = true
        )

        DefaultCardDisplay(
            painter = painterResource(id = R.mipmap.ic_building_foreground),
            onClick = { navController.navigate(AppRoute.Companies.generate()) },
            title = "Companies",
            subtitle = "Get to know the companies at the heart of our event",
        )

        DefaultCardDisplay(
            painter = painterResource(id = R.mipmap.ic_tribune_foreground),
            onClick = { navController.navigate(AppRoute.ProductCategories.generate()) },
            title = "Product Categories",
            subtitle = "Explore the diversity of products at our conference"
        )

        DefaultCardDisplay(
            painter = painterResource(id = R.mipmap.ic_offers_foreground),
            onClick = { navController.navigate(AppRoute.OffersAndDeals.generate()) },
            title = "Offers & Deals",
            subtitle = "Find the best exclusive deals"
        )
    }
}