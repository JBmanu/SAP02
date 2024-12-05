package framework.view;

import java.util.List;

public interface ListenerHireEvent {
    String onClickSignUp(String username, String password);
    String onClickSignIn(String username, String password);
    String onClickAddCredits(String credits);

    List<String> freeEBikes();

    void onClickHire(String eBikeId);

    void onClickLogout();

}
