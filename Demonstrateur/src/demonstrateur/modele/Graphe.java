package demonstrateur.modele;

import java.util.*;

    /**
     * Classe de représentation d'un graphe non orienté
     */
public class Graphe {
    private final List<Sommet> sommets = new ArrayList<>();
    private final List<Arete> aretes = new ArrayList<>();

    public void ajouterSommet(Sommet s){ 
        sommets.add(s); 
    }
    
    public void ajouterArete(Arete a) { 
        aretes.add(a); 
    }

    public List<Sommet> getSommets() { 
        return sommets; 
    }
    
    public List<Arete> getAretes(){ 
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
    
     public static Graphe genererAleatoire(int nbSommets) {
        Graphe g = new Graphe();
        Random r = new Random();

        // Étape 1 : créer les sommets
        for (int i = 0; i < nbSommets; i++) {
            g.ajouterSommet(new Sommet("S" + i, 50 + r.nextInt(400), 50 + r.nextInt(300)));
        }

        List<Sommet> s = g.getSommets();

        // Étape 2 : relier tous les sommets en une chaîne pour assurer la connexité
        for (int i = 0; i < s.size() - 1; i++) {
            int poids = 1 + r.nextInt(20);
            g.ajouterArete(new Arete(s.get(i), s.get(i + 1), poids));
        }

        // Étape 3 : ajouter des arêtes aléatoires supplémentaires
        for (int i = 0; i < s.size(); i++) {
            for (int j = i + 2; j < s.size(); j++) { // éviter doublons directs
                if (r.nextDouble() < 0.3) {
                    int poids = 1 + r.nextInt(20);
                    g.ajouterArete(new Arete(s.get(i), s.get(j), poids));
                }
            }
        }

        return g;
    }


}
