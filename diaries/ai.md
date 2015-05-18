## StreetCar - Intelligence artificielle
----------------
Coutaud Ulysse  
Souchet Julie  

### Jour 1 : Lundi 18 mai  
  
  
#### Critères de choix de l'intelligence artificielle :  
  - construire la voie optimale d'un terminal à l'autre en passant par les arrêts obligatoires  
  - saboter les voies des adversaires  
  - bluffer

#### Calcul de la voie optimale (pour l'instant sans les arrêts):  
On représente le terrain par un graphe, dont les sommets correspondent aux points cardinaux des cases et les arêtes aux rails sur les tuiles.  

On initialise le graphe avec tous les chemins possibles, c'est-à-dire :  
  - Pour une case vide ou contenant une tuile remplaçable : toutes les arêtes reliant tous les sommets  
  - Pour une case contenant une tuile *non* remplaçable : les arêtes représentées par les rails sur la tuile.

Dans chaque case, toute arête reçoit un poids :  
  - Si elle était déjà présente sur le plateau : $0$
  - Si on doit (rem)placer une tuile : "coût" de la tuile minimale contenant cette arête (et les éventuelles arêtes déjà présentes)  
  - Si ce mouvement est interdit : $+\infty$  

On se sert ensuite de l'algorithme A* pour rechercher le chemin le plus court entre les deux cases de départ du terminal et les deux cases d'arrivée (les voies n'étant pas orientées, on peut choisir n'importe quel départ ou arrivée). Comme heuristique, nous avons choisi de calculer la distance en ligne droite entre le point de départ et le point d'arrivée. Comme les voies ne sont pas en diagonales, la distance estimée est toujours inférieure à la distance réelle.  

Le coût d'une tuile dépend d'où on l'obtient :  
  - de la main du joueur : $0$ (on l'a déjà sous la main)  
  - de la pioche : $$\frac{nombreDeTuilesTotalesDansLaPioche}{nombreDeTuilesDeCeTypeDansLaPioche}$$ allant de $1$ à $+\infty$  (plus une tuile est rare, plus il faudra de chance pour l'obtenir)  
  - de la main d'un adversaire ayant commencé son voyage inaugural : $1$ (il suffit de se servir)  
  - du plateau : $1$ (il suffit de l'échanger)  
  
Pour l'instant cet algorithme ne prend pas en compte les arrêts obligatoires sur le trajet de la ligne, mais nous comptons les ajouter demain.