package application;

import java.util.List;
import java.util.Optional;

public interface Application {

    void setEBikeController(EbikeControllerPort ebikeController);
    void setRepository(RepositoryPort repository);
    void setUserView(UserViewPort userView);

    List<String> eBikesIdFree();

    Optional<ErrorApplication> signUp(String username, String password);
    Optional<ErrorApplication> signIn(String username, String password);
    Optional<ErrorApplication> addCreditsOf(float someCredits);
    Optional<Float> creditsOfUser();

    boolean isRegistered(String username);
    boolean userIsLogged();
    void logout();


    Optional<ErrorApplication> startRide(String eBikeId);

    boolean isFreeEBike(String eBikeId);
    boolean isInUseEBike(String eBikeId);
    boolean isLowBatteryEBike(String eBikeId);
    boolean hasHireEBike();

}
