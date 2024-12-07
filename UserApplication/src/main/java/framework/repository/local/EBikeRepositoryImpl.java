package framework.repository.local;

import entity.ebike.EBike;
import entity.ebike.EBikeFactory;
import adapter.EBikeRepository;
import entity.ebike.EBikeState;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class EBikeRepositoryImpl implements EBikeRepository {
    private final EBikeFactory ebikeFactory;
    private final HashSet<EBike> ebikes;

    public EBikeRepositoryImpl() {
        this.ebikes = new HashSet<>();
        this.ebikeFactory = new EBikeFactory.SimpleFactory();
    }

    @Override
    public List<String> eBikesIdFree() {
        return this.ebikes.stream().filter(EBike::isFree).map(EBike::id).toList();
    }

    @Override
    public int count() {
        return this.ebikes.size();
    }

    @Override
    public boolean add() {
        return this.ebikes.add(this.ebikeFactory.createDefault());
    }

    @Override
    public boolean remove(final String id) {
        return this.ebikes.removeIf(ebike -> ebike.id().equals(id));
    }

    @Override
    public boolean contains(final String id) {
        return this.ebikes.stream().anyMatch(ebike -> ebike.id().equals(id));
    }

    @Override
    public boolean hasEBike() {
        return !this.ebikes.isEmpty();
    }

    @Override
    public boolean hireEBike(final String id) {
        return this.ebikes.stream()
                .filter(ebike -> ebike.id().equals(id))
                .findFirst()
                .map(EBike::hire)
                .orElse(false);
    }

    @Override
    public boolean updateEBikePosition(final String id, final Point2D position) {
        this.ebikes.stream().filter(ebike -> ebike.id().equals(id))
                .findFirst()
                .ifPresent(ebike -> ebike.updatePosition(position));
        return this.contains(id);
    }

    @Override
    public boolean stopEBike(final String id) {
        this.ebikes.stream().filter(ebike -> ebike.id().equals(id))
                .findFirst()
                .ifPresent(EBike::stopRide);
        return this.contains(id);
    }

    @Override
    public boolean rechargeEBikeBattery(final String id, final float amount) {
        return false;
    }

    @Override
    public boolean isFree(final String eBikeId) {
        return this.ebikes.stream()
                .filter(ebike -> ebike.id().equals(eBikeId))
                .findFirst()
                .map(EBike::isFree)
                .orElse(false);
    }

    @Override
    public boolean isInUse(final String eBikeId) {
        return this.ebikes.stream()
                .filter(ebike -> ebike.id().equals(eBikeId))
                .findFirst()
                .map(EBike::isInUse)
                .orElse(false);
    }

    @Override
    public boolean isLowBattery(final String eBikeId) {
        return this.ebikes.stream()
                .filter(ebike -> ebike.id().equals(eBikeId))
                .findFirst()
                .map(EBike::isLowBattery)
                .orElse(false);
    }

    @Override
    public void setLowBattery(final String eBikeId) {
        this.ebikes.stream()
                .filter(ebike -> ebike.id().equals(eBikeId))
                .forEach(EBike::setLowBattery);
    }

    @Override
    public void consumeBattery(final String id, final int consumeBattery) {
        this.ebikes.stream()
                .filter(ebike -> ebike.id().equals(id))
                .forEach(ebike -> ebike.consumeBattery(consumeBattery));
    }

    @Override
    public Optional<Integer> batteryOf(final String id) {
        return this.ebikes.stream()
                .filter(ebike -> ebike.id().equals(id))
                .findFirst()
                .map(EBike::battery);
    }

    @Override
    public Optional<Point2D> positionOf(final String id) {
        return this.ebikes.stream()
                .filter(ebike -> ebike.id().equals(id))
                .findFirst()
                .map(EBike::position);
    }

    @Override
    public Optional<EBikeState> stateOf(final String id) {
        return this.ebikes.stream()
                .filter(ebike -> ebike.id().equals(id))
                .findFirst()
                .map(EBike::state);
    }
}
