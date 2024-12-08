package framework.repository.remote;

public final class Root {
    // ROOT
    private static final String PORT = "3000";
    private static final String URL_ROOT = "http://localhost:" + PORT;

    // ROUTES
    public static final String USERS_ROOT = URL_ROOT + "/users";
    public static final String EBIKE_ROOT = URL_ROOT + "/ebikes";

    // USER
    public static final String SIGN_UP_PATH = USERS_ROOT + "/signUp";
    public static final String SIGN_IN_PATH = USERS_ROOT + "/signIn";
    public static final String ADD_CREDITS_PATH = USERS_ROOT + "/addCredits";
    public static final String CREDITS_PATH = USERS_ROOT + "/credits";
    public static final String WITHDRAW_CREDITS_PATH = USERS_ROOT + "/withdrawCredits";
    public static final String CONTAINS_PATH = USERS_ROOT + "/contains";

    // EBIKE
    public static final String EBIKES_ID_FREE = EBIKE_ROOT + "/eBikesIdFree";
    public static final String HAR_EBIKE = EBIKE_ROOT + "hasEBike";

}
