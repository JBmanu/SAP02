package application;

import adapter.View;

import java.util.Optional;

public interface ViewPort {
    void showMessage(Optional<Message> error);

    void showLowBatteryMessage();

    void showHasCreditsMessage();

    void showCredits(Optional<Float> credits);

    void showBattery(Optional<Integer> battery);

    void showHirePanel(String username, Optional<Float> credits);

    void showLoginPanel();

    void addCreditsOf(Optional<Float> credits);

    void hireEBike(Optional<Float> credits, Optional<String> eBikeId);

    void stopEBike(Optional<Float> credits);


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
        public void showLowBatteryMessage() {
            this.showMessage(Optional.of(Message.Error.EBIKE_LOW_BATTERY));
        }

        @Override
        public void showHasCreditsMessage() {
            this.showMessage(Optional.of(Message.Error.ZERO_CREDITS));
        }

        @Override
        public void showCredits(final Optional<Float> credits) {
            credits.ifPresent(this.view::setCredits);
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

        private void showEBikeId(final Optional<String> eBikeId) {
            eBikeId.ifPresent(this.view::setEBikeId);
        }

        @Override
        public void addCreditsOf(final Optional<Float> credits) {
            this.showCredits(credits);
            this.showMessage(Optional.of(Message.Info.CREDITS_ADDED));
        }

        @Override
        public void hireEBike(final Optional<Float> credits, final Optional<String> eBikeId) {
            this.showCredits(credits);
            this.showEBikeId(eBikeId);
            this.view.hireEBike();
        }

        @Override
        public void stopEBike(final Optional<Float> credits) {
            this.showCredits(credits);
            this.showMessage(Optional.of(Message.Info.STOP_EBIKE));
            this.view.stopEBike();
        }

    }
}
