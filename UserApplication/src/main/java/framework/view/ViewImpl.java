package framework.view;

import adapter.UserViewEventPort;
import adapter.View;

import javax.swing.*;
import java.awt.*;
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
        this.getContentPane().removeAll();
        this.add(panel, BorderLayout.CENTER);
        this.setSize(panel.getSize());
        this.setLocationRelativeTo(null);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void setEventPort(final UserViewEventPort eventPort) {
        this.eventPort = Optional.ofNullable(eventPort);
    }

    @Override
    public String onClickSignUp(final String username, final String password) {
        final String message = this.eventPort.isPresent() ?
                this.eventPort.get().onSignUp(username, password) : NOT_CONNECTED;
        if (message.equals(UserViewEventPort.CORRECT)) {
            this.changePanel(this.hirePanel);
        }
        return message;
    }

    @Override
    public String onClickSignIn(final String username, final String password) {
        final String message = this.eventPort.isPresent() ?
                this.eventPort.get().onSignIn(username, password) : NOT_CONNECTED;
        if (message.equals(UserViewEventPort.CORRECT)) {
            this.changePanel(this.hirePanel);
        }
        return message;
    }
}
