package application;

import adapter.View;

import java.util.Optional;

public interface ViewPort {
    void showMessage(Optional<Message> error);

    void showCredits(Optional<Float> credits);

    void hireEBike();

    void showHirePanel(String username, Optional<Float> credits);

    void showLoginPanel();

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

    }
}
