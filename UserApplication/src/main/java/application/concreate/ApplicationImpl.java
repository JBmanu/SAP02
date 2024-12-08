package application.concreate;

import adapter.port.RideEventPort;
import application.*;
import entity.ebike.EBike;
import entity.ebike.EBikeFactory;
import entity.ebike.EBikeState;
import entity.user.User;
import entity.user.UserFactory;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;

public class ApplicationImpl implements Application {
    private final EBikeFactory eBikeFactory;
    private Optional<EBikeControllerPort> ebikeController;
    private Optional<RepositoryPort> repository;
    private Optional<ViewPort> view;

    private Optional<User> user;
    private Optional<EBike> ebike;

    public ApplicationImpl() {
        this.eBikeFactory = new EBikeFactory.SimpleFactory();

        this.ebikeController = Optional.empty();
        this.repository = Optional.empty();
        this.view = Optional.empty();
        this.user = Optional.empty();
        this.ebike = Optional.empty();
    }

    @Override
    public void setEBikeController(final EBikeControllerPort eBikeController) {
        this.ebikeController = Optional.ofNullable(eBikeController);
        this.ebikeController.ifPresent(controller -> controller.setRideEventPort(new RideEventPort.RideEventPortImpl(this)));
    }

    @Override
    public void setRepository(final RepositoryPort repository) {
        this.repository = Optional.ofNullable(repository);
    }

    @Override
    public void setView(final ViewPort view) {
        this.view = Optional.ofNullable(view);
    }

    @Override
    public List<String> eBikesIdFree() {
        return this.repository.map(RepositoryPort::eBikesIdFree).orElse(List.of());
    }

    private void userLogged(final Optional<Message> error, final String username, final String password) {
        if (error.isPresent()) return;
        final UserFactory userFactory = new UserFactory.SimpleFactory();
        this.user = Optional.of(userFactory.createWithoutCredit(username, password));
    }

    @Override
    public Optional<Message> signUp(final String username, final String password) {
        final Optional<Message> error = this.repository.flatMap(repo -> repo.signUp(username, password));
        this.userLogged(error, username, password);
        error.ifPresentOrElse(msg -> this.view.ifPresent(view -> view.showMessage(Optional.of(msg))),
                () -> this.view.ifPresent(view -> view.showHirePanel(username, this.creditsOfUser())));
        return error;
    }

    @Override
    public Optional<Message> signIn(final String username, final String password) {
        final Optional<Message> error = this.repository.flatMap(repo -> repo.signIn(username, password));
        this.userLogged(error, username, password);
        error.ifPresentOrElse(msg -> this.view.ifPresent(view -> view.showMessage(Optional.of(msg))),
                () -> this.view.ifPresent(view -> view.showHirePanel(username, this.creditsOfUser())));
        return error;
    }

    @Override
    public Optional<Message> addCreditsOf(final float someCredits) {
        final Optional<Message> error = this.user
                .flatMap(user -> this.repository.map(repo -> repo.addCreditsTo(user.username(), someCredits)))
                .orElse(Optional.empty());
        error.ifPresentOrElse(
                message -> this.view.ifPresent(view -> view.showMessage(error)),
                () -> this.view.ifPresent(view -> view.addCreditsOf(this.creditsOfUser())));
        return error;
    }

    @Override
    public Optional<Float> creditsOfUser() {
        return this.user
                .flatMap(userLogged -> this.repository.map(repository -> repository.creditsOf(userLogged.username())))
                .orElse(Optional.empty());
    }

    @Override
    public boolean isRegistered(final String username) {
        return this.repository.isPresent() && this.repository.get().containUser(username);
    }

    @Override
    public boolean userIsLogged() {
        return this.user.isPresent();
    }

    @Override
    public void logout() {
        this.user = Optional.empty();
        this.view.ifPresent(ViewPort::showLoginPanel);
    }

    @Override
    public Optional<Message> hireEBike(final String eBikeId) {
        final Optional<Message> error = this.user
                .flatMap(user -> this.repository.map(repo -> repo.hireEBike(user.username(), eBikeId, CREDITS_FOR_HIRE)))
                .orElse(Optional.of(Message.Error.NOT_CONNECTED));
        this.view.ifPresent(view -> view.showMessage(error));

        if (error.isEmpty()) {
            this.ebike = Optional.of(this.eBikeFactory.create(eBikeId));
            this.ebikeController.ifPresent(EBikeControllerPort::rideEBike);
            this.view.ifPresent(view -> view.hireEBike(this.creditsOfUser(), this.eBikeId()));
        }
        return error;
    }

    @Override
    public boolean isFreeEBike(final String eBikeId) {
        return this.repository.map(repo -> repo.isFreeEBike(eBikeId)).orElse(false);
    }

    @Override
    public boolean isInUseEBike(final String eBikeId) {
        return this.repository.map(repo -> repo.isInUseEBike(eBikeId)).orElse(false);
    }

    @Override
    public boolean isLowBatteryEBike(final String eBikeId) {
        return this.repository.map(repo -> repo.isLowBatteryEBike(eBikeId)).orElse(false);
    }

    @Override
    public boolean hasHireEBike() {
        return this.ebike.isPresent();
    }

    @Override
    public void stopEBike() {
        this.ebikeController.ifPresent(EBikeControllerPort::stopEBike);
        this.ebike.ifPresent(ebike -> this.repository.ifPresent(repo -> repo.stopEBike(ebike.id())));
        this.ebike = Optional.empty();
        this.view.ifPresent(view -> view.stopEBike(this.creditsOfUser()));
    }

    @Override
    public boolean eBikesHasLowBattery() {
        final boolean eBikeHasLowBattery = this.ebike.map(ebike -> this.isLowBatteryEBike(ebike.id())).orElse(false);
        if (eBikeHasLowBattery) this.view.ifPresent(ViewPort::showLowBatteryMessage);
        return eBikeHasLowBattery;
    }

    @Override
    public boolean userHasCredits() {
        final boolean hasCredits = this.creditsOfUser().map(credits -> credits >= CREDITS_FOR_RIDE).orElse(false);
        if (!hasCredits) this.view.ifPresent(ViewPort::showHasCreditsMessage);
        return hasCredits;
    }

    @Override
    public void withdrawCredits() {
        this.user.ifPresent(user ->
                this.repository.ifPresent(repo -> repo.withdrawCredits(user.username(), CREDITS_FOR_RIDE)));
        this.view.ifPresent(view -> view.showCredits(this.creditsOfUser()));
    }

    @Override
    public void consumeBattery() {
        this.ebike.ifPresent(ebike ->
                this.repository.ifPresent(repo -> repo.consumeBattery(ebike.id(), CONSUME_BATTERY)));
        this.view.ifPresent(view -> view.showBattery(this.eBikeBattery()));
    }

    @Override
    public Optional<String> eBikeId() {
        return this.ebike.map(EBike::id);
    }

    @Override
    public Optional<Integer> eBikeBattery() {
        return this.ebike.flatMap(eBike -> this.repository.map(repo -> repo.batteryOf(eBike.id())))
                .orElse(Optional.empty());
    }

    @Override
    public Optional<Point2D> eBikePosition() {
        return this.ebike.flatMap(eBike -> this.repository.map(repo -> repo.positionOf(eBike.id())))
                .orElse(Optional.empty());
    }

    @Override
    public Optional<EBikeState> eBikeState() {
        return this.ebike.flatMap(eBike -> this.repository.map(repo -> repo.stateOf(eBike.id())))
                .orElse(Optional.empty());
    }

}
