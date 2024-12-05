package framework.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class EBikePanel extends JPanel {

    private final JLabel valueId;
    private final JLabel valuePosition;
    private final JLabel valueBattery;
    private final JLabel valueStatus;

    public EBikePanel() {
        this.setSize(400, 200);

        this.setLayout(new GridLayout(4, 2, 10, 10));  // Layout a griglia con 4 righe e 2 colonne

        this.valueId = new JLabel("");
        this.valuePosition = new JLabel("");
        this.valueBattery = new JLabel("");
        this.valueStatus = new JLabel("");

        final JLabel labelId = new JLabel("E-Bike ID:");
        final JLabel labelPosition = new JLabel("Position:");
        final JLabel labelBattery = new JLabel("Battery Level:");
        final JLabel labelStatus = new JLabel("State:");

        this.add(labelId);
        this.add(this.valueId);
        this.add(labelPosition);
        this.add(this.valuePosition);
        this.add(labelBattery);
        this.add(this.valueBattery);
        this.add(labelStatus);
        this.add(this.valueStatus);
    }

    public void setId(final String id) {
        this.valueId.setText(id);
    }

    public void setPosition(final Point2D position) {
        this.valuePosition.setText("X: " + position.getX() + ", Y: " + position.getY());
    }

    public void setBattery(final int battery) {
        this.valueBattery.setText(battery + "%");
    }

    public void setStatus(final String status) {
        this.valueStatus.setText(status);
    }

}
