package framework.view;

import javax.swing.*;
import java.awt.*;

public class HirePanel extends JPanel {
    public static final String LABEL_USERNAME = "Username: ";
    public static final String LABEL_CREDITS = "  Credits: ";
    public static final String LOGOUT = "Logout";
    public static final String CURRENCY = "€";
    public static final String HIRE = "Hire";
    public static final String ADD_CREDITS = "Add Credits";

    private final ListenerHireEvent listenerHireEvent;
    private final JComboBox<String> idDisponibiliComboBox;
    private final JLabel usernameValue;
    private final JLabel creditsValue;
    private final JTextField creditsField;

    public HirePanel(final ListenerHireEvent listenerHireEvent) {
        this.setSize(400, 200);
        this.listenerHireEvent = listenerHireEvent;

        // Creazione del pannello principale
        this.setLayout(new BorderLayout());

        final JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        this.usernameValue = new JLabel(" ");
        this.creditsValue = new JLabel(CURRENCY + " ");
        this.creditsField = new JTextField(10);
        final JLabel usernameLabel = new JLabel(LABEL_USERNAME);
        final JLabel creditsLabel = new JLabel(LABEL_CREDITS);

        final JButton addCreditsButton = new JButton(ADD_CREDITS);
        addCreditsButton.addActionListener(e -> this.onClickAddCredits());

        final JButton hireButton = new JButton(HIRE);
        this.idDisponibiliComboBox = new JComboBox<>(new String[]{"ID1", "ID2", "ID3"});
        this.idDisponibiliComboBox.setVisible(false);

        final JButton signOutButton = new JButton(LOGOUT);
        signOutButton.addActionListener(e -> this.onClickLogout());

        hireButton.addActionListener(e -> this.idDisponibiliComboBox.setVisible(true));

        this.idDisponibiliComboBox.addActionListener(e -> this.onClickHire());

        // Aggiunta componenti al pannello superiore
        topPanel.add(usernameLabel);
        topPanel.add(this.usernameValue);
        topPanel.add(creditsLabel);
        topPanel.add(this.creditsValue);
        topPanel.add(addCreditsButton);
        topPanel.add(this.creditsField);
        topPanel.add(signOutButton);
        topPanel.add(hireButton);
        topPanel.add(this.idDisponibiliComboBox);

        this.add(topPanel, BorderLayout.NORTH);
    }

    private void onClickAddCredits() {
        final String credits = this.creditsField.getText();
        TimedMessageDialog.showTimedMessage(
                this.listenerHireEvent.onClickAddCredits(credits), 1000);
    }

    private void onClickHire() {
        final String selectedID = (String) this.idDisponibiliComboBox.getSelectedItem();
        this.idDisponibiliComboBox.setVisible(false); // Nasconde il combo box
        if (selectedID != null) {
            this.listenerHireEvent.onClickHire(selectedID);
            TimedMessageDialog.showTimedMessage("Hai selezionato la bici con ID: " + selectedID, 1000);
        }
    }

    private void onClickLogout() {
        this.listenerHireEvent.onClickLogout();
    }

    public void setUserData(final String username, final float credits) {
        this.usernameValue.setText(username);
        this.creditsValue.setText(CURRENCY + credits);
    }

    public void setCredits(final float credits) {
        this.creditsValue.setText(CURRENCY + credits);
    }
}
