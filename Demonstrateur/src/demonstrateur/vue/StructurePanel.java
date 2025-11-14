package demonstrateur.vue;

import java.awt.*;
import javax.swing.*;


public class StructurePanel extends JPanel {
    private StructurePanel structurePanel;
    private PriorityQueuePanel primQueuePanel;
    private SortedEdgeTablePanel kruskalEdgePanel;

    public StructurePanel() {
        setLayout(new GridLayout(1, 2, 10, 10));
        setPreferredSize(new Dimension(1400, 200)); // largeur = fenêtre, hauteur réduite
        setBorder(BorderFactory.createTitledBorder("Structures internes"));

        primQueuePanel = new PriorityQueuePanel();
        kruskalEdgePanel = new SortedEdgeTablePanel();

        add(primQueuePanel);
        add(kruskalEdgePanel);
    }

    public PriorityQueuePanel getPrimQueuePanel() {
        return primQueuePanel;
    }

    public SortedEdgeTablePanel getKruskalEdgePanel() {
        return kruskalEdgePanel;
    }
}
