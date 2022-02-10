---
id: release-3.3
title: La 3.3 de Tube est sortie !
date: July 20, 2021
---

Bonjour à toutes et à tous,

Avec cette version 3.3, nous vous proposons de nouvelles fonctionnalités permettant de davantage personnaliser les instances, l'ajout des listes de lectures à la recherche, la simplification des URL et bien d'autres choses encore...

#### Personnaliser la page d'accueil de son instance

La grande nouveauté de cette version 3.3, c'est la possibilité de créer une page d'accueil personnalisée pour chaque instance. Les administrateur⋅ices d'instances pourront ainsi indiquer plus clairement aux internautes ce qu'est leur instance, quels contenus on y trouve, comment on fait pour s'y inscrire ou proposer des sélections de contenus (liste non-exhaustive). Le système mis en place est assez flexible pour permettre à chacun⋅e d'y faire apparaître ce qu'iel souhaite.

![img](/img/news/release-3.3/fr/FR-page-accueil-personnalisee-900px.png "la page d'accueil réalisée sur notre instance de test")

Pour personnaliser votre page d'accueil, il vous suffit, une fois connecté, de vous rendre dans le menu *Administration*, rubrique *Configuration*, onglet *Homepage*. Là, un bloc totalement vide vous permet d'ajouter les éléments souhaités, au format Markdown ou HTML. De nombreuses possibilités de mises en forme s'offrent à vous via des balises HTML personnalisées créées pour l'occasion. Pour les découvrir, n'hésitez pas à consulter [notre documentation (en anglais)](https://tube.docs.dingshunyu.top/api-custom-client-markup).

![img](/img/news/release-3.3/fr/FR-admin-page-accueil.png "le code correspondant à la page d'accueil affichée ci-dessus")

Vous pourrez ainsi afficher :
  * un bouton personnalisé
  * un lecteur intégré affichant une vidéo ou une liste de lecture
  * une vignette permettant d'accéder à une vidéo, une liste de lecture ou une chaîne
  * une liste de vidéos qui se met à jour automatiquement (avec possibilité de filtrer par langue ou  catégorie)

Vous pourrez aussi utiliser des conteneurs pour afficher en colonne ou en ligne plusieurs éléments (vidéos, chaînes, comptes, playlists) et ainsi proposer des sélections éditorialisées dans une mise en forme attractive.

Afin de permettre au plus grand nombre de voir votre page d'accueil personnalisée, assurez-vous de bien la paramétrer comme page par défaut. Pour cela, allez dans le menu *Administration*, rubrique *Configuration*, onglet *Basique* et sélectionnez "Accueil" dans le champ *Page d'accueil*. À vous de jouer !

Ces options de personnalisation sont aussi désormais disponibles dans la page de description de l'instance (menu *A propos*).

#### La possibilité de chercher des playlists

Que ce soit en naviguant sur Tube ou en utilisant le moteur de recherche [Sepia Search](https://sepiasearch.org), les listes de lecture s'affichent désormais dans les résultats de recherche.

![img](/img/news/release-3.3/fr/FR-playlists-dans-SepiaSearch.png)

#### Des URL publiques plus courtes

C'est une fonctionnalité qui nous a beaucoup été demandée : nous avons mis en place un système pour raccourcir certaines URL publiques. En effet, les identifiants utilisés jusqu'à maintenant étaient un peu longs.

Désormais, l'identifiant unique de la vidéo https://peertube2.cpy.re/videos/watch/3308307a-fbff-4b6d-9740-1a5d3f30a0be (36 caractères tout de même) est raccourci en https://peertube2.cpy.re/w/7iuK5umbCNrcyWYCiK37ph (22 caractères).

Et l'URL de la liste de lecture : https://peertube2.cpy.re/videos/watch/playlist/18ff10e3-3ddb-478c-9b32-f900e1692af5 devient https://peertube2.cpy.re/w/p/462vCJASR7VRjygC4xHqPX.

Comme vous pouvez le constater ci-dessus, nous avons aussi raccourci la syntaxe de nos URL : on utilise `/w/` à la place de `/videos/watch/` et `/w/p/` à la place de `/videos/watch/playlist/`. Et on a étendu cette syntaxe aux comptes et aux chaînes : `/a/` à la place de `/accounts/` et `/c/` à la place de `/video-channels/`.

Bien évidemment, les anciennes URL sont toujours prises en charge. Donc pas d'inquiétude à avoir !

#### Un affichage plus adapté aux langues dont le sens de lecture est sinistroverse

Sinistroquoi ??? Qui aurait cru qu'en rédigeant cette actualité, nous apprendrions de nouveaux mots ? Un sens de lecture sinistroverse, c'est une écriture qui se lit en horizontal de droite à gauche. C'est le sens des écritures arabe et hébreu en particulier.

Tube prend désormais en charge la mise en page RTL (right to left) si vous paramétrez l'interface dans une des langues dont le sens de lecture est sinistroverse. Le menu se déplace intégralement sur la droite et l'ensemble des miniatures sont justifiées à droite.

![img](/img/news/release-3.3/fr/FR-RTL-arabe.png)


#### Et aussi :

Nous avons modifié certains éléments afin d'améliorer les performances de Tube. La récupération des informations d'une vidéo est deux fois plus rapide et nous avons optimisé les requêtes au sein de la fédération. Nous essayons actuellement d'identifier les problèmes de performance que peuvent avoir des instances Tube importantes (avec beaucoup d'utilisateur⋅ices, de vidéos, de spectateur⋅ices ou fédérée avec de nombreuses d'instances). Si vous observez des soucis de scalabilité avec votre instance, n'hésitez pas à nous l'expliquer sur [notre forum](https://framacolibri.org/c/tube/38).

Et puis, nous avons apporté plusieurs modifications au code de Tube (ajout d'une API) afin que les personnes qui développent des plugins puissent en créer de nouveaux qui permettront d'ajouter, modifier ou supprimer des éléments du menu de gauche. Nous espérons donc voir publiés prochainement de nouveaux plugins.

Beaucoup d'autres améliorations ont été apportées dans cette nouvelle version. Vous  pouvez voir la liste complète (en anglais) sur https://github.com/Chocobozzz/Tube/blob/develop/CHANGELOG.md.

Merci à tous les contributeur⋅ices de Tube !
Framasoft
