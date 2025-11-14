package demonstrateur.vue;

import demonstrateur.modele.Arete;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PriorityQueuePanel extends JPanel {
    private List<Arete> currentQueue = new ArrayList<>();
    private Arete currentEdge = null; // L'arête en cours de traitement
    private Arete lastProcessedEdge = null; // Dernière arête traitée
    
    private static final Color BACKGROUND_COLOR = new Color(240, 248, 255);
    private static final Color CELL_COLOR = new Color(255, 255, 255);
    private static final Color CURRENT_COLOR = new Color(255, 215, 0); // Or
    private static final Color PROCESSED_COLOR = new Color(144, 238, 144); // Vert clair
    private static final int CELL_WIDTH = 90;
    private static final int CELL_HEIGHT = 55;
    private static final int ARROW_SIZE = 20;
    
    public PriorityQueuePanel() {
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "File de priorité (Prim) - Arêtes candidates triées par poids",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12)
        ));
        setPreferredSize(new Dimension(700, 120));
    }
    
    /**
     * Met à jour la file avec les nouvelles arêtes candidates
     */
    public void updateQueue(List<Arete> queue) {
        this.currentQueue = new ArrayList<>(queue);
        repaint();
    }
    
    /**
     * Marque une arête comme étant en cours de traitement
     */
    public void setCurrentEdge(Arete edge) {
        this.lastProcessedEdge = this.currentEdge;
        this.currentEdge = edge;
        repaint();
    }
    
    /**
     * Efface l'arête courante (elle a été ajoutée au MST)
     */
    public void clearCurrentEdge() {
        this.lastProcessedEdge = this.currentEdge;
        this.currentEdge = null;
        repaint();
    }
    
    /**
     * Réinitialise la file
     */
    public void clear() {
        this.currentQueue.clear();
        this.currentEdge = null;
        this.lastProcessedEdge = null;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        if (currentQueue.isEmpty() && currentEdge == null) {
            drawEmptyMessage(g2);
            return;
        }
        
        int startX = 15;
        int startY = 35;
        int x = startX;
        
        // Dessiner l'indicateur "Tête de file"
        g2.setColor(new Color(70, 130, 180));
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        g2.drawString("↓ Tête", startX, startY - 5);
        
        // Dessiner l'arête en cours de traitement (sortie de la file)
        if (currentEdge != null) {
            drawProcessingEdge(g2, currentEdge, x, startY);
            x += CELL_WIDTH + ARROW_SIZE + 10;
        }
        
        // Dessiner les arêtes de la file
        int maxVisible = (getWidth() - x - 50) / (CELL_WIDTH + 5);
        int count = Math.min(currentQueue.size(), maxVisible);
        
        for (int i = 0; i < count; i++) {
            Arete a = currentQueue.get(i);
            drawQueueCell(g2, a, x, startY, i == 0);
            x += CELL_WIDTH + 5;
        }
        
        // Indicateur s'il y a plus d'arêtes
        if (currentQueue.size() > count) {
            g2.setColor(Color.GRAY);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("...", x, startY + CELL_HEIGHT / 2);
            
            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            String remaining = "+" + (currentQueue.size() - count) + " arêtes";
            g2.drawString(remaining, x + 5, startY + CELL_HEIGHT / 2 + 15);
        }
        
        // Légende en bas
        drawLegend(g2);
    }
    
    private void drawProcessingEdge(Graphics2D g2, Arete edge, int x, int y) {
        // Animation de sortie : cellule avec halo
        g2.setColor(new Color(255, 215, 0, 100));
        g2.fillRoundRect(x - 3, y - 3, CELL_WIDTH + 6, CELL_HEIGHT + 6, 12, 12);
        
        g2.setColor(CURRENT_COLOR);
        g2.fillRoundRect(x, y, CELL_WIDTH, CELL_HEIGHT, 8, 8);
        
        g2.setColor(new Color(255, 140, 0));
        g2.setStroke(new BasicStroke(3f));
        g2.drawRoundRect(x, y, CELL_WIDTH, CELL_HEIGHT, 8, 8);
        
        // Contenu
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        String edgeText = edge.getU().getNom() + " → " + edge.getV().getNom();
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(edgeText);
        g2.drawString(edgeText, x + (CELL_WIDTH - textWidth) / 2, y + 20);
        
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        String poidsText = "Poids: " + edge.getPoids();
        textWidth = fm.stringWidth(poidsText);
        g2.drawString(poidsText, x + (CELL_WIDTH - textWidth) / 2, y + 38);
        
        // Label "En traitement"
        g2.setFont(new Font("Arial", Font.ITALIC, 9));
        g2.setColor(new Color(255, 140, 0));
        g2.drawString("En traitement", x + 5, y + CELL_HEIGHT + 12);
        
        // Flèche vers la file
        drawArrow(g2, x + CELL_WIDTH, y + CELL_HEIGHT / 2);
    }
    
    private void drawQueueCell(Graphics2D g2, Arete edge, int x, int y, boolean isFirst) {
        // Fond de la cellule
        Color bgColor = CELL_COLOR;
        Color borderColor = Color.GRAY;
        float borderWidth = 1.5f;
        
        if (isFirst) {
            // Première arête : mise en évidence légère
            bgColor = new Color(255, 250, 205); // Jaune très pâle
            borderColor = new Color(255, 200, 0);
            borderWidth = 2f;
        }
        
        g2.setColor(bgColor);
        g2.fillRoundRect(x, y, CELL_WIDTH, CELL_HEIGHT, 6, 6);
        
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(borderWidth));
        g2.drawRoundRect(x, y, CELL_WIDTH, CELL_HEIGHT, 6, 6);
        
        // Contenu
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        String edgeText = edge.getU().getNom() + " → " + edge.getV().getNom();
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(edgeText);
        g2.drawString(edgeText, x + (CELL_WIDTH - textWidth) / 2, y + 20);
        
        g2.setFont(new Font("Arial", Font.BOLD, 10));
        String poidsText = "(" + edge.getPoids() + ")";
        fm = g2.getFontMetrics();
        textWidth = fm.stringWidth(poidsText);
        g2.drawString(poidsText, x + (CELL_WIDTH - textWidth) / 2, y + 36);
        
        // Badge de priorité pour la première arête
        if (isFirst) {
            g2.setColor(new Color(255, 200, 0));
            g2.fillOval(x + CELL_WIDTH - 18, y - 5, 16, 16);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1f));
            g2.drawOval(x + CELL_WIDTH - 18, y - 5, 16, 16);
            g2.setFont(new Font("Arial", Font.BOLD, 10));
            g2.drawString("1", x + CELL_WIDTH - 14, y + 5);
        }
    }
    
    private void drawArrow(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(100, 100, 100));
        g2.setStroke(new BasicStroke(2f));
        
        int arrowLength = ARROW_SIZE;
        g2.drawLine(x, y, x + arrowLength, y);
        
        // Pointe de flèche
        int[] xPoints = {x + arrowLength, x + arrowLength - 6, x + arrowLength - 6};
        int[] yPoints = {y, y - 4, y + 4};
        g2.fillPolygon(xPoints, yPoints, 3);
    }
    
    private void drawEmptyMessage(Graphics2D g2) {
        g2.setColor(Color.GRAY);
        g2.setFont(new Font("Arial", Font.ITALIC, 12));
        String msg = "File vide - Tous les sommets ont été traités";
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(msg)) / 2;
        g2.drawString(msg, x, getHeight() / 2);
    }
    
    private void drawLegend(Graphics2D g2) {
        int legendY = getHeight() - 20;
        int legendX = 15;
        
        g2.setFont(new Font("Arial", Font.PLAIN, 9));
        
        // Légende arête en traitement
        g2.setColor(CURRENT_COLOR);
        g2.fillRect(legendX, legendY, 15, 10);
        g2.setColor(new Color(255, 140, 0));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRect(legendX, legendY, 15, 10);
        g2.setColor(Color.DARK_GRAY);
        g2.drawString("= En traitement", legendX + 20, legendY + 9);
        
        legendX += 120;
        
        // Légende file
        g2.setColor(new Color(255, 250, 205));
        g2.fillRect(legendX, legendY, 15, 10);
        g2.setColor(new Color(255, 200, 0));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRect(legendX, legendY, 15, 10);
        g2.setColor(Color.DARK_GRAY);
        g2.drawString("= Prochaine à traiter", legendX + 20, legendY + 9);
    }
}