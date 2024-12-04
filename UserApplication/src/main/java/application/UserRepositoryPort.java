package application;

import entity.UserRepository;
import entity.concreate.UserRepositoryImpl;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<ErrorApplication> signUp(String username, String password);

    boolean contain(String username);

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
            return this.userRepository.add(username, password)?
                    Optional.empty() : Optional.of(ErrorApplication.SAME_USERNAME);
        }

        @Override
        public boolean contain(final String username) {
            return this.userRepository.contains(username);
        }
    }
}
