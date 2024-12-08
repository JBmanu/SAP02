package framework.repository.remote;

import application.Message;
import application.RepositoryPort;
import com.google.gson.reflect.TypeToken;
import entity.ebike.EBikeState;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static framework.repository.remote.Key.*;
import static framework.repository.remote.Root.*;

public class RepositoryClientPort implements RepositoryPort {
    private static final Optional<Message> EMPTY_MESSAGE = Optional.empty();
    private final RequestManager requestManager;

    public RepositoryClientPort() {
        this.requestManager = new RequestManager();
    }

    @Override
    public List<String> eBikesIdFree() {
        final Optional<List<String>> eBikesIdFree = this.requestManager.sendGet(EBIKES_ID_FREE, new TypeToken<>() {});
        return eBikesIdFree.orElse(List.of());
    }

    @Override
    public boolean containUser(final String username) {
        final Optional<Boolean> response = this.requestManager.sendPost(CONTAINS_USER_PATH, new TypeToken<>() {},
                Map.of(USERNAME_KEY, username));
        return response.orElse(false);
    }

    @Override
    public boolean containEBike(final String eBikeId) {
        final Optional<Boolean> response = this.requestManager.sendPost(CONTAINS_EBIKE, new TypeToken<>() {},
                Map.of(EBIKE_ID_KEY, eBikeId));
        return response.orElse(false);
    }

    @Override
    public boolean hasEBikes() {
        final Optional<Boolean> response = this.requestManager.sendGet(HAS_EBIKE, new TypeToken<>() {});
        return response.orElse(false);
    }

    @Override
    public Optional<Message> signUp(final String username, final String password) {
        if (username.isBlank() || password.isBlank()) return Optional.of(Message.Error.EMPTY_FIELD);

        final Optional<Boolean> response = this.requestManager.sendPost(SIGN_UP_PATH, new TypeToken<>() {},
                Map.of(USERNAME_KEY, username, PASSWORD_KEY, password));

        return response.map(res -> res ? EMPTY_MESSAGE : Optional.of((Message)Message.Error.SAME_USERNAME))
                .orElse(Optional.of(Message.Error.NOT_CONNECTED));
    }

    @Override
    public Optional<Message> signIn(final String username, final String password) {
        if (username.isBlank() || password.isBlank()) return Optional.of(Message.Error.EMPTY_FIELD);
        if (!this.containUser(username)) return Optional.of(Message.Error.NOT_REGISTERED);

        final Optional<Boolean> response = this.requestManager.sendPost(SIGN_IN_PATH, new TypeToken<>() {},
                Map.of(USERNAME_KEY, username, PASSWORD_KEY, password));

        return response.map(res -> res ? EMPTY_MESSAGE : Optional.of((Message)Message.Error.WRONG_PASSWORD))
                .orElse(Optional.of(Message.Error.NOT_CONNECTED));
    }

    @Override
    public Optional<Message> addCreditsTo(final String username, final float someCredits) {
        if (someCredits < 0) return Optional.of(Message.Error.ADD_NEGATIVE_CREDITS);
        if (someCredits == 0) return Optional.of(Message.Error.ADD_ZERO_CREDITS);

        final Optional<Boolean> response = this.requestManager.sendPost(ADD_CREDITS_PATH, new TypeToken<>() {},
                Map.of(USERNAME_KEY, username, AMOUNT_KEY, someCredits + ""));

        return response.map(res -> res ? EMPTY_MESSAGE : Optional.of((Message)Message.Error.NOT_LOGGED))
                .orElse(Optional.of(Message.Error.NOT_CONNECTED));
    }

    @Override
    public Optional<Float> creditsOf(final String username) {
        return this.requestManager.sendPost(CREDITS_PATH, new TypeToken<>() {}, Map.of(USERNAME_KEY, username));
    }

    @Override
    public Optional<Message> hireEBike(final String username, final String eBikeId, final float WITHOUT_CREDITS) {
        if (!this.hasEBikes()) return Optional.of(Message.Error.NO_EBIKES);
        if (this.isInUseEBike(eBikeId)) return Optional.of(Message.Error.EBIKE_IN_USE);
        if (this.isLowBatteryEBike(eBikeId)) return Optional.of(Message.Error.EBIKE_LOW_BATTERY);
        final boolean canHire = this.withdrawCredits(username, WITHOUT_CREDITS);
        if (!canHire) return Optional.of(Message.Error.ZERO_CREDITS);

        final Optional<Boolean> response = this.requestManager.sendPost(HIRE_EBIKE, new TypeToken<>() {},
                Map.of(EBIKE_ID_KEY, eBikeId));
        return response.map(res -> EMPTY_MESSAGE).orElse(Optional.of(Message.Error.NOT_CONNECTED));
    }

    @Override
    public boolean isFreeEBike(final String eBikeId) {
        final Optional<Boolean> response = this.requestManager.sendPost(IS_FREE_EBIKE, new TypeToken<>() {},
                Map.of(EBIKE_ID_KEY, eBikeId));
        return response.orElse(false);
    }

    @Override
    public boolean isInUseEBike(final String eBikeId) {
        final Optional<Boolean> response = this.requestManager.sendPost(IS_IN_USE_EBIKE, new TypeToken<>() {},
                Map.of(EBIKE_ID_KEY, eBikeId));
        return response.orElse(false);
    }

    @Override
    public boolean isLowBatteryEBike(final String eBikeId) {
        final Optional<Boolean> response = this.requestManager.sendPost(IS_LOW_BATTERY_EBIKE, new TypeToken<>() {},
                Map.of(EBIKE_ID_KEY, eBikeId));
        return response.orElse(false);
    }

    @Override
    public void stopEBike(final String id) {
        this.requestManager.sendPost(STOP_EBIKE, new TypeToken<>() {}, Map.of(EBIKE_ID_KEY, id));
    }

    @Override
    public boolean withdrawCredits(final String username, final float someCredits) {
        final Optional<Boolean> response = this.requestManager.sendPost(WITHDRAW_CREDITS_PATH, new TypeToken<>() {},
                Map.of(USERNAME_KEY, username, AMOUNT_KEY, someCredits + ""));
        return response.orElse(false);
    }

    @Override
    public void consumeBattery(final String id, final int consumeBattery) {
        this.requestManager.sendPost(CONSUME_BATTERY, new TypeToken<>() {},
                Map.of(EBIKE_ID_KEY, id, CONSUME_KEY, consumeBattery + ""));
    }

    @Override
    public Optional<Integer> batteryOf(final String id) {
        return this.requestManager.sendPost(BATTERY_EBIKE, new TypeToken<>() {},
                Map.of(EBIKE_ID_KEY, id));
    }

    @Override
    public Optional<Point2D> positionOf(final String id) {
        return this.requestManager.sendPost(POSITION_EBIKE, new TypeToken<>() {},
                Map.of(EBIKE_ID_KEY, id));
    }

    @Override
    public Optional<EBikeState> stateOf(final String id) {
        return this.requestManager.sendPost(STATE_EBIKE, new TypeToken<>() {},
                Map.of(EBIKE_ID_KEY, id));
    }

}
