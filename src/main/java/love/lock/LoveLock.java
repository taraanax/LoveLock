package love.lock;

import static spark.Spark.*;
import com.google.gson.Gson;

public class LoveLock {

    // stopnje
    static Stage[] stages = new Stage[]{
        new Stage("21.5.", new String[]{
                "Pomemben datum",
                "Čas mature"
        }),
        new Stage("sushi", new String[]{
                "Go-to hrana",
                "First date food"
        }),
        new Stage("7", new String[]{
                "\\int_{0}^{1}\\left(7 + \ln\\left(\\frac{e}{e}\\right)\\cdot\\frac{\\sin(420x)}{x^2+1}\\right)\\,dx",
                "Koliko mesecev sva že skupaj?"
        })
    };

    //  poskusi
    static int attemptsLeft = 3;
    // stage
    static int currentStage = 0;

    public static void main(String[] args) {

        port(4567);
        Gson gson = new Gson();

        // absolutno zero clue
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization");
        });



        // POST request za preverjanje gesla
        post("/check", (request, response) -> {

            PasswordRequest data = gson.fromJson(request.body(), PasswordRequest.class);
            String enteredPassword = data.password;

            PasswordResponse resp = new PasswordResponse();

            if (currentStage >= stages.length) {
                resp.status = "done";
                resp.attemptsLeft = attemptsLeft;
                resp.stage = currentStage;
                response.type("application/json");
                return gson.toJson(resp);
            }

            Stage stage = stages[currentStage];

            if (enteredPassword.equals(stage.password)) {
                currentStage++;
                attemptsLeft = 3;

                if (currentStage < stages.length) {
                    resp.status = "next";
                    resp.message = "Bravo! Naslednje geslo...";
                } else {
                    resp.status = "correct";
                    resp.message = "Odklenjeno! Srečno Valentinovo :3";
                }

                resp.attemptsLeft = attemptsLeft;
                resp.stage = currentStage;

            } else {
                attemptsLeft--;
                resp.status = "wrong";

                if (attemptsLeft > 0) {
                    int hintIndex = 3 - attemptsLeft - 1;

                    if (hintIndex >= 0 && hintIndex < stage.hints.length) {
                        resp.message = stage.hints[hintIndex];
                    } else {
                        resp.message = "Hmm... poskusi še enkrat";
                    }

                    resp.attemptsLeft = attemptsLeft;
                    resp.stage = currentStage;

                } else {
                    resp.message = "Zaklenjeno, počakaj 5 sekund";
                    resp.attemptsLeft = 0;
                    resp.stage = currentStage;
                    attemptsLeft = 3;
                }
            }

            response.type("application/json");
            return gson.toJson(resp);

        });
    }

    //  JSON body
    static class PasswordRequest {
        String password;
    }

    // JSON response
    static class PasswordResponse {
        int stage;
        String status;
        String message;
        int attemptsLeft;
    }

    // stage gesla
    static class Stage {
        String password;
        String [] hints;

        Stage(String password, String[] hints) {
            this.hints = hints;
            this.password = password;
        }
    }
}
