package com.example.radioplayer.ui.screen.radiolist.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.radioplayer.ui.theme.MainTheme

@Composable
fun RadioTitleButton(modifier: Modifier, radioTitle: String, onClick: () -> Unit) {
    TextButton(
        modifier = modifier,
        colors = ButtonDefaults
            .buttonColors(backgroundColor = MainTheme.colors.screenItemBackground),
        onClick = onClick
    ) {
        Text(
            modifier = Modifier.fillMaxWidth()
                               .padding(8.dp),
            text = radioTitle,
            style = MainTheme.typography.basic,
            color = MainTheme.colors.primaryText
        )
    }
}

@Preview
@Composable
fun RadioTitleButtonPreview() {
    MainTheme(darkTheme = false) {
        RadioTitleButton(modifier = Modifier,
                         radioTitle = "RETRO FM",
                         onClick =  { }
        )
    }
}