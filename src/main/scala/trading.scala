import com.msilb.scalandav20.client.OandaApiClient
import com.msilb.scalandav20.common.Environment.Production
import com.msilb.scalandav20.model.instrument.CandlestickGranularity.{H1, M5}
import com.typesafe.scalalogging.slf4j.LazyLogging

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by Pietro.Speri on 15/08/2017.
  */

object trading extends App with LazyLogging {

  logger.info("[CREATING THE CONNECTION TO THE PRODUCTION ENV]")
  val client = new OandaApiClient(Production, "YOUR_AUTH_BEARER_TOKEN")
  val orderIdFut = for {
    candlesticks <- client.getCandlesticks(
      "EUR_USD",
      granularity = Some(M5),
      count = Some(50),
      includeFirst = Some(false)
    ).collect { case Right(r) => r.candles.filter(_.complete) }
    marketOrder <- client.getTransactionsStream(accountId = "")
  } yield marketOrder

  println("Oanda getOpenTrades: "+ Await.result(orderIdFut, Duration.Inf))
}
