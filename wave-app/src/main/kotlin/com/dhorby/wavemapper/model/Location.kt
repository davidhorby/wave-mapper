package com.dhorby.wavemapper.model

import com.dhorby.gcloud.model.com.dhorby.gcloud.model.GeoLocation

sealed interface Location {
    val geoLocation: GeoLocation
}
