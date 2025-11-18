package demonstrateur.controleur;

import demonstrateur.modele.*;
import demonstrateur.vue.GraphPanel;

import java.awt.*;
import javax.swing.JOptionPane;
import java.util.List;

public class GraphControleur {
    private final GraphPanel panelGraph;
    private final GraphPanel panelPrim;
    private final GraphPanel panelKruskal;
    private Graphe graphe;
    private int delayMs = 1500;

    public GraphControleur(GraphPanel panelGraph, GraphPanel panelPrim, GraphPanel panelKruskal) {
        this.panelGraph = panelGraph;
        this.panelPrim = panelPrim;
        this.panelKruskal = panelKruskal;
    }

    public void genererNouveauGraphe() {
        panelPrim.stopAnimation();
        panelKruskal.stopAnimation();
        graphe = Graphe.genererAleatoire(17);
        panelGraph.setGraphe(graphe, "Graphe initial");
        panelPrim.setGraphe(graphe, "Arbre couvrant (Prim)");
        panelKruskal.setGraphe(graphe, "Arbre couvrant (Kruskal)");

        List<Arete> primQueue = graphe.primPriorityQueue(graphe.getSommets().get(0));
        List<Arete> sortedEdges = graphe.sortedEdges();

        panelPrim.setCurrentQueue(primQueue);
        panelKruskal.setFullEdgeList(sortedEdges);

    }

    public void lancerInstantane() {
        if (!validerGraphe()) return;

        panelPrim.stopAnimation();
        panelKruskal.stopAnimation();

        Sommet depart = graphe.getSommets().get(0);

        long debutPrim = System.nanoTime();
        Arbre mstPrim = graphe.Prim(depart);
        long tempsPrim = System.nanoTime() - debutPrim;

        List<Arete> primQueue = graphe.primPriorityQueue(depart);
        List<Arete> sortedEdges = graphe.sortedEdges();

        panelPrim.setCurrentQueue(primQueue);
        panelKruskal.setFullEdgeList(sortedEdges);

        panelPrim.setHighlightArbre(mstPrim, new Color(220, 20, 60));
        panelPrim.setExecutionInfo(
            String.format("Poids total : %d | Temps : %.3f ms | Arêtes : %d",
                mstPrim.getPoidsTotal(),
                tempsPrim / 1_000_000.0,
                mstPrim.getAretes().size()),
            true
        );

        long debutKruskal = System.nanoTime();
        Arbre mstKruskal = graphe.Kruskal();
        long tempsKruskal = System.nanoTime() - debutKruskal;

        panelKruskal.setHighlightArbre(mstKruskal, new Color(30, 144, 255));
        panelKruskal.setExecutionInfo(
            String.format("Poids total : %d | Temps : %.3f ms | Arêtes : %d",
                mstKruskal.getPoidsTotal(),
                tempsKruskal / 1_000_000.0,
                mstKruskal.getAretes().size()),
            true
        );

        panelPrim.refreshAuxiliaryPanels();
        panelKruskal.refreshAuxiliaryPanels();
    }

    public void lancerPasAPas() {
        if (!validerGraphe()) return;

        panelPrim.stopAnimation();
        panelKruskal.stopAnimation();

        Sommet depart = graphe.getSommets().get(0);

        long debutPrim = System.nanoTime();
        List<Arete> seqPrim = graphe.primSequence(depart);
        long tempsPrim = System.nanoTime() - debutPrim;
        Arbre mstPrim = graphe.Prim(depart);

        List<Arete> primQueue = graphe.primPriorityQueue(depart);
        panelPrim.setCurrentQueue(primQueue);
        
        // Si vous avez accès au PriorityQueuePanel depuis le contrôleur :
        // primQueuePanel.updateQueue(primQueue);

        panelPrim.setSequence(seqPrim);
        panelPrim.setExecutionInfo(
            String.format("Poids total : %d | Temps calcul : %.3f ms",
                mstPrim.getPoidsTotal(),
                tempsPrim / 1_000_000.0),
            false
        );
        panelPrim.playSequence(delayMs, new Color(220, 20, 60));

        long debutKruskal = System.nanoTime();
        List<Arete> seqKruskal = graphe.kruskalSequence();
        long tempsKruskal = System.nanoTime() - debutKruskal;
        Arbre mstKruskal = graphe.Kruskal();

        panelKruskal.setSequence(seqKruskal);
        panelKruskal.setExecutionInfo(
            String.format("Poids total : %d | Temps calcul : %.3f ms",
                mstKruskal.getPoidsTotal(),
                tempsKruskal / 1_000_000.0),
            false
        );
        panelKruskal.playSequence(delayMs, new Color(30, 144, 255));
    }

    public void pauseAnimations() {
        panelPrim.pauseAnimation();
        panelKruskal.pauseAnimation();
    }

    public void resumeAnimations() {
        panelPrim.resumeAnimation();
        panelKruskal.resumeAnimation();
    }

    public void stopAnimations() {
        panelPrim.stopAnimation();
        panelKruskal.stopAnimation();
    }

    public void resetAnimations() {
        panelPrim.resetAnimation();
        panelKruskal.resetAnimation();
    }

    public void setDelayMs(int delay) {
        if (delay > 0 && delay <= 5000) {
            this.delayMs = delay;
        }
    }

    public int getDelayMs() {
        return delayMs;
    }

    public boolean isAnimating() {
        return panelPrim.isAnimating() || panelKruskal.isAnimating();
    }

    public boolean isPaused() {
        return panelPrim.isPaused() || panelKruskal.isPaused();
    }

    private boolean validerGraphe() {
        if (graphe == null || graphe.getSommets().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "Veuillez d'abord générer un graphe",
                "Erreur",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
