import domain.User;
import domain.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserFactoryTest {
    private static final String EMAIL = "prova@gmail.com";
    private static final String PASSWORD = "password";

    private UserFactory userFactory;

    @BeforeEach
    public void canCreate() {
        this.userFactory = new UserFactory.SimpleFactory();
    }

    @Test
    public void createWithoutCredit() {
        final User user = this.userFactory.createWithoutCredit(EMAIL, PASSWORD);
        assertEquals(EMAIL, user.username());
        assertEquals(PASSWORD, user.password());
        assertEquals(0.f, user.credits());
    }

    @Test
    public void createWithCredit() {
        final float credits = 10.f;
        final User user = this.userFactory.createWithCredit(EMAIL, PASSWORD, credits);
        assertEquals(EMAIL, user.username());
        assertEquals(PASSWORD, user.password());
        assertEquals(credits, user.credits());
    }

    @Test
    public void createTestUser() {
        final User userTest = this.userFactory.createTest();
        assertNotNull(userTest);
    }

}
