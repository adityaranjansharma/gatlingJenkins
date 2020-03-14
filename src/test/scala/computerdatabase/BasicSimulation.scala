package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  private def getProperty(propertyName: String, defaultValue: String): String = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }

  def buildNumber: String = getProperty("BUILDNUM","${BUILD_NUMBER}").toString

before{
println(s"Running Test With ${buildNumber} build number")
println(s"Running Test With ${BUILD_NUMBER} build number")

}



  val httpProtocol = http
    .baseUrl("http://computer-database.gatling.io") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
    .forever(){
      exec(http(buildNumber + "_request_1")
        .get("/"))
        .pause(7) // Note that Gatling has recorder real time pauses
    }


  setUp(scn.inject(
    nothingFor(5 seconds),
    atOnceUsers(1),
    rampUsers(4) during(30 second)
  ).protocols(httpProtocol)

  ).maxDuration(5 minute)
}
