package com.example.radioplayer.ui.screen.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.radioplayer.R
import com.example.radioplayer.ui.theme.MainTheme

@Composable
fun ActionDialog(title: String,
                 text: String,
                 actionTitle: String,
                 onResult: (choice: Boolean) -> Unit
) {
    AlertDialog(
        backgroundColor = MainTheme.colors.screenItemBackground,
        onDismissRequest = { onResult(false) },
        title = {
            Text(text = title,
                 textAlign = TextAlign.Center,
                 style = MainTheme.typography.headingSmall,
                 color = MainTheme.colors.primaryText)
        },
        text = {
            Text(text = text,
                 textAlign = TextAlign.Center,
                 style = MainTheme.typography.basic,
                 color = MainTheme.colors.primaryText)
        },
        buttons = {
            Row(horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
                                   .padding(bottom = 8.dp)
                                   .height(IntrinsicSize.Min)
            ) {
                TextButton(onClick =  { onResult(false) }) {
                    Text(text = stringResource(id = R.string.action_cancel).uppercase(),
                         style = MainTheme.typography.button,
                         color = MainTheme.colors.primaryText)
                }

                VerticalDivider(modifier = Modifier.padding(vertical = 4.dp))

                TextButton(onClick = { onResult(true) }) {
                    Text(text = actionTitle.uppercase(),
                         style = MainTheme.typography.button,
                         color = MainTheme.colors.primaryText)
                }
            }
        }
    )
}

@Composable
@Preview
fun ActionDialogPreview() {
    MainTheme(darkTheme = false) {
        ActionDialog(
            title = "Add",
            text = "Add music?",
            actionTitle = "Add",
            onResult = { }
        )
    }
}