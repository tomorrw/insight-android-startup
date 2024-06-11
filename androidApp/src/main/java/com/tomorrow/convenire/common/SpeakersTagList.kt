package com.tomorrow.convenire.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tomorrow.convenire.R
import com.tomorrow.convenire.feature_navigation.AppRoute
import com.tomorrow.convenire.launch.LocalNavController
import com.tomorrow.convenire.shared.domain.model.Speaker

@Composable
fun SpeakersTagList(speakers: List<Speaker>) {
    val navController = LocalNavController.current

    Column {
        Spacer(Modifier.height(8.dp))
        speakers.map {
            Spacer(Modifier.height(8.dp))

            Row(
                Modifier.clickable {
                    navController.navigate(
                        AppRoute.Speaker.generateExplicit(
                            it.id
                        )
                    )
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier.clip(CircleShape)
                        .size(18.dp),
                    model = it.nationality?.url,
                    placeholder = painterResource(id = R.drawable.ic_image_default_albumart),
                    error = painterResource(id = R.drawable.ic_image_default_albumart),
                    contentDescription = "floor map",
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = it.fullName.getFormattedName(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}