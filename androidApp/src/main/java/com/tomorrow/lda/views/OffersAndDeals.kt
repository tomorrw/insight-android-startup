package com.tomorrow.lda.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tomorrow.lda.common.GeneralError
import com.tomorrow.lda.common.cards.InlineHighlightedCard
import com.tomorrow.lda.common.view_models.ReadViewModel
import com.tomorrow.lda.feature_listing.ListDisplayPage
import com.tomorrow.lda.feature_navigation.AppRoute
import com.tomorrow.lda.feature_offers.OfferDisplayItem
import com.tomorrow.lda.launch.LocalNavController
import com.tomorrow.lda.mappers.toOfferDisplayItem
import com.tomorrow.lda.shared.domain.use_cases.GetOffersUseCase
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.getViewModel


class OffersAndDealsViewModel : ReadViewModel<List<OfferDisplayItem>>(
    load = {
        GetOffersUseCase().getOffers().map {
            it.mapNotNull { item -> item.toOfferDisplayItem() }
        }
    },
    refresh = {
        GetOffersUseCase().refresh().map {
            it.mapNotNull { item -> item.toOfferDisplayItem() }
        }
    }
)

@Composable
fun OffersAndDeals() {
    val navController = LocalNavController.current
    val viewModel: OffersAndDealsViewModel = getViewModel()

    ListDisplayPage(
        title = "Offers & Deals",
        description = "Find the best exclusive deals",
        onBackPress = { navController.navigate(AppRoute.Exhibitions.generate()) },
        viewModel = viewModel,
        header = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp),
                onClick = { navController.navigate(AppRoute.ClaimedOffers.path) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0XFFE0ECF9),
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(modifier = Modifier.weight(1f), text = "Claimed", maxLines = 1)

                Icon(
                    Icons.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        },
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
                message = "No Offers",
                description = "No offers available now, please try again later",
                onButtonClick = { viewModel.on(ReadViewModel.Event.OnRefresh) },
                buttonText = "Refresh"
            )
        },
        separator = { Spacer(Modifier.height(16.dp)) },
        shouldShowSearchBar = false
    )
}

