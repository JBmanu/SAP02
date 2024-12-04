package entity.ebike;

import java.awt.geom.Point2D;

public interface EBikeRepository {

    int count();

    boolean add();
    boolean remove(String id);
    boolean contains(String id);

    boolean hireEBike(String id);
    boolean updateEBikePosition(String id, Point2D position);
    boolean stopEBike(String id);
    boolean rechargeEBikeBattery(String id, float amount);
}
