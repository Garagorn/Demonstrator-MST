package demonstrateur.modele;

    /**
     * Classe de représentation des aretes d'un graphe
     */
public class Arete implements Comparable<Arete> {
    private final Sommet u;
    private final Sommet v;
    private final int poids;

    /**
     * Constructeur d'une arete
     * @param u Sommet u de l'arete
     * @param v Sommet v de l'arete
     * @param poids Poids de l'arete u,v
     */
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

    /**
     * Methode de  comparaison entre deux arete
     * @param autre Arete que l'on compare  à celle-ci
     * @return Bool  
     */
    @Override
    public int compareTo(Arete autre) {
        return Integer.compare(this.poids, autre.poids);
    }

    @Override
    public String toString() {
        return u + " --(" + poids + ")-- " + v;
    }
}
