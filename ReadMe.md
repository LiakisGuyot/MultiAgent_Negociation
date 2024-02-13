### AZOURI ALEXANDRE
### GUYOT DE LA POMMERAYE Benjamin
# Projet Négociation multi agents

## I/ Utilisation

Pour lancer le projet, il suffit de lancer la classe *Main*.
Nous avons ajouter au constructeur de notre classe *Environnement* un booléan pour autoriser, ou non, les coalitions.

Pour activer les coalitions, il suffit de mettre ligne 5 la variable *allowCoalition* à *BOOLEAN.TRUE*
Pour désactiver les coalitions, il suffit de mettre ligne 5 la variable *allowCoalition* à *BOOLEAN.FALSE*

vidéo de présentation : 
- Avec coalitions : https://youtu.be/lpOqR8ZoR8s
- Sans coalitions : https://youtu.be/DlTCmUoK6KY

## II/ Contexte 
Des agents sont en compétition pour négocier le prix d'un billet auprès d'un fournisseur.
Les agents doivent donc obtenir le billet le plus vite possible et au meilleur prix.
Ceci est une simulation de négociations  reprenant le modèle de Rubinstein. Le Fournisseur fait une offre au négociateur. Si ce dernier accepte la négociation se termine. Sinon, c'est le négociateur qui propose un nouveau prix et c'est au Fournisseur d'accepter. Cette négociation s'exécute tant qu'un accord n'est pas trouvé ou qu'il n'est pas possible de trouver de compromis.

## III/ Structure du projet
Le projet est divisé en 4 packages : 
- `agents` : contient les classes des agents
- `communication` : contient les classes utilisées pour la communication entre les agents
- `items` : contient les classes des objets négociés (ici une seule classe : *Ticket* )
- `stratégies` : contient les classes utilisés pour implémentés les stratégies utilisés par les agents pour négocier

Nous disposons d'une classe `Environnement` responsable de la gestion des agents et des négociations.
Nous avons notamment implémenté une variable *refreshRate* qui permet de définir le nombre d'itérations avant de mettre en place de nouvelles coalitions.


###  1. Agents
#### 1.1. Negociateur
Ils ont 2 principaux attributs:
- un facteur (**factor**)
- un objectif de réduction (**obj_red**)
#### 1.2 Fournisseur
le fournisseur possède ces deux memes attributs, si ce n'est qu'il ne vend le billet qu'à un prix supérieur à
obj_red.


Si obj_red d'un negociateur est inférieur à celui du fournisseur, il ne pourra jamais acheter le billet.

### 2. Communication
Les agents communiquent entre eux pour former des coalitions ou négocier. Ils utilisent pour cela des messages de type *Message*.
Bien que la classe *Negociation* ne soit pas utilisée explicetement pour communiquer, elle est utilisée pour stocker les informations utiles à la communication, notamment le(s) fournisseur(s) et le(s) negociateur(s) concerné(s).



### 3. Items
Un seul objet est négocié ici : le *Ticket*. Il possède un prix, une ville de provenance et une ville de destination ainsi qu'une compagnie.
Nous utilisons peu l'attribut compagnie ici, mais il pourrait être utile pour des négociations plus complexes, notamment en implicant un bien être social du négociateur.

### 4. Stratégies
Nous avons créer une interface *Strategie* qui définit la méthode *appliquer*.
De la sorte nous standardisons les stratégies utilisées par les agents pour négocier.

Nous avons implémenté 2 stratégies différentes :
- *Factor* : la stratégie utilisée par les négociateurs. Elle représente le facteur de négociateur du négociateur. Plus ce facteur est proche de 0, plus le négociateur et agressif. Plus ce facteur est proche de 1, plus le négociateur est passif.
- *FactorFournisseur* : la stratégie utilisée par le fournisseur. Elle représente le facteur de négociation du fournisseur. Plus ce facteur est proche de 0, plus le fournisseur est agressif. Plus ce facteur est proche de 1, plus le fournisseur est passif.


## IV. Limitation de notre approche

Dans notre cas, un négociateur peut, seul, ne pas parvenir a ses fins et ne pas obtenir le billet.

Pour pallier ce problème, nous avons implémenté un système de coalition. 
Dans notre cas la négociation est faite pas un manque de moyen (monétaire) pour réaliser leur tâche (acheter un billet).

Les agents peuvent s'associer pour négocier ensemble avec le fournisseur. Ils obtiennent 
alors en groupe un nouveau facteur de négociation et un nouvel objectif de réduction. Cela permet alors de rebattre
les cartes.

Cependant, cela a un cout! Le score attribué à un seul agent quand il achète un billet est de 1/taille de la coalition.
Donc plus la coalition est grande, moins le score attribué à un agent est grand.

Les coalitions sont formées en suivant une logique exploratoire stochastique, avec un epsilon décroissant limitant 
l'exploration dans les itérations finales et une valeur de tolérance propre à chaque Negociateur 
(égale ici chez tout le monde pour simplifier les choses)
qui peut le pousser à intégrer des formations non optimales.

Les coalitions sont formées toutes les 10 itérations pour éviter les scores biaisés en raison de problèmes de synchronisation.

## V. Conclusion
Nous avons pu, grâce à ce projet, mettre en place un système de négociation multi agents avec des coalitions.
Nous avons pu voir que les agents, en s'associant, pouvaient obtenir des résultats meilleurs que s'ils négociaient seuls.
Cependant, cela a un cout, et il est important de bien choisir ses partenaires de négociation pour obtenir le meilleur score possible.

Le projet pourrait être approfondi en rajoutant différentes stratégies de négociation. Il est aussi possible d'ajouter une dimension de bien-être social par le biais des compagnies proposant les billets.
Notre structure de code permet de rajouter facilement ces points.
