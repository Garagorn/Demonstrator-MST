package demonstrateur.controleur;

import demonstrateur.modele.*;
import demonstrateur.vue.GraphPanel;
import java.awt.*;
import java.util.*;

public class GraphControleur {
    private final GraphPanel panelGraph;
    private final GraphPanel panelPrim;
    private final GraphPanel panelKruskal;
    private Graphe graphe;

    public GraphControleur(GraphPanel panelGraph, GraphPanel panelPrim, GraphPanel panelKruskal) {
        this.panelGraph = panelGraph;
        this.panelPrim = panelPrim;
        this.panelKruskal = panelKruskal;
    }

    public void genererNouveauGraphe() {
        graphe = Graphe.genererAleatoire(20);
        panelGraph.setGraphe(graphe, "Graphe initial");
        panelPrim.setGraphe(graphe, "Arbre couvrant (Prim)");
        panelKruskal.setGraphe(graphe, "Arbre couvrant (Kruskal)");
    }

    public void lancerInstantane() {
        if (graphe == null) return;
        Sommet depart = graphe.getSommets().get(0);

        // Prim
        long debutPrim = System.nanoTime();
        Arbre mstPrim = graphe.Prim(depart);
        long tempsPrim = System.nanoTime() - debutPrim;
        panelPrim.setHighlightArbre(mstPrim, Color.RED);
        panelPrim.setExecutionInfo(
            "Poids total : " + mstPrim.getPoidsTotal() + "\n" +
            "Temps : " + (tempsPrim / 1_000_000.0) + " ms",
            true // mode instantané
        );

        // Kruskal
        long debutKruskal = System.nanoTime();
        Arbre mstKruskal = graphe.Kruskal();
        long tempsKruskal = System.nanoTime() - debutKruskal;
        panelKruskal.setHighlightArbre(mstKruskal, Color.BLUE);
        panelKruskal.setExecutionInfo(
            "Poids total : " + mstKruskal.getPoidsTotal() + "\n" +
            "Temps : " + (tempsKruskal / 1_000_000.0) + " ms",
            true // mode instantané
        );
    }

    public void lancerPasAPas() {
        if (graphe == null) return;
        Sommet depart = graphe.getSommets().get(0);

        // Prim
        long debutPrim = System.nanoTime();
        java.util.List<Arete> seqPrim = graphe.primSequence(depart);
        long tempsPrim = System.nanoTime() - debutPrim;
        panelPrim.setSequence(seqPrim);
        panelPrim.playSequence(2000, Color.RED); // 2000ms = 2 secondes entre chaque étape
        Arbre mstPrim = graphe.Prim(depart);
        panelPrim.setExecutionInfo(
            "Poids total : " + mstPrim.getPoidsTotal() + "\n" +
            "Temps : " + (tempsPrim / 1_000_000.0) + " ms",
            false // mode pas à pas
        );

        // Kruskal
        long debutKruskal = System.nanoTime();
        java.util.List<Arete> seqKruskal = graphe.kruskalSequence();
        long tempsKruskal = System.nanoTime() - debutKruskal;
        panelKruskal.setSequence(seqKruskal);
        panelKruskal.playSequence(2000, Color.BLUE); // 2000ms = 2 secondes entre chaque étape
        Arbre mstKruskal = graphe.Kruskal();
        panelKruskal.setExecutionInfo(
            "Poids total : " + mstKruskal.getPoidsTotal() + "\n" +
            "Temps : " + (tempsKruskal / 1_000_000.0) + " ms",
            false // mode pas à pas
        );
    }
}
