package adapter;


public interface View {
    void setEventPort(ViewEventPort eventPort);

    void setCredits(Float credits);

    void showError(String string);

    void hireEBike();

    void stopEBike();

    void showHirePanel(String username);

    void showLoginPanel();

    void setBattery(Integer integer);

    void setEBikeId(String eBikeId);

}
