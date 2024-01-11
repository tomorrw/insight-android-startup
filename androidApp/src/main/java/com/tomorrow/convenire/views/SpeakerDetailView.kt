package com.tomorrow.convenire.views

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
import com.tomorrow.convenire.common.GeneralError
import com.tomorrow.convenire.common.PageTabDisplay
import com.tomorrow.convenire.common.SocialLink
import com.tomorrow.convenire.common.SocialPlatform
import com.tomorrow.convenire.common.ensureSize
import com.tomorrow.convenire.common.headers.EntityDetailHeaderLayout
import com.tomorrow.convenire.common.view_models.DefaultReadView
import com.tomorrow.convenire.common.view_models.ReadViewModel
import com.tomorrow.convenire.launch.LocalNavController
import com.tomorrow.convenire.mappers.toPageUi
import com.tomorrow.convenire.shared.domain.model.SpeakerDetail
import com.tomorrow.convenire.shared.domain.use_cases.GetSpeakerByIdUseCase
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

class SpeakerDetailViewModel(id: String) :
    ReadViewModel<SpeakerDetail>(
        load = { GetSpeakerByIdUseCase().getSpeaker(id) },
        emptyCheck = { it.socialLinks.isEmpty() && it.detailPages.getDataIfLoaded()?.isEmpty() == true }
    )

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
            title = speakerDetail.getFullName(),
            subtitle = "${speakerDetail.title}${speakerDetail.nationality?.name?.let { " | $it" } ?: ""}",
            image = speakerDetail.image ?: "",
            socialLinks = speakerDetail.socialLinks?.map {
                SocialLink(SocialPlatform.fromDomain(it), it.url)
            },
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
            speakerDetail.detailPages.getDataIfLoaded()?.let {
                val pages =
                    remember(it) { derivedStateOf { it.map { page -> page.toPageUi() } } }
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
