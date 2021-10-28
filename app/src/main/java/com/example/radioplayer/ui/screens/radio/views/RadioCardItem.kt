package com.example.radioplayer.ui.screens.radio.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.radioplayer.ui.theme.MainTheme
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata

@Composable
fun RadioCardItem(radioItem: MediaItem, onClick: () -> Unit) {
    Card(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp / 2)
        .fillMaxWidth()
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(
                bounded = true,
                color = MainTheme.colors.rippleEffectColor
            ),
            onClick = onClick
        ),
        elevation = 8.dp,
        backgroundColor = MainTheme.colors.screenItemBackground,
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            text = radioItem.mediaMetadata.title.toString(),
            style = MainTheme.typography.basic,
            color = MainTheme.colors.primaryText
        )
    }
}

@Preview
@Composable
fun RadioCardItemPreview() {
    MainTheme(darkTheme = false) {
        RadioCardItem(
            MediaItem.Builder()
                .setUri("uri")
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle("RETRO FM")
                        .build()
                )
                .build()
        ) { }
    }
}
