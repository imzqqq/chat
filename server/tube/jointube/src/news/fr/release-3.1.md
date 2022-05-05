---
id: release-3.1
title: La 3.1 de Tube est sortie !
date: March 24, 2021
---

<p>Bonjour à toutes et à tous,</p>
<p
 >Après avoir publié la v3 de Tube début janvier, il était temps de publier une
  nouvelle version majeure avec son lot d'améliorations et de nouvelles fonctionnalités. Voici donc quelques
  explications sur ce que vous apporte cette version 3.1.</p>
<h4>Des fonctionnalités de transcodage améliorées
</h4>
<p
 >Transcodage ? Quésaco ? C'est le processus qui convertit un fichier audio ou vidéo d'un
  format d'encodage à un autre, afin de rendre les fichiers multimédias visibles sur différentes plateformes et
  différents appareils. Sur Tube, on utilise le programme FFmpeg pour transcoder les vidéos que vous mettez en
  ligne. D'ailleurs, vous aurez sûrement remarqué le message d'avertissement "La vidéo est en cours de transcodage, il
  est possible qu'elle ne fonctionne pas correctement" qui s'affiche lorsque vous publiez une vidéo. Car tant que
  l'opération de transcodage n'est pas terminée, votre vidéo n'est pas forcément lisible depuis tous vos appareils.</p>
<p
 >Jusqu'à maintenant les règles de transcodage étaient les mêmes sur toutes les instances
  Tube : il n'était pas possible de les modifier. A partir de cette version 3.1, <strong>il est possible de créer
    des profils de transcodage via l'installation de plugins sur une instance</strong>. Les administrateur⋅ices
  d'instances peuvent, après avoir installé un plugin, choisir un profil de transcodage en fonction de leurs besoins.
  Nous espérons qu'iels seront nombreu⋅ses à créer de tels plugins permettant de personnaliser leurs paramètres FFmpeg.
</p>
<p
 >Par exemple, il sera possible de créer un profil de transcodage spécifique qui favorise
  les vidéos en direct (par rapport aux autres vidéos) au niveau de la bande passante. De la même façon, une instance
  Tube spécialisée dans la diffusion de contenus musicaux sera ravie de pouvoir créer un profil de haute qualité
  audio.</p>
<p
 >Cette version 3.1 modifie aussi la gestion des tâches de transcodage. Précédemment, au
  niveau de chaque instance, le transcodage avait lieu chronologiquement, au fur et à mesure de la publication de vidéos
  par les utilisateur⋅ices. Ainsi, lorsqu'un⋅e vidéaste publiait plusieurs vidéos à la suite, il bloquait le transcodage
  des vidéos des autres utilisateur⋅ices de l'instance. Nous avons donc <strong>modifié le système de gestion des
    priorités pour ces tâches de transcodage</strong> afin que lorsqu'un⋅e utilisateur⋅ice publie de nombreux fichiers
  sur une période donnée, on dépriorise le transcodage d'une partie de ses vidéos (elles sont mises en liste d'attente)
  si un⋅e autre utilisateur⋅ice publie un fichier. Dit autrement, on diminue la priorité du transcodage en fonction de
  la quantité de vidéos publiées par l'utilisateur⋅ice au cours des 7 derniers jours. Cela évite qu'un⋅e seul⋅e
  utilisateur⋅ice bloque la mise en ligne des contenus de tou⋅tes les autres : c'est donc plus équitable. D'ailleurs les
  administrateur⋅ices peuvent désormais voir la progression du transcodage des vidéos dans la liste des travaux en cours
  sur leur instance.</p>
<figure><img loading="lazy" src="/img/news/release-3.1/fr/jobs.png" alt=""></figure>
<p
 >Enfin, nous permettons désormais aux administrateur⋅ices d'instances de <strong>définir
    le nombre de vidéos à transcoder en simultané</strong>. Bien sûr, permettre le transcodage de plusieurs vidéos à la
  fois nécessite que l'instance ait une grande puissance de calcul. Si vous souhaitez permettre que plus d'une vidéo
  soit transcodée à la fois sur votre instance, assurez-vous d'avoir le matériel adapté.</p>
<h4>Une interface toujours plus agréable
</h4>
<p
 >Parce que nous savons que l'interface de Tube n'est pas toujours facile à
  comprendre, nous continuons à l'améliorer afin que toutes et tous vous soyez à l'aise en utilisant cet outil.</p>
<p
 >La modification la plus visible, c'est la disparition, dans le menu de gauche de la
  catégorie "Les plus appréciées". En contrepartie, <strong>nous avons ajouté à la catégorie "Tendances" 3 possibilités
    de tri des vidéos</strong> :</p>
<ul>
  <li>
    <em>hot</em> : une sélection des vidéos récentes sur lesquelles il y a le plus d'interactions</li>
  <li>
    <em>vues</em> : la liste des vidéos les plus vues au cours des dernières 24h</li>
  <li><em>likes</em> : les vidéos les
    plus appréciées</li>
</ul>
<figure><img loading="lazy" src="/img/news/release-3.1/fr/trending.png" alt=""></figure>
<p
 >Nous avons modifié quelques éléments dans le menu Administration (accessible uniquement
  aux administrateur⋅ices d'une instance). Par exemple dans l'onglet " Utilisateurs", nous avons déplacé le bouton
  "Créer un utilisateur" à gauche afin de le rendre plus visible. Et on a ajouté la possibilité pour les
  administrateur⋅ices de personnaliser la valeur du quota vidéo (total et journalier) de chaque utilisateur⋅ice.</p>
<figure><img loading="lazy" src="/img/news/release-3.1/fr/quota.png" alt=""></figure>
<h4>Et aussi :</h4>
<p
 >Vous pouvez maintenant vous <strong>abonner facilement à un compte hébergé sur une
    instance différente de celle sur laquelle vous êtes inscrit⋅e</strong> en cliquant sur le bouton "S'abonner" situé
  sous une vidéo puis en y inscrivant votre identifiant tube (username@domaine).</p>
<figure><img loading="lazy" src="/img/news/release-3.1/fr/subscribe.png" alt=""></figure>
<p
 >Les administrateur⋅ices d'une instance peuvent désormais <strong>paramétrer le nombre
    maximum d'imports simultanés de vidéos</strong> (que ces imports soient fait via une URL ou via un torrent). Cela
  permet d'éviter aux grosses instances d'avoir des listes d'attente trop longues.</p>
<p
 >Enfin, nous avons aussi mis en place un système de création asynchrone de torrents afin
  de résoudre des bugs d'interruption du téléversement de certaines vidéos.</p>
<p
 >Beaucoup d'autres améliorations ont été apportées dans cette nouvelle version. Vous
  pouvez voir la liste complète (en anglais) sur <a target="_blank"
    href="https://github.com/Chocobozzz/Tube/blob/develop/CHANGELOG.md">https://github.com/Chocobozzz/Tube/blob/develop/CHANGELOG.md</a>.
</p>
<p><span>Merci à tous les contributeurs de Tube !</span><br> Framasoft </p>
