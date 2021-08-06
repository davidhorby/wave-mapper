package com.dhorby.wavemapper

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class WavePageTest {

    @Test
    fun toGoogleMapFormat() {
        val expectedResult = """
             ['Lat', 'Long', 'Name', 'Marker'],
             [51.41, 1.78, 'Sandettie 0.3m 23km', 'verysmall'],
             [50.04, -6.04, 'Seven Stones 0.5m 76km', 'small'],
             [51.41, -6.42, 'M5 2.3m 1km', 'big']
        """.trimIndent().filter { !it.isWhitespace() }

        val testData = listOf(
            Location(
                id = "162304",
                name = "Sandettie",
                lat = 51.41F,
                lon = 1.78F,
                listOf(DatePeriod(date = LocalDate.now(), waveHeight = 0.3F, windSpeed = 23))
            ),
            Location(
                id = "162305",
                name = "Seven Stones",
                lat = 50.04F,
                lon = -6.04F,
                listOf(DatePeriod(date = LocalDate.now(), waveHeight = 0.5F, windSpeed = 76))
            ),
            Location(
                id = "162306",
                name = "M5",
                lat = 51.41F,
                lon = -6.42F,
                listOf(DatePeriod(date = LocalDate.now(), waveHeight = 2.3F, windSpeed = 1))
            )
        )

        val result = XMLWaveParser.toGoogleMapFormat(testData).filter { !it.isWhitespace() }
        assertThat(result, equalTo(expectedResult))

    }
}
