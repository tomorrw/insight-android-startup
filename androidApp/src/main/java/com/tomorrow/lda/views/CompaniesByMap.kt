package com.tomorrow.lda.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.tomorrow.lda.R
import com.tomorrow.lda.common.cards.InlineCardDisplay
import com.tomorrow.lda.common.headers.PageHeaderLayout
import com.tomorrow.lda.common.rememberForeverLazyListState
import com.tomorrow.lda.common.view_models.DefaultReadView
import com.tomorrow.lda.common.view_models.ReadViewModel
import com.tomorrow.lda.feature_listing.ListDisplay
import com.tomorrow.lda.feature_listing.ListDisplayItemInterface
import com.tomorrow.lda.feature_listing.ListLoader
import com.tomorrow.lda.feature_navigation.AppRoute
import com.tomorrow.lda.launch.LocalNavController
import com.tomorrow.lda.shared.domain.model.Company
import com.tomorrow.lda.shared.domain.use_cases.GetCompaniesUseCase
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.getViewModel
import java.util.UUID

class FloorMapListDisplayItem(
    override val id: String = UUID.randomUUID().toString(),
    override val title: String,
    override val description: String? = null,
    override val imageUrlString: String? = null,
    val floorMapGroup: Company.FloorMapGroup,
) : ListDisplayItemInterface

fun Company.toFloorMapListDisplayItem(): FloorMapListDisplayItem? = this.floorMapGroup?.let {
    FloorMapListDisplayItem(
        id = this.id,
        title = this.title,
        description = this.objectsClause,
        imageUrlString = this.image,
        floorMapGroup = it
    )
}


class CompaniesByMapViewModel : ReadViewModel<List<FloorMapListDisplayItem>>(
    load = {
        GetCompaniesUseCase().getCompanies().map { companies ->
            companies.mapNotNull { it.toFloorMapListDisplayItem() }
        }
    },
    refresh = {
        GetCompaniesUseCase().refresh().map { companies ->
            companies.mapNotNull { it.toFloorMapListDisplayItem() }
        }
    }
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompaniesByMap() {
    val viewModel: CompaniesByMapViewModel = getViewModel()
    val navController = LocalNavController.current

    val pullToRefreshState = rememberPullRefreshState(
        refreshing = viewModel.state.isRefreshing,
        onRefresh = { viewModel.on(ReadViewModel.Event.OnRefresh) }
    )
    val listState = rememberForeverLazyListState(key = "companiesByMap")

    PageHeaderLayout(
        title = "Exhibition Map",
        onBackPress = { navController.popBackStack() })
    {
        DefaultReadView(
            viewModel = viewModel,
            loader = { ListLoader() }
        ) { companies ->
            val firstItemIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }
            val firstItemOffset by remember { derivedStateOf { listState.firstVisibleItemScrollOffset } }
            var firstItemHeight by remember { mutableStateOf(0) }
            val density = LocalDensity.current
            val context = LocalContext.current
            val bottomOfHeaderToCenterOfArrow by remember { derivedStateOf { firstItemHeight / 2 } }
            val header = remember(firstItemOffset, firstItemIndex, firstItemHeight) {
                derivedStateOf {
                    val indexOfItemPointedByArrow =
                        if (firstItemOffset + bottomOfHeaderToCenterOfArrow > firstItemHeight)
                            firstItemIndex + 1
                        else firstItemIndex

                    val company = companies.getOrNull(indexOfItemPointedByArrow)
                    return@derivedStateOf (company
                        ?: companies.lastOrNull())?.floorMapGroup // index + 1 might be out of bounds if we are on the last element
                }
            }

            ListDisplay(
                items = companies,
                onItemClick = { id ->
                    navController.navigate(
                        AppRoute.Company.generateExplicit(
                            id
                        )
                    )
                },
                state = pullToRefreshState,
                isRefreshing = viewModel.state.isRefreshing,
                listState = listState,
                shouldShowSearchBar = false,
                itemDisplay = { item ->
                    InlineCardDisplay(
                        modifier = Modifier.onGloballyPositioned {
                            if (item.id !== companies.getOrNull(firstItemIndex)?.id) return@onGloballyPositioned
                            if (firstItemHeight != it.size.height) firstItemHeight = it.size.height
                        },
                        name = item.title,
                        avatar = item.imageUrlString,
                        detailText = item.description,
                        onClick = { navController.navigate(AppRoute.Company.generateExplicit(item.id)) }
                    )
                },
                header = {
                    Box(Modifier.fillMaxWidth()) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth(),
                            model = header.value?.floorImage,
                            placeholder = painterResource(id = R.drawable.ic_image_default_albumart),
                            error = painterResource(id = R.drawable.ic_image_default_albumart),
                            contentDescription = "floor map",
                            contentScale = ContentScale.Fit
                        )
                        AsyncImage(
                            modifier = Modifier.matchParentSize(),
                            model = header.value?.image,
                            contentDescription = "floor map",
                            placeholder = painterResource(id = R.drawable.ic_locating),
                            error = painterResource(id = R.drawable.ic_locating),
                            contentScale = ContentScale.Fit
                        )
                        CompositionLocalProvider(
                            LocalDensity provides Density(LocalDensity.current.density, 1f)
                        ) {
                            val size = 25.dp
                            Icon(
                                modifier = Modifier
                                    // beware don't change values those affect what header is displayed
                                    .size(size)
                                    .offset(y = with(density) { bottomOfHeaderToCenterOfArrow.toDp() } + size / 2)
                                    .align(Alignment.BottomEnd),
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                },
                isScrollableToLastElement = true,
            )

            LaunchedEffect(companies) {
                companies.forEach {
                    // preloading images
                    context.imageLoader.enqueue(
                        ImageRequest.Builder(context).data(it.floorMapGroup.image).build()
                    )
                }
            }
        }
    }
}