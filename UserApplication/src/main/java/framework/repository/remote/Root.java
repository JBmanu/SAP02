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
    public static final String CONTAINS_USER_PATH = USERS_ROOT + "/contains";

    // EBIKE
    public static final String EBIKES_ID_FREE = EBIKE_ROOT + "/eBikesIdFree";
    public static final String CREATE_EBIKE = EBIKE_ROOT + "/create";
    public static final String HAS_EBIKE = EBIKE_ROOT + "/hasEBikes";
    public static final String CONTAINS_EBIKE = EBIKE_ROOT + "/contains";
    public static final String HIRE_EBIKE = EBIKE_ROOT + "/hire";
    public static final String STOP_EBIKE = EBIKE_ROOT + "/stop";
    public static final String RECHARGE_BATTERY = EBIKE_ROOT + "/rechargeBattery";
    public static final String CONSUME_BATTERY = EBIKE_ROOT + "/consumeBattery";
    public static final String IS_FREE_EBIKE = EBIKE_ROOT + "/isFree";
    public static final String IS_IN_USE_EBIKE = EBIKE_ROOT + "/isInUse";
    public static final String IS_LOW_BATTERY_EBIKE = EBIKE_ROOT + "/isLowBattery";
    public static final String BATTERY_EBIKE = EBIKE_ROOT + "/battery";
    public static final String POSITION_EBIKE = EBIKE_ROOT + "/position";
    public static final String STATE_EBIKE = EBIKE_ROOT + "/state";

}
