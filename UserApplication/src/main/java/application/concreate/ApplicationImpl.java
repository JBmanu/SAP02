package application.concreate;

import application.*;
import entity.ebike.EBike;
import entity.ebike.EBikeFactory;
import entity.user.User;
import entity.user.UserFactory;

import java.util.List;
import java.util.Optional;

public class ApplicationImpl implements Application {
    private static final float WITHOUT_CREDITS = 10f;
    private Optional<EbikeControllerPort> ebikeController;
    private Optional<RepositoryPort> repository;
    private Optional<UserViewPort> userView;

    private Optional<User> user;
    private Optional<EBike> ebike;

    public ApplicationImpl() {
        this.ebikeController = Optional.empty();
        this.repository = Optional.empty();
        this.userView = Optional.empty();
        this.user = Optional.empty();
        this.ebike = Optional.empty();
    }

    @Override
    public void setEBikeController(final EbikeControllerPort ebikeController) {
        this.ebikeController = Optional.ofNullable(ebikeController);
    }

    @Override
    public void setRepository(final RepositoryPort repository) {
        this.repository = Optional.ofNullable(repository);
    }

    @Override
    public void setUserView(final UserViewPort userView) {
        this.userView = Optional.ofNullable(userView);
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
        this.userView.ifPresent(view -> view.showError(error));
        return error;
    }

    @Override
    public Optional<ErrorApplication> signIn(final String username, final String password) {
        final Optional<ErrorApplication> error = this.repository.isPresent() ?
                this.repository.get().signIn(username, password) : Optional.empty();
        this.userLogged(error, username, password);
        this.userView.ifPresent(view -> view.showError(error));
        return error;
    }

    @Override
    public Optional<ErrorApplication> addCreditsOf(final float someCredits) {
        final Optional<ErrorApplication> error = this.user.isPresent() && this.repository.isPresent() ?
                this.repository.get().addCreditsTo(this.user.get().username(), someCredits) : Optional.empty();
        this.userView.ifPresent(view -> view.showError(error));
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
                this.repository.get().hireEBike(this.user.get().username(), eBikeId, WITHOUT_CREDITS) :
                Optional.of(ErrorApplication.NOT_CONNECTED);
        if (error.isEmpty()) {
            final EBikeFactory eBikeFactory = new EBikeFactory.SimpleFactory();
            this.ebike = Optional.of(eBikeFactory.create(eBikeId));
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

}
