import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;

public final class Metrics {
    // Contatore per il numero di richieste
    public static final Counter requestCounter = Counter.build()
            .name("requests_total")
            .help("Total number of requests")
            .register();

    // Istogramma per misurare la latenza
    public static final Histogram requestLatency = Histogram.build()
            .name("request_latency_seconds")
            .help("Request latency in seconds")
            .register();
}