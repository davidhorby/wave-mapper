package com.dhorby.gcloud

import DataStoreClient.writeToDatastore
import com.dhorby.gcloud.algorithms.RandomSharkGenerator
import com.dhorby.gcloud.model.com.dhorby.gcloud.model.GeoLocation
import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SharkFrenzyFunc : HttpFunction {

    @Throws(Exception::class)
    override fun service(request: HttpRequest, response: HttpResponse) {

        val LOG: Logger = LoggerFactory.getLogger(SharkFrenzyFunc::class.java)

        val sharkFrenzy =
            RandomSharkGenerator.createFrenzyOfSharks("Sharkey", 4, GeoLocation(4.124932, 64.561496))

        sharkFrenzy.forEach { shark ->
            writeToDatastore(shark.second)
        }
    }



}



