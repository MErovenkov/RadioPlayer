package com.example.radioplayer.ui.screen.favoritelist.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.radioplayer.R
import com.example.radioplayer.ui.theme.MainTheme

@Composable
fun ActionsRow(modifier: Modifier,
               onDelete: () -> Unit,
               onCopy: () -> Unit
) {
    Row(modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onCopy) {
            Icon(painter = painterResource(id = R.drawable.ic_content_copy),
                 tint = MainTheme.colors.tintIcon,
                 contentDescription = stringResource(id = R.string.button_copy_favorite_music))
        }

        IconButton(onClick = onDelete) {
            Icon(painter = painterResource(id = R.drawable.ic_content_delete),
                 tint = MainTheme.colors.tintIcon,
                 contentDescription = stringResource(id = R.string.button_delete_favorite_music))
        }
    }
}


@Preview
@Composable
fun ActionsRowPreview() {
    MainTheme(darkTheme = false) {
        ActionsRow(modifier = Modifier,
                   onDelete = { },
                   onCopy = { }
        )
    }
}