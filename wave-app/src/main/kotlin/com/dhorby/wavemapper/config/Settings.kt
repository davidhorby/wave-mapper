package com.dhorby.wavemapper.config

object Settings {
    val ENV = System.getenv("ENV") ?: "local"
    val HOST = System.getenv("HOST") ?: "localhost"
    val PORT: Int = (System.getenv("PORT") ?: "8080").toInt()
    val PROJECT_ID = System.getenv("DATASTORE_PROJECT_ID") ?: "analytics-springernature"
}
