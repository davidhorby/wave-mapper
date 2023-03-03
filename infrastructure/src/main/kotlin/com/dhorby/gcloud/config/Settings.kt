package com.dhorby.gcloud.config

object Settings {
    val ENV = System.getenv("ENV")?:"local"
    val HOST = System.getenv("DATASTORE_HOST")?:"http://localhost:8081"
    val PROJECT_ID = System.getenv("DATASTORE_PROJECT_ID")?:"analytics-springernature"
}
