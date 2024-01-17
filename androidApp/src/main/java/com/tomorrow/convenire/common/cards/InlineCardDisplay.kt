package com.tomorrow.convenire.common.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun InlineCardDisplay(
    modifier: Modifier = Modifier,
    name: String? = null,
    avatar: String? = null,
    detailText: String? = null,
    onClick: () -> Unit = {}
) = InlineCardDisplay(
    modifier = modifier,
    nameComposable = name?.let {
        {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary),
                maxLines = 1,
            )
        }
    },
    avatar = avatar,
    detailText = detailText,
    onClick = onClick,
)

@Composable
fun InlineCardDisplay(
    modifier: Modifier = Modifier,
    nameComposable: (@Composable () -> Unit)? = null,
    avatar: String? = null,
    detailText: String? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(
                vertical = 16.dp,
                horizontal = 8.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            avatar?.let {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(48.dp)
                        .border(1.dp, Color(0xFFDAE6F1), CircleShape)
                        .background(
                            color = if (avatar.isNotEmpty())
                                MaterialTheme.colorScheme.background
                            else
                                Color(0xFFD3E2F0)
                        ),
                ) {
                    AsyncImage(
                        model = avatar,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                nameComposable?.let { it() }

                detailText?.let {
                    if (it.isNotBlank()) Text(
                        text = detailText,
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        maxLines = 1,
                    )
                } ?: Box(
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .height(16.dp)
                        .width(170.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFD3E2F0))
                )
            }
        }
    }
}

//@Composable
//@Preview(showBackground = true)
//fun PatientCardPreview() {
//    UserCard(
//        name = "Dr Elio Maroun",
//        detailText = "eliom@tm.services",
//        onClick = {}
//    )
//}
