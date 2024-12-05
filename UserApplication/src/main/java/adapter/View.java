package adapter;


public interface View {
    void setEventPort(ViewEventPort eventPort);

    void setCredits(Float credits);

    void showError(String string);

    void hireEBike();

    void showHirePanel(String username);

    void showLoginPanel();
}
