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
            final Optional<ErrorApplication> error = this.contain(username) ?
                    Optional.of(ErrorApplication.SAME_USERNAME) : Optional.empty();
            if (error.isEmpty())
                this.userRepository.add(username, password);

            return error;
        }

        @Override
        public boolean contain(final String username) {
            return this.userRepository.contains(username);
        }
    }
}
