package com.dhorby.wavemapper.data

import com.dhorby.wavemapper.XMLWaveParser
import com.dhorby.wavemapper.secrets.AccessSecretVersion
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled

internal class XMLWaveParserTest {

    @Test
    @Disabled
    fun getWaveDataAsJson() {
        val waveDataAsJson = AccessSecretVersion.accessSecretVersion("MetOfficeApiKey")?.let { metOfficeApiKey ->
            XMLWaveParser.getWaveDataAsJson(metOfficeApiKey)
        }
        assertThat(waveDataAsJson, equalTo("adfsasd"))

    }

    @Test
    fun getWaveDataAsGoogleMapFormat() {
    }
}
