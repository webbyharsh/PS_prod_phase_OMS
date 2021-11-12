import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class GenerateTokenPerformanceTest extends Simulation {

  val httpProtocol = http
    // Here is the root for all relative URLs
    .baseUrl("http://localhost:9005/api/v1/user")
    // Here are the common headers
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,/;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  // A scenario is a chain of requests and pauses
  val scn = scenario("Load testing for generate-token api")

    .exec(
      http("Logging in the demo user")
        .post("/generate-token")
        .body(StringBody("""{
                            "username":"gatling@test.com",
                            "password":"test"
                            } """))
        .asJson)


  //setUp(scn.inject(atOnceUsers(50)).protocols(httpProtocol))

  setUp(
    scn.inject(
      nothingFor(4.seconds), // 1
      atOnceUsers(10), // 2
      rampUsers(100).during(5.seconds), // 3
      constantUsersPerSec(100).during(15.seconds), // 4
      constantUsersPerSec(150).during(15.seconds).randomized, // 5
      rampUsersPerSec(500).to(1000).during(10.minutes), // 6
      rampUsersPerSec(500).to(1000).during(10.minutes).randomized, // 7
      heavisideUsers(1000).during(2.minutes) // 8
    ).protocols(httpProtocol)
  )

}