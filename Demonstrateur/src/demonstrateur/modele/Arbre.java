package demonstrateur.modele;

import java.util.*;
/**
     * Classe de représentation d'un arbre
     */
public class Arbre {
    private final List<Sommet> sommets;
    private final List<Arete> aretes;
    private int poidsTotal;
    
    /**
     * Constructeur de l'arbre
     */
    public Arbre() {
        this.sommets = new ArrayList<>();
        this.aretes = new ArrayList<>();
        this.poidsTotal = 0;
    }

    /***
     * Methode d'ajout de sommet
     * @param s Sommet à ajouter au  graphe
     */
    public void ajouterSommet(Sommet s) {
        if (!sommets.contains(s))
            sommets.add(s);
    }
    
    /***
     * Methode d'ajout d'arete
     * @param a Arete à ajouter au  graphe
     */
    public void ajouterArete(Arete a) {
        if (!aretes.contains(a)) {
            aretes.add(a);
            poidsTotal += a.getPoids();
            ajouterSommet(a.getU());
            ajouterSommet(a.getV());
        }
    }

    /**
     * Getter des sommets de l'arbre
     * @return Liste des sommets de l'arbre
     */
    public List<Sommet> getSommets() {
        return sommets;
    }

    /**
     * Getter  des aretes
     * @return Liste des aretes de l'arbre
     */
    public List<Arete> getAretes() {
        return aretes;
    }

    /**
     * Getter  du poids de l'abre
     * @return poidstotal Le pods  de l'arbre
     */
    public int getPoidsTotal() {
        return poidsTotal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Arbre couvrant :\n");
        for (Arete a : aretes) {
            sb.append(a).append("\n");
        }
        sb.append("Poids total : ").append(poidsTotal);
        return sb.toString();
    }

    /**
     * Convertit l’arbre en un objet Graphe pour affichage graphique
     * @return Graphe Un arbre  transformer en graphe
     */
    public Graphe toGraphe() {
        Graphe g = new Graphe();
        for (Sommet s : sommets) g.ajouterSommet(s);
        for (Arete a : aretes) g.ajouterArete(a);
        return g;
    }
    
    /**
     * Methode d'affichage  d'un arbre
     */
    public void afficherArbre() {
        for (Arete a : aretes) {
            System.out.println(a.getU().getNom() + " -- " + a.getPoids() + " -- " + a.getV().getNom());
        }
        System.out.println("Poids total : " + poidsTotal);
    }
    
}
