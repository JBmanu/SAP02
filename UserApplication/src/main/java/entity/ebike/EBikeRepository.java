package entity.ebike;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;

public interface EBikeRepository {
    List<String> eBikesIdFree();

    int count();

    boolean add();
    boolean remove(String id);
    boolean contains(String id);
    boolean hasEBike();

    boolean hireEBike(String id);
    boolean updateEBikePosition(String id, Point2D position);
    boolean stopEBike(String id);
    boolean rechargeEBikeBattery(String id, float amount);

    boolean isFree(String eBikeId);
    boolean isInUse(String eBikeId);
    boolean isLowBattery(String eBikeId);

    void setLowBattery(String eBikeId);

    void consumeBattery(String id, int consumeBattery);

    Optional<Integer> batteryOf(String id);

    Optional<Point2D> positionOf(String id);

    Optional<EBikeState> stateOf(String id);

}
