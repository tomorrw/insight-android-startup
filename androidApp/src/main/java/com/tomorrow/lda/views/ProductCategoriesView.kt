package com.tomorrow.lda.views

import androidx.compose.runtime.Composable
import com.tomorrow.lda.common.view_models.ReadViewModel
import com.tomorrow.lda.feature_listing.ListDisplayItem
import com.tomorrow.lda.feature_listing.ListDisplayPage
import com.tomorrow.lda.feature_navigation.AppRoute
import com.tomorrow.lda.launch.LocalNavController
import com.tomorrow.lda.mappers.toListDisplayItem
import com.tomorrow.lda.shared.domain.use_cases.GetCategoriesUseCase
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

