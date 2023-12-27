package com.tomorrow.convenire.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tomorrow.convenire.common.GeneralError
import com.tomorrow.convenire.common.Loader
import com.tomorrow.convenire.common.SectionDisplay
import com.tomorrow.convenire.common.buttons.ActionButton
import com.tomorrow.convenire.common.headers.SecondaryEntityDetailHeaderLayout
import com.tomorrow.convenire.common.view_models.DefaultReadView
import com.tomorrow.convenire.common.view_models.ReadViewModel
import com.tomorrow.convenire.feature_navigation.AppRoute
import com.tomorrow.convenire.launch.LocalNavController
import com.tomorrow.convenire.mappers.toPageUi
import com.tomorrow.convenire.shared.domain.model.Post
import com.tomorrow.convenire.shared.domain.use_cases.GetPostByIdUseCase
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

class NewsArticleViewModel(id: String) :
    ReadViewModel<Post>(load = { GetPostByIdUseCase().getPost(id) }) {
}

@Composable
fun PostView(id: String) {
    val navController = LocalNavController.current
    val viewModel: NewsArticleViewModel = getViewModel { parametersOf(id) }

    DefaultReadView(viewModel = viewModel, error = {
        GeneralError(
            modifier = Modifier.padding(16.dp),
            message = it,
            description = "Please check your internet connection and try again.",
            onButtonClick = { viewModel.on(ReadViewModel.Event.OnRefresh) },
            hasBackButton = true
        )
    }) { d ->
        SecondaryEntityDetailHeaderLayout(
            title = "",
            image = d.image ?: "",
            socialLinks = null,
            onBack = { navController.popBackStack() },
        ) {
            item {
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = d.title,
                        style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurface)
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = d.description,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    Spacer(Modifier.height(18.dp))

                    Text(
                        text = d.getHumanReadablePublishedAt(),
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        modifier = Modifier
                    )


                    if (d.company != null) {
                        Spacer(Modifier.height(18.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(AppRoute.Company.generateExplicit(id))
                                }
                                .padding(10.dp)
                        )
                        {
                            d.company?.image?.let {
                                AsyncImage(
                                    model = it,
                                    contentDescription = "company image",
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(18.dp)
                                        .background(MaterialTheme.colorScheme.background)
                                )
                            }

                            d.company?.title?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                                    modifier = Modifier
                                        .padding(start = 4.dp)
                                        .align(Alignment.CenterVertically)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))
            }

            d.detailPage.getDataIfLoaded()?.toPageUi()?.sections?.let { sections ->
                items(sections) { section ->
                    SectionDisplay(section = section)

                    Spacer(Modifier.height(32.dp))
                }
            } ?: item { Loader() }

            d.action.map { item { ActionButton(Modifier.padding(horizontal = 16.dp).padding(vertical = 5.dp), action = it) } }
        }
    }
}