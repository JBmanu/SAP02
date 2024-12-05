package application;

import entity.ebike.EBikeRepository;
import entity.ebike.concreate.EBikeRepositoryImpl;
import entity.user.UserRepository;
import entity.user.concreate.UserRepositoryImpl;

import java.util.List;
import java.util.Optional;

public interface RepositoryPort {
    List<String> eBikesIdFree();

    boolean contain(String username);

    Optional<ErrorApplication> signUp(String username, String password);
    Optional<ErrorApplication> signIn(String username, String password);
    Optional<ErrorApplication> addCreditsTo(String username, float someCredits);

    Optional<Float> creditsOf(String username);

    Optional<ErrorApplication> hireEBike(String username, String eBikeId, float withoutCredits);
    boolean isFreeEBike(String eBikeId);
    boolean isInUseEBike(String eBikeId);
    boolean isLowBatteryEBike(String eBikeId);

    void stopEBike(String id);

    void withdrawCredits(String username, float someCredits);

    void consumeBattery(String id, int consumeBattery);

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
        public List<String> eBikesIdFree() {
            return this.ebikeRepository.eBikesIdFree();
        }

        @Override
        public boolean contain(final String username) {
            return this.userRepository.contains(username);
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
        public Optional<Float> creditsOf(final String username) {
            return this.userRepository.creditsOf(username);
        }

        @Override
        public Optional<ErrorApplication> hireEBike(final String username, final String eBikeId, final float WITHOUT_CREDITS) {
            if (this.isInUseEBike(eBikeId)) return Optional.of(ErrorApplication.EBIKE_IN_USE);
            if (this.isLowBatteryEBike(eBikeId)) return Optional.of(ErrorApplication.EBIKE_LOW_BATTERY);
            final boolean canHire = this.userRepository.withdrawCredits(username, WITHOUT_CREDITS);
            if (!canHire) return Optional.of(ErrorApplication.ZERO_CREDITS);
            this.ebikeRepository.hireEBike(eBikeId);
            return Optional.empty();
        }

        @Override
        public boolean isFreeEBike(final String eBikeId) {
            return this.ebikeRepository.isFree(eBikeId);
        }

        @Override
        public boolean isInUseEBike(final String eBikeId) {
            return this.ebikeRepository.isInUse(eBikeId);
        }

        @Override
        public boolean isLowBatteryEBike(final String eBikeId) {
            return this.ebikeRepository.isLowBattery(eBikeId);
        }

        @Override
        public void stopEBike(final String id) {
            this.ebikeRepository.stopEBike(id);
        }

        @Override
        public void withdrawCredits(final String username, final float someCredits) {
            this.userRepository.withdrawCredits(username, someCredits);
        }

        @Override
        public void consumeBattery(final String id, final int consumeBattery) {
            this.ebikeRepository.consumeBattery(id, consumeBattery);
        }

    }
}
