import com.orbitz.consul.Consul;
import io.javalin.Javalin;

public class Launcher {
    public static final int PORT = 3000;
    public static final String USER_PORT = "3001";
    public static final String EBIKE_PORT = "3002";

    public static final String USERS_ROOT = "http://UserRepository:" + USER_PORT;
    public static final String EBIKE_ROOT = "http://EBikeRepository:" + EBIKE_PORT;


    public static void main(String[] args) {
        final RequestManager requestManager = new RequestManager();
        final Javalin app = Javalin.create().start(PORT);


        Consul consul = Consul.builder().build();

        // Funzione per ottenere l'URL del servizio degli utenti da Consul
//        Handler getUserServiceUrl = ctx -> {
//            // Cerca il servizio "user-service" in Consul
//            Optional<List<ServiceEntry>> services = consul.catalogClient().getService("user-service").getResponse();
//            if (services.isEmpty()) {
//                ctx.status(500).result("User service not found");
//                return;
//            }
//
//            // Usa il primo servizio trovato
//            ServiceEntry service = services.get(0);
//            String userServiceUrl = "http://" + service.getService().getAddress() + ":" + service.getService().getPort();
//
//            // Proxy la richiesta al microservizio degli utenti
//            String result = HttpUtils.get(userServiceUrl + "/users");
//            ctx.result(result);
//        };

//        Handler getUserServiceUrl = ctx -> {
//            // Cerca il servizio "user-service" in Consul
//            final var services = consul.catalogClient().getServices();
//            boolean serviceFound = false;
//            String userServiceUrl = "";
//
//            // Cerca tra i servizi disponibili per trovare quello giusto
//            for (Service service : services.values()) {
//                if ("user-service".equals(service.getService())) {
//                    userServiceUrl = "http://" + service.getAddress() + ":" + service.getPort();
//                    serviceFound = true;
//                    break;
//                }
//            }
//
//            if (!serviceFound) {
//                ctx.status(500).result("User service not found");
//                return;
//            }
//
//            // Proxy la richiesta al microservizio degli utenti
//            String result = HttpUtils.get(userServiceUrl + "/users");
//            ctx.result(result);
//        };


        // Proxy per la rotta /users che inoltra le richieste get al servizio sulla porta 3001
        app.get("/users*", ctx -> {
            final String backendUrl = USERS_ROOT + ctx.path();
            final String response = requestManager.send(backendUrl);
            ctx.json(response);
        });

        // Proxy per la rotta /users che inoltra le richieste post al servizio sulla porta 3001
        app.post("/users*", ctx -> {
            final String backendUrl = USERS_ROOT + ctx.path();
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
            final String metrics = requestManager.send(USERS_ROOT + "/metrics") +
                    requestManager.send(EBIKE_ROOT + "/metrics");
            ctx.result(metrics);
        });

    }
}
