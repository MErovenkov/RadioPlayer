package com.example.radioplayer.ui.screen.view

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.radioplayer.R
import com.example.radioplayer.ui.theme.MainTheme

@Composable
fun ErrorMessage(@StringRes message: Int) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
           verticalArrangement = Arrangement.Center,
           horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(modifier = Modifier.size(96.dp),
             imageVector = Icons.Filled.Warning,
             tint = MainTheme.colors.error,
             contentDescription = stringResource(id = R.string.error_message)
        )

        Text(modifier = Modifier.padding(top = 16.dp, bottom = 24.dp),
             text = stringResource(id = message),
             style = MainTheme.typography.basic,
             color = MainTheme.colors.primaryText,
             textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview
fun RadioErrorPreview() {
    MainTheme(darkTheme = false) {
        ErrorMessage(R.string.error_radio_json_not_found)
    }
}