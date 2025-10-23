package demonstrateur.controleur;

import demonstrateur.modele.*;
import demonstrateur.vue.*;
import java.awt.Color;

public class GraphControleur {
    private GraphPanel panelBase;
    private GraphPanel panelPrim;
    private GraphPanel panelKruskal;
    private Graphe graphe;

    public GraphControleur(GraphPanel panelBase, GraphPanel panelPrim, GraphPanel panelKruskal) {
        this.panelBase = panelBase;
        this.panelPrim = panelPrim;
        this.panelKruskal = panelKruskal;
    }

    public void genererNouveauGraphe() {
        graphe = Graphe.genererAleatoire(6);
        Sommet depart = graphe.getSommets().get(0);

        Arbre arbrePrim = graphe.Prim(depart);
        Arbre arbreKruskal = graphe.Kruskal();

        // même graphe pour les 3 panneaux
        panelBase.setGraphe(graphe, "Graphe initial");
        panelPrim.setGraphe(graphe, "Arbre couvrant - Prim");
        panelKruskal.setGraphe(graphe, "Arbre couvrant - Kruskal");

        // surlignage des arbres
        panelPrim.setHighlightArbre(arbrePrim, Color.GREEN);
        panelKruskal.setHighlightArbre(arbreKruskal, Color.BLUE);
    }
}
