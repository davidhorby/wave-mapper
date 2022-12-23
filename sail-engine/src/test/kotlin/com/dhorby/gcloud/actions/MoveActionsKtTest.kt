package com.dhorby.gcloud.actions

import com.dhorby.gcloud.data.TestData.testSharkLocation
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

class MoveActionsKtTest {


    @Test
    fun loadPieceLocation() {

        val mapper = jacksonObjectMapper()
        val sharkLocationAsJson = Json.encodeToString(testSharkLocation)
        val pieceLocation = loadPieceLocation(sharkLocationAsJson)
        assertThat(pieceLocation, equalTo(testSharkLocation))

    }
}