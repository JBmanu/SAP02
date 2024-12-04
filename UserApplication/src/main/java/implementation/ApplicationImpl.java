package implementation;

import application.*;

import java.util.Optional;

public class ApplicationImpl implements Application {
    private Optional<EbikeControllerPort> ebikeController;
    private Optional<UserRepositoryPort> userRepository;
    private Optional<UserViewPort> userView;

    public ApplicationImpl(final Optional<EbikeControllerPort> ebikeController,
                           final Optional<UserRepositoryPort> userRepository,
                           final Optional<UserViewPort> userView) {
        this.ebikeController = ebikeController;
        this.userRepository = userRepository;
        this.userView = userView;
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
        Optional<ErrorApplication> error = Optional.empty();
        if (this.userRepository.isPresent())
            error = this.userRepository.get().signUp(username, password);
        return error;
    }

    @Override
    public boolean containUser(final String username) {
        return this.userRepository.isPresent() && this.userRepository.get().contain(username);
    }
}
