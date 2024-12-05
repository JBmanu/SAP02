package framework.view;

import javax.swing.*;
import java.awt.*;

public class HirePanel extends JPanel {
    private final ListenerHireEvent listenerHireEvent;

    public HirePanel(final ListenerHireEvent listenerHireEvent) {
        this.setSize(400, 200);
        this.listenerHireEvent = listenerHireEvent;

        // Creazione del pannello principale
        this.setLayout(new BorderLayout());

        // Creazione del pannello superiore con FlowLayout centrato
        final JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Aggiunta degli elementi al pannello superiore
        final JLabel usernameLabel = new JLabel("Username: ");
        final JLabel usernameValue = new JLabel("MarioRossi"); // Valore di esempio
        final JLabel creditoLabel = new JLabel("  Credito: ");
        final JLabel creditoValue = new JLabel("€50.00"); // Valore di esempio

        final JButton noleggioButton = new JButton("Noleggia");
        final JComboBox<String> idDisponibiliComboBox = new JComboBox<>(new String[]{"ID1", "ID2", "ID3"});
        idDisponibiliComboBox.setVisible(false); // Nasconde il combo box inizialmente

        // Listener per mostrare la tendina
        noleggioButton.addActionListener(e -> idDisponibiliComboBox.setVisible(true));

        // Listener per nascondere la tendina dopo la selezione
        idDisponibiliComboBox.addActionListener(e -> {
            final String selectedID = (String) idDisponibiliComboBox.getSelectedItem();
            idDisponibiliComboBox.setVisible(false); // Nasconde il combo box
            if (selectedID != null) {
                TimedMessageDialog.showTimedMessage("Hai selezionato la bici con ID: " + selectedID, 1000);
            }
        });

        // Aggiunta componenti al pannello superiore
        topPanel.add(usernameLabel);
        topPanel.add(usernameValue);
        topPanel.add(creditoLabel);
        topPanel.add(creditoValue);
        topPanel.add(noleggioButton);
        topPanel.add(idDisponibiliComboBox);

        // Aggiunta del pannello superiore al pannello principale
        this.add(topPanel, BorderLayout.NORTH);
    }

}
