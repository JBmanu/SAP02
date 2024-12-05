package framework.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.List;

public class HirePanel extends JPanel {
    public static final String LABEL_USERNAME = "Username: ";
    public static final String LABEL_CREDITS = "  Credits: ";
    public static final String LOGOUT = "Logout";
    public static final String CURRENCY = "€";
    public static final String HIRE = "Hire";
    public static final String ADD_CREDITS = "Add Credits";

    private final ListenerHireEvent listenerHireEvent;
    private final JComboBox<String> eBikesIdFreeBox;
    private final JLabel usernameValue;
    private final JLabel creditsValue;
    private final JTextField creditsField;
    private boolean loadEBikesIdFree;

    public HirePanel(final ListenerHireEvent listenerHireEvent) {
        this.setSize(800, 200);
        this.listenerHireEvent = listenerHireEvent;
        this.loadEBikesIdFree = false;

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
        this.eBikesIdFreeBox = new JComboBox<>(new String[]{ });
        this.eBikesIdFreeBox.setVisible(false);
        hireButton.addActionListener(this::onClickHireButton);
        this.eBikesIdFreeBox.addItemListener(this::onClickHireEBike);

        final JButton signOutButton = new JButton(LOGOUT);
        signOutButton.addActionListener(e -> this.onClickLogout());

        topPanel.add(usernameLabel);
        topPanel.add(this.usernameValue);
        topPanel.add(creditsLabel);
        topPanel.add(this.creditsValue);
        topPanel.add(addCreditsButton);
        topPanel.add(this.creditsField);
        topPanel.add(signOutButton);
        topPanel.add(hireButton);
        topPanel.add(this.eBikesIdFreeBox);

        this.add(topPanel, BorderLayout.NORTH);
    }

    private void onClickAddCredits() {
        final String credits = this.creditsField.getText();
        TimedMessageDialog.showTimedMessage(
                this.listenerHireEvent.onClickAddCredits(credits), 1000);
    }

    private void onClickHireEBike(final ItemEvent event) {
        if (!this.loadEBikesIdFree) return;
        final String selectedID = (String) event.getItem();
        this.eBikesIdFreeBox.setVisible(false);
        if (selectedID == null) return;
        this.listenerHireEvent.onClickHire(selectedID);
        TimedMessageDialog.showTimedMessage("Hai selezionato la bici con ID: " + selectedID, 1000);
    }

    private void onClickHireButton(final ActionEvent event) {
        this.loadEBikesIdFree = false;
        final List<String> eBikesFree = this.listenerHireEvent.freeEBikes();
        this.eBikesIdFreeBox.removeAllItems();
        eBikesFree.forEach(this.eBikesIdFreeBox::addItem);
        this.eBikesIdFreeBox.setVisible(true);
        this.loadEBikesIdFree = true;
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
