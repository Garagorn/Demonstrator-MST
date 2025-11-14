Démonstrateur Minimum-Spanning-Tree (MST)

	Démonstrateur de l'utilisation du calcul d'un arbre couvrant minimal avec les algorithmes de Prim et Kruskal.
Ce démonstrateur est codé en Java et à pour rendu graphique Java Swing.

0) - Logiciels utilisé (Linux)
	Ant
	Java

1) - Pour commencer

Si vous souhaitez lancer directement le démonstrateur vous pouvez dans un terminal effectuer la commande $ ant

/demonstrateur-mst/Demonstrateur$ ant

En lançant cette commande le build.xml va effectuer la compilation puis exécuter et lancer  le démonstrateur.

Si vous souhaitez seulement compiler le code : 
	/demonstrateur-mst/Demonstrateur$ ant compile
Si vous souhaitez seulement éxécuter le code après avoir compiler : 
	/demonstrateur-mst/Demonstrateur$ ant run
Si vous souhaitez supprimer la javadoc et  le fichier de build : 
	/demonstrateur-mst/Demonstrateur$ ant clean

Une fois la commande ant lancée ou la compilation et éxécution le démonstrateur se lance.


2) - Démonstrateur 

Il est composé de plusieurs boutons et de deux modes d'éxécutions :
	Nouveau graphe - Il permet de génerer un nouveau graphe (non-orienté, connexe et pondéré) à 20 arêtes
	Exécution Instannée - C'est une des deux façons dont peut se dérouler le démonstrateur.
	Exécution pas à pas - Il permet d'effectuer au démonstrateur de différencier de plusieurs étapes chaque étape de tri pour pouvoir voir se réaliser les étapes de tri.
	Pause - Il permet de mettre en pause le mode pas à pas
	Reprendre - Il permet de reprendre le mode pas à pas
	Arrêter - Il permet d'arrêter le tri des graphes
	Reset - Il permet de remettre à 0 l'état de tri du graphe.

Chaque tri avec algorithme peut se dérouler de 2 façons différentes : 
	
	1 - Exécution Instannée - Le démonstrateur affiche le résultat de l'arbre couvrant minimal avec Prim et Kruskal sur le graphe généré aléatoirement.

	2 - Exécution pas à pas - Il permet d'afficher les étapes de façon automatique, il laisse quelques secondes (0,2 à 3s suivant le choix de l'utilisateur) entre  chaque étape pour laisser une certaine visibilité du déroulement du démonstrateur. Durant ces étapes se construit l'arbre couvrant minimal. A chaque étape de tri l'étape actuelle est en surbrillance et l'on connaît le poids de l'arête que l'on trie.

A la fin du tri on connaît pour chaque arbre le poids total de ses arêtes et le temps d'exécution de chacun des deux algorithme.


Auteurs : 
THOMAS Matthieu
TELLIER  Basile
