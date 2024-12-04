package application;

import entity.ebike.EBikeRepository;
import entity.ebike.concreate.EBikeRepositoryImpl;
import entity.user.UserRepository;
import entity.user.concreate.UserRepositoryImpl;

import java.util.Optional;

public interface RepositoryPort {
    Optional<ErrorApplication> signUp(String username, String password);
    Optional<ErrorApplication> signIn(String username, String password);
    Optional<ErrorApplication> addCreditsTo(String username, float someCredits);

    boolean contain(String username);

    Optional<Float> creditsOf(String username);


    class RepositoryPortImpl implements RepositoryPort {
        private final UserRepository userRepository;
        private final EBikeRepository ebikeRepository;

        public RepositoryPortImpl() {
            this.userRepository = new UserRepositoryImpl();
            this.ebikeRepository = new EBikeRepositoryImpl();
        }

        public RepositoryPortImpl(final UserRepository userRepository, final EBikeRepository ebikeRepository) {
            this.userRepository = userRepository;
            this.ebikeRepository = ebikeRepository;
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
        public Optional<ErrorApplication> addCreditsTo(final String username, final float someCredits) {
            if (someCredits < 0) return Optional.of(ErrorApplication.ADD_NEGATIVE_CREDITS);
            if (someCredits == 0) return Optional.of(ErrorApplication.ADD_ZERO_CREDITS);
            if (!this.userRepository.addCreditsTo(username, someCredits))
                return Optional.of(ErrorApplication.NOT_LOGGED);
            return Optional.empty();
        }

        @Override
        public boolean contain(final String username) {
            return this.userRepository.contains(username);
        }

        @Override
        public Optional<Float> creditsOf(final String username) {
            return this.userRepository.creditsOf(username);
        }
    }
}
