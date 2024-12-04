package application;

import java.util.Optional;

public interface Application {

    void setEbikeController(EbikeControllerPort ebikeController);
    void setUserRepository(RepositoryPort userRepository);
    void setUserView(UserViewPort userView);

    Optional<ErrorApplication> signUp(String username, String password);
    Optional<ErrorApplication> signIn(String username, String password);
    Optional<ErrorApplication> addCreditsTo(float someCredits);

    Optional<Float> creditsOfUser();

    boolean isRegistered(String username);

    boolean userIsLogged();

    void logout();
}
