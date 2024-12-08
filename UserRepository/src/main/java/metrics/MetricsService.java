package metrics;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;

public final class MetricsService {
    public static final String USERNAME = "username";
    public static final String STATUS = "status";
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";


    private final Counter userRegistrations;
    private final Counter userLogins;
    private final Counter creditsAdded;
    private final Counter creditsRemoved;
    private final Gauge totalCredits;

    public MetricsService() {
         this.userRegistrations = Counter.build()
                 .name("user_registrations_total")
                 .help("Total number of user registrations.")
                 .labelNames(USERNAME, STATUS)  // Aggiungi etichette per "user_id" e "status"
                 .register();

        this.userLogins = Counter.build()
                .name("user_logins_total")
                .help("Total number of user logins.")
                .labelNames(USERNAME, STATUS)  // Etichette per "user_id" e "status"
                .register();

        this.creditsAdded = Counter.build()
                .name("credits_added_total")
                .help("Total number of credits added by users.")
                .labelNames(USERNAME, "amount", STATUS)  // Etichette per l'utente, importo e stato
                .register();

        this.creditsRemoved = Counter.build()
                .name("credits_removed_total")
                .help("Total number of credits removed by users.")
                .labelNames(USERNAME, "amount", STATUS)  // Etichette per l'utente, importo e stato
                .register();

        this.totalCredits = Gauge.build()
                .name("total_user_credits")
                .help("Total number of credits across all users.")
                .labelNames(USERNAME)  // Etichetta per l'utente
                .register();
    }

    public void registerUser(final String username, final boolean isSuccess) {
        final String status = isSuccess ? SUCCESS : FAILURE;
        this.userRegistrations.labels(username, status).inc();
    }

    public void loginUser(final String username, final boolean isSuccess) {
        final String status = isSuccess ? SUCCESS : FAILURE;
        this.userLogins.labels(username, status).inc();
    }

    public void addCredits(final String username, final float amount, final boolean isSuccess) {
        final String status = isSuccess ? SUCCESS : FAILURE;
        this.creditsAdded.labels(username, Float.toString(amount), status).inc();
        this.totalCredits.labels(username).inc(amount);
    }

    public void removeCredits(final String username, final float amount, final boolean isSuccess) {
        final String status = isSuccess ? SUCCESS : FAILURE;
        this.creditsRemoved.labels(username, Float.toString(amount), status).inc();
        this.totalCredits.labels(username).dec(amount);
    }

}
