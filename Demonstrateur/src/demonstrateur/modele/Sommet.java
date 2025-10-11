package demonstrateur.modele;

import java.util.Objects;

    /**
     * Classe de représentation des sommets d'un graphe
     */
public class Sommet {
    private String nom;
    private int x, y;

    
    public Sommet(String nom, int x, int y) {
        this.nom = nom;
        this.x = x;
        this.y = y;
    }

    public String getNom() { return nom; }
    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public String toString() {
        return nom + " (" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sommet)) return false;
        Sommet sommet = (Sommet) o;
        return nom.equals(sommet.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }
}
