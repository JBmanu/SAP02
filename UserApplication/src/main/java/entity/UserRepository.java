package entity;

import java.util.Optional;

public interface UserRepository {
    int count();


    boolean add(User user);

    boolean add(String username, String password);

    boolean remove(String username);

    boolean addCreditsTo(String username, float amount);


    boolean contains(String username);

    Optional<User> userOf(String username);

    Optional<Float> creditsOf(String username);

}
