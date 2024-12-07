package framework.repository.remote;

public final class Root {
    // ROOT
    public static final String PORT = "3000";
    public static final String URL_ROOT = "http://localhost:" + PORT;

    // ROUTES
    public static final String USERS_PATH = "/users";
    public static final String EBIKE_PATH = "/ebikes";

    // USER
    public static final String SIGN_UP_PATH = USERS_PATH + "/signUp";
    public static final String SIGN_IN_PATH = USERS_PATH + "/signIn";
    public static final String ADD_CREDITS_PATH = USERS_PATH + "/addCredits";
    public static final String CREDITS_PATH = USERS_PATH + "/credits";
    public static final String WITHDRAW_CREDITS_PATH = USERS_PATH + "/withdrawCredits";
    public static final String CONTAINS_PATH = USERS_PATH + "/contains";

    // EBIKE

}
