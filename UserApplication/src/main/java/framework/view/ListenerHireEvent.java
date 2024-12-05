package framework.view;

import java.util.List;

public interface ListenerHireEvent {
    void onClickSignUp(String username, String password);
    void onClickSignIn(String username, String password);
    void onClickAddCredits(String credits);
    void onClickHire(String eBikeId);
    void onStopHireEBike();

    List<String> freeEBikes();

    boolean canHireEBike();

    void onClickLogout();

}
