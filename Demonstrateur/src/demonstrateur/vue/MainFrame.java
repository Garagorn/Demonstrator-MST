package demonstrateur.vue;

import demonstrateur.controleur.*;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Démonstrateur : Prim & Kruskal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 500);

        // Trois panneaux
        GraphPanel panelBase = new GraphPanel();
        GraphPanel panelPrim = new GraphPanel();
        GraphPanel panelKruskal = new GraphPanel();

        // Contrôleur
        GraphControleur controller = new GraphControleur(panelBase, panelPrim, panelKruskal);

        // Bouton
        JButton btnGenerer = new JButton("Générer un graphe");
        btnGenerer.addActionListener(e -> controller.genererNouveauGraphe());

        // Ligne de boutons en haut
        JPanel top = new JPanel();
        top.add(btnGenerer);

        // Trois vues côte à côte
        JPanel panels = new JPanel(new GridLayout(1, 3));
        panels.add(panelBase);
        panels.add(panelPrim);
        panels.add(panelKruskal);

        add(top, BorderLayout.NORTH);
        add(panels, BorderLayout.CENTER);
    }
}
