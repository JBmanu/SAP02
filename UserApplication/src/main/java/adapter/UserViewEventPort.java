package adapter;

import application.Application;
import application.ErrorApplication;

import java.util.Optional;

public interface UserViewEventPort {
    String CORRECT = "Correct";

    String onSignUp(String username, String password);

    String onSignIn(String username, String password);


    class UserViewEventPortImpl implements UserViewEventPort {
        private final Application application;

        public UserViewEventPortImpl(final Application application) {
            this.application = application;
        }

        @Override
        public String onSignUp(final String username, final String password) {
            final Optional<ErrorApplication> errorApplication = this.application.signUp(username, password);
            return errorApplication.map(Object::toString).orElse(CORRECT);
        }

        @Override
        public String onSignIn(final String username, final String password) {
            final Optional<ErrorApplication> errorApplication = this.application.signIn(username, password);
            return errorApplication.map(Object::toString).orElse(CORRECT);
        }
    }
}
