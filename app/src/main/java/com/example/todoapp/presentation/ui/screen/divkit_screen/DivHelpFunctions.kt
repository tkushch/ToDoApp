package com.example.todoapp.presentation.ui.screen.divkit_screen
import android.content.Context
import androidx.core.content.ContextCompat
import com.example.todoapp.R

fun replaceColorsInJson(context: Context, jsonString: String): String {
    val backgroundColor =
        String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(context, R.color.back_primary))
    val textColor =
        String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(context, R.color.label_primary))
    var updatedJsonString = jsonString
    updatedJsonString = updatedJsonString.replace("@color/background_color", backgroundColor)
    updatedJsonString = updatedJsonString.replace("@color/text_color", textColor)
    return updatedJsonString
}