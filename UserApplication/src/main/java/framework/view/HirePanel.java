package framework.view;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HirePanel extends JPanel {
    public static final String LABEL_USERNAME = "Username: ";
    public static final String LABEL_CREDITS = "  Credits: ";
    public static final String LOGOUT = "Logout";
    public static final String CURRENCY = "€";
    public static final String HIRE = "Hire";
    public static final String ADD_CREDITS = "Add Credits";

    private final ListenerHireEvent listenerHireEvent;
    private final JLabel usernameValue;
    private final JLabel creditsValue;
    private final JTextField creditsField;
    private final JButton hireButton;

    private final JPopupMenu eBikesIdFreePopupMenu;
    private final JScrollPane scrollPane;

    public HirePanel(final ListenerHireEvent listenerHireEvent) {
        this.setSize(800, 200);
        this.setLayout(new BorderLayout());

        this.listenerHireEvent = listenerHireEvent;

        this.usernameValue = new JLabel(" ");
        this.creditsValue = new JLabel(CURRENCY + " ");
        this.creditsField = new JTextField(10);
        final JLabel usernameLabel = new JLabel(LABEL_USERNAME);
        final JLabel creditsLabel = new JLabel(LABEL_CREDITS);

        final JButton addCreditsButton = new JButton(ADD_CREDITS);
        addCreditsButton.addActionListener(e -> this.onClickAddCredits());

        this.hireButton = new JButton(HIRE);
        this.hireButton.addActionListener(this::onClickHireButton);
        this.scrollPane = new JScrollPane();
        this.scrollPane.setPreferredSize(new Dimension(150, 100));  // Impostare una dimensione per limitare la visibilità a 5 elementi
        this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.eBikesIdFreePopupMenu = new JPopupMenu();
        this.eBikesIdFreePopupMenu.add(this.scrollPane);


        final JButton signOutButton = new JButton(LOGOUT);
        signOutButton.addActionListener(e -> this.onClickLogout());

        final JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(usernameLabel);
        topPanel.add(this.usernameValue);
        topPanel.add(creditsLabel);
        topPanel.add(this.creditsValue);
        topPanel.add(addCreditsButton);
        topPanel.add(this.creditsField);
        topPanel.add(signOutButton);
        topPanel.add(this.hireButton);
        topPanel.add(this.eBikesIdFreePopupMenu);

        this.add(topPanel, BorderLayout.NORTH);
    }

    private void onClickAddCredits() {
        final String credits = this.creditsField.getText();
        TimedMessageDialog.showTimedMessage(
                this.listenerHireEvent.onClickAddCredits(credits), 500);
    }

    private void onClickHireButton(final ActionEvent event) {
        if (!this.listenerHireEvent.canHireEBike()) {
            TimedMessageDialog.showTimedMessage("Have already ebike", 500);
            return;
        }

        this.eBikesIdFreePopupMenu.show(this, this.hireButton.getX(), this.hireButton.getY());
        final String[] eBikesIdFree = this.listenerHireEvent.freeEBikes().toArray(new String[0]);
        final JList<String> list = new JList<>(eBikesIdFree);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.scrollPane.setViewportView(list);
        this.eBikesIdFreePopupMenu.setVisible(true);
        list.addListSelectionListener(e -> {
            final String eBikeId = list.getSelectedValue();
            TimedMessageDialog.showTimedMessage(this.listenerHireEvent.onClickHire(eBikeId), 500);
        });
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
