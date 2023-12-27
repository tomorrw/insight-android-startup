package com.tomorrow.convenire.common.headers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tomorrow.convenire.R
import com.tomorrow.convenire.common.AppSeparator
import com.tomorrow.convenire.common.SocialLink
import com.tomorrow.convenire.common.Socials
import com.tomorrow.convenire.common.buttons.BackButton
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntityDetailHeaderLayout(
    title: String,
    subtitle: String,
    image: String,
    socialLinks: List<SocialLink>?,
    decorativeIcon: (@Composable () -> Unit)? = null,
    onBack: () -> Unit,
    shareLink: String,
    content: @Composable () -> Unit,
) = Column {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        windowInsets = WindowInsets(12.dp, 12.dp, 12.dp, 0.dp),
        title = {},
        navigationIcon = { BackButton(onClick = { onBack() }) },
        actions = {
//            ShareButton(webLink = shareLink)
        },
    )

    Column(
        Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = Modifier
                .size(78.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.background),
            model = image,
            contentDescription = "",
            error = painterResource(id = R.drawable.ic_image_default_albumart),
            contentScale = ContentScale.Crop,
        )

        Row(
            Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppSeparator(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 21.dp)
            )
            val iconSize = with(LocalDensity.current) { 16.dp.toSp() }
            Text(
                buildAnnotatedString {
                    append(title)
                    decorativeIcon?.let {
                        append(" ")
                        appendInlineContent("inlineContent", "[icon]")
                    }
                },
                style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
                modifier = Modifier.padding(horizontal = 8.dp),
                overflow = TextOverflow.Ellipsis,
                inlineContent = mapOf(
                    Pair("inlineContent",
                        InlineTextContent(
                            Placeholder(
                                width = iconSize,
                                height = iconSize,
                                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                            )
                        ) { _ ->
                            decorativeIcon?.let {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                ) { it() }
                            }
                        }
                    )
                )
            )

            AppSeparator(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 21.dp)
            )
        }

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            textAlign = TextAlign.Center
        )

        socialLinks?.let {
            Socials(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                links = it,
            )
        }
    }

    Spacer(Modifier.height(32.dp))

    content()
}