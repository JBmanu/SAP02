package domain;

import java.util.Optional;

public interface UserRepository {
    int count();


    void add(User user);

    void remove(String username);

    void addCreditsTo(String username, float amount);


    boolean contains(String username);

    Optional<User> userOf(String username);

    Optional<Float> creditsOf(String username);

}
