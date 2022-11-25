package com.dhorby.wavemapper

import com.dhorby.wavemapper.EnvSettings.MAPS_API_KEY
import com.dhorby.wavemapper.EnvSettings.MET_OFFICE_API_KEY
import com.dhorby.wavemapper.EnvSettings.RUN_WITH_LOCAL_KEYS
import com.dhorby.wavemapper.secrets.AccessSecretVersion

object Constants {

    val mapsApiKey: String = if(!EnvSettings.RUN_WITH_LOCAL_KEYS) {
        AccessSecretVersion.accessSecretVersion("mapsApiKey") ?: throw Exception("Invalid Maps API key")
    } else MAPS_API_KEY
    val metOfficeApiKey: String = if(!EnvSettings.RUN_WITH_LOCAL_KEYS) {
        AccessSecretVersion.accessSecretVersion("MetOfficeApiKey") ?: throw Exception("Invalid Met Office API key")
    } else MET_OFFICE_API_KEY

//    {
//        when {
//            RUN_WITH_LOCAL_KEYS -> MAPS_API_KEY
//            else -> AccessSecretVersion.accessSecretVersion("mapsApiKey") ?: ""
//        }
//    }

//    val metOfficeApiKey by lazy {
//        AccessSecretVersion.accessSecretVersion("metOfficeApiKey") ?: ""
//    }
//    fun metOfficeApiKey(): String = when {
//        RUN_WITH_LOCAL_KEYS -> MET_OFFICE_API_KEY
//        else -> AccessSecretVersion.accessSecretVersion("metOfficeApiKey") ?: ""
//    }
    val siteListUrl =
        "http://datapoint.metoffice.gov.uk/public/data/val/wxmarineobs/all/xml/sitelist?res=3hourly&key=${metOfficeApiKey}"
    const val metOfficeUrl = "http://datapoint.metoffice.gov.uk/public/data/val/wxmarineobs/all/xml/"
}
