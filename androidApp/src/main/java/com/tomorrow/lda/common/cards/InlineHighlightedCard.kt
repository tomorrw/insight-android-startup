package com.tomorrow.lda.common.cards

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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun InlineHighlightedCard(
    modifier: Modifier = Modifier,
    name: String,
    avatar: String? = null,
    detailText: String? = null,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                )
            )
            .clickable { onClick() }
            .background(Color(0xFFFFFFFF))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            avatar?.let {
                Box(
                    modifier = Modifier
                        .padding(14.dp)
                        .clip(CircleShape)
                        .size(75.dp)
                        .border(0.3.dp, Color(0xFFDAE6F1), CircleShape)
                        .background(
                            shape = CircleShape,
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

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp
                    ),
                    maxLines = 2,
                )

                detailText?.let {
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = (Color(0xFF6594BD)),
                            fontSize = 14.sp
                        ),
                        maxLines = 1,
                    )
                }
            }
        }
    }
}