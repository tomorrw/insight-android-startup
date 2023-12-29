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
import com.tomorrow.convenire.common.view_models.DefaultReadView
import com.tomorrow.convenire.common.view_models.ReadViewModel
import com.tomorrow.convenire.feature_navigation.AppRoute
import com.tomorrow.convenire.launch.LocalNavController
import com.tomorrow.convenire.shared.domain.model.ConfigurationData
import com.tomorrow.convenire.shared.domain.use_cases.GetConfigurationUseCase
import com.tomorrow.convenire.shared.domain.use_cases.GetUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.androidx.compose.koinViewModel

class ExhibitionsViewModel : ReadViewModel<ConfigurationData>(
    load = {
        GetConfigurationUseCase().getTicketInfo()
    },

    refresh = { GetConfigurationUseCase().getTicketInfo() }

)

@Composable
fun ExhibitionsView() = PageHeaderLayout(
    title = "Exhibitions",
    subtitle = "Explore, Connect, Discover"
) {
    val navController = LocalNavController.current
    val viewModel: ExhibitionsViewModel = koinViewModel()

    DefaultReadView(viewModel = viewModel) { configuration ->

        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = 24.dp)
        ) {
            if (configuration.showExhibitionMap) {
                DefaultCardDisplay(
                    painter = painterResource(id = R.mipmap.ic_map_foreground),
                    onClick = { navController.navigate(AppRoute.CompaniesByMap.generate()) },
                    title = "Exhibition Map",
                    subtitle = "Explore venue layout",
                    isHighlighted = true,
                )
            }
            DefaultCardDisplay(
                painter = painterResource(id = R.mipmap.ic_building_foreground),
                onClick = { navController.navigate(AppRoute.Companies.generate()) },
                title = "Companies",
                subtitle = "Get to know the companies at the heart of our event",
                isHighlighted = !configuration.showExhibitionMap,
            )

            DefaultCardDisplay(
                painter = painterResource(id = R.mipmap.ic_tribune_foreground),
                onClick = { navController.navigate(AppRoute.ProductCategories.generate()) },
                title = "Product Categories",
                subtitle = "Explore the diversity of products at our conference"
            )

            if (configuration.showExhibitionOffers) {
                DefaultCardDisplay(
                    painter = painterResource(id = R.mipmap.ic_offers_foreground),
                    onClick = { navController.navigate(AppRoute.OffersAndDeals.generate()) },
                    title = "Offers & Deals",
                    subtitle = "Find the best exclusive deals"
                )
            }
        }
    }
}