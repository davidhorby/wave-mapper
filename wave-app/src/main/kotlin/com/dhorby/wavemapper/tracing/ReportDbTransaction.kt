package com.dhorby.wavemapper.tracing

import org.http4k.websocket.WsFilter
import java.time.Clock
import java.time.Duration

object ReportDbTransaction {
    operator fun invoke(
        clock: Clock = Clock.systemUTC(),
        transactionLabeler: DbTransactionLabeler = { it },
        recordFn: (DbTransaction) -> Unit
    ): WsFilter = WsFilter { next ->
        {
            clock.instant().let { start ->
                next(it).apply {
                    recordFn(transactionLabeler(
                        DbTransaction(
                        request = it,
                        response = this,
                        duration = Duration.between(start, clock.instant())
                    )
                    ))
                }
            }
        }
    }
}