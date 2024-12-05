package framework.view;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final ListenerHireEvent listenerHireEvent;

    public LoginPanel(final ListenerHireEvent listenerHireEvent) {
        this.setSize(400, 300);  // Dimensioni della finestra
        this.listenerHireEvent = listenerHireEvent;

        // Pannello principale con BoxLayout verticale
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));  // Margini

        // Etichetta e campo di testo per l'username
        final JLabel usernameLabel = new JLabel("Username:");
        this.usernameField = new JTextField(15);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));  // Allarga il campo al massimo

        // Etichetta e campo di testo per la password
        final JLabel passwordLabel = new JLabel("Password:");
        this.passwordField = new JPasswordField(15);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));  // Allarga il campo al massimo

        // Pulsanti per Sign In e Sign Up
        final JButton signInButton = new JButton("Sign In");
        final JButton signUpButton = new JButton("Sign Up");
        signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Spaziatura tra gli elementi
        panel.add(usernameLabel);
        panel.add(Box.createVerticalStrut(5));  // Spazio verticale tra label e campo
        panel.add(this.usernameField);
        panel.add(Box.createVerticalStrut(10));  // Spazio verticale tra campi
        panel.add(passwordLabel);
        panel.add(Box.createVerticalStrut(5));  // Spazio verticale tra label e campo
        panel.add(this.passwordField);
        panel.add(Box.createVerticalStrut(15));  // Spazio verticale tra campi e pulsanti
        panel.add(signInButton);
        panel.add(Box.createVerticalStrut(10));  // Spazio verticale tra i pulsanti
        panel.add(signUpButton);

        this.add(panel);
        this.setVisible(true);

        signUpButton.addActionListener(e -> this.clickSignUp());
        signInButton.addActionListener(e -> this.clickSignIn());
    }

    private void clickSignUp() {
        SwingUtilities.invokeLater(() -> {
            final String username = this.usernameField.getText();
            final String password = new String(this.passwordField.getPassword());
            TimedMessageDialog.showTimedMessage(
                    this.listenerHireEvent.onClickSignUp(username, password), 1000);
        });
    }

    private void clickSignIn() {
        SwingUtilities.invokeLater(() -> {
            final String username = this.usernameField.getText();
            final String password = new String(this.passwordField.getPassword());
            TimedMessageDialog.showTimedMessage(
                    this.listenerHireEvent.onClickSignIn(username, password), 1000);
        });
    }
}
