package com.dhorby.gcloud.model

import com.google.cloud.storage.Storage

interface RemoteStorage {
}

abstract class FakeStorage(): Storage {

}
