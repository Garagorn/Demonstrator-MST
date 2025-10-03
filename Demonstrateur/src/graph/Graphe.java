package graph;

import java.util.*;

    /**
     * Classe de représentation d'un graphe non orienté
     */
public class Graphe {
    private Set<Sommet> sommets;
    private List<Arete> aretes;

    public Graphe() {
        this.sommets = new HashSet<>();
        this.aretes = new ArrayList<>();
    }

    /**
   * Ajoute une arête entre deux sommets dans le graphe.
   * @param u Le sommet de départ
   * @param v Le sommet d'arrivée
   * @param poids Le poid de l'arrête
   */
    public void ajouterArete(Sommet u, Sommet v, int poids) {
        Arete arete = new Arete(u, v, poids);
        aretes.add(arete);
        sommets.add(u);
        sommets.add(v);
    }

    /**
   * Renvoie la liste des sommets
   *  @return Set<Sommet> Les sommets du graphe
   */
    public Set<Sommet> getSommets() {
        return sommets;
    }

    /**
   * Renvoie la liste des aretes
   * @return La liste des arretes
   */
    public List<Arete> getAretes() {
        return aretes;
    }

    /**
   * Affichage du graphe en console
   */
    public void afficherGraphe() {
        System.out.println("Visualisation du graphe :");

        for (Arete a : aretes) {
            String u = a.getU().getNom();
            String v = a.getV().getNom();
            int poids = a.getPoids();
            System.out.println(u + " -- " + poids + " -- " + v);
        }
    }

}
