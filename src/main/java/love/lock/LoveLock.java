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
                "xx",
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

            if (enteredPassword.equals(CORRECT_PASSWORD)) {
                resp.status = "correct";
                resp.message = "Srečno Valentinovo :3";
                resp.attemptsLeft = attemptsLeft;
            } else {
                attemptsLeft--;
                resp.status = "wrong";

                if (attemptsLeft > 0) {
                    resp.message = getHint(attemptsLeft);
                    resp.attemptsLeft = attemptsLeft;
                } else {
                    resp.message = "Zaklenjeno, počakaj 5 sekund";
                    resp.attemptsLeft = 0;
                    // fejk timer reset
                    attemptsLeft = 3;
                }
            }

            response.type("application/json");
            return gson.toJson(resp);

        });
    }

    // namigi
    private static String getHint(int attemptsLeft) {
        if (attemptsLeft == 2) return "Pomemben datum";
        if (attemptsLeft == 1) return "Čas mature";
        return "";
    }

    //  JSON body
    static class PasswordRequest {
        String password;
    }

    // JSON response
    static class PasswordResponse {
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
