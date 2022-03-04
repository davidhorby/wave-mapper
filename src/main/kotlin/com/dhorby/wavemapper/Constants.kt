package com.dhorby.wavemapper

import com.dhorby.wavemapper.secrets.AccessSecretVersion

class Constants {

    companion object {
        const val metOfficeUrl = "http://datapoint.metoffice.gov.uk/public/data/val/wxmarineobs/all/xml/"
        val mapsApiKey: String =
            AccessSecretVersion.accessSecretVersion("mapsApiKey") ?: throw Exception("Invalid Maps API key")
        val metOfficeApiKey: String =
            AccessSecretVersion.accessSecretVersion("MetOfficeApiKey") ?: throw Exception("Invalid Met Office API key")
    }
}
