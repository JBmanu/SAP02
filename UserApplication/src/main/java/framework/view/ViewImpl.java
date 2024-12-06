package framework.view;

import adapter.ViewEventPort;
import adapter.View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ViewImpl extends JFrame implements View, ListenerHireEvent {
    public static final String NOT_CONNECTED = "NOT CONNECTED";

    private final HirePanel hirePanel;
    private final LoginPanel loginPanel;

    private Optional<ViewEventPort> eventPort;

    public ViewImpl() {
        this.eventPort = Optional.empty();

        this.hirePanel = new HirePanel(this);
        this.loginPanel = new LoginPanel(this);

        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.changePanel(this.loginPanel);
        this.setVisible(true);
    }

    private void changePanel(final JPanel panel) {
        // da vedere bene la dimensione solo
        this.getContentPane().removeAll();
        this.add(panel, BorderLayout.CENTER);
        this.setSize(panel.getSize());
        this.setLocationRelativeTo(null);
        this.revalidate();
        this.repaint();
        SwingUtilities.invokeLater(() -> {

        });
    }

    @Override
    public void setEventPort(final ViewEventPort eventPort) {
        this.eventPort = Optional.ofNullable(eventPort);
    }

    @Override
    public void setCredits(final Float credits) {
        this.hirePanel.setCredits(credits);
    }

    @Override
    public void showError(final String string) {
        TimedMessageDialog.showTimedMessage(this, string, 500);
    }

    @Override
    public void hireEBike() {
        this.hirePanel.showStopHireButton();
    }

    @Override
    public void stopEBike() {
        this.hirePanel.showHireButton();
    }

    @Override
    public void showHirePanel(final String username) {
        this.hirePanel.setUsername(username);
        this.changePanel(this.hirePanel);
    }

    @Override
    public void showLoginPanel() {
        this.changePanel(this.loginPanel);
    }

    @Override
    public void setBattery(final Integer integer) {
        this.hirePanel.setBattery(integer);
    }

    @Override
    public void setEBikeId(final String eBikeId) {
        this.hirePanel.setEBikeId(eBikeId);
    }

    @Override
    public void onClickSignUp(final String username, final String password) {
        this.eventPort.ifPresent(port -> port.onSignUp(username, password));
    }

    @Override
    public void onClickSignIn(final String username, final String password) {
        this.eventPort.ifPresent(port ->  port.onSignIn(username, password));
    }

    @Override
    public void onClickAddCredits(final String credits) {
        this.eventPort.ifPresent(port -> port.onAddCredits(credits));
    }

    @Override
    public List<String> freeEBikes() {
        return this.eventPort.map(ViewEventPort::eBikesFree).orElse(new ArrayList<>());
    }

    @Override
    public void onClickHire(final String eBikeId) {
        this.eventPort.ifPresent(port -> port.onHireEBike(eBikeId));
    }

    @Override
    public void onStopHireEBike() {
        this.eventPort.ifPresent(ViewEventPort::onStopHireEBike);
    }

    @Override
    public boolean canHireEBike() {
        return this.eventPort.map(ViewEventPort::canHireEBike).orElse(false);
    }

    @Override
    public void onClickLogout() {
        this.eventPort.ifPresent(ViewEventPort::onLogout);
    }
}
