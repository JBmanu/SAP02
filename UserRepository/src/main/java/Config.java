import domain.User;
import domain.UserFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public final class Config {
    private static final Properties config = new Properties();

    public static void loadConfig() {
        try {
            config.load(Objects.requireNonNull(Config.class.getClassLoader().getResourceAsStream("config.properties")));
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static int port() {
        return Integer.parseInt(config.getProperty("service.port", "7000"));
    }

    // Carica gli utenti predefiniti dalla configurazione
    public static List<User> loadDefaultUsers() {
        final UserFactory factory = new UserFactory.SimpleFactory();
        final List<User> users = new ArrayList<>();

        final int nUsers = Integer.parseInt(config.getProperty("users.count", "3"));
        for (int i = 1; i <= nUsers; i++) {
            final String username = config.getProperty("user" + i + ".username");
            final String password = config.getProperty("user" + i + ".password");
            final float credits = Float.parseFloat(config.getProperty("user" + i + ".credits"));
            users.add(factory.createWithCredit(username, password, credits));
        }
        return users;
    }

}
