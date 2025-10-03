package graph;

public class Main {
    public static void main(String[] args) {
        Graphe g = new Graphe();

        Sommet a = new Sommet("A");
        Sommet b = new Sommet("B");
        Sommet c = new Sommet("C");
        Sommet d = new Sommet("D");
        Sommet e = new Sommet("E");

        g.ajouterArete(a, b, 4);
        g.ajouterArete(a, c, 2);
        g.ajouterArete(b, c, 5);
        g.ajouterArete(b, d, 10);
        g.ajouterArete(c, d, 3);
        g.ajouterArete(d, e, 1);

        g.afficherGraphe();

        for (Sommet s : g.getSommets()) {
            System.out.println("Sommet: " + s.getNom());
            for (Arete A : g.getAretes()) {
                if (A.getU().equals(s)) {
                    System.out.println("  --> Connecté à: " + A.getV().getNom() + " (poids: " + A.getPoids() + ")");
                } else if (A.getV().equals(s)) {
                    System.out.println("  --> Connecté à: " + A.getU().getNom() + " (poids: " + A.getPoids() + ")");
                }
            }
        }

    }
}
