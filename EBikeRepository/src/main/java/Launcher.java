import com.google.gson.Gson;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import concreate.EBikeRepositoryImpl;
import domain.EBikeRepository;
import domain.EBikeState;
import io.javalin.Javalin;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import metrics.MetricsService;

import java.awt.geom.Point2D;
import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Launcher {
    public static final String EBIKE_ROOT = "/ebikes";
    public static final String ID = "id";
    public static final String RECHARGE = "recharge";
    private static final String CONSUME_BATTERY = "consumeBattery";

    public static void main(final String[] args) {
        Config.loadConfig();

        final int port = Config.port();
        final RegisterService registerService = new RegisterService();
        final MetricsService metricsService = new MetricsService();
        final EBikeRepository repository = new EBikeRepositoryImpl();
        final Javalin app = Javalin.create().start(port);
        final Gson gson = new Gson();

        registerService.register(port);

        for (int i = 0; i < Config.loadDefaultEBikes(); i++) {
            final boolean add = repository.add();
            metricsService.registerBike(repository.lastId(), add);
        }

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

        app.get(EBIKE_ROOT + "/hasEBikes", ctx -> {
            ctx.json(repository.hasEBikes());
        });

        // POST
        app.post(EBIKE_ROOT + "/create", ctx -> {
            final boolean add = repository.add();
            metricsService.registerBike(repository.lastId(), add);
            ctx.json(add);
        });

        app.post(EBIKE_ROOT + "/contains", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String id = bodyJson.get(ID);
            ctx.json(repository.contains(id));
        });

        app.post(EBIKE_ROOT + "/hire", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String id = bodyJson.get(ID);
            final boolean hireEBike = repository.hireEBike(id);
            metricsService.updateBikeState(id, repository.stateOf(id), hireEBike);
            ctx.json(hireEBike);
        });

        app.post(EBIKE_ROOT + "/stop", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String id = bodyJson.get(ID);
            final boolean stopEBike = repository.stopEBike(id);
            metricsService.updateBikeState(id, repository.stateOf(id), stopEBike);
            ctx.json(stopEBike);
        });

        app.post(EBIKE_ROOT + "/rechargeBattery", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String id = bodyJson.get(ID);
            final String rechargeStr = bodyJson.get(RECHARGE);
            final int recharge = Integer.parseInt(Objects.requireNonNull(rechargeStr));
            final boolean recharged = repository.rechargeEBikeBattery(id, recharge);
            metricsService.updateBatteryLevel(id, repository.batteryOf(id), recharged);
            ctx.json(recharged);
        });

        app.post(EBIKE_ROOT + "/consumeBattery", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String id = bodyJson.get(ID);
            final String consumeBatteryStr = bodyJson.get(CONSUME_BATTERY);
            final int consumeBattery = Integer.parseInt(Objects.requireNonNull(consumeBatteryStr));
            final boolean consumed = repository.consumeBattery(id, consumeBattery);
            metricsService.updateBatteryLevel(id, repository.batteryOf(id), consumed);
            ctx.json(consumed);
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
            ctx.json(repository.isLowBattery(id));
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

        // METRICS
        app.get("/metrics", ctx -> {
            final StringWriter writer = new StringWriter();
            TextFormat.write004(writer, CollectorRegistry.defaultRegistry.metricFamilySamples());
            ctx.result(writer.toString());
        });

        // Health check
        app.get("/health", ctx -> {
            System.out.println("Health check");
            ctx.result("OK");
        });


    }

}
