package entity;

import entity.concreate.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class UserRepositoryTest {
    private final UserFactory userFactory = new UserFactory.SimpleFactory();
    private UserRepository userRepository;

    @BeforeEach
    public void canCreate() {
        this.userRepository = new UserRepositoryImpl();
    }

    @Test
    public void countUsers() {
        assertEquals(0, this.userRepository.count());
    }

    @Test
    public void addUsers() {
        final User userTest = this.userFactory.createTest();
        assertTrue(this.userRepository.add(userTest));
        assertEquals(1, this.userRepository.count());
        assertEquals(Optional.of(userTest), this.userRepository.userOf(userTest.username()));
    }

    @Test
    public void removeUsers() {
        final User user = this.userFactory.createTest();
        this.userRepository.add(user);
        assertTrue(this.userRepository.remove(user.username()));
        assertEquals(0, this.userRepository.count());
    }

    @Test
    public void containsUser() {
        final User user = this.userFactory.createTest();
        this.userRepository.add(user);
        assertTrue(this.userRepository.contains(user.username()));
    }

    @Test
    public void addSameUser() {
        final User user = this.userFactory.createTest();
        final User user1 = this.userFactory.createTest();
        this.userRepository.add(user);
        assertFalse(this.userRepository.add(user1));
    }

    @Test
    public void readUserNotExistent() {
        final User user = this.userFactory.createTest();
        assertEquals(Optional.empty(), this.userRepository.userOf(user.username()));
    }

    @Test
    public void addCreditsToUser() {
        final User user = this.userFactory.createTest();
        final float credits = 100f;
        this.userRepository.add(user);
        assertTrue(this.userRepository.addCreditsTo(user.username(), credits));
        assertEquals(Optional.of(credits), this.userRepository.creditsOf(user.username()));
    }

    @Test
    public void readCreditsNotExistentUser() {
        final User user = this.userFactory.createTest();
        assertEquals(Optional.empty(), this.userRepository.creditsOf(user.username()));
    }

}
