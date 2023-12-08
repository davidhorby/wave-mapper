package com.dhorby.gcloud

import DataStoreClient
import com.dhorby.gcloud.wavemapper.DataStorage
import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse

class ResetFirestoreFunc : HttpFunction {

    private val dataStorage:DataStorage = DataStorage(DataStoreClient(com.dhorby.wavemapper.WaveServiceRoutes.events))

    @Throws(Exception::class)
    override fun service(request: HttpRequest, response: HttpResponse) {
        dataStorage.clear("PieceLocation")
    }

}



