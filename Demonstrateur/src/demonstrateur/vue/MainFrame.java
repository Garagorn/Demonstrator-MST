package demonstrateur.vue;

import demonstrateur.controleur.*;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Démonstrateur MST - Prim & Kruskal");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Création des panneaux
        GraphPanel panelGraph = new GraphPanel();
        GraphPanel panelPrim = new GraphPanel();
        GraphPanel panelKruskal = new GraphPanel();

        panelGraph.setPreferredSize(new Dimension(400, 700));
        panelPrim.setPreferredSize(new Dimension(400, 700));
        panelKruskal.setPreferredSize(new Dimension(400, 700));

        JPanel center = new JPanel(new GridLayout(1, 3));
        center.add(panelGraph);
        center.add(panelPrim);
        center.add(panelKruskal);
        add(center, BorderLayout.CENTER);

        // Création du contrôleur
        GraphControleur controller = new GraphControleur(panelGraph, panelPrim, panelKruskal);

        // Boutons
        JButton btnGenerer = new JButton("Générer graphe");
        JButton btnInstant = new JButton("Exécuter (instantané)");
        JButton btnStepByStep = new JButton("Exécuter (pas à pas)");

        btnGenerer.addActionListener(e -> controller.genererNouveauGraphe());
        btnInstant.addActionListener(e -> controller.lancerInstantane());
        btnStepByStep.addActionListener(e -> controller.lancerPasAPas());

        // Ajout des boutons au panneau supérieur
        JPanel top = new JPanel();
        top.add(btnGenerer);
        top.add(btnInstant);
        top.add(btnStepByStep);
        add(top, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
