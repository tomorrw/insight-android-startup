package com.tomorrow.mobile_starter_app.views

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
import com.tomorrow.components.headers.EntityDetailHeaderLayout
import com.tomorrow.components.others.GeneralError
import com.tomorrow.components.others.SocialLink
import com.tomorrow.mobile_starter_app.packageImplementation.mappers.toSocialPlatform
import com.tomorrow.mobile_starter_app.common.PageTabDisplay
import com.tomorrow.mobile_starter_app.common.mappers.toPageUi
import com.tomorrow.mobile_starter_app.launch.LocalNavController
import com.tomorrow.mobile_starter_app.shared.domain.model.SpeakerDetail
import com.tomorrow.mobile_starter_app.shared.domain.use_cases.GetSpeakerByIdUseCase
import com.tomorrow.readviewmodel.DefaultReadView
import com.tomorrow.readviewmodel.ReadViewModel
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
    val navController = LocalNavController.current

    DefaultReadView(viewModel = viewModel, error = {
        GeneralError(
            modifier = Modifier.padding(16.dp),
            message = it,
            description = "Please check your internet connection and try again.",
            onButtonClick = { viewModel.on(ReadViewModel.Event.OnRefresh) },
            onBackClick = { navController.popBackStack() }
        )
    }) { speakerDetail ->
        EntityDetailHeaderLayout(
            title = speakerDetail.fullName.getFormattedName(),
            subtitle = "${speakerDetail.title}${speakerDetail.nationality?.name?.let { " | $it" } ?: ""}",
            image = speakerDetail.image ?: "",
            socialLinks = speakerDetail.socialLinks.map {
                SocialLink(it.platform.toSocialPlatform(), it.url)
            }.take(5),
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
            val surfaceColor = MaterialTheme.colorScheme.surface
            speakerDetail.detailPages.getDataIfLoaded()?.let {
                val pages =
                    remember(it) { derivedStateOf { it.map { page -> page.toPageUi(surfaceColor) } } }
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
