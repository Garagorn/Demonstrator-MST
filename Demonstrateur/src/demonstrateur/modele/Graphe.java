package demonstrateur.modele;

import java.util.*;

    /**
     * Classe de représentation d'un graphe non orienté
     */
public class Graphe {
    private final List<Sommet> sommets = new ArrayList<>();
    private final List<Arete> aretes = new ArrayList<>();

    /**
     * Methode d'ajout  de sommet
     * @param s Sommet à  ajouter au graphe
     */
    public void ajouterSommet(Sommet s){ 
        sommets.add(s); 
    }
    
    /**
     * Methode d'ajout d'arete
     * @param a Arete à  ajouter au graphe
     */
    public void ajouterArete(Arete a) { 
        aretes.add(a); 
    }

    /**
     * Getter des sommets
     * @return Liste des sommets
     */
    public List<Sommet> getSommets() { 
        return sommets; 
    }
    
    /**
     * Getter des aretes
     * @return Liste des aretes
     */
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
    
    /**
     * Generateur de graphe
     * @param nbSommets Nombre de sommets que  l'on souhaite générer dans le graphe
     * @return Graphe le graphe  produit de la génération
     */
     public static Graphe genererAleatoire(int nbSommets) {
        Graphe g = new Graphe();
        Random r = new Random();

        // Créer les sommets
        for (int i = 0; i < nbSommets; i++) {
            g.ajouterSommet(new Sommet("S" + i, 50 + r.nextInt(400), 50 + r.nextInt(300)));
        }

        List<Sommet> s = g.getSommets();

        // Relier tous les sommets en une chaîne pour assurer la connexité
        for (int i = 0; i < s.size() - 1; i++) {
            int poids = 1 + r.nextInt(20);
            g.ajouterArete(new Arete(s.get(i), s.get(i + 1), poids));
        }

        // Ajouter des arêtes aléatoires supplémentaires
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
/**
    fonction prim(G, s)
           pour tout sommet t
                  cout[t] := +∞
                  pred[t] := null
           cout[s] := 0
           F := file de priorité contenant les sommets de G avec cout[.] comme priorité 
           tant que F ≠ vide
                t := F.defiler
                pour toute arête t--u avec u appartenant à F
                    si cout[u] >= poids de l'arête entre les sommets t et u
                           pred[u] := t
                           cout[u] := poids de l'arête entre les sommets t et u
                           F.notifierDiminution(u)

           retourner pred
 */
     
     /**
      * Méthode de calcul d'un Arbre couvrant minimal
      * @param depart Sommet de départ du traitement
      * @return Arbre Résultant du traitement du graphe    
      */
    public Arbre Prim(Sommet depart){
        Arbre prim = new Arbre();
        List<Sommet> sommets = getSommets();
         
        Map<Sommet, Integer> cout = new HashMap<>();
        Map<Sommet, Sommet> pred = new HashMap<>();
        //Parcours des sommets
        for (Sommet t : sommets) {
            cout.put(t, Integer.MAX_VALUE);
            pred.put(t, null);
        }
        cout.put(depart, 0);

        // File de priorité (triée sur le cout)
        PriorityQueue<Sommet> file = new PriorityQueue<>(Comparator.comparingInt(cout::get));
        file.addAll(sommets);

        // Boucle principale
        while (!file.isEmpty()) {
            Sommet t = file.poll(); // sommet avec le plus petit cout

            // Si t a un prédécesseur, on ajoute l'arête correspondante au MST
            Sommet parent = pred.get(t);
            if (parent != null) {
                Arete a = getArete(parent, t);
                if (a != null) {
                    prim.ajouterArete(a);
                }
            }

            // Exploration des voisins de t
            for (Arete a : getAretes()) {
                Sommet u = null;
                if (a.getU().equals(t))
                    u = a.getV();
                else if (a.getV().equals(t))
                    u = a.getU();

                if (u != null && file.contains(u)) {
                    int poids = a.getPoids();
                    if (poids < cout.get(u)) {
                        cout.put(u, poids);
                        pred.put(u, t);

                        // Mise à jour de la priorité (on retire et on réinsère)
                        file.remove(u);
                        file.add(u);
                    }
                }
            }
        }

        return prim;
    }

    /**Kruskal(G) :
    1   A := ø
    2   pour chaque sommet v de G :
    3      créerEnsemble(v)
    4   trier les arêtes de G par poids croissant
    5   pour chaque arête (u, v) de G prise par poids croissant :
    6      si find(u) ≠ find(v) :
    7         ajouter l'arête (u, v) à l'ensemble A
    8         union(u, v)
    9   renvoyer A **/
 
    public Arbre Kruskal()
    {
        Arbre kruskal =  new Arbre();

        //Trie arete pas poids croissant
        List<Arete> areteTriees = new ArrayList<>(aretes);
        Collections.sort(areteTriees);

        //init Union-Find
        Map<Sommet, Sommet> parent = new HashMap<>();
        for (Sommet s : sommets) {
            parent.put(s, s);
        }

        // Méthode find
        java.util.function.Function<Sommet, Sommet> find = new java.util.function.Function<>() {
            @Override
            public Sommet apply(Sommet s) {
                Sommet p = parent.get(s);
                if (p != s) {
                    parent.put(s, this.apply(p)); // compression de chemin
                }
                return parent.get(s);
            }
        };

        // Méthode union : fusion de deux ensembles
        java.util.function.BiConsumer<Sommet, Sommet> union = (s1, s2) -> {
            Sommet root1 = find.apply(s1);
            Sommet root2 = find.apply(s2);
            if (root1 != root2) {
                parent.put(root2, root1);
            }
        };

        //parcours des arêtes triées
        for (Arete a : areteTriees) {
            Sommet u = a.getU();
            Sommet v = a.getV();

            // Si les deux sommets appartiennent à des ensembles différents -> pas de cycle
            if (find.apply(u) != find.apply(v)) {
                kruskal.ajouterArete(a);
                union.accept(u, v);
            }
        }

        return kruskal;
    }
    /**
     * Getter de l'arete etre deux sommets
     * @param s1 1er Sommet
     * @param s2 2e sommet
     * @return Arete L'arete entre le sommet S1 et S2
     */
    private Arete getArete(Sommet s1, Sommet s2) {
        for (Arete a : aretes) {
            if ((a.getU().equals(s1) && a.getV().equals(s2)) ||
                (a.getU().equals(s2) && a.getV().equals(s1))) {
                return a;
            }
        }
        return null;
    }
    
}
