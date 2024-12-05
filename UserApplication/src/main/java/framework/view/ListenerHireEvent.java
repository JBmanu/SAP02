package framework.view;

public interface ListenerHireEvent {
    String onClickSignUp(String username, String password);
    String onClickSignIn(String username, String password);
    String onClickAddCredits(String credits);

    void onClickHire(String eBikeId);

    void onClickLogout();

}
