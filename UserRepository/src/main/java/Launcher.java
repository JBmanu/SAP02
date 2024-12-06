import com.google.gson.Gson;
import concreate.UserRepositoryImpl;
import domain.User;
import domain.UserRepository;
import io.javalin.Javalin;


public final class Launcher {
    public static final int PORT = 3000;

    public static void main(final String[] args) {
        final UserRepository repository = new UserRepositoryImpl();
        final Javalin app = Javalin.create().start(PORT);
        final Gson gson = new Gson();

        repository.add("manuel", "1234");
        repository.add("marco", "1234");
        repository.add("mario", "1234");


        // ottenere tutti gli utenti
        app.get("/users", ctx -> ctx.json(repository.users()));

        // aggiungere un utente
        app.post("/users", ctx -> {
            final User user = gson.fromJson(ctx.body(), User.class);
            if (repository.add(user)) {
                ctx.status(201);
            } else {
                ctx.status(400).result("Username già esistente o dati invalidi.");
            }
        });

//        app.put("/users/:id", ctx -> {
//            final int id = Integer.parseInt(ctx.pathParam("id"));
//            final User updatedUser = gson.fromJson(ctx.body(), User.class);
//            if (updateUser(id, updatedUser)) {
//                ctx.status(204);
//            } else {
//                ctx.status(404).result("Utente non trovato.");
//            }
//        });
//
//        app.delete("/users/:id", ctx -> {
//            final int id = Integer.parseInt(ctx.pathParam("id"));
//            if (deleteUser(id)) {
//                ctx.status(204);
//            } else {
//                ctx.status(404).result("Utente non trovato.");
//            }
//        });
    }
}
