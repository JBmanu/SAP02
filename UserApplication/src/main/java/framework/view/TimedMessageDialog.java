package framework.view;

import javax.swing.*;
import java.awt.*;

public final class TimedMessageDialog {

    public static void showTimedMessage(final String message, final int milliseconds) {
        SwingUtilities.invokeLater(() -> createTimedMessage(message, milliseconds));
    }

    private static void createTimedMessage(final String message, final int milliseconds) {
        final JDialog dialog = new JDialog();
        dialog.setUndecorated(true);
        dialog.setLayout(new BorderLayout());

        final JPanel roundedPanel = new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                final Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255));
                g2d.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 30, 30);
                g2d.setColor(new Color(100, 149, 237)); 
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 30, 30);
            }
        };

        roundedPanel.setOpaque(false); // Rende trasparente il pannello per mostrare solo il bordo arrotondato
        roundedPanel.setLayout(new BorderLayout());
        roundedPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20)); // Margini interni

        // Creazione del messaggio al centro del pannello
        final JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        roundedPanel.add(messageLabel, BorderLayout.CENTER);

        // Aggiunge il pannello arrotondato al dialogo
        dialog.add(roundedPanel, BorderLayout.CENTER);
//        dialog.setSize(300, 120); // Dimensioni del dialogo
        dialog.pack();
        dialog.setLocationRelativeTo(null);// Centra il dialogo sullo schermo

        // Timer per chiudere il dialogo dopo un certo tempo
        final Timer timer = new Timer(milliseconds, e -> {
            dialog.dispose(); // Chiude il dialogo
        });
        timer.setRepeats(false); // Il timer deve essere eseguito solo una volta
        timer.start();

        dialog.setVisible(true);
    }
}
