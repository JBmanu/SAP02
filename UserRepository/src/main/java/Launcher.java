import com.google.gson.Gson;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import concreate.UserRepositoryImpl;
import domain.User;
import domain.UserRepository;
import io.javalin.Javalin;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import metrics.MetricsService;
import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public final class Launcher {
    public static final String USERS_ROOT = "/users";

    // KEY NAMES
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String AMOUNT = "amount";

    public static void main(final String[] args) {
        Config.loadConfig();
        final int port = Config.port();
        final MetricsService metricsService = new MetricsService();
        final UserRepository repository = new UserRepositoryImpl();
        final Javalin app = Javalin.create().start(port);
        final Gson gson = new Gson();

        final Consul consul = Consul.builder()
                .withUrl("http://Consul:8500")
                .build();
        final String serviceId = "user-service";
        consul.agentClient().register(ImmutableRegistration.builder()
                .id(serviceId)
                .name(serviceId)
                .address("UserRepository")
                .port(port)
                .build());

        // Health check
        app.get("/health", ctx -> {
            System.out.println("Health check");
            ctx.result("OK");
        });

        // Load default users
        Config.loadDefaultUsers().forEach(user -> {
            final boolean added = repository.add(user);
            metricsService.registerUser(user.username(), added);
        });

        // GET
        app.get(USERS_ROOT, ctx -> {
            final String json = gson.toJson(repository.users().stream().toList());
            ctx.json(json);
        });

        // Only test
        app.get(USERS_ROOT + "/{username}", ctx -> {
            final String userId = ctx.pathParam(USERNAME);
            final Optional<User> user = repository.userOf(userId);
            final String json = user.map(gson::toJson).orElse("");
            ctx.json(json);
        });

        // POST
        app.post(USERS_ROOT + "/signUp", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String username = bodyJson.get(USERNAME);
            final String password = bodyJson.get(PASSWORD);
            final boolean added = repository.add(username, password);
            metricsService.registerUser(username, added);
            ctx.json(added);
        });

        app.post(USERS_ROOT + "/signIn", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String username = bodyJson.get(USERNAME);
            final String password = bodyJson.get(PASSWORD);
            final boolean authenticated = repository.authentication(username, password);
            metricsService.loginUser(username, authenticated);
            ctx.json(authenticated);
        });

        app.post(USERS_ROOT + "/contains", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String username = bodyJson.get(USERNAME);
            final boolean contains = repository.contains(username);
            ctx.json(contains);
        });

        app.post(USERS_ROOT + "/credits", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String userId = bodyJson.get(USERNAME);
            final Optional<Float> credits = repository.creditsOf(userId);
            final String json = credits.map(gson::toJson).orElse("");
            ctx.json(json);
        });

        app.post(USERS_ROOT + "/addCredits", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String username = bodyJson.get(USERNAME);
            final String amountStr = bodyJson.get(AMOUNT);
            final float amount = Float.parseFloat(Objects.requireNonNull(amountStr));
            final boolean added = repository.addCreditsTo(username, amount);
            metricsService.addCredits(username, amount, added);
            ctx.json(added);
        });

        app.post(USERS_ROOT + "/withdrawCredits", ctx -> {
            final Map<String, String> bodyJson = ctx.bodyAsClass(Map.class);
            final String username = bodyJson.get(USERNAME);
            final String amountStr = bodyJson.get(AMOUNT);
            final float amount = Float.parseFloat(Objects.requireNonNull(amountStr));
            final boolean withdrawn = repository.withdrawCredits(username, amount);
            metricsService.removeCredits(username, amount, withdrawn);
            ctx.json(withdrawn);
        });

        // METRICS
        app.get("/metrics", ctx -> {
            final StringWriter writer = new StringWriter();
            TextFormat.write004(writer, CollectorRegistry.defaultRegistry.metricFamilySamples());
            ctx.result(writer.toString());
        });

    }
}
