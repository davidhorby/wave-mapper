package com.dhorby.wavemapper

import com.dhorby.wavemapper.secrets.AccessSecretVersion

class Constants {

    companion object {
        val mapsApiKey: String =
            AccessSecretVersion.accessSecretVersion("mapsApiKey") ?: throw Exception("Invalid Maps API key")
        val metOfficeApiKey: String =
            AccessSecretVersion.accessSecretVersion("MetOfficeApiKey") ?: throw Exception("Invalid Met Office API key")
        val siteListUrl = "http://datapoint.metoffice.gov.uk/public/data/val/wxmarineobs/all/xml/sitelist?res=3hourly&key=$metOfficeApiKey"
        const val metOfficeUrl = "http://datapoint.metoffice.gov.uk/public/data/val/wxmarineobs/all/xml/"
    }
}
