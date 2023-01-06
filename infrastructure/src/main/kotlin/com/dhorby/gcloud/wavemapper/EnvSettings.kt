package com.dhorby.gcloud.wavemapper

object EnvSettings {
    val MAPS_API_KEY: String = System.getenv("MAPS_API_KEY")?:""
    val MET_OFFICE_API_KEY: String = System.getenv("MET_OFFICE_API_KEY")?:""
    val RUN_WITH_LOCAL_KEYS:Boolean = System.getenv("RUN_WITH_LOCAL_KEYS").toBoolean()
}
