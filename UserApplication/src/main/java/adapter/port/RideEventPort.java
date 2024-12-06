package adapter.port;

import application.Application;

public interface RideEventPort {

    void onRide();

    boolean userHaveCredits();

    void stopEBike();

    boolean eBikeIsLowBattery();

    class RideEventPortImpl implements RideEventPort {
        private final Application application;

        public RideEventPortImpl(final Application application) {
            this.application = application;
        }

        @Override
        public void onRide() {
            this.application.withdrawCredits();
            this.application.consumeBattery();
        }

        @Override
        public boolean userHaveCredits() {
            return this.application.userHasCredits();
        }

        @Override
        public void stopEBike() {
            this.application.stopEBike();
        }

        @Override
        public boolean eBikeIsLowBattery() {
            return this.application.eBikesHasLowBattery();
        }
    }
}
