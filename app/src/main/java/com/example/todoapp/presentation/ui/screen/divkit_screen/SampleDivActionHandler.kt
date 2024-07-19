package com.example.todoapp.presentation.ui.screen.divkit_screen

import android.net.Uri
import com.example.todoapp.presentation.ui.MainActivity
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div.json.expressions.ExpressionResolver
import com.yandex.div2.DivAction

class SampleDivActionHandler(private val activity: MainActivity) : DivActionHandler() {
    override fun handleAction(
        action: DivAction, view: DivViewFacade, resolver: ExpressionResolver
    ): Boolean {
        val url =
            action.url?.evaluate(resolver) ?: return super.handleAction(action, view, resolver)

        return if (url.scheme == SCHEME_SAMPLE && handleSampleAction(url)) {
            true
        } else {
            super.handleAction(action, view, resolver)
        }
    }

    private fun handleSampleAction(action: Uri): Boolean {
        return when (action.host) {
            "go_back" -> {
                activity.onBackButtonClick()
                true
            }

            else -> false
        }
    }

    companion object {
        const val SCHEME_SAMPLE = "sample-action"
    }
}