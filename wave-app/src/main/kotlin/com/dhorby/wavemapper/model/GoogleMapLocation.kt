package com.dhorby.wavemapper.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.http4k.core.Body
import org.http4k.format.Jackson.auto

data class GoogleMapLocation(
    @JsonProperty("plus_code") val plusCode: PlusCode,
    @JsonProperty("results") val results: List<AddressComponents>,
)

data class PlusCode(
    @JsonProperty("global_code") val globalCode: String,
)

data class GoogleMapLocationResults(
    val results: List<GoogleMapLocation>,
)

data class AddressComponents(
    @JsonProperty("address_components") val addressComponents: List<AddressComponent>,
    @JsonProperty("formatted_address") val formattedAddress: String,
)

data class AddressComponent(
    @JsonProperty("long_name") val longName: String,
    @JsonProperty("short_name") val shortName: String,
)

val googleMapLocationLens = Body.auto<GoogleMapLocation>().toLens()
