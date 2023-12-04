import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ChainedCalls extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

            private static ChainBuilder authenticate =
            exec(http("Authenticate")
                    .post("/authenticate")
                    .body(StringBody("{\n" +
                            "  \"password\": \"admin\",\n" +
                            "  \"username\": \"admin\"\n" +
                            "}"))
                    .check(jmesPath("token").saveAs("jwtToken")));

            private static ChainBuilder createNewGame =
            exec(http("Create new game")
                    .post("/videogame")
                    .header("Authorization", "Bearer #{jwtToken}")
                    .body(StringBody(
                            "{\n" +
                                    "  \"category\": \"Platform\",\n" +
                                    "  \"name\": \"Mario\",\n" +
                                    "  \"rating\": \"Mature\",\n" +
                                    "  \"releaseDate\": \"2012-05-04\",\n" +
                                    "  \"reviewScore\": 85\n" +
                                    "}"
                    )));

                    private ScenarioBuilder scnath = scenario("Video Game Db - Section 5 code")
                    .exec(authenticate)
                    .exec(createNewGame);

    //-----------------------------------------***********************----------------------------------------------------//                

    private static ChainBuilder getAllVideoGames =
            exec(http("Get all video games")
                    .get("/videogame")
                    .check(status().not(404), status().not(500)));

    private static ChainBuilder getSpecificVideoGame =
            exec(http("Get specific video game")
                    .get("/videogame/1")
                    .check(status().is(200)))
                    .pause(1, 10);

    private ScenarioBuilder scn = scenario("Video Game Db - Section 5 code")
            .exec(getAllVideoGames)
            .pause(5)
            .exec(getSpecificVideoGame)
            .pause(5)
            .exec(getAllVideoGames)
            .pause(Duration.ofMillis(4000));

    {
        setUp(
                //scnath.injectOpen(atOnceUsers(3))
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }
}