## StreetCar - Interface homme machine

Vizzini Jeremy
Sellam Louis

### Jour 1 : Lundi 18 mai

Prototype d'interface :
https://moqups.com/louissellam/WC9XQzbg

Nous avons eu un audit de la partie IHM.
Cela a révélé des problèmes tel que :  
  - Les boutons `Retour` / `Annuler` / `Quitter`.  
  - Emplacement du menu dans la vue en jeu.  
  - Accessibilité des regles dans la vu en jeu.  
  - Pouvoir faire autre chose dans la salle d'attente comme lire les règles.  
  - Faire une salle d'attente commune à l'host et aux clients.  
  
Nous avons crée l'aborescence des sources dans differents dossiers.  
Nous avons à la structure du code de la partie IHM.  

### Jour 2 : Mardi 19 mai

Implémentation à deux d'un controlleur de fenêtre et de celui d'une vue.
Puis première utilisation afin de réaliser la fenêtre principale avec le menu principal.
Louis fait les autres menu et Jeremy complète la partie util.

### Jour 3 : Mercredi 20 mai

Implémentation des fenêtres de chaque page (menu de base, salle d'attente, etc...)
A faire: 
  - résolution du problème de rafraichisemment lors du changement de la taille de la fenêtre
  - mettre des listener sur les ComboBox, TextField, boutons des options
  - afficher les titres de chaque panel
  - afficher et mettre les avatars cliquables
  
### Jour 4 : Jeudi 21 mai 

Première implémentation da la vue en jeu faite: répartition de la vue en 3 panels (joueurs, plateau, chat).
Découpage interne de chaque panel fait.
A faire:
  - Faire un panel pour chaque joueur avec son avatar, ses cartes, etc
  - Affichage de l'avatar avec bulles à chaque message (chat)
  - Plateau central à découper en case
  
### Jour 5 : Vendredi 22 mai 

Retour en arrière de la vue principale: affichage des adversaires à gauche et du joueur en bas.  
Affichage de l'avatar, cartes et stations des adversaires fait.  
Plateau central découpé en case.  


