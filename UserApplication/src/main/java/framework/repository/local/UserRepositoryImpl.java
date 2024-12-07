package framework.repository.local;

import entity.user.User;
import entity.user.UserFactory;
import adapter.UserRepository;

import java.util.HashSet;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private final UserFactory userFactory;
    private final HashSet<User> users;

    public UserRepositoryImpl() {
        this.userFactory = new UserFactory.SimpleFactory();
        this.users = new HashSet<>();
    }

    @Override
    public int count() {
        return this.users.size();
    }

    @Override
    public boolean add(final User user) {
        return this.users.add(user);
    }

    @Override
    public boolean add(final String username, final String password) {
        return this.users.add(this.userFactory.createWithoutCredit(username, password));
    }

    @Override
    public boolean remove(final String username) {
        return this.users.removeIf(user -> user.username().equals(username));
    }

    @Override
    public boolean addCreditsTo(final String username, final float amount) {
        this.users.stream()
                .filter(user -> user.username().equals(username))
                .forEach(user -> user.addCredits(amount));
        return this.contains(username);
    }

    @Override
    public boolean withdrawCredits(final String username, final float amount) {
        final Optional<User> optionalUser = this.users.stream()
                .filter(user -> user.username().equals(username) && user.hasSufficientCredits(amount))
                .findFirst();
        return optionalUser.map(user -> {
            user.withdrawCredits(amount);
            return true;
        }).orElse(false);
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

    @Override
    public boolean authentication(final String username, final String password) {
        return this.users.stream()
                .anyMatch(user -> user.username().equals(username) &&
                        user.password().equals(password));
    }
}
