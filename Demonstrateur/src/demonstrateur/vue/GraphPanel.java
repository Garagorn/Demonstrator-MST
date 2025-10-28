package demonstrateur.vue;

import demonstrateur.modele.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

public class GraphPanel extends JPanel {
    private Graphe graphe;
    private String titre = "";
    private java.util.List<Arete> highlightedEdges = new ArrayList<>();
    private Color highlightColor = Color.GREEN;
    private String executionInfo = "";
    private boolean isInstantMode = false;

    // Pour l’animation pas à pas
    private java.util.List<Arete> sequence = new ArrayList<>();
    private int currentIndex = 0;
    private javax.swing.Timer timer;

    public GraphPanel() {
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.WHITE);
    }

    public void setGraphe(Graphe g, String titre) {
        this.graphe = g;
        this.titre = titre;
        repaint();
    }

    public void setHighlightArbre(Arbre arbre, Color color) {
        this.highlightedEdges = new ArrayList<>(arbre.getAretes());
        this.highlightColor = color;
        repaint();
    }

    public void setExecutionInfo(String info, boolean isInstant) {
        this.executionInfo = info;
        this.isInstantMode = isInstant;
        repaint();
    }

    public void setSequence(java.util.List<Arete> seq) {
        this.sequence = seq;
        this.currentIndex = 0;
    }

    public void playSequence(int delayMs, Color color) {
        if (sequence == null || sequence.isEmpty()) return;
        if (timer != null && timer.isRunning()) timer.stop();
        this.currentIndex = 0;
        this.highlightColor = color;
        highlightedEdges.clear();
        timer = new javax.swing.Timer(delayMs, e -> {
            if (currentIndex < sequence.size()) {
                Arete a = sequence.get(currentIndex);
                highlightedEdges.add(a);
                currentIndex++;
                repaint();
            } else {
                ((javax.swing.Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    public void resetAnimation() {
        if (timer != null && timer.isRunning()) timer.stop();
        highlightedEdges.clear();
        currentIndex = 0;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graphe == null) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(1.5f));

        // --- Dessin des arêtes normales ---
        g2.setColor(Color.LIGHT_GRAY);
        for (Arete a : graphe.getAretes()) {
            int x1 = a.getU().getX(), y1 = a.getU().getY();
            int x2 = a.getV().getX(), y2 = a.getV().getY();
            g2.drawLine(x1, y1, x2, y2);
            // Poids
            String p = String.valueOf(a.getPoids());
            int px = (x1 + x2) / 2, py = (y1 + y2) / 2;
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(p, px, py);
            g2.setColor(Color.LIGHT_GRAY);
        }

        // --- Dessin des arêtes surlignées (MST / étape) ---
        g2.setColor(highlightColor);
        g2.setStroke(new BasicStroke(3f));
        for (Arete a : highlightedEdges) {
            int x1 = a.getU().getX(), y1 = a.getU().getY();
            int x2 = a.getV().getX(), y2 = a.getV().getY();
            g2.drawLine(x1, y1, x2, y2);
        }

        // --- Dessin des sommets ---
        for (Sommet s : graphe.getSommets()) {
            int x = s.getX(), y = s.getY();
            g2.setColor(Color.ORANGE);
            g2.fillOval(x - 8, y - 8, 16, 16);
            g2.setColor(Color.BLACK);
            g2.drawOval(x - 8, y - 8, 16, 16);
            g2.drawString(s.getNom(), x + 10, y);
        }

        // --- Titre et infos ---
        g2.setColor(Color.BLACK);
        g2.drawString(titre, 10, 20);
        String infoToShow = isInstantMode ? executionInfo : executionInfo.split("\n")[0];
        g2.drawString(infoToShow, 10, getHeight() - 10);
    }
}
