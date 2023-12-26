package com.tomorrow.lda.views

import androidx.compose.runtime.Composable
import com.tomorrow.lda.common.view_models.DefaultReadView
import com.tomorrow.lda.common.view_models.ReadViewModel
import com.tomorrow.lda.feature_listing.ListDisplayItem
import com.tomorrow.lda.feature_listing.ListDisplayPage
import com.tomorrow.lda.feature_navigation.AppRoute
import com.tomorrow.lda.launch.LocalNavController
import com.tomorrow.lda.mappers.toListDisplayItem
import com.tomorrow.lda.shared.domain.model.Company
import com.tomorrow.lda.shared.domain.use_cases.GetCategoriesUseCase
import com.tomorrow.lda.shared.domain.use_cases.GetCompaniesByCategoryUseCase
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
            title = viewModel.state.viewData?.name ?: "",
            description = viewModel.state.viewData?.description ?: "",
            onBackPress = { navController.popBackStack() },
            onItemClick = { navController.navigate(AppRoute.Company.generateExplicit(it)) },
            viewModel = companiesByCategoryViewModel
        )
    }
}