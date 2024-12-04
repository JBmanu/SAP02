package application;

import java.util.Optional;

public interface Application {

    void setEbikeController(Optional<EbikeControllerPort> ebikeController);
    void setUserRepository(Optional<UserRepositoryPort> userRepository);
    void setUserView(Optional<UserViewPort> userView);

    Optional<ErrorApplication> signUp(String username, String password);
    Optional<ErrorApplication> signIn(String username, String password);

    boolean containUser(String username);

    boolean userIsLogged();
}
