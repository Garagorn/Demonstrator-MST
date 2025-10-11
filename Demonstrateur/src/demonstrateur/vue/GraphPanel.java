package demonstrateur.vue;

import demonstrateur.modele.*;
import javax.swing.*;
import java.awt.*;

public class GraphPanel extends JPanel {
    private Graphe graphe;

    public void setGraphe(Graphe g) {
        this.graphe = g;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graphe == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));

        // Dessiner les arêtes
        g2.setColor(Color.GRAY);
        for (Arete a : graphe.getAretes()) {
            int x1 = a.getU().getX();
            int y1 = a.getU().getY();
            int x2 = a.getV().getX();
            int y2 = a.getV().getY();
            g2.drawLine(x1, y1, x2, y2);
            g2.setColor(Color.BLACK);
            g2.drawString(String.valueOf(a.getPoids()), (x1 + x2) / 2, (y1 + y2) / 2);
            g2.setColor(Color.GRAY);
        }

        // Dessiner les sommets
        for (Sommet s : graphe.getSommets()) {
            g2.setColor(Color.ORANGE);
            g2.fillOval(s.getX() - 10, s.getY() - 10, 20, 20);
            g2.setColor(Color.BLACK);
            g2.drawString(s.getNom(), s.getX() - 10, s.getY() - 15);
        }
    }
}