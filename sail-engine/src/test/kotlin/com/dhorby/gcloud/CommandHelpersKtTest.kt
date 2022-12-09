package com.dhorby.gcloud

import com.dhorby.gcloud.data.TestData
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class CommandHelpersKtTest {

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

    @Test
    fun extractLocation() {
        val testJson = """
             {
                  "id": "234ea",
                  "name": "Albert",
                  "geoLocation": {
                    "lat": 39.45,
                    "lon": -5.01
                  },
                  "pieceType": "BOAT"
             }
        """.trimIndent()
        val pieceLocation = testJson.extractPieceLocation()
        assertThat(pieceLocation, equalTo(TestData.testBoatLocation))
    }
}
