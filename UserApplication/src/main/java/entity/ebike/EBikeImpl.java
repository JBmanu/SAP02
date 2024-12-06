package entity.ebike;

import java.awt.geom.Point2D;

public class EBikeImpl implements EBike {
    public static final int MAX_LEVEL_BATTERY = 100;
    public static final int LOW_BATTERY = 20;
    private final String id;
    private EBikeState state;
    private int battery;

    private Point2D position;
    private V2d direction;
    private float speed;

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
    public void updatePosition(final Point2D position) {
        this.position = position;
    }

    @Override
    public boolean hire() {
        if (this.isFree()) {
            this.state = EBikeState.IN_USE;
            return true;
        }
        return false;
    }

    @Override
    public void stopRide() {
        if (this.isInUse()) {
            this.state = EBikeState.FREE;
        }
        this.speed = 0;
    }

    @Override
    public boolean isFree() {
        return this.state == EBikeState.FREE;
    }

    @Override
    public boolean isInUse() {
        return this.state == EBikeState.IN_USE;
    }

    @Override
    public boolean isLowBattery() {
        return this.state == EBikeState.LOW_BATTERY;
    }

    @Override
    public void setLowBattery() {
        this.battery = LOW_BATTERY;
        this.state = EBikeState.LOW_BATTERY;
    }

    @Override
    public void consumeBattery(final int consumeBattery) {
        this.battery -= consumeBattery;
        if (this.battery < LOW_BATTERY) {
            this.setLowBattery();
        }
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
