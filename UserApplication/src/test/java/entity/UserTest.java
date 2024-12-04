package entity;

import concreate.UserImpl;
import domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {
    private static final String USERNAME = "prova@gmail.com";
    private static final String PASSWORD = "password";
    private static final float INIT_CREDITS = 0.f;
    private User user;

    @BeforeEach
    public void setUp() {
        this.user = new UserImpl(USERNAME, PASSWORD);
    }

    @Test
    public void canCreate() {
        assertNotNull(this.user);
    }

    @Test
    public void readUsername() {
        assertEquals(USERNAME, this.user.username());
    }

    @Test
    public void readPassword() {
        assertEquals(PASSWORD, this.user.password());
    }

    @Test
    public void readCredits() {
        assertEquals(INIT_CREDITS, this.user.credits());
    }

    @Test
    public void addCredits() {
        final float amount = 10.f;
        this.user.addCredits(amount);
        assertEquals(amount, this.user.credits());
    }

    @Test
    public void withdrawCredits() {
        final float amount = 10.f;
        this.user.addCredits(amount);
        this.user.withdrawCredits(amount);
        assertEquals(INIT_CREDITS, this.user.credits());
    }

    @Test
    public void sameUser() {
        final User user2 = new UserImpl(USERNAME, PASSWORD);
        assertEquals(this.user.username(), user2.username());
        assertEquals(this.user.password(), user2.password());
        assertEquals(this.user, user2);
    }
}
