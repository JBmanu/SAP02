package domain;

import java.util.HashSet;
import java.util.Optional;

public interface UserRepository {
    int count();

    HashSet<User> users();

    boolean add(User user);

    boolean add(String username, String password);

    boolean remove(String username);

    boolean addCreditsTo(String username, float amount);

    boolean withdrawCredits(String username, float amount);


    boolean contains(String username);

    Optional<User> userOf(String username);

    Optional<Float> creditsOf(String username);

    boolean authentication(String username, String password);

}
