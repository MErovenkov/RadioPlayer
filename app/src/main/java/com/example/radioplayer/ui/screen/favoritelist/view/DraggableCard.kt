package com.example.radioplayer.ui.screen.favoritelist.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.radioplayer.R
import com.example.radioplayer.ui.screen.view.FrameItem
import com.example.radioplayer.ui.theme.MainTheme

@Composable
fun DraggableCard(musicTitle: String,
                  isRevealed: Boolean,
                  cardOffset: Int,
                  animationDuration: Int = 500,
                  onRevealed: (isRevealed: Boolean) -> Unit,
) {
    val offsetTransition by animateIntAsState(
        targetValue = if (isRevealed) -cardOffset else 0,
        animationSpec = tween(animationDuration),
    )

    FrameItem(modifier = Modifier.fillMaxWidth()
                                 .height(IntrinsicSize.Min)
                                 .offset { IntOffset(offsetTransition, 0) }
                                 .pointerInput(musicTitle) {
                                     detectHorizontalDragGestures { _, dragAmount ->
                                         onRevealed(dragAmount < 0)
                                     }
                                 }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Image(modifier = Modifier.padding(start = 16.dp)
                                     .weight(1f),
                  painter = painterResource(id = R.drawable.ic_music_note),
                  contentDescription = stringResource(id = R.string.ic_music_note)
            )

            Text(modifier = Modifier.fillMaxWidth()
                                    .padding(vertical = 16.dp, horizontal = 8.dp)
                                    .weight(6f),
                 text = musicTitle,
                 style = MainTheme.typography.basic,
                 color = MainTheme.colors.primaryText,
                 maxLines = 1,
                 overflow = TextOverflow.Ellipsis
            )

            Image(modifier = Modifier.weight(1f),
                  painter = painterResource(id = R.drawable.ic_arrow_back),
                  contentDescription = stringResource(id = R.string.ic_arrow_back)
            )
        }
    }
}

@Preview
@Composable
fun DraggableCardPreview() {
    MainTheme(darkTheme = false) {
        DraggableCard(musicTitle = "Dead Inside - Nita Strauss, David Draiman, Disturbed",
                      isRevealed = false,
                      cardOffset = 0,
                      animationDuration = 100,
                      onRevealed = { }
        )
    }
}