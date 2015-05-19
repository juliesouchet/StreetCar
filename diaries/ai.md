## StreetCar - Intelligence artificielle

Coutaud Ulysse  
Souchet Julie

-----

### Jour 1 : Lundi 18 mai  
  
  
#### Critères de choix de l'intelligence artificielle :  
>  construire la voie optimale d'un terminal à l'autre en passant par les arrêts obligatoires  

>  saboter les voies des adversaires  

>  bluffer  


#### Calcul de la voie optimale (pour l'instant sans les arrêts):  
* On représente le terrain par un graphe, dont les sommets correspondent aux points cardinaux des cases et les arêtes aux rails sur les tuiles.  
  

* On initialise le graphe avec tous les chemins possibles, c'est-à-dire :  
    + Pour une case vide ou contenant une tuile remplaçable : toutes les arêtes reliant tous les sommets  
    + Pour une case contenant une tuile *non* remplaçable : les arêtes représentant les rails sur la tuile.  
  

* Dans chaque case, toute arête reçoit un poids :  
    + Si elle était déjà présente sur le plateau : $0$  
    + Si on doit (rem)placer une tuile : coût de la tuile minimale contenant cette arête (et les éventuelles arêtes déjà présentes)    
  *Attention : on joue 2 tuiles / tour*  
    + Si ce mouvement est interdit : $+\infty$  
  *Attention : on peut échanger 2 tuiles simulanément pour créer une connexion*  
  

* Le coût d'une tuile dépend d'où on l'obtient :  
    + de la main du joueur : $0$ (on l'a déjà sous la main)  
    + de la pioche : $$\frac{nombreDeTuilesTotalesDansLaPioche}{nombreDeTuilesDeCeTypeDansLaPioche}$$     allant de $1$ à $+\infty$  (plus une tuile est rare, plus il faudra de chance pour l'obtenir)  
    + de la main d'un adversaire ayant commencé son voyage inaugural : $1$ (il suffit de se servir)  
    + du plateau : $1$ (il suffit de l'échanger)  
  

* On se sert ensuite de l'algorithme `A*` pour rechercher le chemin le plus court entre les deux cases de départ du terminal et les deux cases d'arrivée (les voies n'étant pas orientées, on peut choisir n'importe quel terminal de départ ou d'arrivée). Comme heuristique, nous avons choisi de calculer la distance en ligne droite entre le point de départ et le point d'arrivée. Comme les voies ne sont pas en diagonales, la distance estimée est toujours inférieure à la distance réelle.  
  
  
Pour l'instant cet algorithme ne prend pas en compte les arrêts obligatoires sur le trajet de la ligne, mais nous comptons les ajouter demain.  

-------------------

### Jour 2 : Mardi 19 mai  

#### Calcul de la voie optimale avec les arrêts :  

* __Algorithme :__  
Afin d'améliorer l'algorithme ci-dessus, nous allons, pour chaque combinaison des arrêts obligatoires (1-2-3, 2-1-3, 3-2-1...) calculer les chemins les plus courts, puis les mettre bout à bout. Le résultat gagnant sera celui de meilleure qualité.  

* __Code :__  
Nous avons commencé à construire la structure du code de l'IA, avec toutes les fonctions nécessaires.