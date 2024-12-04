package application;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<ErrorApplication> signUp(String username, String password);

    boolean contain(String username);
}
