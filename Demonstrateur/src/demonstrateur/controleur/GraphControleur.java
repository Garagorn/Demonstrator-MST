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

        // === Prim ===
        long debutPrim = System.nanoTime();
        Arbre arbrePrim = graphe.Prim(depart);
        long tempsPrim = System.nanoTime() - debutPrim;

        // === Kruskal ===
        long debutKruskal = System.nanoTime();
        Arbre arbreKruskal = graphe.Kruskal();
        long tempsKruskal = System.nanoTime() - debutKruskal;

        // Mise à jour des 3 panneaux
        panelBase.setGraphe(graphe, "Graphe initial");

        panelPrim.setGraphe(graphe, "Prim - MST");
        panelPrim.setHighlightArbre(arbrePrim, Color.GREEN);
        panelPrim.setExecutionInfo(
                String.format("Temps: %.3f ms", tempsPrim / 1_000_000.0)
        );

        panelKruskal.setGraphe(graphe, "Kruskal - MST");
        panelKruskal.setHighlightArbre(arbreKruskal, Color.BLUE);
        panelKruskal.setExecutionInfo(
                String.format("Temps: %.3f ms", tempsKruskal / 1_000_000.0)
        );

    }
}
