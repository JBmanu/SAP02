package application.concreate;

import adapter.EBikeRepository;
import application.Message;
import application.RepositoryPort;
import com.google.gson.Gson;
import entity.ebike.EBikeState;
import framework.repository.EBikeRepositoryImpl;
import org.springframework.web.client.RestTemplate;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepositoryClientPort implements RepositoryPort {
    private static final String PORT = "3000";
    private static final String URL_ROOT = "http://localhost:" + PORT;
    public static final String USERS_PATH = "/users";
    public static final String SIGN_UP_PATH = USERS_PATH + "/signUp";
    public static final String SIGN_IN_PATH = USERS_PATH + "/signIn";
    public static final String ADD_CREDITS_PATH = USERS_PATH + "/addCredits";
    public static final String CREDITS_PATH = USERS_PATH + "/credits";
    public static final String WITHDRAW_CREDITS_PATH = USERS_PATH + "/withdrawCredits";
    public static final String CONTAINS_PATH = USERS_PATH + "/contains";
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String AMOUNT_KEY = "amount";

    private final EBikeRepository ebikeRepository;
    private final Gson gson;

    public RepositoryClientPort() {
        this(new EBikeRepositoryImpl());
    }

    public RepositoryClientPort(final EBikeRepository eBikeRepository) {
        this.ebikeRepository = eBikeRepository;
        this.gson = new Gson();
    }

    private <T> Optional<T> postRequest(final String urlPath, final Class<T> responseType, final Map<String, String> request) {
        final RestTemplate restTemplate = new RestTemplate();
        Optional<T> response = Optional.empty();
        final String message = this.gson.toJson(request);
        try {
            response = Optional.ofNullable(restTemplate.postForObject(URL_ROOT + urlPath, message, responseType));
        } catch (final Exception ignored) {
        }
        return response;
    }

    @Override
    public List<String> eBikesIdFree() {
        return this.ebikeRepository.eBikesIdFree();
    }

    @Override
    public boolean contain(final String username) {
        return this.postRequest(CONTAINS_PATH,
                Boolean.class, Map.of(USERNAME_KEY, username)).orElse(false);
    }

    @Override
    public Optional<Message> signUp(final String username, final String password) {
        if (username.isBlank() || password.isBlank()) return Optional.of(Message.Error.EMPTY_FIELD);

        final Optional<Boolean> response = this.postRequest(SIGN_UP_PATH, Boolean.class,
                Map.of(USERNAME_KEY, username, PASSWORD_KEY, password));

        return (Optional<Message>) response.map(res ->
                        res ? Optional.empty() : Optional.of(Message.Error.SAME_USERNAME))
                .orElse(Optional.of(Message.Error.NOT_CONNECTED));
    }

    @Override
    public Optional<Message> signIn(final String username, final String password) {
        if (username.isBlank() || password.isBlank()) return Optional.of(Message.Error.EMPTY_FIELD);
        if (!this.contain(username)) return Optional.of(Message.Error.NOT_REGISTERED);
        final Optional<Boolean> response = this.postRequest(SIGN_IN_PATH, Boolean.class,
                Map.of(USERNAME_KEY, username, PASSWORD_KEY, password));

        return (Optional<Message>) response.map(res -> res ? Optional.empty() : Optional.of(Message.Error.SAME_USERNAME))
                .orElse(Optional.of(Message.Error.NOT_CONNECTED));
    }

    @Override
    public Optional<Message> addCreditsTo(final String username, final float someCredits) {
        if (someCredits < 0) return Optional.of(Message.Error.ADD_NEGATIVE_CREDITS);
        if (someCredits == 0) return Optional.of(Message.Error.ADD_ZERO_CREDITS);

        final Optional<Boolean> response = this.postRequest(ADD_CREDITS_PATH, Boolean.class,
                Map.of(USERNAME_KEY, username, AMOUNT_KEY, someCredits + ""));

        return (Optional<Message>) response.map(res -> res ? Optional.empty() : Optional.of(Message.Error.NOT_LOGGED))
                .orElse(Optional.of(Message.Error.NOT_CONNECTED));
    }

    @Override
    public Optional<Float> creditsOf(final String username) {
        return this.postRequest(CREDITS_PATH, Float.class, Map.of(USERNAME_KEY, username));
    }

    @Override
    public Optional<Message> hireEBike(final String username, final String eBikeId, final float WITHOUT_CREDITS) {
        if (!this.ebikeRepository.hasEBike()) return Optional.of(Message.Error.NO_EBIKES);
        if (this.isInUseEBike(eBikeId)) return Optional.of(Message.Error.EBIKE_IN_USE);
        if (this.isLowBatteryEBike(eBikeId)) return Optional.of(Message.Error.EBIKE_LOW_BATTERY);
        final boolean canHire = this.withdrawCredits(username, WITHOUT_CREDITS);
        if (!canHire) return Optional.of(Message.Error.ZERO_CREDITS);
        this.ebikeRepository.hireEBike(eBikeId);
        return Optional.empty();
    }

    @Override
    public boolean isFreeEBike(final String eBikeId) {
        return this.ebikeRepository.isFree(eBikeId);
    }

    @Override
    public boolean isInUseEBike(final String eBikeId) {
        return this.ebikeRepository.isInUse(eBikeId);
    }

    @Override
    public boolean isLowBatteryEBike(final String eBikeId) {
        return this.ebikeRepository.isLowBattery(eBikeId);
    }

    @Override
    public void stopEBike(final String id) {
        this.ebikeRepository.stopEBike(id);
    }

    @Override
    public boolean withdrawCredits(final String username, final float someCredits) {
        return this.postRequest(WITHDRAW_CREDITS_PATH, Boolean.class,
                        Map.of(USERNAME_KEY, username, AMOUNT_KEY, someCredits + ""))
                .orElse(false);
    }

    @Override
    public void consumeBattery(final String id, final int consumeBattery) {
        this.ebikeRepository.consumeBattery(id, consumeBattery);
    }

    @Override
    public Optional<Integer> batteryOf(final String id) {
        return this.ebikeRepository.batteryOf(id);
    }

    @Override
    public Optional<Point2D> positionOf(final String id) {
        return this.ebikeRepository.positionOf(id);
    }

    @Override
    public Optional<EBikeState> stateOf(final String id) {
        return this.ebikeRepository.stateOf(id);
    }

}
