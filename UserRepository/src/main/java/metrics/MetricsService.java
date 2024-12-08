package metrics;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;

public final class MetricsService {
    // LABEL NAMES
    public static final String USERNAME = "username";
    public static final String AMOUNT = "amount";
    public static final String STATUS = "status";

    // STATUS VALUES
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    // COUNTER NAMES
    public static final String USER_REGISTRATIONS_TOTAL = "user_registrations_total";
    public static final String USER_LOGINS_TOTAL = "user_logins_total";
    public static final String CREDITS_ADDED_TOTAL = "credits_added_total";
    public static final String CREDITS_REMOVED_TOTAL = "credits_removed_total";
    public static final String TOTAL_USER_CREDITS = "total_user_credits";

    private final Counter userRegistrations;
    private final Counter userLogins;
    private final Counter creditsAdded;
    private final Counter creditsRemoved;
    private final Gauge totalCredits;

    public MetricsService() {
         this.userRegistrations = Counter.build()
                 .name(USER_REGISTRATIONS_TOTAL)
                 .help("Total number of user registrations.")
                 .labelNames(USERNAME, STATUS)
                 .register();

        this.userLogins = Counter.build()
                .name(USER_LOGINS_TOTAL)
                .help("Total number of user logins.")
                .labelNames(USERNAME, STATUS)
                .register();

        this.creditsAdded = Counter.build()
                .name(CREDITS_ADDED_TOTAL)
                .help("Total number of credits added by users.")
                .labelNames(USERNAME, AMOUNT, STATUS)
                .register();

        this.creditsRemoved = Counter.build()
                .name(CREDITS_REMOVED_TOTAL)
                .help("Total number of credits removed by users.")
                .labelNames(USERNAME, AMOUNT, STATUS)
                .register();

        this.totalCredits = Gauge.build()
                .name(TOTAL_USER_CREDITS)
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
