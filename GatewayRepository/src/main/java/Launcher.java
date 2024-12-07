import io.javalin.Javalin;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class Launcher {

    public static void main(String[] args) {
        // Crea un'app Javalin
        Javalin app = Javalin.create().start(3000);
        // Configura il routing per l'API Gateway
        // Proxy per la rotta /users/
        // Proxy per la rotta /users che inoltra le richieste al servizio sulla porta 3001
        app.get("/users", ctx -> {
            System.out.println("proxy: " + ctx.req().getRequestURI());
            String backendUrl = "http://localhost:3001/users";
            String response = proxyRequest(backendUrl);
            ctx.result(response);
        });

        // Proxy per la rotta /bike che inoltra le richieste al servizio sulla porta 3002
        app.get("/bike/*", ctx -> {
            String backendUrl = "http://localhost:3002/ebikes" + ctx.req().getRequestURI();
            String response = proxyRequest(backendUrl);
            ctx.result(response);
        });   }

    // Funzione di proxy per inoltrare le richieste
    public static String proxyRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Crea una richiesta HTTP GET
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Esegui la richiesta e ottieni la risposta
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            // Restituisci il corpo della risposta come stringa
            return response.body().string();
        }
    }
}
