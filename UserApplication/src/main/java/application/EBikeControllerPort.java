package application;

import adapter.RideEventPort;
import utils.ThreadUtils;

import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public interface EBikeControllerPort {
    int WAIT_COST_RIDE = 500;
    int WAIT_STOP_EBIKE = 600;

    void rideEBike();

    void stopEBike();

    void setRideEventPort(RideEventPort.RideEventPortImpl rideEventPort);

    class EBikeControllerPortImpl extends Thread implements EBikeControllerPort {
        private Optional<RideEventPort> rideEventPort = Optional.empty();
        private boolean isRunning;
        private boolean isRiding;

        private final Lock mutex;
        private final Condition condition;

        public EBikeControllerPortImpl() {
            this.mutex = new ReentrantLock();
            this.condition = this.mutex.newCondition();
            this.isRunning = true;
            this.isRiding = false;
            this.start();
        }

        @Override
        public void run() {
            while (this.isRunning) {
                this.waitUntilPlay();
                ThreadUtils.sleep(WAIT_COST_RIDE);
                this.rideEventPort.ifPresent(RideEventPort::onRide);

                this.rideEventPort.ifPresent(rideEventPort -> {
                    if (!rideEventPort.userHaveCredits() || rideEventPort.eBikeIsLowBattery()) {
                        ThreadUtils.sleep(WAIT_STOP_EBIKE);
                        rideEventPort.stopEBike();
                    }
                });
            }
        }

        private void waitUntilPlay() {
            try {
                this.mutex.lock();
                while (!this.isRiding) this.condition.await();
            } catch (final InterruptedException ignored) {
            } finally {
                this.mutex.unlock();
            }
        }

        private void play() {
            try {
                this.mutex.lock();
                this.isRiding = true;
                this.condition.signalAll();
            } finally {
                this.mutex.unlock();
            }
        }

        @Override
        public void rideEBike() {
            this.isRiding = true;
            this.play();
        }

        @Override
        public void stopEBike() {
            this.isRiding = false;
        }

        @Override
        public void setRideEventPort(final RideEventPort.RideEventPortImpl rideEventPort) {
            this.rideEventPort = Optional.ofNullable(rideEventPort);
        }

        public void terminate() {
            this.isRunning = false;
        }
    }
}
