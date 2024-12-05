package application;

import adapter.RideEventPort;

import java.util.Optional;

public interface EBikeControllerPort {
    void rideEBike(RideEventPort rideEventPort);

    void stopEBike();

    class EBikeControllerPortImpl extends Thread implements EBikeControllerPort {
        private Optional<RideEventPort> rideEventPort = Optional.empty();
        private boolean isRiding = false;

        @Override
        public void run() {
            while (this.isRiding) {
                try {
                    sleep(500);
                } catch (final InterruptedException ignored) { }
                this.rideEventPort.ifPresent(RideEventPort::onRide);

                this.rideEventPort.ifPresent(rideEventPort -> {
                    if (!rideEventPort.userHaveCredits()) {
                        rideEventPort.stopRide();
                    }
                });
            }
        }

        @Override
        public void rideEBike(final RideEventPort rideEventPort) {
            this.rideEventPort = Optional.ofNullable(rideEventPort);
            this.isRiding = true;
            this.start();
        }

        @Override
        public void stopEBike() {
            this.isRiding = false;
        }
    }
}
