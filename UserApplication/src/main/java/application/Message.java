package application;

public interface Message {

    enum Error implements Message {
        NOT_LOGGED,
        NOT_REGISTERED,
        NOT_CONNECTED,

        EMPTY_FIELD,
        SAME_USERNAME,
        WRONG_PASSWORD,

        ZERO_CREDITS,
        ADD_NEGATIVE_CREDITS,
        ADD_ZERO_CREDITS,

        EBIKE_IN_USE,
        EBIKE_LOW_BATTERY, NO_EBIKES,
    }

    enum Info implements Message {
        STOP_EBIKE,
        CREDITS_ADDED,
    }

}
