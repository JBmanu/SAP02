package entity.ebike;

import java.awt.geom.Point2D;

public interface EBike {
    String id();
    EBikeState state();
    int battery();
    Point2D position();



    void increaseBattery(int amount);
    void decreaseBattery(int amount);

    void setStateInUse();
    void setStateFree();
    void setStateLowBattery();
}
