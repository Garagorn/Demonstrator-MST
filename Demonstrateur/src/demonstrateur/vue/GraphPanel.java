package demonstrateur.vue;

import demonstrateur.modele.Arbre;
import demonstrateur.modele.Arete;
import demonstrateur.modele.Graphe;
import demonstrateur.modele.Sommet;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;



public class GraphPanel extends JPanel {
    private Sommet sommetDepart;

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

    private PriorityQueuePanel primQueuePanel;
    private SortedEdgeTablePanel kruskalEdgePanel;
    private List<Arete> currentQueue = new ArrayList<>();
    private List<Arete> fullEdgeList = new ArrayList<>();
    
    public void setPrimQueuePanel(PriorityQueuePanel panel) {
        this.primQueuePanel = panel;
    }

    public void setKruskalEdgePanel(SortedEdgeTablePanel panel) {
        this.kruskalEdgePanel = panel;
    }

    public void setCurrentQueue(List<Arete> queue) {
        this.currentQueue = queue;
    }

    public void setFullEdgeList(List<Arete> edges) {
        this.fullEdgeList = edges;
    }

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
        this.sommetDepart = graphe.getSommets().get(0); // ou celui que tu veux
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
                    if (primQueuePanel != null) {
                        primQueuePanel.clearCurrentEdge();
                    }
                }
                
                // Nouvelle arête courante
                currentEdge = sequence.get(currentIndex);
                
                if (primQueuePanel != null && graphe != null && sommetDepart != null) {
                    // Recalculer la file en tenant compte des arêtes déjà ajoutées
                    List<Arete> updatedQueue = graphe.primPriorityQueueAtStep(
                        sommetDepart, 
                        highlightedEdges
                    );
                    
                    // Retirer l'arête courante de la file
                    updatedQueue.remove(currentEdge);
                    
                    primQueuePanel.updateQueue(updatedQueue);
                    primQueuePanel.setCurrentEdge(currentEdge);
                }
                
                if (kruskalEdgePanel != null) {
                    kruskalEdgePanel.updateEdges(fullEdgeList, highlightedEdges);
                }

                currentIndex++;
                updateStepInfo();
                repaint();
                
                if (currentIndex >= sequence.size()) {
                    Timer finalTimer = new Timer(delayMs, evt -> {
                        if (currentEdge != null) {
                            highlightedEdges.add(currentEdge);
                            currentEdge = null;
                            if (primQueuePanel != null) {
                                primQueuePanel.clear(); // File vide à la fin
                            }
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
        
        if (primQueuePanel != null && graphe != null && sommetDepart != null) {
            List<Arete> initialQueue = graphe.primPriorityQueueAtStep(sommetDepart, new ArrayList<>());
            primQueuePanel.updateQueue(initialQueue);
        }
        
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
    
    private void calculateScaling(Graphics2D g2) {
        if (graphe == null || graphe.getSommets().isEmpty()) return;
        
        // Trouver les bornes du graphe
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        
        for (Sommet s : graphe.getSommets()) {
            minX = Math.min(minX, s.getX());
            maxX = Math.max(maxX, s.getX());
            minY = Math.min(minY, s.getY());
            maxY = Math.max(maxY, s.getY());
        }
        
        int graphWidth = maxX - minX;
        int graphHeight = maxY - minY;
        
        // Marges
        int margin = 40;
        int availableWidth = getWidth() - 2 * margin;
        int availableHeight = getHeight() - 2 * margin - 60; // 60 pour l'info en bas
        
        // Calculer le facteur d'échelle
        double scaleX = (double) availableWidth / graphWidth;
        double scaleY = (double) availableHeight / graphHeight;
        double scale = Math.min(scaleX, scaleY);
        
        // Appliquer la transformation
        g2.translate(margin - minX * scale, margin - minY * scale);
        g2.scale(scale, scale);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graphe == null) return;
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // --- Titre (avant transformation) ---
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString(titre, 10, 20);
        
        // Sauvegarder l'état graphique
        Graphics2D g2Graph = (Graphics2D) g2.create();
        
        // Appliquer la mise à l'échelle
        calculateScaling(g2Graph);
        
        // --- Dessin des arêtes normales ---
        g2Graph.setColor(Color.LIGHT_GRAY);
        g2Graph.setStroke(new BasicStroke(EDGE_WIDTH));
        for (Arete a : graphe.getAretes()) {
            drawEdge(g2Graph, a, Color.LIGHT_GRAY, EDGE_WIDTH, false);
        }
        
        // --- Dessin des arêtes déjà ajoutées au MST ---
        g2Graph.setColor(highlightColor);
        g2Graph.setStroke(new BasicStroke(HIGHLIGHT_WIDTH));
        for (Arete a : highlightedEdges) {
            drawEdge(g2Graph, a, highlightColor, HIGHLIGHT_WIDTH, true);
        }
        
        // --- Dessin de l'arête en cours d'ajout ---
        if (currentEdge != null) {
            drawCurrentEdge(g2Graph, currentEdge);
        }
        
        // --- Dessin des sommets ---
        drawVertices(g2Graph);
        
        // Libérer le contexte graphique transformé
        g2Graph.dispose();
        
        // --- Informations d'exécution (après transformation, sur g2 original) ---
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

    public void refreshAuxiliaryPanels() {
        if (primQueuePanel != null) {
            primQueuePanel.updateQueue(currentQueue);
        }
        if (kruskalEdgePanel != null) {
            kruskalEdgePanel.updateEdges(fullEdgeList, highlightedEdges);
        }
    }

}