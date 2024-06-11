package com.tomorrow.convenire.views

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tomorrow.components.others.GeneralError
import com.tomorrow.convenire.packageImplementation.ListDisplayReadViewModelImplementation
import com.tomorrow.convenire.packageImplementation.mappers.toListDisplayItem
import com.tomorrow.convenire.feature_navigation.AppRoute
import com.tomorrow.convenire.launch.LocalNavController
import com.tomorrow.convenire.shared.domain.use_cases.GetCategoriesUseCase
import com.tomorrow.listdisplay.ListDisplayItem
import com.tomorrow.listdisplay.ListDisplayPage
import com.tomorrow.readviewmodel.ReadViewModel
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.getViewModel

class ProductCategoriesViewModel : ListDisplayReadViewModelImplementation<ListDisplayItem>(
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
        viewModel = viewModel,
        emptyListView = {
            GeneralError(
                modifier = Modifier.padding(16.dp),
                message = "No data found",
                description = "Stay Tuned for more updates!",
                onButtonClick = { viewModel.on(ReadViewModel.Event.OnRefresh) },
            )
        }
    )
}

