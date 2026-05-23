package com.mefy.platemate.presentation.common.text

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

sealed interface UiText {
    data class Resource(
        @param:StringRes val id: Int,
        val args: List<Any> = emptyList()
    ) : UiText

    data class Dynamic(val value: String) : UiText
}

fun UiText.resolve(context: Context): String =
    resolveWith { id, args ->
        if (args.isEmpty()) {
            context.getString(id)
        } else {
            context.getString(id, *args)
        }
    }

@Composable
fun UiText.resolve(): String = resolve(LocalContext.current)

internal fun UiText.resolveWith(
    resolver: (id: Int, args: Array<out Any>) -> String
): String = when (this) {
    is UiText.Dynamic -> value
    is UiText.Resource -> {
        val resolvedArgs = args.map { arg ->
            if (arg is UiText) {
                arg.resolveWith(resolver)
            } else {
                arg
            }
        }.toTypedArray()
        resolver(id, resolvedArgs)
    }
}
