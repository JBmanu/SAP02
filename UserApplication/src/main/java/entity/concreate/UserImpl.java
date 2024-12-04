package entity.concreate;

import entity.User;

public class UserImpl implements User {
    private record DataUser(String username, String password, float credits) { }

    private static final int INIT_CREDITS = 0;
    private final DataUser dataUser;
    private float credits;

    public UserImpl(final String username, final String password, final float credits) {
        this.dataUser = new DataUser(username, password, credits);
        this.credits = credits;
    }

    public UserImpl(final String email, final String password) {
        this(email, password, INIT_CREDITS);
    }

    @Override
    public String username() {
        return this.dataUser.username();
    }

    @Override
    public String password() {
        return this.dataUser.password();
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
    public boolean equals(final Object obj) {
        return obj instanceof final UserImpl user && this.dataUser.username().equals(user.dataUser.username());
    }
}
