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

    private void showHirePanel(final String username) {
        this.eventPort.ifPresent(port ->
                this.hirePanel.setUserData(username, port.credits()));
        this.changePanel(this.hirePanel);
    }

    @Override
    public void onClickSignUp(final String username, final String password) {
        this.eventPort.ifPresent(port -> {
            final String message = port.onSignUp(username, password);
            if (message.equals(ViewEventPort.CORRECT)) {
                this.showHirePanel(username);
            }
        });
    }

    @Override
    public void onClickSignIn(final String username, final String password) {
        this.eventPort.ifPresent(port -> {
            final String message = port.onSignIn(username, password);
            if (message.equals(ViewEventPort.CORRECT)) {
                this.showHirePanel(username);
            }
        });
    }

    @Override
    public void onClickAddCredits(final String credits) {
        final String message = this.eventPort.isPresent() ?
                this.eventPort.get().onAddCredits(credits) : NOT_CONNECTED;
        if (message.equals(ViewEventPort.CORRECT)) {
            this.hirePanel.setCredits(this.eventPort.get().credits());
            TimedMessageDialog.showTimedMessage(this, "Credits added", 500);
        }

    }

    @Override
    public List<String> freeEBikes() {
        final List<String> eBikesFree = new ArrayList<>();
        this.eventPort.ifPresent(port -> eBikesFree.addAll(port.eBikesFree()));
        return eBikesFree;
    }

    @Override
    public void onClickHire(final String eBikeId) {
        final String message = this.eventPort.isPresent() ?
                this.eventPort.get().onHireEBike(eBikeId) : NOT_CONNECTED;

        if (message.equals(ViewEventPort.CORRECT)) {
            this.hirePanel.setCredits(this.eventPort.get().credits());
            this.hirePanel.showStopHireButton();
        }
    }

    @Override
    public void onStopHireEBike() {
        this.eventPort.ifPresent(ViewEventPort::onStopHireEBike);
        this.hirePanel.setCredits(this.eventPort.get().credits());
        TimedMessageDialog.showTimedMessage(this, "Stop ride ebike", 500);
    }

    @Override
    public boolean canHireEBike() {
        final boolean canHire = this.eventPort.map(ViewEventPort::canHireEBike).orElse(false);
        if (!canHire) {
            TimedMessageDialog.showTimedMessage(this, "Have already ebike", 500);
        }
        return canHire;
    }

    @Override
    public void onClickLogout() {
        this.changePanel(this.loginPanel);
        this.eventPort.ifPresent(ViewEventPort::onLogout);
    }
}
