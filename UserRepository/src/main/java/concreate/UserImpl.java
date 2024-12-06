package concreate;

import domain.User;

public class UserImpl implements User {
    private record UserCredential(String username, String password) { }

    private static final int INIT_CREDITS = 0;
    private final UserCredential userCredential;
    private float credits;

    public UserImpl(final String username, final String password, final float credits) {
        this.userCredential = new UserCredential(username, password);
        this.credits = credits;
    }

    public UserImpl(final String email, final String password) {
        this(email, password, INIT_CREDITS);
    }

    @Override
    public String username() {
        return this.userCredential.username();
    }

    @Override
    public String password() {
        return this.userCredential.password();
    }

    @Override
    public float credits() {
        return this.credits;
    }

    @Override
    public void addCredits(final float amount) {
        this.credits += amount;
    }

    @Override
    public void withdrawCredits(final float amount) {
        this.credits -= amount;
    }

    @Override
    public boolean hasSufficientCredits(final float amount) {
        return this.credits >= amount;
    }

    @Override
    public int hashCode() {
        return this.username().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof final User user && this.userCredential.username().equals(user.username());
    }
}
