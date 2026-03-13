package demonstrateur.vue;

import demonstrateur.modele.Arete;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class SortedEdgeTablePanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable edgeTable;
    private List<Arete> addedEdges;
    private Arete currentEdge = null;
    
    private static final Color HEADER_COLOR = new Color(210, 105, 30);
    private static final Color ADDED_COLOR = new Color(144, 238, 144); // Vert clair
    private static final Color CURRENT_COLOR = new Color(255, 215, 0); // Or
    private static final Color REJECTED_COLOR = new Color(255, 200, 200); // Rouge clair
    private static final Color PENDING_COLOR = new Color(245, 245, 220); // Beige
    
    public SortedEdgeTablePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 250, 240));
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(HEADER_COLOR, 2),
            "Arêtes triées par poids (Kruskal)",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12)
        ));
        
        // Modèle avec colonnes
        tableModel = new DefaultTableModel(
            new Object[]{"U", "V", "Poids", "État"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Toutes les cellules non éditables
            }
        };
        
        edgeTable = new JTable(tableModel);
        
        // Style de la table
        edgeTable.setRowHeight(28);
        edgeTable.setFont(new Font("Arial", Font.PLAIN, 12));
        edgeTable.setGridColor(new Color(200, 200, 200));
        edgeTable.setShowGrid(true);
        edgeTable.setIntercellSpacing(new Dimension(1, 1));
        edgeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Style de l'en-tête
        JTableHeader header = edgeTable.getTableHeader();
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 32));
        
        // Largeur des colonnes
        edgeTable.getColumnModel().getColumn(0).setPreferredWidth(60);  // U
        edgeTable.getColumnModel().getColumn(1).setPreferredWidth(60);  // V
        edgeTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // Poids
        edgeTable.getColumnModel().getColumn(3).setPreferredWidth(120); // État
        
        // Renderer personnalisé pour centrer le texte et colorer les lignes
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column
                );
                
                setHorizontalAlignment(SwingConstants.CENTER);
                
                if (!isSelected) {
                    // Récupérer l'état de la ligne
                    String etat = (String) tableModel.getValueAt(row, 3);
                    
                    if (etat.contains("En cours")) {
                        c.setBackground(CURRENT_COLOR);
                        c.setForeground(Color.BLACK);
                        setFont(new Font("Arial", Font.BOLD, 12));
                    } else if (etat.contains("Ajoutée")) {
                        c.setBackground(ADDED_COLOR);
                        c.setForeground(new Color(0, 100, 0)); // Vert foncé
                        setFont(new Font("Arial", Font.PLAIN, 12));
                    } else if (etat.contains("Rejetée")) {
                        c.setBackground(REJECTED_COLOR);
                        c.setForeground(new Color(139, 0, 0)); // Rouge foncé
                        setFont(new Font("Arial", Font.PLAIN, 12));
                    } else {
                        c.setBackground(PENDING_COLOR);
                        c.setForeground(Color.DARK_GRAY);
                        setFont(new Font("Arial", Font.PLAIN, 12));
                    }
                } else {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }
                
                // Mettre en gras le poids
                if (column == 2) {
                    setFont(new Font("Arial", Font.BOLD, 13));
                }
                
                return c;
            }
        };
        
        // Appliquer le renderer à toutes les colonnes
        for (int i = 0; i < edgeTable.getColumnCount(); i++) {
            edgeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Renderer spécial pour la colonne État avec des icônes
        edgeTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column
                );
                
                setHorizontalAlignment(SwingConstants.CENTER);
                
                String etat = (String) value;
                
                if (!isSelected) {
                    if (etat.contains("En cours")) {
                        c.setBackground(CURRENT_COLOR);
                        c.setForeground(new Color(255, 140, 0));
                        setFont(new Font("Arial", Font.BOLD, 11));
                    } else if (etat.contains("Ajoutée")) {
                        c.setBackground(ADDED_COLOR);
                        c.setForeground(new Color(0, 100, 0));
                        setFont(new Font("Arial", Font.BOLD, 11));
                    } else if (etat.contains("Rejetée")) {
                        c.setBackground(REJECTED_COLOR);
                        c.setForeground(new Color(139, 0, 0));
                        setFont(new Font("Arial", Font.BOLD, 11));
                    } else {
                        c.setBackground(PENDING_COLOR);
                        c.setForeground(Color.GRAY);
                        setFont(new Font("Arial", Font.ITALIC, 11));
                    }
                }
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(edgeTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Panneau de légende en bas
        add(createLegendPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        legendPanel.setBackground(new Color(255, 250, 240));
        legendPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        legendPanel.add(createLegendItem("En cours d'examen", CURRENT_COLOR));
        legendPanel.add(createLegendItem("Ajoutée au MST", ADDED_COLOR));
        legendPanel.add(createLegendItem("Rejetée (cycle)", REJECTED_COLOR));
        legendPanel.add(createLegendItem("En attente", PENDING_COLOR));
        
        return legendPanel;
    }
    
    private JLabel createLegendItem(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 10));
        label.setOpaque(true);
        label.setBackground(color);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        return label;
    }
    
    public void updateEdges(List<Arete> edges, List<Arete> added) {
        this.addedEdges = added;
        tableModel.setRowCount(0);
        
        for (Arete a : edges) {
            boolean isAdded = added.contains(a);
            boolean isCurrent = a.equals(currentEdge);
            
            String etat;
            if (isCurrent) {
                etat = "En cours...";
            } else if (isAdded) {
                etat = "Ajoutée";
            } else {
                etat = "En attente";
            }
            
            tableModel.addRow(new Object[]{
                a.getU().getNom(), 
                a.getV().getNom(), 
                a.getPoids(), 
                etat
            });
        }
        
        // Scroll vers l'arête courante si elle existe
        if (currentEdge != null) {
            int currentIndex = edges.indexOf(currentEdge);
            if (currentIndex >= 0) {
                edgeTable.scrollRectToVisible(
                    edgeTable.getCellRect(currentIndex, 0, true)
                );
            }
        }
    }
    
    /**
     * Marque une arête comme étant en cours d'examen
     */
    public void setCurrentEdge(Arete edge) {
        this.currentEdge = edge;
    }
    
    /**
     * Marque une arête comme rejetée (créerait un cycle)
     */
    public void setRejectedEdge(Arete edge) {
        if (addedEdges == null) return;
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String u = (String) tableModel.getValueAt(i, 0);
            String v = (String) tableModel.getValueAt(i, 1);
            int poids = (Integer) tableModel.getValueAt(i, 2);
            
            if (edge.getU().getNom().equals(u) && 
                edge.getV().getNom().equals(v) && 
                edge.getPoids() == poids) {
                
                tableModel.setValueAt("Rejetée (cycle)", i, 3);
                break;
            }
        }
    }
    
    /**
     * Réinitialise le tableau
     */
    public void clear() {
        tableModel.setRowCount(0);
        currentEdge = null;
        addedEdges = null;
    }
}