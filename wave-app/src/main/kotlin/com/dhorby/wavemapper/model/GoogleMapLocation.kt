package com.dhorby.wavemapper.model

import org.http4k.core.Body
import org.http4k.format.Jackson.auto

data class GoogleMapLocation(val plus_code:GlobalCode)
data class GlobalCode(val global_code:String)


val googleMapLocationLens = Body.auto<GoogleMapLocation>().toLens()



