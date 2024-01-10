package com.tomorrow.convenire.views

import androidx.compose.runtime.Composable
import com.tomorrow.convenire.common.view_models.DefaultReadView
import com.tomorrow.convenire.common.view_models.ReadViewModel
import com.tomorrow.convenire.feature_listing.ListDisplayItem
import com.tomorrow.convenire.feature_listing.ListDisplayPage
import com.tomorrow.convenire.feature_navigation.AppRoute
import com.tomorrow.convenire.launch.LocalNavController
import com.tomorrow.convenire.mappers.toListDisplayItem
import com.tomorrow.convenire.shared.domain.model.Company
import com.tomorrow.convenire.shared.domain.use_cases.GetCategoriesUseCase
import com.tomorrow.convenire.shared.domain.use_cases.GetCompaniesByCategoryUseCase
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

class CategoryViewModel(id: String) : ReadViewModel<Company.Category?>(
    load = { GetCategoriesUseCase().getCategories().map { it.firstOrNull { cat -> cat.id == id } } }
)

class CompaniesByCategoryViewModel(categoryId: String) : ReadViewModel<List<ListDisplayItem>>(
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
            viewModel = companiesByCategoryViewModel
        )
    }
}