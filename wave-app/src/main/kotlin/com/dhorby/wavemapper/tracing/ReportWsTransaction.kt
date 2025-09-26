package com.dhorby.wavemapper.tracing

import org.http4k.websocket.WsFilter
import java.time.Clock
import java.time.Duration

typealias DbTransactionLabeler = (DbTransaction) -> DbTransaction

object ReportWsTransaction {
    operator fun invoke(
        clock: Clock = Clock.systemUTC(),
        transactionLabeler: WsTransactionLabeler = { it },
        recordFn: (WsTransaction) -> Unit,
    ): WsFilter =
        WsFilter { next ->
            {
                clock.instant().let { start ->
                    next(it).apply {
                        recordFn(
                            transactionLabeler(
                                WsTransaction(
                                    request = it,
                                    response = this,
                                    duration = Duration.between(start, clock.instant()),
                                ),
                            ),
                        )
                    }
                }
            }
        }
}
