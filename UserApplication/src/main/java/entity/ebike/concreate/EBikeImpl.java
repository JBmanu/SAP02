package entity.ebike.concreate;

import entity.ebike.EBike;
import entity.ebike.EBikeState;
import entity.ebike.V2d;

import java.awt.geom.Point2D;

public class EBikeImpl implements EBike {
    public static final int MAX_LEVEL_BATTERY = 100;
    public static final int MIN_LEVEL_BATTERY = 0;
    private final String id;
    private EBikeState state;
    private int battery;

    private V2d direction;
    private Point2D position;

    public EBikeImpl(final String id) {
        this.id = id;
        this.state = EBikeState.FREE;
        this.battery = MAX_LEVEL_BATTERY;
        this.position = new Point2D.Float(0, 0);
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public EBikeState state() {
        return this.state;
    }

    @Override
    public int battery() {
        return this.battery;
    }

    @Override
    public Point2D position() {
        return this.position;
    }

    @Override
    public void increaseBattery(final int amount) {
        if (this.battery >= MAX_LEVEL_BATTERY) return;
        this.battery += amount;
    }

    @Override
    public void decreaseBattery(final int amount) {
        if (this.battery <= MIN_LEVEL_BATTERY) return;
        this.battery -= amount;
    }

    @Override
    public void setStateInUse() {
        this.state = EBikeState.IN_USE;
    }

    @Override
    public void setStateFree() {
        this.state = EBikeState.FREE;
    }

    @Override
    public void setStateLowBattery() {
        this.state = EBikeState.LOW_BATTERY;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof final EBike eBike && eBike.id().equals(this.id);
    }
}
