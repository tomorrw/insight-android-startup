package com.tomorrow.lda.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.tomorrow.lda.common.PageTabDisplay
import com.tomorrow.lda.common.SocialLink
import com.tomorrow.lda.common.SocialPlatform
import com.tomorrow.lda.common.ensureSize
import com.tomorrow.lda.common.headers.EntityDetailHeaderLayout
import com.tomorrow.lda.common.view_models.DefaultReadView
import com.tomorrow.lda.common.view_models.ReadViewModel
import com.tomorrow.lda.launch.LocalNavController
import com.tomorrow.lda.mappers.toPageUi
import com.tomorrow.lda.shared.domain.model.Company
import com.tomorrow.lda.shared.domain.use_cases.GetCompanyByIdUseCase
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
            title = viewModel.state.viewData?.title ?: "",
            subtitle = viewModel.state.viewData?.boothDescription ?: "",
            image = viewModel.state.viewData?.image ?: "",
            socialLinks = viewModel.state.viewData?.socialLinks?.map {
                SocialLink(SocialPlatform.fromDomain(it), it.url)
            }?.ensureSize(5),
            onBack = { navController.popBackStack() },
            shareLink = ""
        ) {
            viewModel.state.viewData?.detailPages?.getDataIfLoaded()?.let {
                val pages =
                    remember(it) { derivedStateOf { it.toList().map { page -> page.toPageUi() } } }
                if (pages.value.isNotEmpty())
                    PageTabDisplay(pages.value)
            }
        }
    }
}
