package application.concreate;

import application.*;
import entity.User;
import entity.UserFactory;

import java.util.Optional;

public class ApplicationImpl implements Application {
    private Optional<EbikeControllerPort> ebikeController;
    private Optional<UserRepositoryPort> userRepository;
    private Optional<UserViewPort> userView;

    private Optional<User> user;

    public ApplicationImpl(final Optional<EbikeControllerPort> ebikeController,
                           final Optional<UserRepositoryPort> userRepository,
                           final Optional<UserViewPort> userView) {
        this.ebikeController = ebikeController;
        this.userRepository = userRepository;
        this.userView = userView;
        this.user = Optional.empty();
    }

    @Override
    public void setEbikeController(final Optional<EbikeControllerPort> ebikeController) {
        this.ebikeController = ebikeController;
    }

    @Override
    public void setUserRepository(final Optional<UserRepositoryPort> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void setUserView(final Optional<UserViewPort> userView) {
        this.userView = userView;
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
        return error;
    }

    @Override
    public Optional<ErrorApplication> signIn(final String username, final String password) {
        final Optional<ErrorApplication> error = this.userRepository.isPresent() ?
                this.userRepository.get().signIn(username, password) : Optional.empty();
        this.userLogged(error, username, password);
        return error;
    }

    @Override
    public Optional<ErrorApplication> addCreditsTo(final float someCredits) {
        return this.user.isPresent() && this.userRepository.isPresent() ?
                this.userRepository.get().addCreditsTo(this.user.get().username(), someCredits) : Optional.empty();
    }

    @Override
    public Optional<Float> creditsOfUser() {
        return this.user
                .flatMap(userLogged -> this.userRepository.map(repository ->
                        repository.creditsOf(userLogged.username())))
                .orElse(Optional.empty());
    }

    @Override
    public boolean containUser(final String username) {
        // da rivedere o togliere
        return this.userRepository.isPresent() && this.userRepository.get().contain(username);
    }

    @Override
    public boolean userIsLogged() {
        return this.user.isPresent();
    }


}
