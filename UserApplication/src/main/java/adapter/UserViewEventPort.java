package adapter;

import application.Application;
import application.ErrorApplication;

import java.util.Collection;
import java.util.Optional;

public interface UserViewEventPort {
    String CORRECT = "Correct";

    Collection<String> eBikesFree();

    String onSignUp(String username, String password);

    String onSignIn(String username, String password);

    String onAddCredits(String credits);

    String onHireEBike(String eBikeId);

    void onLogout();

    float credits();


    class UserViewEventPortImpl implements UserViewEventPort {
        private final Application application;

        public UserViewEventPortImpl(final Application application) {
            this.application = application;
        }

        @Override
        public String onSignUp(final String username, final String password) {
            final Optional<ErrorApplication> error = this.application.signUp(username, password);
            return error.map(Object::toString).orElse(CORRECT);
        }

        @Override
        public String onSignIn(final String username, final String password) {
            final Optional<ErrorApplication> error = this.application.signIn(username, password);
            return error.map(Object::toString).orElse(CORRECT);
        }

        @Override
        public String onAddCredits(final String credits) {
            float creditsFloat = 0;
            try {
                creditsFloat = Float.parseFloat(credits);
            } catch (final NumberFormatException ignored) {}

            final Optional<ErrorApplication> error = this.application.addCreditsOf(creditsFloat);
            return error.map(Object::toString).orElse(CORRECT);
        }

        @Override
        public String onHireEBike(final String eBikeId) {
            return this.application.hireEBike(eBikeId).map(Object::toString).orElse(CORRECT);
        }

        @Override
        public Collection<String> eBikesFree() {
            return this.application.eBikesIdFree();
        }

        @Override
        public void onLogout() {
            this.application.logout();
        }

        @Override
        public float credits() {
            return this.application.creditsOfUser().orElse(0f);
        }
    }
}
