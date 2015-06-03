# Generalités :


le coup de chaque joueur qui a une couleur ET un "pion/jeton/avatar/..." (val & riyane)
    nous, ce qu'on voudrait, c'est que chaque pion ai une couleur, et que chaque couleur représente ce pion

quand un joueur fait un essai, est-ce qu'on afficher ce qu'il est en train d'essayer aux autres joueurs?
    (val & riyane)
=> non

est-ce qu'on informe un joueur qui veut commencer son voyage d'inauguration qu'il n'a pas un chemin valide (cad reliant ses deux terminus et passant par toutes les bonnes étapes de sa route)     (val & riyane)
=> on met un message d'avertissement  

# Engine :
 * Synchroniser toutes les methodes d'interface reseau (PlayerInterface et GameInterface)
 * Etat des joueurs :  
    + ~~data.isConstructing(player) : boolean~~  
    + ~~data.hasPlayedFirstAction(player) : boolean~~  
    + ~~data.firstTerminus(player) : Point (les coordonnées du 1er terminus de la ligne (le plus en haut à gauche))~~  
    + ~~data.secondTerminus(player) : Point (l'autre terminus)~~
 * Limitation de vitesse (qui est commune à tous les joueurs)  
    + ~~data.maximumSpeed( ) : int~~  
    (VIII. pouvoir se servir dans la main des joueurs en voyage et l'ajouter à sa main)  
 * Voyage des trams :  
    IV. suivre la position des trams des joueurs  
    V. pouvoir les faire avancer  
    VI. suivre la limite de vitesse (commune à tous les joueurs, et évoluant)  
    VII. prendre en compte les arrêts  
 * Fin de partie : soit un joueur a complété son voyage, soit tous les joueurs ont posé leurs tuiles et la pioche est vide, mais aucun n'a complété son trajet
    
# IA :
 * ~~Automate jouant aléatoirement~~
 * ~~Automate vérifiant la complétion du trajet et commençant son voyage~~
 * Algorithme minimax
 * Fonction d'évaluation : 
    + faire tourner x parties avec des automates et regarder les probabilités de victoire
    + comparer par rapport au chemin optimal
 * Couple/singleton d'actions : uniformiser
 * fin de partie : isGameBlocked : prendre en compte le cas où on ne peut plus rien poser (aucun choix acceptable)  
 
# GUI :
  * Dans les settings, rajouter la difficulté: nombre de bâtiments à relier dans une ligne (entre 2 et 3)
