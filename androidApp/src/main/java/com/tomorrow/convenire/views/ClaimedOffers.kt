package com.tomorrow.convenire.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tomorrow.components.cards.InlineHighlightedCard
import com.tomorrow.components.others.GeneralError
import com.tomorrow.convenire.packageImplementation.ListDisplayReadViewModelImplementation
import com.tomorrow.convenire.packageImplementation.mappers.toOfferDisplayItem
import com.tomorrow.convenire.packageImplementation.models.OfferDisplayItem
import com.tomorrow.convenire.feature_navigation.AppRoute
import com.tomorrow.convenire.launch.LocalNavController
import com.tomorrow.convenire.shared.domain.use_cases.GetOffersUseCase
import com.tomorrow.listdisplay.ListDisplayPage
import com.tomorrow.readviewmodel.ReadViewModel
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.getViewModel

class ClaimedOffersViewModel : ListDisplayReadViewModelImplementation<OfferDisplayItem>(
    load = {
        GetOffersUseCase().getClaimedOffers().map {
            it.mapNotNull { item -> item.toOfferDisplayItem() }
        }
    },
    refresh = {
        GetOffersUseCase().refreshClaimedOffers().map {
            it.mapNotNull { item -> item.toOfferDisplayItem() }
        }
    }
)

@Composable
fun ClaimedOffers() {
    val navController = LocalNavController.current
    val viewModel: ClaimedOffersViewModel = getViewModel()

    ListDisplayPage(
        title = "Claimed",
        description = "All the offers you’ve claimed",
        onBackPress = { navController.navigate(AppRoute.Exhibitions.generate()) },
        onItemClick = { navController.navigate(AppRoute.ProductCategory.generateExplicit(it)) },
        viewModel = viewModel,
        itemDisplay = {
            InlineHighlightedCard(
                name = it.title,
                avatar = it.imageUrlString,
                detailText = it.description,
                onClick = { navController.navigate(AppRoute.Post.generateExplicit(it.postId)) }
            )
        },
        emptyListView = {
            GeneralError(
                message = "No Claimed Offers",
                description = "You don't have any claimed offers.",
                onButtonClick = { viewModel.on(ReadViewModel.Event.OnRefresh) },
                buttonText = "Refresh"
            )
        },
        separator = { Spacer(Modifier.height(16.dp)) },
        shouldShowSearchBar = false
    )
}

