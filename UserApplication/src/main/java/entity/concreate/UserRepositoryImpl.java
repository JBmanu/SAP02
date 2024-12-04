package entity.concreate;

import entity.User;
import entity.UserRepository;

import java.util.HashSet;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private final HashSet<User> users;

    public UserRepositoryImpl() {
        this.users = new HashSet<>();
    }

    @Override
    public int count() {
        return this.users.size();
    }

    @Override
    public void add(final User user) {
        this.users.add(user);
    }

    @Override
    public void remove(final String username) {
        this.users.removeIf(user -> user.username().equals(username));
    }

    @Override
    public void addCreditsTo(final String username, final float amount) {
        this.users.stream()
                .filter(user -> user.username().equals(username))
                .forEach(user -> user.addCredits(amount));
    }

    @Override
    public boolean contains(final String username) {
        return this.users.stream().anyMatch(user -> user.username().equals(username));
    }

    @Override
    public Optional<User> userOf(final String username) {
        return this.users.stream()
                .filter(user -> user.username().equals(username))
                .findFirst();
    }

    @Override
    public Optional<Float> creditsOf(final String username) {
        return this.users.stream()
                .filter(user -> user.username().equals(username))
                .findFirst().map(User::credits);
    }
}
