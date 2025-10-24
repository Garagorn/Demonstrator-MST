package demonstrateur.vue;

import demonstrateur.modele.*;
import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class GraphPanel extends JPanel {
    private Graphe graphe;
    private Set<Arete> highlightedArretes = new HashSet<>();
    private String titre = "";
    private Color highlightColor = Color.GREEN; // par défaut (Prim)
    private String infoExecution = "";

    public void setGraphe(Graphe g, String titre) {
        this.graphe = g;
        this.titre = titre;
        this.highlightedArretes.clear();
        repaint();
    }

    public void setHighlightArbre(Arbre arbre, Color color) {
        this.highlightedArretes.clear();
        this.highlightedArretes.addAll(arbre.getAretes());
        this.highlightColor = color;
        repaint();
    }

    public void setExecutionInfo(String info) {
        this.infoExecution = info;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graphe == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));

        // Titre
        g2.setColor(Color.BLACK);
        g2.drawString(titre, 10, 15);

        // 1️⃣ Dessiner toutes les arêtes en gris clair
        g2.setColor(Color.LIGHT_GRAY);
        for (Arete a : graphe.getAretes()) {
            int x1 = a.getU().getX(), y1 = a.getU().getY();
            int x2 = a.getV().getX(), y2 = a.getV().getY();
            g2.drawLine(x1, y1, x2, y2);
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(String.valueOf(a.getPoids()), (x1 + x2) / 2, (y1 + y2) / 2);
            g2.setColor(Color.LIGHT_GRAY);
        }

        // 2️⃣ Dessiner les arêtes de l’arbre en couleur
        g2.setColor(highlightColor);
        g2.setStroke(new BasicStroke(3));
        for (Arete a : highlightedArretes) {
            int x1 = a.getU().getX(), y1 = a.getU().getY();
            int x2 = a.getV().getX(), y2 = a.getV().getY();
            g2.drawLine(x1, y1, x2, y2);
        }

        // 3️⃣ Dessiner les sommets
        for (Sommet s : graphe.getSommets()) {
            g2.setColor(Color.ORANGE);
            g2.fillOval(s.getX() - 10, s.getY() - 10, 20, 20);
            g2.setColor(Color.BLACK);
            g2.drawString(s.getNom(), s.getX() - 10, s.getY() - 15);
        }

        if (!infoExecution.isEmpty()) {
            g.drawString(infoExecution, 10, getHeight() - 10);
        }

    }
}
