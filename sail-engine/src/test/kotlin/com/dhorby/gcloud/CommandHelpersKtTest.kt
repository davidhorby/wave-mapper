package com.dhorby.gcloud

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CommandHelpersKtTest {

    @Test
    fun extractCommand() {
        val testJson = """
            {
              "name": "dave",
              "command": "C w h"
            }
        """.trimIndent()
        val command = testJson.extractCommand()
        assertThat(command, equalTo(Command("dave", "C w h")))
    }
}
