package com.dhorby.wavemapper.actions

import com.dhorby.wavemapper.FunctionalTest
import com.dhorby.wavemapper.fake.FakeStorageAdapter
import com.dhorby.wavemapper.port.StoragePort

class RaceActionsFunctionalTests:RaceActionsContract, FunctionalTest {

    override val fakeStorageAdapter: StoragePort= FakeStorageAdapter()
    override val raceActions: RaceActions = RaceActions(fakeStorageAdapter)
}