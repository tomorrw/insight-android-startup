package com.tomorrow.lda.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tomorrow.lda.common.GeneralError
import com.tomorrow.lda.common.PageTabDisplay
import com.tomorrow.lda.common.SocialLink
import com.tomorrow.lda.common.SocialPlatform
import com.tomorrow.lda.common.ensureSize
import com.tomorrow.lda.common.headers.EntityDetailHeaderLayout
import com.tomorrow.lda.common.view_models.DefaultReadView
import com.tomorrow.lda.common.view_models.ReadViewModel
import com.tomorrow.lda.launch.LocalNavController
import com.tomorrow.lda.mappers.toPageUi
import com.tomorrow.lda.shared.domain.model.SpeakerDetail
import com.tomorrow.lda.shared.domain.use_cases.GetSpeakerByIdUseCase
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

class SpeakerDetailViewModel(id: String) :
    ReadViewModel<SpeakerDetail>(load = { GetSpeakerByIdUseCase().getSpeaker(id) })

@Composable
fun SpeakerDetailView(id: String) {
    val viewModel: SpeakerDetailViewModel = getViewModel { parametersOf(id) }

    DefaultReadView(viewModel = viewModel, error = {
        GeneralError(
            modifier = Modifier.padding(16.dp),
            message = it,
            description = "Please check your internet connection and try again.",
            onButtonClick = { viewModel.on(ReadViewModel.Event.OnRefresh) },
            hasBackButton = true
        )
    }) { speakerDetail ->
        val navController = LocalNavController.current
        EntityDetailHeaderLayout(
            title = viewModel.state.viewData?.getFullName() ?: "",
            subtitle = "${viewModel.state.viewData?.title ?: ""}${viewModel.state.viewData?.nationality?.name?.let { " | $it" } ?: ""}",
            image = viewModel.state.viewData?.image ?: "",
            socialLinks = viewModel.state.viewData?.socialLinks?.map {
                SocialLink(SocialPlatform.fromDomain(it), it.url)
            }?.ensureSize(5),
            onBack = { navController.popBackStack() },
            shareLink = "",
            decorativeIcon = speakerDetail.nationality?.url?.let {
                {
                    AsyncImage(
                        it,
                        contentDescription = "country",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(16.dp)
                            .background(MaterialTheme.colorScheme.background)
                    )
                }
            }
        ) {
            viewModel.state.viewData?.detailPages?.getDataIfLoaded()?.let {
                val pages =
                    remember(it) { derivedStateOf { it.map { page -> page.toPageUi() } } }
                if (pages.value.isNotEmpty())
                    PageTabDisplay(pages.value)
            }
        }
    }
}
