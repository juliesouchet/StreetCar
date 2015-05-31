## StreetCar - moteur (Model and controllers)

PRULIERE Valentin
SID-LAKHDAR Riyane

-------------------

### Jour 1 : Vendredi 15 mai, Samedi 16, dimanche 17 mai

#### Conception de l'architecture golbale de l'application
  * __ Critères de choix 1: __
  Le jeu étant doté d'une main cachée, il est primordial de concevoir dès le début notre application comme une application répartie (même si la communication en réseaux n'est pas implémenté), et ceux pour permettre à deux joueurs d'être sur des écrans partagés.

  * __ Critères de choix 2: __
  Le thread joueur ne doit jamais attendre l'action du joueur ou la réponse du moteur de façon bloquante, au risque de figer l'interface graphique.  De même l'application ne peut pas attendre l'action du joueur de façon bloquante au risque de retarder la réaction à l'action d'un autre joueur.

  * __ Critères de choix 3: __
  L'action du moteur étant multi thread, il est nécessaire de vérfifier, à chaque action d'un joueur sur le moteur, l'identité du joueur et de lever une exception si ce n'est pas son tour.  De même, il sera nécessaire de centraliser les données du jeu dans une entité synchronisée entre les différents thread du moteur.
  
  Enfin, il sera nécessaire de protéger les données du jeu lors de la transmission du model du moteur vers les joueurs: transmettre au joueur une copie (au sens java) du model, dans laquelle les joueurs n'ont accés qu'aux données les conscernants, ou aux données partgés.

#### Architecture choisie:
Serveur	central(moteur)	+ clients(joueurs).
Pour des raisons liées à l'IHM, le serveur pourra être un thread lancé par le joueur hébergeur (sur sa machine), ou un processus public hebergé par un serveur dédié.  Les différences liées à l'acés à l'adresse ip du serveur ont été définies et communiqués à l'équipe IHM.
  

-------------------

### Jour 2 : Lundi 18 mai
#### Test de la faisabilité de l'architecture:
  * Présentation de l'architecture globale de l'application au reste de l'équipe.
  * Consception de l'integration des entités IA et IHM dans l'architecture globale.
  * Test de communication entre les thread moteur et joueur (Etude et test du protocole RMI).
  * Estimation des difficultés liées à la communication des futures données (plus complexes que de simples String).
  * Premiére implantation des différentes classes de l'architecture.  Test de communication entre client (joueur) et serveur (différents joueurs) au travers d'actions simple: Fonction de création de parties, et fonction de login à une partie existant.

-------------------

### Jour 3 : Mardi 19 mai
  * Implantation d'une IHM basique en ligne de commande pour tester l'architecture globale et la communication entre joueur et moteur (communication de base: création partie, login et logout d'une partie)

  * Liste des données à stocker dans le modéle du jeux
  * Separation des données partagées et des données spécifiques à chaque joueur
  * Liste des besoins de l'IHM sur les données
  * Liste des besoins de l'IA sur les données
  * Liste des besoins du moteur (lié aux scénarios de jeux) sur les données

-------------------

### Jour 4 : Mercredi 20 mai
  Debut d'implantation du modéle: classe Data qui centralise l'ensemble des données du jeu.
  Cette classe est conçue en réponse aux:
  * Différents besoins de l'IHM, l'IA et des scénarios de jeux (listés précédemment).
  * Besoins de synchronisation des accès au moteur.
  * Différents besoins de communication de l'état du modél entre clients et serveur (scerialisation des données à partager).
  * Aspects de scécurités: les différents joueurs ne peuvent pas modifier le modéle sans passer par le moteur.  Les joueurs ne peuvent pas avoir accès aux données cachées des autres joueurs (main des autres joueur, ou contenu restant de la pioche)

-------------------

### Jour 5 : Jeudi 21 mai
  Implantation de la classe tuile: Renomages des images correspondantes aux tuiles.  Le nom d'une tuile respecte une convention établie et permettant de détermniner de maniere unique une tuile ainsi que ces différents attributs (chemins, arbres, batiments, etc).   Cette convention permet notamment de rendre la classe tuile utilisables par les différentes équipes (moteur, IA, IHM).  

  Suites de l'implantation des méthodes de la classe Data définies précédemment.

  Implantation d'une méthode de recherche de chemin dans le terrain (inspiré de l'algorithme A*)
-------------------

### Jour 6 : Vendredi 22 mai  

Rencontre avec Mr LACHAIZE Renaud responsable de l'UE Systéme et Réseaux.   Explication ...............

#############################  A faire #############################

-------------------

### Jour 7 : Samedi 23 mai  
  Suite de l'implantation du modéle (classe Data et sous classes Tile, Deck, Hand) en réponse aux attentes décrites le jeudi 21 mai.
  Déboguage de ces classes en utilisant:
  * L'IHM basique en ligne de commande implanté le lundi 18 mai (test la création et l'initialisation des données).
  * Le créateur de terrain implanté par l'équipe IA.

-------------------

### Jour 8 : Dimanche 24 mai

Nous avons géré le probléme de la communication du modéle (classe Data) aux différentes parties:
  * La classe Data est instenciée par le thread du jeu (serveur).  Il n'en existe qu'un seul exemplaire.  Pour des raisons de sécurité et de serialisabilité, nous n'avons déclaré qu'un seul constructeur public de cette classe (appelé par le thread de la partie Game (serveur)).
  * Pour pouvoir communiquer l'état du modéle aux différents joueurs, nous avons créé une méthode de clonage à la classe Data.  Cette méthode, de type deep clone, dépend de l'identifiant du joueur à qui est destinée le clone.  Ainsi, l'ensemble des données partagées par tous les joueurs ainsi que les données privées spécifiques au joueur destinataire sont clonées.   Le fait que ce clonage soit de type deep clone assure la sécurité du modéle à l'intérieur de la classe Game (aucun pointeur sur une adresse mémoire de Game n'est comuniquée).
  * Pour pouvoir communiquer l'état du modéle aux différents joueur à travers le réseau, nous avons rendu la classe Data, ainsi que les différentes classes de données qu'elle contient, serializable.

-------------------

### Jour 9 : Lundi 25 mai
  Creation de la classe Engine.java.

  Changement de l'architecture de la partie moteur:
  * La classe Game reçoit une requête des joueurs.  Si la requête est impossible, une exception est levée.  Sinon, la requête est placée dans une liste d'attente à destination du thread de la classe Engine.
  * La classe Engine contient une fille d'attente connue par Game.  Le thread de la classe Engine est réveillé lorsqu'un nouvel élément est ajouté à la file.

  Nous avons assuré et testé la cohérence des données du thread Engine.

-------------------

### Jour 10 : Mardi 26 mai
Creation des joueurs de types IA:
  - Soucouche de la classe InterfacePlayer (joueur a distant), specifique aux joueur IA.
  - Cette classe qui implante Runable est lancée dans un thread indépendant.  Elle est instancié par le joueur hébergeur.  Elle commence par se loguer à une partie qui lui est passée en parametre.  Puis elle réagie à chaque changement de l'etat du jeux en jouant un coup seulement si c son tour.   Les coup joué est déterminer par l'algorithme d'IA choisi en début.

  Création des test de joueur (IHM vs IHM, IHM vs IA, et IA vs IA)

-------------------

### Jour 11 : Mercredi 27 mai

Suite de l'integration du (moteur + joueurs) avec l'IHM (gui)
Optimisation des classe Direction et Data pour les besoins de l'IA (trés grand nombre d'appel aux methodes de DATA)

-------------------

### Jour 12 : Jeudi 28 mai

Optimisation de la classe Engine pour les besoin de l'ihm:
  - Les thread joueur demandent une nouvelle action à la classe game.
  - Cette demande léve une excepion specifique en cas d'erreur (exception de type java Exception qui est donc obligatoirement traité par l'appelant)
  - Si non, l'action est placée dans la file d'attente du thread Engine, avant que la main soit rendu à l'utilisateur.   Le moteur reveillera le thread de la classe engine une fois l'action traitée.   Nous avons ainsi considérablement reduit le temps d'attente des joueur et rendu l'affichage beaucoup plus fluide.   Nous avons egalement gardé la contrainte de l'IHM de gérer tous les types d'erreur de l'utilisateur par un affichage specifique.

-------------------

### Jour 13 : Vendredi 29 mai

Creation de la table de gestion des connexions:
  - Nous avons créé une classe LoginInfo corespondant aux information de connexion d'un joueur.
  - La table des connexions est un tableau associant a chaque joueur attendu ou deja present ses information (Joueur interdit / Humain/IA, niveau de l'ia, haute de la partie...)
  - Cette table est totalement administree par le joueur haute.  Lors d'une connexion d'un joueur, le systeme cherche une case libre de la table correspondante au joueur.  Si une case est trouvée, la table est mise à jour.  Si non, le joueur est rejeté.

-------------------

### Jour 14 : Samedi 30 mai

Creation des methodes de debut de voyage et de voyage.  Ces methodes respecte les principes du moteur deja enoncé (les methodes appelés par les joueur levent une exception specifique en cas d'erreur. Si non, elles ajoutent une action specifique a la file d'attente du thread moteur, et rendent la main au joueur sans attendre la fin du traitement)

Test des scenarios de jeux:
  - Classe Test_IHM_IA: nous avons ainsi testé la reponse du moteur a une partie complete ou un joueur humain joue contre une IA aleatoire.
  - Correction des bug de synchronisation des thread joueur et moteur
-------------------

### Jour 15 : Dimanche 31 mai
