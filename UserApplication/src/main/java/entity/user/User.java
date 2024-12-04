package entity.user;

public interface User {
    String username();

    String password();

    float credits();

    void addCredits(float amount);

    void withdrawCredits(float amount);
}
