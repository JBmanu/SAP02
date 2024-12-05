package application;

import entity.ebike.EBikeState;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;

public interface Application {
    float CREDITS_FOR_HIRE = 10f;
    float CREDITS_FOR_RIDE = 0.5f;
    int CONSUME_BATTERY = 1;

    void setEBikeController(EBikeControllerPort eBikeController);
    void setRepository(RepositoryPort repository);
    void setView(ViewPort view);

    List<String> eBikesIdFree();

    Optional<Message> signUp(String username, String password);
    Optional<Message> signIn(String username, String password);
    Optional<Message> addCreditsOf(float someCredits);
    Optional<Float> creditsOfUser();

    boolean isRegistered(String username);
    boolean userIsLogged();
    void logout();


    Optional<Message> hireEBike(String eBikeId);

    boolean isFreeEBike(String eBikeId);
    boolean isInUseEBike(String eBikeId);
    boolean isLowBatteryEBike(String eBikeId);
    boolean hasHireEBike();
    void stopEBike();

    boolean eBikesHasLowBattery();
    boolean userHasCredits();
    void withdrawCredits();
    void consumeBattery();

    Optional<String> eBikeId();
    Optional<Integer> eBikeBattery();
    Optional<Point2D> eBikePosition();
    Optional<EBikeState> eBikeState();
}
