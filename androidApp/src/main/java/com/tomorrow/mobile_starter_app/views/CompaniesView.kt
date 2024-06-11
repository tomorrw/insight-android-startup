package com.tomorrow.mobile_starter_app.views

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tomorrow.components.others.GeneralError
import com.tomorrow.mobile_starter_app.packageImplementation.ListDisplayReadViewModelImplementation
import com.tomorrow.mobile_starter_app.packageImplementation.mappers.toListDisplayItem
import com.tomorrow.mobile_starter_app.feature_navigation.AppRoute
import com.tomorrow.mobile_starter_app.launch.LocalNavController
import com.tomorrow.mobile_starter_app.shared.domain.use_cases.GetCompaniesUseCase
import com.tomorrow.listdisplay.ListDisplayItem
import com.tomorrow.listdisplay.ListDisplayPage
import com.tomorrow.readviewmodel.ReadViewModel
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.getViewModel

class CompaniesViewModel : ListDisplayReadViewModelImplementation<ListDisplayItem>(
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