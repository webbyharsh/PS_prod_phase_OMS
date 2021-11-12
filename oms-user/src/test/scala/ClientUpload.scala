import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random

class ClientUploadSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://44.196.252.191:9002/api/v1")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val csvFeeder = csv(fileName = "./src/test/resources/csvFeeder.csv").circular
  
  def getCSV() = {
  	repeat(times=1){
  	  feed (csvFeeder)
  		.exec(http("Post Order: Save To Database")
  		.post("/client/upload-csv")
  		.header("userId", "${userId}")
        .bodyPart(RawFileBodyPart("file", "./src/test/resources/csvFiles/${csvName}")).asMultipartForm
		.check(status.in(201, 422)))
  	}
  }
  
  val scn = scenario("Upload CSV file")
    .exec(getCSV())
    
	setUp(
	  scn.inject(
		nothingFor(4.seconds), // 1
		atOnceUsers(1), // 2
		constantUsersPerSec(1).during(15.minutes).randomized, // 3
		rampUsers(2).during(10.seconds), //4
		constantUsersPerSec(35).during(10.seconds)
	  ).protocols(httpProtocol)
	)
	
  
}