package utils;

public final class ThreadUtils {

    public static void sleep(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException ignored) { }
    }
}
