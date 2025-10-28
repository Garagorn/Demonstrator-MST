package demonstrateur.modele;

import java.util.*;

/**
 * Classe de représentation d'un graphe non orienté
 */
public class Graphe {
    private final List<Sommet> sommets = new ArrayList<>();
    private final List<Arete> aretes = new ArrayList<>();

    public void ajouterSommet(Sommet s) { sommets.add(s); }
    public void ajouterArete(Arete a) { aretes.add(a); }

    public List<Sommet> getSommets() { return sommets; }
    public List<Arete> getAretes() { return aretes; }

    public Arete getArete(Sommet u, Sommet v) {
        for (Arete a : aretes) {
            if ((a.getU().equals(u) && a.getV().equals(v)) ||
                (a.getU().equals(v) && a.getV().equals(u)))
                return a;
        }
        return null;
    }

    public void afficherGraphe() {
        System.out.println("Visualisation du graphe :");
        for (Arete a : aretes)
            System.out.println(a.getU().getNom() + " -- " + a.getPoids() + " -- " + a.getV().getNom());
    }

    /** Génère un graphe aléatoire plus “étalé” */
    public static Graphe genererAleatoire(int nbSommets) {
        Graphe g = new Graphe();
        Random r = new Random();

        // création sommets dans une zone répartie en grille
        int cols = (int) Math.ceil(Math.sqrt(nbSommets));
        for (int i = 0; i < nbSommets; i++) {
            int row = i / cols;
            int col = i % cols;
            int x = 80 + col * 120 + r.nextInt(60);
            int y = 80 + row * 120 + r.nextInt(60);
            g.ajouterSommet(new Sommet("S" + i, x, y));
        }

        // connexité : chaîne
        List<Sommet> s = g.getSommets();
        for (int i = 0; i < s.size() - 1; i++) {
            int poids = 1 + r.nextInt(20);
            g.ajouterArete(new Arete(s.get(i), s.get(i + 1), poids));
        }

        // arêtes supplémentaires
        for (int i = 0; i < s.size(); i++) {
            for (int j = i + 2; j < s.size(); j++) {
                if (r.nextDouble() < 0.25) {
                    int poids = 1 + r.nextInt(20);
                    g.ajouterArete(new Arete(s.get(i), s.get(j), poids));
                }
            }
        }
        return g;
    }

    /** PRIM classique */
    public Arbre Prim(Sommet depart) {
        Arbre prim = new Arbre();
        Map<Sommet, Integer> cout = new HashMap<>();
        Map<Sommet, Sommet> pred = new HashMap<>();

        for (Sommet t : sommets) {
            cout.put(t, Integer.MAX_VALUE);
            pred.put(t, null);
        }
        cout.put(depart, 0);

        PriorityQueue<Sommet> file = new PriorityQueue<>(Comparator.comparingInt(cout::get));
        file.addAll(sommets);

        while (!file.isEmpty()) {
            Sommet t = file.poll();
            Sommet parent = pred.get(t);
            if (parent != null) {
                Arete a = getArete(parent, t);
                if (a != null) prim.ajouterArete(a);
            }
            for (Arete a : aretes) {
                Sommet u = null;
                if (a.getU().equals(t)) u = a.getV();
                else if (a.getV().equals(t)) u = a.getU();

                if (u != null && file.contains(u)) {
                    int poids = a.getPoids();
                    if (poids < cout.get(u)) {
                        cout.put(u, poids);
                        pred.put(u, t);
                        file.remove(u);
                        file.add(u);
                    }
                }
            }
        }
        return prim;
    }

    /** Séquence d’arêtes ajoutées par Prim */
    public List<Arete> primSequence(Sommet depart) {
        List<Arete> sequence = new ArrayList<>();
        Map<Sommet, Integer> cout = new HashMap<>();
        Map<Sommet, Sommet> pred = new HashMap<>();

        for (Sommet t : sommets) {
            cout.put(t, Integer.MAX_VALUE);
            pred.put(t, null);
        }
        cout.put(depart, 0);

        PriorityQueue<Sommet> file = new PriorityQueue<>(Comparator.comparingInt(cout::get));
        file.addAll(sommets);

        while (!file.isEmpty()) {
            Sommet t = file.poll();
            Sommet parent = pred.get(t);
            if (parent != null) {
                Arete a = getArete(parent, t);
                if (a != null) sequence.add(a);
            }
            for (Arete a : aretes) {
                Sommet u = null;
                if (a.getU().equals(t)) u = a.getV();
                else if (a.getV().equals(t)) u = a.getU();

                if (u != null && file.contains(u)) {
                    int poids = a.getPoids();
                    if (poids < cout.get(u)) {
                        cout.put(u, poids);
                        pred.put(u, t);
                        file.remove(u);
                        file.add(u);
                    }
                }
            }
        }
        return sequence;
    }

    /** Séquence d’arêtes ajoutées par Kruskal */
    public List<Arete> kruskalSequence() {
        List<Arete> seq = new ArrayList<>();
        List<Arete> sorted = new ArrayList<>(aretes);
        Collections.sort(sorted);

        Map<Sommet, Sommet> parent = new HashMap<>();
        for (Sommet s : sommets) parent.put(s, s);

        java.util.function.Function<Sommet, Sommet> find = new java.util.function.Function<>() {
            @Override
            public Sommet apply(Sommet s) {
                Sommet p = parent.get(s);
                if (p != s) parent.put(s, this.apply(p));
                return parent.get(s);
            }
        };

        java.util.function.BiConsumer<Sommet, Sommet> union = (a, b) -> parent.put(find.apply(b), find.apply(a));

        for (Arete a : sorted) {
            Sommet u = a.getU(), v = a.getV();
            if (find.apply(u) != find.apply(v)) {
                seq.add(a);
                union.accept(u, v);
            }
        }
        return seq;
    }
    
    /** Version complète de Kruskal (MST final) */
    public Arbre Kruskal() {
        Arbre mst = new Arbre();
        List<Arete> sorted = new ArrayList<>(aretes);
        Collections.sort(sorted);

        Map<Sommet, Sommet> parent = new HashMap<>();
        for (Sommet s : sommets) parent.put(s, s);

        java.util.function.Function<Sommet, Sommet> find = new java.util.function.Function<>() {
            @Override
            public Sommet apply(Sommet s) {
                Sommet p = parent.get(s);
                if (p != s) parent.put(s, this.apply(p));
                return parent.get(s);
            }
        };

        java.util.function.BiConsumer<Sommet, Sommet> union = (a, b) -> parent.put(find.apply(b), find.apply(a));

        for (Arete a : sorted) {
            Sommet u = a.getU(), v = a.getV();
            if (find.apply(u) != find.apply(v)) {
                mst.ajouterArete(a);
                union.accept(u, v);
            }
        }
        return mst;
    }

}
