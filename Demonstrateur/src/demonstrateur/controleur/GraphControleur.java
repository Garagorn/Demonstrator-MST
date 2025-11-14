package demonstrateur.controleur;

import demonstrateur.modele.*;
import demonstrateur.vue.GraphPanel;
import java.awt.*;
import java.util.*;
import javax.swing.JOptionPane;

public class GraphControleur {
    private final GraphPanel panelGraph;
    private final GraphPanel panelPrim;
    private final GraphPanel panelKruskal;
    private Graphe graphe;
    private int delayMs = 1500; // Délai par défaut entre les étapes
    
    public GraphControleur(GraphPanel panelGraph, GraphPanel panelPrim, GraphPanel panelKruskal) {
        this.panelGraph = panelGraph;
        this.panelPrim = panelPrim;
        this.panelKruskal = panelKruskal;
    }
    
    public void genererNouveauGraphe() {
        // Arrêter les animations en cours
        panelPrim.stopAnimation();
        panelKruskal.stopAnimation();
        
        graphe = Graphe.genererAleatoire(17);
        panelGraph.setGraphe(graphe, "Graphe initial");
        panelPrim.setGraphe(graphe, "Arbre couvrant (Prim)");
        panelKruskal.setGraphe(graphe, "Arbre couvrant (Kruskal)");
    }
    
    public void lancerInstantane() {
        if (!validerGraphe()) return;
        
        // Arrêter les animations en cours
        panelPrim.stopAnimation();
        panelKruskal.stopAnimation();
        
        Sommet depart = graphe.getSommets().get(0);
        
        // Prim
        long debutPrim = System.nanoTime();
        Arbre mstPrim = graphe.Prim(depart);
        long tempsPrim = System.nanoTime() - debutPrim;
        
        panelPrim.setHighlightArbre(mstPrim, new Color(220, 20, 60)); // Rouge crimson
        panelPrim.setExecutionInfo(
            String.format("Poids total : %d | Temps : %.3f ms | Arêtes : %d", 
                mstPrim.getPoidsTotal(),
                tempsPrim / 1_000_000.0,
                mstPrim.getAretes().size()),
            true
        );
        
        // Kruskal
        long debutKruskal = System.nanoTime();
        Arbre mstKruskal = graphe.Kruskal();
        long tempsKruskal = System.nanoTime() - debutKruskal;
        
        panelKruskal.setHighlightArbre(mstKruskal, new Color(30, 144, 255)); // Bleu dodger
        panelKruskal.setExecutionInfo(
            String.format("Poids total : %d | Temps : %.3f ms | Arêtes : %d", 
                mstKruskal.getPoidsTotal(),
                tempsKruskal / 1_000_000.0,
                mstKruskal.getAretes().size()),
            true
        );
    }
    
    public void lancerPasAPas() {
        if (!validerGraphe()) return;
        
        // Arrêter les animations en cours
        panelPrim.stopAnimation();
        panelKruskal.stopAnimation();
        
        Sommet depart = graphe.getSommets().get(0);
        
        // Prim - Calcul de la séquence
        long debutPrim = System.nanoTime();
        java.util.List<Arete> seqPrim = graphe.primSequence(depart);
        long tempsPrim = System.nanoTime() - debutPrim;
        
        // Calculer le MST pour avoir le poids total
        Arbre mstPrim = graphe.Prim(depart);
        
        panelPrim.setSequence(seqPrim);
        panelPrim.setExecutionInfo(
            String.format("Poids total : %d | Temps calcul : %.3f ms", 
                mstPrim.getPoidsTotal(),
                tempsPrim / 1_000_000.0),
            false
        );
        panelPrim.playSequence(delayMs, new Color(220, 20, 60)); // Rouge crimson
        
        // Kruskal - Calcul de la séquence
        long debutKruskal = System.nanoTime();
        java.util.List<Arete> seqKruskal = graphe.kruskalSequence();
        long tempsKruskal = System.nanoTime() - debutKruskal;
        
        // Calculer le MST pour avoir le poids total
        Arbre mstKruskal = graphe.Kruskal();
        
        panelKruskal.setSequence(seqKruskal);
        panelKruskal.setExecutionInfo(
            String.format("Poids total : %d | Temps calcul : %.3f ms", 
                mstKruskal.getPoidsTotal(),
                tempsKruskal / 1_000_000.0),
            false
        );
        panelKruskal.playSequence(delayMs, new Color(30, 144, 255)); // Bleu dodger
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