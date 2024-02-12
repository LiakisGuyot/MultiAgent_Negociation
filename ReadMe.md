Projet Négociation multi agents

Contexte : des agents sont en compétition pour négocier le prix d'un billet auprès d'un fournisseur.
Les agents doivent donc obtenir le billet le plus vite possible et au meilleur prix.

Négociateurs:
Ils ont 2 principaux attributs: 
- un facteur (factor) qui intervient dans la stratégie de négociation du prix. Plus ce facteur
est proche de 0, plus l'agent est agressif. Plus ce facteur est proche de 1, plus l'agent est passif.
- un objectif de réduction (obj_red) qui est un pourcentage en dessous duquel l'agent n'accepte pas d'acheter.

le fournisseur possède ces deux memes attributs, si ce n'est qu'il ne vend le billet qu'à un prix supérieur à
obj_red.

Si obj_red d'un negociateur est inférieur à celui du fournisseur, il ne pourra jamais acheter le billet.

La solution à ce blocage?

Former un coalition. Les agents peuvent s'associer pour négocier ensemble avec le fournisseur. Ils obtiennent 
alors en groupe un nouveau facteur de négociation et un nouvel objectif de réduction. Cela permet alors de rebattre
les cartes.

Cependant, cela a un cout! Le score attribué à un seul agent quand il achète un billet est de 1/taille de la coalition.
Donc plus la coalition est grande, moins le score attribué à un agent est grand.

Les coalitions sont formées en suivant une logique exploratoire stochastique, avec un epsilon décroissant limitant 
l'exploration dans les itérations finales et une valeur de tolérance propre à chaque Negociateur 
(égale ici chez tout le monde pour simplifier les choses)
qui peut le pousser à intégrer des formations non optimales.

Les coalitions sont formées toutes les 10 itérations pour éviter les scores biaisés en raison de problèmes de synchronisation.
