package demonstrateur.modele;

import java.util.Objects;

    /**
     * Classe de représentation des sommets d'un graphe
     */
public class Sommet {
    private final String nom;
    private final int x, y;

    /**
     * Constructeur d'un sommet
     * @param nom Nom du sommet
     * @param x Coordonnée x
     * @param y Coordonnée y
     */
    public Sommet(String nom, int x, int y) {
        this.nom = nom;
        this.x = x;
        this.y = y;
    }

    /**
     * Getter du nom du sommet
     * @return nom Nom du sommet
     */
    public String getNom(){ 
        return this.nom; 
    }
    
    /**
     * Getter coord x
     * @return x  Coordonnée x
     */
    public int getX(){ 
        return this.x; 
    }
    
    /**
     * Getter coord y
     * @return y  Coordonnée y
     */
    public int getY(){ 
        return this.y; 
    }

    @Override
    public String toString() {
        return this.nom + " (" + this.x + "," + this.y + ")";
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
