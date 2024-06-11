package com.tomorrow.mobile_starter_app.views

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tomorrow.components.headers.EntityDetailHeaderLayout
import com.tomorrow.components.others.GeneralError
import com.tomorrow.components.others.SocialLink
import com.tomorrow.mobile_starter_app.packageImplementation.mappers.toSocialPlatform
import com.tomorrow.mobile_starter_app.common.PageTabDisplay
import com.tomorrow.mobile_starter_app.common.mappers.toPageUi
import com.tomorrow.mobile_starter_app.launch.LocalNavController
import com.tomorrow.mobile_starter_app.shared.domain.model.Company
import com.tomorrow.mobile_starter_app.shared.domain.use_cases.GetCompanyByIdUseCase
import com.tomorrow.readviewmodel.DefaultReadView
import com.tomorrow.readviewmodel.ReadViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

class CompanyViewModel(id: String) :
    ReadViewModel<Company>(
        load = { GetCompanyByIdUseCase().getCompany(id) },
        refresh = { GetCompanyByIdUseCase().getCompany(id) },
        emptyCheck = { it.socialLinks.isEmpty() && it.detailPages.getDataIfLoaded()?.isEmpty() == true }
    )

@Composable
fun CompanyView(id: String) {
    val viewModel: CompanyViewModel = getViewModel { parametersOf(id) }
    DefaultReadView(viewModel = viewModel) {
        val navController = LocalNavController.current
        EntityDetailHeaderLayout(
            title = it.title,
            subtitle = it.boothDescription,
            image = it.image ?: "",
            socialLinks = it.socialLinks.map {
                SocialLink( it.platform.toSocialPlatform(), it.url)
            }.take(5),
            onBack = { navController.popBackStack() },
            shareLink = ""
        ) {
            it.detailPages.getDataIfLoaded()?.let {
                val pages =
                    remember(it) { derivedStateOf { it.toList().map { page -> page.toPageUi() } } }
                if (pages.value.isNotEmpty())
                    PageTabDisplay(pages.value)
                else GeneralError(
                    modifier = Modifier.padding(16.dp),
                    message = "No data found",
                    description = "Stay Tuned for more updates!",
                )
            }
        }
    }
}
