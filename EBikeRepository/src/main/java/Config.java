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

    public static int loadDefaultEBikes() {
        final String eBikeCount = config.getProperty("eBike.count", "0");
        return Integer.parseInt(eBikeCount);
    }

}
