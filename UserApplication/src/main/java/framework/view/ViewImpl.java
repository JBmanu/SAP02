package framework.view;

import adapter.UserViewEventPort;
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

    private Optional<UserViewEventPort> eventPort;

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
    public void setEventPort(final UserViewEventPort eventPort) {
        this.eventPort = Optional.ofNullable(eventPort);
    }

    private void showHirePanel(final String username) {
        this.eventPort.ifPresent(port ->
                this.hirePanel.setUserData(username, port.credits()));
        this.changePanel(this.hirePanel);
    }

    @Override
    public String onClickSignUp(final String username, final String password) {
        final String message = this.eventPort.isPresent() ?
                this.eventPort.get().onSignUp(username, password) : NOT_CONNECTED;
        if (message.equals(UserViewEventPort.CORRECT)) {
            this.showHirePanel(username);
        }
        return message;
    }

    @Override
    public String onClickSignIn(final String username, final String password) {
        final String message = this.eventPort.isPresent() ?
                this.eventPort.get().onSignIn(username, password) : NOT_CONNECTED;
        if (message.equals(UserViewEventPort.CORRECT)) {
            this.showHirePanel(username);
        }
        return message;
    }

    @Override
    public String onClickAddCredits(final String credits) {
        final String message = this.eventPort.isPresent() ?
                this.eventPort.get().onAddCredits(credits) : NOT_CONNECTED;
        if (message.equals(UserViewEventPort.CORRECT)) {
            this.hirePanel.setCredits(this.eventPort.get().credits());
        }
        return message;
    }

    @Override
    public List<String> freeEBikes() {
        final List<String> eBikesFree = new ArrayList<>();
        this.eventPort.ifPresent(port -> eBikesFree.addAll(port.eBikesFree()));
        return eBikesFree;
    }

    @Override
    public String onClickHire(final String eBikeId) {
        final String message = this.eventPort.isPresent() ?
                this.eventPort.get().onHireEBike(eBikeId) : NOT_CONNECTED;

        if (message.equals(UserViewEventPort.CORRECT)) {
            this.hirePanel.setCredits(this.eventPort.get().credits());
        }

        return message;
    }

    @Override
    public void onStopHireEBike() {
        this.eventPort.ifPresent(UserViewEventPort::onStopHireEBike);
        this.hirePanel.setCredits(this.eventPort.get().credits());
    }

    @Override
    public boolean canHireEBike() {
        return this.eventPort.map(UserViewEventPort::canHireEBike).orElse(false);
    }

    @Override
    public void onClickLogout() {
        this.changePanel(this.loginPanel);
        this.eventPort.ifPresent(UserViewEventPort::onLogout);
    }
}
