package com.example.todoapp.presentation.ui.screen.edit_screen.composable_details

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.R
import com.example.todoapp.presentation.ui.theme.AppTheme


@Preview(showBackground = true)
@Composable
fun EditTextField(
    text: String = "",
    onEditText: (text: String) -> Unit = {},
    isError: Boolean = false,
) {
    TextField(

        value = text,
        onValueChange = { onEditText(it) },
        placeholder = {
            Text(
                text = stringResource(R.string.enter_the_task),
                fontSize = 16.sp,
                fontFamily = FontFamily.Default,
                color = AppTheme.colorScheme.labelSecondary
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(shape = RoundedCornerShape(8.dp), elevation = 4.dp),
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = AppTheme.colorScheme.backSecondary,
            unfocusedContainerColor = AppTheme.colorScheme.backSecondary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = AppTheme.colorScheme.labelPrimary,
            unfocusedTextColor = AppTheme.colorScheme.labelPrimary,
            focusedLabelColor = AppTheme.colorScheme.labelSecondary,
            unfocusedLabelColor = AppTheme.colorScheme.labelSecondary,
            cursorColor = AppTheme.colorScheme.labelSecondary,
            errorIndicatorColor = AppTheme.colorScheme.colorBlue,
            errorTextColor = AppTheme.colorScheme.colorRed,
        ),
        textStyle = AppTheme.typographyScheme.body,
        minLines = 3,
        shape = RoundedCornerShape(8.dp),
        isError = text.isBlank() && isError
    )
}