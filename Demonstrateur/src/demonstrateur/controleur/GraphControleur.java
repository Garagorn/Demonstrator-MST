package demonstrateur.controleur;

import demonstrateur.modele.*;
import demonstrateur.vue.*;

public class GraphControleur {
    private GraphPanel panel;
    private Graphe graphe;

    public GraphControleur(GraphPanel panel) {
        this.panel = panel;
    }

    public void genererNouveauGraphe() {
        graphe = Graphe.genererAleatoire(6);
        panel.setGraphe(graphe);
    }
}
