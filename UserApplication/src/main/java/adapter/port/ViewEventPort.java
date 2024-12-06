package adapter.port;

import application.Application;

import java.util.List;

public interface ViewEventPort {
    List<String> eBikesFree();

    void onSignUp(String username, String password);

    void onSignIn(String username, String password);

    void onAddCredits(String credits);

    void onHireEBike(String eBikeId);

    void onStopHireEBike();

    void onLogout();

    float credits();

    boolean canHireEBike();



    class ViewEventPortImpl implements ViewEventPort {
        private final Application application;

        public ViewEventPortImpl(final Application application) {
            this.application = application;
        }

        @Override
        public void onSignUp(final String username, final String password) {
            this.application.signUp(username, password);
        }

        @Override
        public void onSignIn(final String username, final String password) {
            this.application.signIn(username, password);
        }

        @Override
        public void onAddCredits(final String credits) {
            final float creditsFloat;
            try {
                creditsFloat = Float.parseFloat(credits);
                this.application.addCreditsOf(creditsFloat);
            } catch (final NumberFormatException ignored) {}
        }

        @Override
        public void onHireEBike(final String eBikeId) {
            this.application.hireEBike(eBikeId);
        }

        @Override
        public void onStopHireEBike() {
            this.application.stopEBike();
        }

        @Override
        public List<String> eBikesFree() {
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

        @Override
        public boolean canHireEBike() {
            return !this.application.hasHireEBike();
        }


    }
}
