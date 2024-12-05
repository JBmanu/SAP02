package entity.ebike;

import java.awt.geom.Point2D;
import java.util.List;

public interface EBikeRepository {
    List<String> eBikesIdFree();

    int count();

    boolean add();
    boolean remove(String id);
    boolean contains(String id);

    boolean hireEBike(String id);
    boolean updateEBikePosition(String id, Point2D position);
    boolean stopEBike(String id);
    boolean rechargeEBikeBattery(String id, float amount);

    boolean isFree(String eBikeId);
    boolean isInUse(String eBikeId);
    boolean isLowBattery(String eBikeId);

    void setLowBattery(String eBikeId);

}
