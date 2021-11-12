import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
class SearchOrderSimulation extends Simulation {
  val httpProtocol = http
    // Here is the root for all relative URLs
    .baseUrl("http://localhost:9003/api/v1")
    // Here are the common headers
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,/;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  // A scenario is a chain of requests and pauses
  val scn = scenario("Load testing Search Order API")
    .exec(
      http("Search Order: search data from Database")
        .put("/search-order")
        .header("userId", "2")
        .header("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXh0QGdtYWlsLmNvbSIsInJvbGVzIjoiUk9MRV9BRE1JTiIsImlkIjoxLCJpc0FjdGl2ZSI6dHJ1ZSwiZXhwIjoxNjI4ODQwNjg3LCJpYXQiOjE2Mjg4Mzg4ODd9.3IOLKNBeA4Ze2zPwblm2VHhWTh0cBnvaFOu5fdsNaYk")
        .body(StringBody("""{
  			"clientEmail": null,
  			"clientName": null,
  			"endDate": null,
  			"startDate": null,
  			"stock": null,
  			"type": null
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
      rampUsersPerSec(100).to(200).during(10.minutes), // 6
      rampUsersPerSec(100).to(200).during(10.minutes).randomized, // 7
      heavisideUsers(200).during(2.minutes) // 8
    ).protocols(httpProtocol)
  )
}