package application;

import adapter.View;

import java.util.Optional;

public interface ViewPort {
    void showError(Optional<ErrorApplication> error);

    void showCredits(Optional<Float> credits);

    class ViewPortImpl implements ViewPort {
        private final View view;

        public ViewPortImpl(final View view) {
            this.view = view;
        }

        @Override
        public void showError(final Optional<ErrorApplication> error) {
            error.ifPresent(errorApplication -> this.view.showError(errorApplication.toString()));
        }

        @Override
        public void showCredits(final Optional<Float> credits) {
            credits.ifPresent(this.view::setCredits);
        }
    }
}
