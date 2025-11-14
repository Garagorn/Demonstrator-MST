package demonstrateur.vue;

import demonstrateur.modele.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;

public class GraphPanel extends JPanel {
    // Constantes pour l'affichage
    private static final int VERTEX_RADIUS = 8;
    private static final int VERTEX_DIAMETER = 16;
    private static final float EDGE_WIDTH = 1.5f;
    private static final float HIGHLIGHT_WIDTH = 3f;
    private static final float CURRENT_EDGE_WIDTH = 4.5f;
    
    private Graphe graphe;
    private String titre = "";
    private java.util.List<Arete> highlightedEdges = new ArrayList<>();
    private Arete currentEdge = null; // Arête en cours d'ajout
    private Color highlightColor = Color.GREEN;
    private Color currentEdgeColor = Color.ORANGE; // Couleur pour l'arête actuelle
    private String executionInfo = "";
    private boolean isInstantMode = false;
    
    // Pour l'animation pas à pas
    private java.util.List<Arete> sequence = new ArrayList<>();
    private int currentIndex = 0;
    private javax.swing.Timer timer;
    private boolean isPaused = false;
    
    public GraphPanel() {
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }
    
    public void setGraphe(Graphe g, String titre) {
        this.graphe = g;
        this.titre = titre;
        this.highlightedEdges.clear();
        this.currentEdge = null;
        this.currentIndex = 0;
        repaint();
    }
    
    public void setHighlightArbre(Arbre arbre, Color color) {
        this.highlightedEdges = new ArrayList<>(arbre.getAretes());
        this.currentEdge = null;
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
        this.highlightedEdges.clear();
        this.currentEdge = null;
    }
    
    public void playSequence(int delayMs, Color color) {
        if (sequence == null || sequence.isEmpty()) return;
        
        stopAnimation();
        
        this.currentIndex = 0;
        this.highlightColor = color;
        this.highlightedEdges.clear();
        this.currentEdge = null;
        this.isPaused = false;
        
        timer = new javax.swing.Timer(delayMs, e -> {
            if (!isPaused && currentIndex < sequence.size()) {
                // L'arête précédente devient permanente
                if (currentEdge != null) {
                    highlightedEdges.add(currentEdge);
                }
                
                // Nouvelle arête courante
                currentEdge = sequence.get(currentIndex);
                currentIndex++;
                
                // Mise à jour de l'info d'étape
                updateStepInfo();
                repaint();
                
                // Si c'est la dernière arête, on l'ajoute définitivement après un délai
                if (currentIndex >= sequence.size()) {
                    Timer finalTimer = new Timer(delayMs, evt -> {
                        if (currentEdge != null) {
                            highlightedEdges.add(currentEdge);
                            currentEdge = null;
                            repaint();
                        }
                        ((Timer) evt.getSource()).stop();
                    });
                    finalTimer.setRepeats(false);
                    finalTimer.start();
                    ((javax.swing.Timer) e.getSource()).stop();
                }
            } else if (currentIndex >= sequence.size()) {
                ((javax.swing.Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }
    
    private void updateStepInfo() {
        if (currentEdge != null) {
            String baseInfo = executionInfo.split("\n")[0]; // Garde le poids total
            String stepInfo = String.format("Étape %d/%d : Ajout de %s--%s (poids: %d)", 
                currentIndex, 
                sequence.size(),
                currentEdge.getU().getNom(),
                currentEdge.getV().getNom(),
                currentEdge.getPoids());
            executionInfo = baseInfo + "\n" + stepInfo;
        }
    }
    
    public void pauseAnimation() {
        isPaused = true;
    }
    
    public void resumeAnimation() {
        isPaused = false;
    }
    
    public void stopAnimation() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        isPaused = false;
    }
    
    public void resetAnimation() {
        stopAnimation();
        highlightedEdges.clear();
        currentEdge = null;
        currentIndex = 0;
        repaint();
    }
    
    public boolean isAnimating() {
        return timer != null && timer.isRunning();
    }
    
    public boolean isPaused() {
        return isPaused;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graphe == null) return;
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // --- Dessin des arêtes normales (en gris clair) ---
        g2.setColor(Color.LIGHT_GRAY);
        g2.setStroke(new BasicStroke(EDGE_WIDTH));
        for (Arete a : graphe.getAretes()) {
            drawEdge(g2, a, Color.LIGHT_GRAY, EDGE_WIDTH, false);
        }
        
        // --- Dessin des arêtes déjà ajoutées au MST ---
        g2.setColor(highlightColor);
        g2.setStroke(new BasicStroke(HIGHLIGHT_WIDTH));
        for (Arete a : highlightedEdges) {
            drawEdge(g2, a, highlightColor, HIGHLIGHT_WIDTH, true);
        }
        
        // --- Dessin de l'arête en cours d'ajout (avec effet visuel spécial) ---
        if (currentEdge != null) {
            drawCurrentEdge(g2, currentEdge);
        }
        
        // --- Dessin des sommets ---
        drawVertices(g2);
        
        // --- Titre ---
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString(titre, 10, 20);
        
        // --- Informations d'exécution ---
        drawExecutionInfo(g2);
    }
    
    private void drawEdge(Graphics2D g2, Arete a, Color color, float width, boolean showWeight) {
        int x1 = a.getU().getX(), y1 = a.getU().getY();
        int x2 = a.getV().getX(), y2 = a.getV().getY();
        
        g2.setColor(color);
        g2.setStroke(new BasicStroke(width));
        g2.drawLine(x1, y1, x2, y2);
        
        // Affichage du poids
        if (showWeight) {
            String p = String.valueOf(a.getPoids());
            int px = (x1 + x2) / 2;
            int py = (y1 + y2) / 2;
            
            // Fond blanc pour le poids
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(p);
            g2.setColor(Color.WHITE);
            g2.fillRect(px - textWidth/2 - 2, py - fm.getHeight()/2, textWidth + 4, fm.getHeight());
            
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 11));
            g2.drawString(p, px - textWidth/2, py + fm.getAscent()/2);
        }
    }
    
    private void drawCurrentEdge(Graphics2D g2, Arete a) {
        int x1 = a.getU().getX(), y1 = a.getU().getY();
        int x2 = a.getV().getX(), y2 = a.getV().getY();
        
        // Effet de pulsation avec un trait plus épais
        g2.setColor(currentEdgeColor);
        g2.setStroke(new BasicStroke(CURRENT_EDGE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(x1, y1, x2, y2);
        
        // Halo autour de l'arête
        g2.setColor(new Color(255, 165, 0, 50)); // Orange transparent
        g2.setStroke(new BasicStroke(CURRENT_EDGE_WIDTH + 4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(x1, y1, x2, y2);
        
        // Affichage du poids en surbrillance
        String p = String.valueOf(a.getPoids());
        int px = (x1 + x2) / 2;
        int py = (y1 + y2) / 2;
        
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(p);
        
        // Fond jaune pour l'arête courante
        g2.setColor(new Color(255, 255, 0, 200));
        g2.fillRoundRect(px - textWidth/2 - 4, py - fm.getHeight()/2 - 2, 
                         textWidth + 8, fm.getHeight() + 4, 5, 5);
        
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString(p, px - textWidth/2, py + fm.getAscent()/2);
    }
    
    private void drawVertices(Graphics2D g2) {
        for (Sommet s : graphe.getSommets()) {
            int x = s.getX(), y = s.getY();
            
            // Vérifier si le sommet fait partie de l'arête courante
            boolean isCurrentVertex = false;
            if (currentEdge != null) {
                isCurrentVertex = currentEdge.getU().equals(s) || currentEdge.getV().equals(s);
            }
            
            // Ombre portée
            g2.setColor(new Color(0, 0, 0, 50));
            g2.fillOval(x - VERTEX_RADIUS + 2, y - VERTEX_RADIUS + 2, VERTEX_DIAMETER, VERTEX_DIAMETER);
            
            // Sommet
            if (isCurrentVertex) {
                g2.setColor(new Color(255, 200, 0)); // Jaune doré pour les sommets de l'arête courante
            } else {
                g2.setColor(Color.ORANGE);
            }
            g2.fillOval(x - VERTEX_RADIUS, y - VERTEX_RADIUS, VERTEX_DIAMETER, VERTEX_DIAMETER);
            
            // Contour
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(isCurrentVertex ? 2.5f : 1.5f));
            g2.drawOval(x - VERTEX_RADIUS, y - VERTEX_RADIUS, VERTEX_DIAMETER, VERTEX_DIAMETER);
            
            // Nom du sommet
            g2.setFont(new Font("Arial", Font.BOLD, 10));
            g2.drawString(s.getNom(), x + 12, y + 4);
        }
    }
    
    private void drawExecutionInfo(Graphics2D g2) {
        if (executionInfo.isEmpty()) return;
        
        String[] lines = executionInfo.split("\n");
        int lineHeight = 18;
        int padding = 10;
        int boxHeight = lines.length * lineHeight + padding * 2;
        int boxWidth = 300;
        
        // Fond semi-transparent
        g2.setColor(new Color(255, 255, 255, 230));
        g2.fillRoundRect(5, getHeight() - boxHeight - 5, boxWidth, boxHeight, 10, 10);
        
        // Bordure
        g2.setColor(new Color(100, 100, 100, 150));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(5, getHeight() - boxHeight - 5, boxWidth, boxHeight, 10, 10);
        
        // Texte
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        int yOffset = getHeight() - boxHeight + padding + 8;
        for (int i = 0; i < lines.length; i++) {
            // Mettre en gras la ligne de l'étape courante
            if (i > 0 && lines[i].startsWith("Étape")) {
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                g2.setColor(currentEdgeColor);
            } else {
                g2.setFont(new Font("Arial", Font.PLAIN, 12));
                g2.setColor(Color.BLACK);
            }
            g2.drawString(lines[i], 15, yOffset);
            yOffset += lineHeight;
        }
    }
}