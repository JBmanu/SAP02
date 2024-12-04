package entity.ebike.concreate;

import entity.ebike.EBike;
import entity.ebike.EBikeFactory;
import entity.ebike.EBikeRepository;

import java.awt.geom.Point2D;
import java.util.HashSet;

public class EBikeRepositoryImpl implements EBikeRepository {
    private final EBikeFactory ebikeFactory;
    private final HashSet<EBike> ebikes;

    public EBikeRepositoryImpl() {
        this.ebikes = new HashSet<>();
        this.ebikeFactory = new EBikeFactory.SimpleFactory();
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
}
