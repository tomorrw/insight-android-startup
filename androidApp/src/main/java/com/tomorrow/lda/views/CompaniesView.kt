package com.tomorrow.lda.views

import androidx.compose.runtime.Composable
import com.tomorrow.lda.common.view_models.ReadViewModel
import com.tomorrow.lda.feature_listing.ListDisplayItem
import com.tomorrow.lda.feature_listing.ListDisplayPage
import com.tomorrow.lda.feature_navigation.AppRoute
import com.tomorrow.lda.launch.LocalNavController
import com.tomorrow.lda.mappers.toListDisplayItem
import com.tomorrow.lda.shared.domain.use_cases.GetCompaniesUseCase
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.getViewModel

class CompaniesViewModel : ReadViewModel<List<ListDisplayItem>>(
    load = {
        GetCompaniesUseCase().getCompanies()
            .map { companies -> companies.map { it.toListDisplayItem() } }
    },
    refresh = {
        GetCompaniesUseCase().refresh()
            .map { companies -> companies.map { it.toListDisplayItem() } }
    }
)

@Composable
fun CompaniesView() {
    val viewModel: CompaniesViewModel = getViewModel()
    val navController = LocalNavController.current

    ListDisplayPage(
        title = "Companies",
        onBackPress = { navController.popBackStack() },
        onItemClick = { id -> navController.navigate(AppRoute.Company.generateExplicit(id)) },
        viewModel = viewModel
    )
}