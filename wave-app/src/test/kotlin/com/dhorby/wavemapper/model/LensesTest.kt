package com.dhorby.wavemapper.model


import com.dhorby.wavemapper.addPieceWsMessageLens
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.websocket.WsMessage
import org.junit.jupiter.api.Test

class LensesTest {

    @Test
    fun `should deserialise a post message`() {
        val remoteMessage = """
                {
                  "name": "Sue",
                  "pieceType": "BOAT",
                  "lat": "51.35",
                  "lon": "-71.31"
                }"""
        val expectedMessage = AddPieceWsMessage(
            name = "Sue",
            pieceType = "BOAT",
            lat = 51.35,
            lon = -71.31
        )
        val message: AddPieceWsMessage = addPieceWsMessageLens(WsMessage(remoteMessage))
        assertThat(message, equalTo(expectedMessage))
    }
}