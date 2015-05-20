# Generalités :


le coup de chaque joueur qui a une couleur ET un "pion/jeton/avatar/..." (val & riyane)
    nous, ce qu'on voudrait, c'est que chaque pion ai une couleur, et que chaque couleur représente ce pion

quand un joueur fait un essai, est-ce qu'on afficher ce qu'il est en train d'essayer aux autres joueurs?
    (val & riyane)
=> non  

est-ce qu'on informe un joueur qui veut commencer son voyage d'inauguration qu'il n'a pas un chemin valide (cad reliant ses deux terminus et passant par toutes les bonnes étapes de sa route)     (val & riyane)
=> on met un message d'avertissement  

# Engine :

# IA :
Pour ceux de l'engine :  

 * Pioche et toutes les méthodes associées (état courant (probabilités de tirs pour chaque type de tuile), ~~tirer une carte~~ géré par le data...)  
 * Mains des différents joueurs  
 * Historique des coups joués (tous les joueurs)  
 * Etat des joueurs :  
    + data.isContructing(player) : boolean  
    + data.hasPlayedFirstAction(player) : boolean  
    + data.firstTerminus(player) : Point (les coordonnées du 1er terminus de la ligne (le plus en haut à gauche))  
    + data.secondTerminus(player) : Point (l'autre terminus)  
  * Limitation de vitesse (qui est commune à tous les joueurs)  
    + data.maximumSpeed( ) : int
 
# GUI :
