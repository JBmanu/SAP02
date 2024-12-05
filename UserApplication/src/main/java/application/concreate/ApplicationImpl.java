package application.concreate;

import adapter.RideEventPort;
import application.*;
import entity.ebike.EBike;
import entity.ebike.EBikeFactory;
import entity.user.User;
import entity.user.UserFactory;

import java.util.List;
import java.util.Optional;

public class ApplicationImpl implements Application {
    private Optional<EBikeControllerPort> ebikeController;
    private Optional<RepositoryPort> repository;
    private Optional<ViewPort> view;

    private Optional<User> user;
    private Optional<EBike> ebike;

    public ApplicationImpl() {
        this.ebikeController = Optional.empty();
        this.repository = Optional.empty();
        this.view = Optional.empty();
        this.user = Optional.empty();
        this.ebike = Optional.empty();
    }

    @Override
    public void setEBikeController(final EBikeControllerPort ebikeController) {
        this.ebikeController = Optional.ofNullable(ebikeController);
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

    private void userLogged(final Optional<ErrorApplication> error, final String username, final String password) {
        if (this.repository.isEmpty() || error.isPresent()) return;
        final UserFactory userFactory = new UserFactory.SimpleFactory();
        this.user = Optional.of(userFactory.createWithoutCredit(username, password));
    }

    @Override
    public Optional<ErrorApplication> signUp(final String username, final String password) {
        final Optional<ErrorApplication> error = this.repository.isPresent() ?
                this.repository.get().signUp(username, password) : Optional.empty();
        this.userLogged(error, username, password);
        this.view.ifPresent(view -> view.showError(error));
        return error;
    }

    @Override
    public Optional<ErrorApplication> signIn(final String username, final String password) {
        final Optional<ErrorApplication> error = this.repository.isPresent() ?
                this.repository.get().signIn(username, password) : Optional.empty();
        this.userLogged(error, username, password);
        this.view.ifPresent(view -> view.showError(error));
        return error;
    }

    @Override
    public Optional<ErrorApplication> addCreditsOf(final float someCredits) {
        final Optional<ErrorApplication> error = this.user.isPresent() && this.repository.isPresent() ?
                this.repository.get().addCreditsTo(this.user.get().username(), someCredits) : Optional.empty();
        this.view.ifPresent(view -> view.showError(error));
        return error;
    }

    @Override
    public Optional<Float> creditsOfUser() {
        return this.user
                .flatMap(userLogged -> this.repository.map(repository ->
                        repository.creditsOf(userLogged.username())))
                .orElse(Optional.empty());
    }

    @Override
    public boolean isRegistered(final String username) {
        return this.repository.isPresent() && this.repository.get().contain(username);
    }

    @Override
    public boolean userIsLogged() {
        return this.user.isPresent();
    }

    @Override
    public void logout() {
        this.user = Optional.empty();
    }


    @Override
    public Optional<ErrorApplication> hireEBike(final String eBikeId) {
        final Optional<ErrorApplication> error = this.repository.isPresent() && this.user.isPresent() ?
                this.repository.get().hireEBike(this.user.get().username(), eBikeId, CREDITS_FOR_HIRE) :
                Optional.of(ErrorApplication.NOT_CONNECTED);
        if (error.isEmpty()) {
            final EBikeFactory eBikeFactory = new EBikeFactory.SimpleFactory();
            this.ebike = Optional.of(eBikeFactory.create(eBikeId));
            this.ebikeController.ifPresent(controller ->
                    controller.rideEBike(new RideEventPort.RideEventPortImpl(this)));
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
    }

    @Override
    public boolean userHasCredits() {
        return this.creditsOfUser().map(credits -> credits >= CREDITS_FOR_RIDE).orElse(false);
    }

    @Override
    public void withdrawCredits() {
        this.user.ifPresent(user ->
                this.repository.ifPresent(repo ->
                        repo.withdrawCredits(user.username(), CREDITS_FOR_RIDE)));
        this.view.ifPresent(view -> view.showCredits(this.creditsOfUser()));
        System.out.println("Riding...");
    }

}
