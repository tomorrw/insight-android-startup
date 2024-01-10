package com.tomorrow.convenire.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.tomorrow.convenire.common.PageTabDisplay
import com.tomorrow.convenire.common.SocialLink
import com.tomorrow.convenire.common.SocialPlatform
import com.tomorrow.convenire.common.ensureSize
import com.tomorrow.convenire.common.headers.EntityDetailHeaderLayout
import com.tomorrow.convenire.common.view_models.DefaultReadView
import com.tomorrow.convenire.common.view_models.ReadViewModel
import com.tomorrow.convenire.launch.LocalNavController
import com.tomorrow.convenire.mappers.toPageUi
import com.tomorrow.convenire.shared.domain.model.Company
import com.tomorrow.convenire.shared.domain.use_cases.GetCompanyByIdUseCase
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

class CompanyViewModel(id: String) :
    ReadViewModel<Company>(
        load = { GetCompanyByIdUseCase().getCompany(id) },
        refresh = { GetCompanyByIdUseCase().getCompany(id) }
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
                SocialLink(SocialPlatform.fromDomain(it), it.url)
            }?.ensureSize(5),
            onBack = { navController.popBackStack() },
            shareLink = ""
        ) {
            it.detailPages.getDataIfLoaded()?.let {
                val pages =
                    remember(it) { derivedStateOf { it.toList().map { page -> page.toPageUi() } } }
                if (pages.value.isNotEmpty())
                    PageTabDisplay(pages.value)
            }
        }
    }
}
