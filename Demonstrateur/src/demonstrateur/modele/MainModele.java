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
        Graphe g = Graphe.genererAleatoire(20);
        System.out.println("=== Graphe initial ===");
        g.afficherGraphe();

        //Sélection du sommet de départ
        Sommet depart = g.getSommets().get(0);
        System.out.println("\nSommet de depart : " + depart.getNom());

        //Exécution de Prim
        long debutPrim = System.nanoTime();
        Arbre mstPrim = g.Prim(depart);
        long tempsPrim = System.nanoTime() - debutPrim;

        //Aficahge MST Prim + Tps d'exec
        System.out.println("\n=== Arbre couvrant minimal (Prim) ===");
        mstPrim.afficherArbre();
        System.out.println("Temps d'execution Prim : "
                + tempsPrim + " ns (" + (tempsPrim / 1_000_000.0) + " ms)");

        //Kruskal
        long debutKruskal = System.nanoTime();
        Arbre mstKruskal = g.Kruskal();
        long tempsKruskal = System.nanoTime() - debutKruskal;

        //Affichage valeur MST Kruskal + Tps exec
        System.out.println("\n=== Arbre couvrant minimal (Kruskal) ===");
        mstKruskal.afficherArbre();
        System.out.println("Temps d'execution Kruskal : "
                + tempsKruskal + " ns (" + (tempsKruskal / 1_000_000.0) + " ms)");
    }
}
