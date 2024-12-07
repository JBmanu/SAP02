import com.google.gson.Gson;
import concreate.EBikeRepositoryImpl;
import domain.EBikeRepository;
import domain.EBikeState;
import io.javalin.Javalin;

import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Launcher {
    public static final int PORT = 3002;
    public static final String EBIKE_ROOT = "/ebikes";
    public static final String ID = "id";
    public static final String RECHARGE = "recharge";

    public static void main(final String[] args) {
        final EBikeRepository repository = new EBikeRepositoryImpl();
        final Javalin app = Javalin.create().start(PORT);
        final Gson gson = new Gson();

        for (int i = 0; i < 10; i++) repository.add();

        // GET
        app.get(EBIKE_ROOT, ctx -> {
            final String json = gson.toJson(repository.eBikes().stream().toList());
            ctx.json(json);
        });

        app.get(EBIKE_ROOT + "/eBikesIdFree", ctx -> {
            // controllo
            final String json = gson.toJson(repository.eBikesIdFree().stream().toList());
            ctx.json(json);
        });

        app.get(EBIKE_ROOT + "/hasEBike", ctx -> {
            ctx.json(repository.hasEBike());
        });

        // POST
        app.post(EBIKE_ROOT + "/create", ctx -> {
            ctx.json(repository.add());
        });

        app.post(EBIKE_ROOT + "/contains", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String id = bodyJson.get(ID);
            ctx.json(repository.contains(id));
        });

        app.post(EBIKE_ROOT + "/hire", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String id = bodyJson.get(ID);
            ctx.json(repository.hireEBike(id));
        });

        app.post(EBIKE_ROOT + "/stop", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String id = bodyJson.get(ID);
            ctx.json(repository.stopEBike(id));
        });

        app.post(EBIKE_ROOT + "/rechargeBattery", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String id = bodyJson.get(ID);
            final String rechargeStr = bodyJson.get(RECHARGE);
            final int recharge = Integer.parseInt(Objects.requireNonNull(rechargeStr));
            ctx.json(repository.rechargeEBikeBattery(id, recharge));
        });

        app.post(EBIKE_ROOT + "/isFree", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String id = bodyJson.get(ID);
            ctx.json(repository.isFree(id));
        });

        app.post(EBIKE_ROOT + "/isInUse", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String id = bodyJson.get(ID);
            ctx.json(repository.isInUse(id));
        });

        app.post(EBIKE_ROOT + "/isLowBattery", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String id = bodyJson.get(ID);
            ctx.json(repository.isInUse(id));
        });

        app.post(EBIKE_ROOT + "/battery", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String id = bodyJson.get(ID);
            final Optional<Integer> battery = repository.batteryOf(id);
            final String json = battery.map(gson::toJson).orElse("");
            ctx.json(json);
        });

        app.post(EBIKE_ROOT + "/position", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String id = bodyJson.get(ID);
            final Optional<Point2D> position = repository.positionOf(id);
            final String json = position.map(gson::toJson).orElse("");
            ctx.json(json);
        });

        app.post(EBIKE_ROOT + "/state", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String id = bodyJson.get(ID);
            final Optional<EBikeState> state = repository.stateOf(id);
            final String json = state.map(gson::toJson).orElse("");
            ctx.json(json);
        });


    }

}
