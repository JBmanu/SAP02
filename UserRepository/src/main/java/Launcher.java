import com.google.gson.Gson;
import concreate.UserRepositoryImpl;
import domain.User;
import domain.UserRepository;
import io.javalin.Javalin;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public final class Launcher {
    public static final int PORT = 3000;
    public static final String USERS_ROOT = "/users";

    public static void main(final String[] args) {
        final UserRepository repository = new UserRepositoryImpl();
        final Javalin app = Javalin.create().start(PORT);
        final Gson gson = new Gson();

        repository.add("manuel", "1234");
        repository.add("marco", "1234");
        repository.add("mario", "1234");


        // GET
        app.get(USERS_ROOT, ctx -> {
            final String json = gson.toJson(repository.users().stream().toList());
            ctx.json(json);
        });

        app.get(USERS_ROOT + "/{username}", ctx -> {
            final String userId = ctx.pathParam("username");
            final Optional<User> user = repository.userOf(userId);
            final String json = user.map(gson::toJson).orElse("");
            ctx.json(json);
        });

        app.get(USERS_ROOT + "/credits/{username}", ctx -> {
            final String userId = ctx.pathParam("username");
            final Optional<Float> credits = repository.creditsOf(userId);
            final String json = credits.map(gson::toJson).orElse("");
            ctx.json(json);
        });

        // POST
        app.post(USERS_ROOT + "/signUp", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String username = bodyJson.get("username");
            final String password = bodyJson.get("password");
            final boolean added = repository.add(username, password);
            ctx.json(added);
        });

        app.post(USERS_ROOT + "/signIn", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String username = bodyJson.get("username");
            final String password = bodyJson.get("password");
            final boolean authenticated = repository.authentication(username, password);
            ctx.json(authenticated);
        });


        // PUT
        app.put(USERS_ROOT + "/addCredits", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String username = bodyJson.get("username");
            final String amountStr = bodyJson.get("amount");
            final float amount = Float.parseFloat(Objects.requireNonNull(amountStr));
            final boolean added = repository.addCreditsTo(username, amount);
            ctx.json(added);
        });

        app.put(USERS_ROOT + "/withdrawCredits", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String username = bodyJson.get("username");
            final String amountStr = bodyJson.get("amount");
            final float amount = Float.parseFloat(Objects.requireNonNull(amountStr));
            final boolean withdrawn = repository.withdrawCredits(username, amount);
            ctx.json(withdrawn);
        });
    }
}
