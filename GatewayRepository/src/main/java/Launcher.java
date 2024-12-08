import com.orbitz.consul.Consul;
import com.orbitz.consul.model.catalog.CatalogService;
import io.javalin.Javalin;

import java.util.List;

public class Launcher {
    public static final int PORT = 3000;
    public static final String EBIKE_PORT = "3002";

    public static final String EBIKE_ROOT = "http://EBikeRepository:" + EBIKE_PORT;


    public static void main(String[] args) {
        final RequestManager requestManager = new RequestManager();
        final Javalin app = Javalin.create().start(PORT);
        final Consul consul = Consul.builder().build();

        final List<CatalogService> serviceList = consul.catalogClient().getService("user-service").getResponse();

        final String userUrl = serviceList.stream().findFirst()
                .map(service -> "http://" + service.getAddress() + ":" + service.getServicePort()).orElse("");


        // Proxy per la rotta /users che inoltra le richieste get al servizio sulla porta 3001
        app.get("/users*", ctx -> {
            final String backendUrl = userUrl + ctx.path();
            final String response = requestManager.send(backendUrl);
            ctx.json(response);
        });

        // Proxy per la rotta /users che inoltra le richieste post al servizio sulla porta 3001
        app.post("/users*", ctx -> {
            final String backendUrl = userUrl + ctx.path();
            final String response = requestManager.send(backendUrl, ctx.body());
            ctx.json(response);
        });

        // Proxy per la rotta /ebike che inoltra le richieste get al servizio sulla porta 3002
        app.get("/ebike*", ctx -> {
            final String backendUrl = EBIKE_ROOT + ctx.path();
            final String response = requestManager.send(backendUrl);
            ctx.json(response);
        });

        // Proxy per la rotta /ebike che inoltra le richieste post al servizio sulla porta 3002
        app.post("/ebike*", ctx -> {
            final String backendUrl = EBIKE_ROOT + ctx.path();
            final String response = requestManager.send(backendUrl, ctx.body());
            ctx.json(response);
        });

        // METRICS
        app.get("/metrics", ctx -> {
            final String metrics = requestManager.send(userUrl + "/metrics") +
                    requestManager.send(EBIKE_ROOT + "/metrics");
            ctx.result(metrics);
        });

    }
}
