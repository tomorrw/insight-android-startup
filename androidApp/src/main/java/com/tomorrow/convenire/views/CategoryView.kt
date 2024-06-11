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
import com.tomorrow.convenire.shared.domain.model.Company
import com.tomorrow.convenire.shared.domain.use_cases.GetCategoriesUseCase
import com.tomorrow.convenire.shared.domain.use_cases.GetCompaniesByCategoryUseCase
import com.tomorrow.listdisplay.ListDisplayItem
import com.tomorrow.listdisplay.ListDisplayPage
import com.tomorrow.readviewmodel.DefaultReadView
import com.tomorrow.readviewmodel.ReadViewModel
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

class CategoryViewModel(id: String) : ReadViewModel<Company.Category?>(
    load = { GetCategoriesUseCase().getCategories().map { it.firstOrNull { cat -> cat.id == id } } }
)

class CompaniesByCategoryViewModel(categoryId: String) : ListDisplayReadViewModelImplementation<ListDisplayItem>(
    load = {
        GetCompaniesByCategoryUseCase().getCompanies(categoryId)
            .map { companies -> companies.map { it.toListDisplayItem() } }
    },
    refresh = {
        GetCompaniesByCategoryUseCase().getCompanies(categoryId)
            .map { companies -> companies.map { it.toListDisplayItem() } }
    }
)

@Composable
fun CategoryView(id: String) {
    val navController = LocalNavController.current

    val viewModel: CategoryViewModel = getViewModel { parametersOf(id) }
    val companiesByCategoryViewModel: CompaniesByCategoryViewModel =
        getViewModel { parametersOf(id) }

    DefaultReadView(viewModel = viewModel) {
        ListDisplayPage(
            title = it?.name ?: "",
            description = it?.description ?: "",
            onBackPress = { navController.popBackStack() },
            onItemClick = { navController.navigate(AppRoute.Company.generateExplicit(it)) },
            viewModel = companiesByCategoryViewModel,
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
}