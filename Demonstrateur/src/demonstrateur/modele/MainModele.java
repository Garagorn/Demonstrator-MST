/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demonstrateur.modele;

/**
 *
 * @author tellier212@CAMPUS
 */
public class MainModele {
    public static void main(String[] args) {

        //Génération d’un graphe aléatoire
        Graphe g = Graphe.genererAleatoire(6); // par ex. 6 sommets
        System.out.println("=== Graphe initial ===");
        g.afficherGraphe();

        //Sélection du sommet de départ
        Sommet depart = g.getSommets().get(0);
        System.out.println("\nSommet de départ : " + depart.getNom());

        //Exécution de Prim
        //long debut = System.currentTimeMillis();
        Arbre mst = g.Prim(depart);
        //System.out.println(System.currentTimeMillis()-debut);

        //Affichage du résultat
        System.out.println("\n=== Arbre couvrant minimal (Prim) ===");
        mst.afficherArbre();

        //Kruskal

        //long debut2 = System.currentTimeMillis();
        Arbre kruskal = g.Kruskal();
        //System.out.println(System.currentTimeMillis()-debut2);

        System.out.println("\n=== Arbre couvrant minimal (Kruskal) ===");
        kruskal.afficherArbre();
    }
}
