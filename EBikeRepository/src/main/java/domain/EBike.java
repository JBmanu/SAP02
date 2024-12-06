package domain;

import java.awt.geom.Point2D;

public interface EBike {
    String id();
    EBikeState state();
    int battery();
    Point2D position();

    boolean hire();
    void updatePosition(Point2D position);
    void stopRide();

    boolean isFree();
    boolean isInUse();
    boolean isLowBattery();

    void setLowBattery();

    void consumeBattery(int consumeBattery);
}
