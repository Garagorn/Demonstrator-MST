package demonstrateur.modele;

    /**
     * Classe de représentation des aretes d'un graphe
     */
public class Arete implements Comparable<Arete> {
    private Sommet u;
    private Sommet v;
    private int poids;

    public Arete(Sommet u, Sommet v, int poids) {
        this.u = u;
        this.v = v;
        this.poids = poids;
    }

    /**
     * Renvoie le sommet u de l'arete
     * @return le sommet u
     */
    public Sommet getU() {
        return u;
    }

    /**
     *  Renvoie le sommet v de l'arete
     * @return le sommet v
     */
    public Sommet getV() {
        return v;
    }

    /**
     * Renvoie le poids du sommet sur lequel la fonction est appelée
     * @return le poids du sommet
     */
    public int getPoids() {
        return poids;
    }

    @Override
    public int compareTo(Arete autre) {
        return Integer.compare(this.poids, autre.poids);
    }

    @Override
    public String toString() {
        return u + " --(" + poids + ")-- " + v;
    }
}
