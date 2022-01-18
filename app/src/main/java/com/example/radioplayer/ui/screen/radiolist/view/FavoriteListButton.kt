package com.example.radioplayer.ui.screen.radiolist.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.radioplayer.R
import com.example.radioplayer.ui.theme.MainTheme

@Composable
fun FavoriteListButton(modifier: Modifier,
                       onClick: () -> Unit) {
    Button(
        modifier = modifier,
        border = BorderStroke(0.dp, Color.Transparent),
        elevation = null,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MainTheme.colors.screenItemBackground
        ),
        onClick = onClick
    ) {
        Image(painter = painterResource(R.drawable.ic_favorite_list),
              contentDescription = stringResource(id = R.string.button_favorite_list)
        )
    }
}

@Preview
@Composable
fun FavoriteListButtonPreview() {
    MainTheme(darkTheme = false) {
        FavoriteListButton(
            modifier = Modifier,
            onClick = { }
        )
    }
}