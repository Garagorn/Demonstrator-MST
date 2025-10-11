package demonstrateur.vue;

import demonstrateur.controleur.*;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Démonstrateur : Graphe aléatoire");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);

        GraphPanel panel = new GraphPanel();
        GraphControleur controller = new GraphControleur(panel);

        JButton btnGenerer = new JButton("Générer un graphe");
        btnGenerer.addActionListener(e -> controller.genererNouveauGraphe());

        JPanel top = new JPanel();
        top.add(btnGenerer);

        add(top, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }
}
