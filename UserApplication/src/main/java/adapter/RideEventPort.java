package adapter;

import application.Application;

public interface RideEventPort {

    void onRide();

    boolean userHaveCredits();

    void stopRide();

    class RideEventPortImpl implements RideEventPort {
        private final Application application;

        public RideEventPortImpl(final Application application) {
            this.application = application;
        }

        @Override
        public void onRide() {
            this.application.withdrawCredits();
        }

        @Override
        public boolean userHaveCredits() {
            return this.application.userHasCredits();
        }

        @Override
        public void stopRide() {
            this.application.stopEBike();
        }
    }
}
