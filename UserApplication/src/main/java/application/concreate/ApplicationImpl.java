package application.concreate;

import application.*;
import entity.User;
import entity.UserFactory;
import entity.UserRepository;

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

    @Override
    public Optional<ErrorApplication> signUp(final String username, final String password) {
        return this.userRepository.isPresent() ?
                this.userRepository.get().signUp(username, password) : Optional.empty();
    }

    @Override
    public Optional<ErrorApplication> signIn(final String username, final String password) {
        final Optional<ErrorApplication> error = this.userRepository.isPresent() ?
                this.userRepository.get().signIn(username, password) : Optional.empty();
        if (this.userRepository.isPresent() && error.isEmpty()) {
            final UserFactory userFactory = new UserFactory.SimpleFactory();
            final float credits = this.userRepository.get().creditsOf(username);
            this.user = Optional.of(userFactory.createWithCredit(username, password, credits));
        }
        return error;
    }

    @Override
    public boolean containUser(final String username) {
        return this.userRepository.isPresent() && this.userRepository.get().contain(username);
    }

    @Override
    public boolean userIsLogged() {
        return this.user.isPresent();
    }
}
