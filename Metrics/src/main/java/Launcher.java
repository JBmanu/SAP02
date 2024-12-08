import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Launcher {

    private static final CollectorRegistry registry = new CollectorRegistry();

    public static void main(String[] args) throws IOException {
        // Start HTTP server to expose metrics on /metrics
        HTTPServer server = new HTTPServer.Builder()
                .withPort(3003)
                .withRegistry(registry)// Porta del microservizio centrale
                .build();

        // Timer per raccogliere periodicamente le metriche dagli altri microservizi
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                collectMetrics();
            }
        }, 0, 10000); // Ogni 30 secondi
    }

    private static void collectMetrics() {
        // URL degli altri microservizi da cui raccogliere le metriche
        String[] serviceUrls = {
                "http://localhost:3001/metrics"
                // Aggiungi altri endpoint se necessario
        };

        for (String url : serviceUrls) {
            try {
                // Effettua una richiesta HTTP agli endpoint /metrics degli altri microservizi
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == 200) {
                    try (Scanner scanner = new Scanner(connection.getInputStream())) {
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            // Qui puoi analizzare o trasformare i dati, oppure lasciarli invariati
                            System.out.println("Raccolta metrica: " + line);
                        }
                    }
                } else {
                    System.err.println("Errore nello scraping: " + url);
                }

            } catch (IOException e) {
                System.err.println("Errore durante la raccolta delle metriche da: " + url);
                e.printStackTrace();
            }
        }
    }
}
