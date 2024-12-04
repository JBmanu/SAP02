package application.concreate;

import application.*;
import entity.ebike.EBike;
import entity.user.User;
import entity.user.UserFactory;

import java.util.Optional;

public class ApplicationImpl implements Application {
    private Optional<EbikeControllerPort> ebikeController;
    private Optional<UserRepositoryPort> userRepository;
    private Optional<UserViewPort> userView;

    private Optional<User> user;
    private Optional<EBike> ebike;

    public ApplicationImpl() {
        this.ebikeController = Optional.empty();
        this.userRepository = Optional.empty();
        this.userView = Optional.empty();
        this.user = Optional.empty();
        this.ebike = Optional.empty();
    }

    @Override
    public void setEbikeController(final EbikeControllerPort ebikeController) {
        this.ebikeController = Optional.ofNullable(ebikeController);
    }

    @Override
    public void setUserRepository(final UserRepositoryPort userRepository) {
        this.userRepository = Optional.ofNullable(userRepository);
    }

    @Override
    public void setUserView(final UserViewPort userView) {
        this.userView = Optional.ofNullable(userView);
    }

    private void userLogged(final Optional<ErrorApplication> error, final String username, final String password) {
        if (this.userRepository.isEmpty() || error.isPresent()) return;
        final UserFactory userFactory = new UserFactory.SimpleFactory();
        this.user = Optional.of(userFactory.createWithoutCredit(username, password));
    }

    @Override
    public Optional<ErrorApplication> signUp(final String username, final String password) {
        final Optional<ErrorApplication> error = this.userRepository.isPresent() ?
                this.userRepository.get().signUp(username, password) : Optional.empty();
        this.userLogged(error, username, password);
        this.userView.ifPresent(view -> view.showError(error));
        return error;
    }

    @Override
    public Optional<ErrorApplication> signIn(final String username, final String password) {
        final Optional<ErrorApplication> error = this.userRepository.isPresent() ?
                this.userRepository.get().signIn(username, password) : Optional.empty();
        this.userLogged(error, username, password);
        this.userView.ifPresent(view -> view.showError(error));
        return error;
    }

    @Override
    public Optional<ErrorApplication> addCreditsTo(final float someCredits) {
        final Optional<ErrorApplication> error = this.user.isPresent() && this.userRepository.isPresent() ?
                this.userRepository.get().addCreditsTo(this.user.get().username(), someCredits) : Optional.empty();
        this.userView.ifPresent(view -> view.showError(error));
        return error;
    }

    @Override
    public Optional<Float> creditsOfUser() {
        return this.user
                .flatMap(userLogged -> this.userRepository.map(repository ->
                        repository.creditsOf(userLogged.username())))
                .orElse(Optional.empty());
    }

    @Override
    public boolean isRegistered(final String username) {
        return this.userRepository.isPresent() && this.userRepository.get().contain(username);
    }

    @Override
    public boolean userIsLogged() {
        return this.user.isPresent();
    }

    @Override
    public void logout() {
        this.user = Optional.empty();
    }

}
