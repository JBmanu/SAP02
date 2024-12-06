package application;

import adapter.View;

import java.util.Optional;

public interface ViewPort {
    void showMessage(Optional<Message> error);

    void showCredits(Optional<Float> credits);

    void hireEBike();

    void showHirePanel(String username, Optional<Float> credits);

    void showLoginPanel();

    void showBattery(Optional<Integer> battery);

    void showEBikeId(Optional<String> eBikeId);

    void stopEBike(Optional<Float> credits);

    void showLowBatteryMessage(boolean eBikeHasLowBattery);

    void showHasCreditsMessage(boolean hasCredits);


    class ViewPortImpl implements ViewPort {
        private final View view;

        public ViewPortImpl(final View view) {
            this.view = view;
        }

        @Override
        public void showMessage(final Optional<Message> error) {
            error.ifPresent(errorApplication -> this.view.showError(errorApplication.toString()));
        }

        @Override
        public void showCredits(final Optional<Float> credits) {
            credits.ifPresent(this.view::setCredits);
        }

        @Override
        public void hireEBike() {
            this.view.hireEBike();
        }

        @Override
        public void showHirePanel(final String username, final Optional<Float> credits) {
            this.view.showHirePanel(username);
            this.showCredits(credits);
        }

        @Override
        public void showLoginPanel() {
            this.view.showLoginPanel();
        }

        @Override
        public void showBattery(final Optional<Integer> battery) {
            battery.ifPresent(this.view::setBattery);
        }

        @Override
        public void showEBikeId(final Optional<String> eBikeId) {
            eBikeId.ifPresent(this.view::setEBikeId);
        }

        @Override
        public void stopEBike(final Optional<Float> credits) {
            this.showCredits(credits);
            this.showMessage(Optional.of(Message.Info.STOP_EBIKE));
            this.view.stopEBike();
        }

        @Override
        public void showLowBatteryMessage(final boolean eBikeHasLowBattery) {
            if (eBikeHasLowBattery) {
                this.showMessage(Optional.of(Message.Error.EBIKE_LOW_BATTERY));
            }
        }

        @Override
        public void showHasCreditsMessage(final boolean hasCredits) {
            if (!hasCredits) {
                this.showMessage(Optional.of(Message.Error.ZERO_CREDITS));
            }
        }

    }
}
