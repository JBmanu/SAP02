package framework.repository.remote;

import adapter.EBikeRepository;
import application.Message;
import application.RepositoryPort;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import entity.ebike.EBikeState;
import framework.repository.local.EBikeRepositoryImpl;
import org.springframework.web.client.RestTemplate;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static framework.repository.remote.Key.*;
import static framework.repository.remote.Root.*;

public class RepositoryClientPort implements RepositoryPort {
    private final EBikeRepository ebikeRepository;
    private final RequestManager requestManager;

    public RepositoryClientPort() {
        this(new EBikeRepositoryImpl());
    }

    public RepositoryClientPort(final EBikeRepository eBikeRepository) {
        this.ebikeRepository = eBikeRepository;
        this.requestManager = new RequestManager();
    }

    @Override
    public List<String> eBikesIdFree() {
        final Optional<List<String>> eBikesIdFree = this.requestManager.sendGet(EBIKES_ID_FREE, new TypeToken<>() {});
        return eBikesIdFree.orElse(List.of());
    }

    @Override
    public boolean contain(final String username) {
        return this.requestManager.sendPost(CONTAINS_PATH,
                Boolean.class, Map.of(USERNAME_KEY, username)).orElse(false);
    }

    @Override
    public Optional<Message> signUp(final String username, final String password) {
        if (username.isBlank() || password.isBlank()) return Optional.of(Message.Error.EMPTY_FIELD);

        final Optional<Boolean> response = this.requestManager.sendPost(SIGN_UP_PATH, Boolean.class,
                Map.of(USERNAME_KEY, username, PASSWORD_KEY, password));

        return (Optional<Message>) response.map(res ->
                        res ? Optional.empty() : Optional.of(Message.Error.SAME_USERNAME))
                .orElse(Optional.of(Message.Error.NOT_CONNECTED));
    }

    @Override
    public Optional<Message> signIn(final String username, final String password) {
        if (username.isBlank() || password.isBlank()) return Optional.of(Message.Error.EMPTY_FIELD);
        if (!this.contain(username)) return Optional.of(Message.Error.NOT_REGISTERED);
        final Optional<Boolean> response = this.requestManager.sendPost(SIGN_IN_PATH, Boolean.class,
                Map.of(USERNAME_KEY, username, PASSWORD_KEY, password));

        return (Optional<Message>) response.map(res -> res ? Optional.empty() : Optional.of(Message.Error.SAME_USERNAME))
                .orElse(Optional.of(Message.Error.NOT_CONNECTED));
    }

    @Override
    public Optional<Message> addCreditsTo(final String username, final float someCredits) {
        if (someCredits < 0) return Optional.of(Message.Error.ADD_NEGATIVE_CREDITS);
        if (someCredits == 0) return Optional.of(Message.Error.ADD_ZERO_CREDITS);

        final Optional<Boolean> response = this.requestManager.sendPost(ADD_CREDITS_PATH, Boolean.class,
                Map.of(USERNAME_KEY, username, AMOUNT_KEY, someCredits + ""));

        return (Optional<Message>) response.map(res -> res ? Optional.empty() : Optional.of(Message.Error.NOT_LOGGED))
                .orElse(Optional.of(Message.Error.NOT_CONNECTED));
    }

    @Override
    public Optional<Float> creditsOf(final String username) {
        return this.requestManager.sendPost(CREDITS_PATH, Float.class, Map.of(USERNAME_KEY, username));
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
        return this.requestManager.sendPost(WITHDRAW_CREDITS_PATH, Boolean.class,
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
