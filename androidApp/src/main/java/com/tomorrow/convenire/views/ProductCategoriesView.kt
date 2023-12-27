package com.tomorrow.convenire.views

import androidx.compose.runtime.Composable
import com.tomorrow.convenire.common.view_models.ReadViewModel
import com.tomorrow.convenire.feature_listing.ListDisplayItem
import com.tomorrow.convenire.feature_listing.ListDisplayPage
import com.tomorrow.convenire.feature_navigation.AppRoute
import com.tomorrow.convenire.launch.LocalNavController
import com.tomorrow.convenire.mappers.toListDisplayItem
import com.tomorrow.convenire.shared.domain.use_cases.GetCategoriesUseCase
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.getViewModel

class ProductCategoriesViewModel : ReadViewModel<List<ListDisplayItem>>(
    load = {
        GetCategoriesUseCase().getCategories()
            .map { cat -> cat.map { it.toListDisplayItem() } }
    },
    refresh = {
        GetCategoriesUseCase().refresh()
            .map { cat -> cat.map { it.toListDisplayItem() } }
    }
)

@Composable
fun ProductCategoriesView() {
    val navController = LocalNavController.current
    val viewModel: ProductCategoriesViewModel = getViewModel()

    ListDisplayPage(
        title = "Product Categories",
        onBackPress = { navController.navigate(AppRoute.Exhibitions.generate()) },
        onItemClick = { navController.navigate(AppRoute.ProductCategory.generateExplicit(it)) },
        viewModel = viewModel
    )
}

