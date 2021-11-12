import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class FillOrderSimulation extends Simulation {

  val httpProtocol = http
    // Here is the root for all relative URLs
    .baseUrl("http://localhost:9011/api/v1")
    // Here are the common headers
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,/;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  // A scenario is a chain of requests and pauses
  val scn = scenario("Load testing Fill Capture API")
  
    .exec(
      http("Post Fill Order: update To Database")
        .post("/fill/response")
       .body(StringBody("""{
                            "orderId": "1",
                            "exchangeId": "1",
                            "quantity": "100",
                            "stock": "Test Stock",
                            "executedPrice": "500",
                            "executedTime": "2021-01-25T21:34:55"
                            } """))
                      .asJson)
    

  //setUp(scn.inject(atOnceUsers(50)).protocols(httpProtocol))
  
  setUp(
  scn.inject(
    nothingFor(4.seconds), // 1
    atOnceUsers(10), // 2
    rampUsers(10).during(5.seconds), // 3
    constantUsersPerSec(20).during(15.seconds), // 4
    constantUsersPerSec(20).during(15.seconds).randomized, // 5
    rampUsersPerSec(10).to(20).during(10.minutes), // 6
    rampUsersPerSec(10).to(20).during(10.minutes).randomized, // 7
    heavisideUsers(100).during(2.minutes) // 8
  ).protocols(httpProtocol)
)

}