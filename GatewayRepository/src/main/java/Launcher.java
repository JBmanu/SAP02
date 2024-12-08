import io.javalin.Javalin;

import java.util.concurrent.atomic.AtomicReference;

public class Launcher {
    public static final int PORT = 3000;


    public static void main(String[] args) {
        final RequestManager requestManager = new RequestManager();
        final ServiceDiscovery serviceDiscovery = new ServiceDiscovery();
        final Javalin app = Javalin.create().start(PORT);
        final AtomicReference<String> userUrl = new AtomicReference<>(serviceDiscovery.userUrl());
        final AtomicReference<String> eBikeUrl = new AtomicReference<>(serviceDiscovery.eBikeUrl());

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
            final String backendUrl = eBikeUrl + ctx.path();
            final String response = requestManager.send(backendUrl);
            ctx.json(response);
        });

        // Proxy per la rotta /ebike che inoltra le richieste post al servizio sulla porta 3002
        app.post("/ebike*", ctx -> {
            final String backendUrl = eBikeUrl + ctx.path();
            final String response = requestManager.send(backendUrl, ctx.body());
            ctx.json(response);
        });

        // METRICS
        app.get("/metrics", ctx -> {
            final String metrics = requestManager.send(userUrl + "/metrics") +
                    requestManager.send(eBikeUrl + "/metrics");
            ctx.result(metrics);
        });


        // Health check
        app.get("/health", ctx -> {
            System.out.println("Health check");
            userUrl.set(serviceDiscovery.userUrl());
            eBikeUrl.set(serviceDiscovery.eBikeUrl());
            ctx.result("OK");
        });

    }
}
