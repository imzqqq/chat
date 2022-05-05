---
id: release-3.4
title: Et une 3.4 pour Tube !
date: September 08, 2021
---

Bonjour à toutes et à tous,

Chez Framasoft, on ne passe pas tout l'été à se dorer la pilule au soleil, on prend aussi du temps pour continuer le développement de nos logiciels préférés 😆. Avec cette version 3.4 de Tube, nous vous proposons de nouvelles fonctionnalités afin que votre usage de l'outil soit toujours plus agréable. Petit tour des nouveautés !

#### Des filtres sur toutes les pages Tube

Nous avons ajouté un système de filtrage des vidéos sur les pages des comptes et des chaînes, mais aussi sur les pages de présentation proposées par chaque instance (Tendances / Vidéos ajoutées récemment / Vidéos locales).

Depuis la version 3.2, il était déjà possible de trier l'affichage des vidéos en fonction de plusieurs critères : date de publication, les plus vues, les plus appréciées, les plus longues, etc. Avec ce nouveau système de filtrage, vous avez la possibilité de filtrer les vidéos en fonction de :

 * la langue de la vidéo,
 * le niveau de sensibilité du contenu,
 * la portée de la vidéo : vidéos locales (de l'instance sur laquelle vous êtes) ou fédérées (des instances suivies),
 * le type de vidéos (vidéos en direct ou VOD - ou les 2),
 * la catégorie thématique de la vidéo.

![img](/img/news/release-3.4/fr/FR-filtres.png)

Pour cela, il vous suffit de cliquer sur le bouton *More filters* (plus de filtres) situé en haut à gauche de chaque page où sont listées des vidéos et de compléter les champs proposés. Vous aurez sûrement remarqué qu'à côté de ce bouton *More filters*, apparaissent déjà les filtres qui sont configurés par défaut pour votre compte. Ainsi, sur la capture d'écran ci-dessus, on voit que sont activés les filtres *Sensitive content : hidden* et *Scope : Locales*. C'est bien pratique pour s'y retrouver !

#### S'abonner à une chaîne ou un compte en tant qu'instance

Lorsque vous êtes en charge de l'administration d'une instance Tube, vous pouvez fédérer celle-ci avec d'autres instances. Vous créez ainsi votre bulle de fédération. Il est désormais possible de suivre spécifiquement un compte ou une chaîne sans forcément se fédérer à l'instance qui l'héberge.

Si vous êtes administrateur⋅ice d'une instance et que vous souhaitez suivre un compte ou une chaîne spécifique, c'est très simple, il vous suffit d'aller dans le menu *Administration*, onglet *Fédération*, rubrique *Abonnements*. Apparaît alors la liste des instances auxquelles votre instance s'est abonnée. Si vous cliquez sur le bouton orange, vous pouvez alors ajouter manuellement les identifiants de chaînes ou de comptes. Les utilisateur⋅ices de votre instance auront alors accès aux contenus publiés par cette chaîne (ou ce compte).

![img](/img/news/release-3.4/fr/FR-abo-chaine.png)

#### Filtrer les résultats d'une recherche de vidéos sur une instance

Les filtres disponibles suite à une recherche de vidéos depuis n'importe quelle instance étaient déjà nombreux. Mais nous venons d'ajouter la possibilité de filtrer les vidéos en indiquant une instance précise sur laquelle on souhaite limiter la recherche. Prenons l'exemple d'une recherche de vidéos sur la permaculture. Vous avez identifié la qualité des contenus de l'instance xxxx.xyz et son chouette travail éditorial. Vous pouvez désormais indiquer dans le champ "Hôte de l'instance Tube" l'URL de l'instance sur laquelle vous souhaitez réaliser cette recherche. Vous ne verrez ainsi apparaître dans les résultats que les vidéos traitant de la permaculture disponibles sur cette instance.

![img](/img/news/release-3.4/fr/FR-searchfilter-instance-host.png)

#### Mise à jour du lecteur vidéo

Nous avons mis à jour la bibliothèque HLS.js qui est utilisée par le lecteur vidéo de Tube. Désormais, Tube détecte et mémorise votre niveau de bande passante et évite ainsi le changement de la qualité de votre vidéo lorsque vous lancez sa lecture. Auparavant, le lecteur vous proposait des vidéos en qualité moyenne par défaut et vous pouviez observer un changement de qualité au bout de quelques secondes si vous aviez une bonne connexion. Dorénavant, le lecteur identifie automatiquement le niveau de bande passante lors de votre précédente lecture et vous propose la définition la plus adaptée.

Et si vous souhaitez tout de même choisir la définition de la vidéo que vous voulez visionner, c'est bien évidemment possible. Cette mise à jour permet de prendre en compte immédiatement votre demande de définition spécifique.

#### Et aussi :

Tube prend désormais en charge de manière native l'enregistrement des vidéos sur les systèmes de stockage de données de type `object-storage` (`s3`). Encore en version bêta, cette nouvelle fonctionnalité permet aux administrateur⋅ices d'instances Tube d'héberger les vidéos de leur instance via ce système de stockage à la demande et donc de ne plus à avoir à se soucier de la taille de leur serveur.

Enfin, nous cherchons toujours à identifier les problèmes de performance que peuvent avoir des instances Tube importantes (avec beaucoup d'utilisateur⋅ices, de vidéos, de spectateur⋅ices ou fédérées avec de nombreuses d'instances). Si vous observez des soucis de scalabilité avec votre instance, n'hésitez pas à nous en parler sur [notre forum](https://framacolibri.org/c/tube/).

Beaucoup d'autres améliorations ont été apportées dans cette nouvelle version. Vous  pouvez voir la liste complète (en anglais) sur https://github.com/Chocobozzz/Tube/blob/develop/CHANGELOG.md.

Merci à tous les contributeur⋅ices de Tube !
Framasoft
