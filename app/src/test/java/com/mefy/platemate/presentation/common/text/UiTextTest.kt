package com.mefy.platemate.presentation.common.text

import org.junit.Assert.assertEquals
import org.junit.Test

class UiTextTest {

    @Test
    fun resolveWith_returnsDynamicValue() {
        val result = UiText.Dynamic("hello").resolveWith { _, _ ->
            error("Resolver should not be called for dynamic text.")
        }

        assertEquals("hello", result)
    }

    @Test
    fun resolveWith_resolvesNestedUiTextArgsForResource() {
        val text = UiText.Resource(
            id = 101,
            args = listOf(UiText.Dynamic("Fatih"), 5)
        )

        val result = text.resolveWith { id, args ->
            "$id:${args.joinToString(",")}"
        }

        assertEquals("101:Fatih,5", result)
    }

    @Test
    fun resolveWith_handlesResourceWithoutArgs() {
        val text = UiText.Resource(id = 7)

        val result = text.resolveWith { id, args ->
            "$id/${args.size}"
        }

        assertEquals("7/0", result)
    }
}
