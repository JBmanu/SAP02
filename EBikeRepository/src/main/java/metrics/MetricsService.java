package metrics;

import domain.EBikeState;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;

import java.util.Optional;

public class MetricsService {
    // LABEL NAMES
    public static final String BIKE_ID = "bike_id";
    public static final String STATUS = "status";
    public static final String BATTERY_LEVEL = "level";

    // STATUS VALUES
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    private final Counter bikesRegistered;
    private final Gauge bikeState;
    private final Gauge bikeBatteryLevel;

    public MetricsService() {
        this.bikesRegistered = Counter.build()
                .name("bikes_registered_total")
                .help("Total number of bikes registered.")
                .labelNames(BIKE_ID, STATUS)
                .register();
        this.bikeState = Gauge.build()
                .name("bike_state")
                .help("State of the bike: 0 = free, 1 = in use, 2 = battery low")
                .labelNames(BIKE_ID, "state", STATUS)
                .register();

        this.bikeBatteryLevel = Gauge.build()
                .name("bike_battery_level")
                .help("Battery level of the bike.")
                .labelNames(BIKE_ID, BATTERY_LEVEL, STATUS)
                .register();
    }

    public void registerBike(final String bikeId, final boolean isSuccess) {
        final String status = isSuccess ? SUCCESS : FAILURE;
        this.bikesRegistered.labels(bikeId, status).inc();
    }

    public void updateBikeState(final String bikeId, final Optional<EBikeState> state, final boolean isSuccess) {
        final String status = isSuccess ? SUCCESS : FAILURE;
        final int stateValue = state.map(EBikeState::ordinal).orElse(-1);
        this.bikeState.labels(bikeId, state.toString(), status).set(stateValue);
    }

    public void updateBatteryLevel(final String bikeId, final Optional<Integer> level, final boolean isSuccess) {
        final String status = isSuccess ? SUCCESS : FAILURE;
        final int levelValue = level.orElse(-1);
        this.bikeBatteryLevel.labels(bikeId, String.valueOf(level), status).set(levelValue);
    }

}
