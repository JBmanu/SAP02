package application;

import entity.UserRepository;
import entity.concreate.UserRepositoryImpl;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<ErrorApplication> signUp(String username, String password);

    Optional<ErrorApplication> signIn(String username, String password);

    boolean contain(String username);

    float creditsOf(String username);

    class UserRepositoryPortImpl implements UserRepositoryPort {
        private final UserRepository userRepository;

        public UserRepositoryPortImpl() {
            this.userRepository = new UserRepositoryImpl();
        }

        @Override
        public Optional<ErrorApplication> signUp(final String username, final String password) {
            final boolean emptyFields = username.isBlank() || password.isBlank();
            if (emptyFields)
                return Optional.of(ErrorApplication.EMPTY_FIELD);
            return this.userRepository.add(username, password) ?
                    Optional.empty() : Optional.of(ErrorApplication.SAME_USERNAME);
        }

        @Override
        public Optional<ErrorApplication> signIn(final String username, final String password) {
            final boolean emptyFields = username.isBlank() || password.isBlank();
            if (emptyFields)
                return Optional.of(ErrorApplication.EMPTY_FIELD);
            if (!this.contain(username))
                return Optional.of(ErrorApplication.NOT_REGISTERED);
            return this.userRepository.checkPasswordOf(username, password) ?
                    Optional.empty() : Optional.of(ErrorApplication.WRONG_PASSWORD);
        }

        @Override
        public boolean contain(final String username) {
            return this.userRepository.contains(username);
        }

        @Override
        public float creditsOf(final String username) {
            return this.userRepository.creditsOf(username).orElse(0f);
        }
    }
}
