package com.dhorby.gcloud

import DataStoreClient
import com.dhorby.gcloud.algorithms.RandomSharkGenerator
import com.dhorby.gcloud.model.GeoLocation
import com.dhorby.gcloud.wavemapper.DataStorage
import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse

class SharkFrenzyFunc : HttpFunction {

    private val dataStorage: DataStorage = DataStorage(DataStoreClient(com.dhorby.wavemapper.WaveServiceRoutes.events))

    @Throws(Exception::class)
    override fun service(request: HttpRequest, response: HttpResponse) {

        val sharkFrenzy =
            RandomSharkGenerator.createFrenzyOfSharks("Sharkey", 4, GeoLocation(4.124932, 64.561496))

        sharkFrenzy.forEach { shark ->
            dataStorage.write(shark.second)
        }
    }



}



