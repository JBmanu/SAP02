package application;

import adapter.EBikeRepository;
import entity.ebike.EBikeState;
import framework.repository.local.EBikeRepositoryImpl;
import adapter.UserRepository;
import framework.repository.local.UserRepositoryImpl;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;

public interface RepositoryPort {
    List<String> eBikesIdFree();

    boolean contain(String username);

    Optional<Message> signUp(String username, String password);
    Optional<Message> signIn(String username, String password);
    Optional<Message> addCreditsTo(String username, float someCredits);

    Optional<Float> creditsOf(String username);

    Optional<Message> hireEBike(String username, String eBikeId, float withoutCredits);
    boolean isFreeEBike(String eBikeId);
    boolean isInUseEBike(String eBikeId);
    boolean isLowBatteryEBike(String eBikeId);

    void stopEBike(String id);

    boolean withdrawCredits(String username, float someCredits);

    void consumeBattery(String id, int consumeBattery);

    Optional<Integer> batteryOf(String id);

    Optional<Point2D> positionOf(String id);

    Optional<EBikeState> stateOf(String id);

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
        public Optional<Message> signUp(final String username, final String password) {
            if (username.isBlank() || password.isBlank()) return Optional.of(Message.Error.EMPTY_FIELD);
            return this.userRepository.add(username, password) ?
                    Optional.empty() : Optional.of(Message.Error.SAME_USERNAME);
        }

        @Override
        public Optional<Message> signIn(final String username, final String password) {
            if (username.isBlank() || password.isBlank()) return Optional.of(Message.Error.EMPTY_FIELD);
            if (!this.contain(username)) return Optional.of(Message.Error.NOT_REGISTERED);
            return this.userRepository.authentication(username, password) ?
                    Optional.empty() : Optional.of(Message.Error.WRONG_PASSWORD);
        }

        @Override
        public Optional<Message> addCreditsTo(final String username, final float someCredits) {
            if (someCredits < 0) return Optional.of(Message.Error.ADD_NEGATIVE_CREDITS);
            if (someCredits == 0) return Optional.of(Message.Error.ADD_ZERO_CREDITS);
            if (!this.userRepository.addCreditsTo(username, someCredits))
                return Optional.of(Message.Error.NOT_LOGGED);
            return Optional.empty();
        }

        @Override
        public Optional<Float> creditsOf(final String username) {
            return this.userRepository.creditsOf(username);
        }

        @Override
        public Optional<Message> hireEBike(final String username, final String eBikeId, final float WITHOUT_CREDITS) {
            if (!this.ebikeRepository.hasEBike()) return Optional.of(Message.Error.NO_EBIKES);
            if (this.isInUseEBike(eBikeId)) return Optional.of(Message.Error.EBIKE_IN_USE);
            if (this.isLowBatteryEBike(eBikeId)) return Optional.of(Message.Error.EBIKE_LOW_BATTERY);
            final boolean canHire = this.userRepository.withdrawCredits(username, WITHOUT_CREDITS);
            if (!canHire) return Optional.of(Message.Error.ZERO_CREDITS);
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
        public boolean withdrawCredits(final String username, final float someCredits) {
            return this.userRepository.withdrawCredits(username, someCredits);
        }

        @Override
        public void consumeBattery(final String id, final int consumeBattery) {
            this.ebikeRepository.consumeBattery(id, consumeBattery);
        }

        @Override
        public Optional<Integer> batteryOf(final String id) {
            return this.ebikeRepository.batteryOf(id);
        }

        @Override
        public Optional<Point2D> positionOf(final String id) {
            return this.ebikeRepository.positionOf(id);
        }

        @Override
        public Optional<EBikeState> stateOf(final String id) {
            return this.ebikeRepository.stateOf(id);
        }

    }
}
