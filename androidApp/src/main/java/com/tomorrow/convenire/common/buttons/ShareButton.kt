package com.tomorrow.convenire.common.buttons

import android.content.Intent
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.tomorrow.convenire.R

@Composable
fun ShareButton(
    webLink: String?,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    val context = LocalContext.current

    IconButton(
        onClick = { context.startActivity(createShareSheetIntent(webLink)) },
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_share_icon),
            contentDescription = "Share Button",
            tint = tint
        )
    }
}

fun createShareSheetIntent(webLink: String?): Intent? {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, webLink)
        type = "text/plain"
    }
    return Intent.createChooser(sendIntent, null)
}