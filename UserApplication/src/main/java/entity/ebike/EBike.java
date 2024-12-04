package entity.ebike;

public interface EBike {
    String id();
    EBikeState state();
    int battery();

    void increaseBattery(int amount);
    void decreaseBattery(int amount);

    void setStateInUse();
    void setStateFree();
    void setStateLowBattery();
}
