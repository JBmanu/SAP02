import io.prometheus.client.Histogram;
import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;

public class Launcher {

    public static final int PORT = 3003;

    public static void main(final String[] args) {
        // Avvia il server Prometheus
        try {
            HTTPServer server = new HTTPServer(PORT); // Le metriche saranno disponibili su http://localhost:1234/metrics
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Simula una logica per generare metriche
        simulateRequests();
    }

    private static void simulateRequests() {
        for (int i = 0; i < 10; i++) {
            // Avvia un timer per la latenza
            Metrics.requestCounter.inc(); // Incrementa il contatore
            final Histogram.Timer timer = Metrics.requestLatency.startTimer();
            try {
                // Simula una logica (ad esempio una richiesta lenta)
                Thread.sleep((long) (Math.random() * 1000));
            } catch (final InterruptedException e) {
                e.printStackTrace();
            } finally {
                timer.observeDuration(); // Registra la durata della richiesta
            }
        }
    }
}
