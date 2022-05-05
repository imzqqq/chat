---
id: release-3.0rc
title: La Release Candidate de la version 3 de Tube est publiée !
date: December 16, 2020
---

<p>Bonjour à toutes et à tous,</p>
<p>La v3 de Tube est presque terminée, et nous venons de publier <a
    href="https://github.com/Chocobozzz/Tube/releases/tag/v3.0.0-rc.1">la RC (release candidate)</a> pour les admins
  qui souhaitent la tester et nous faire des retours afin que nous puissions publier une belle v3 début janvier.</p>
<h4>Un direct en pair-à-pair
  minimaliste et efficace</h4>
<p>La grande fonctionnalité de cette v3 sera la diffusion en direct, et nous sommes
  fier·es
  de pouvoir dire que ça marche très bien ! 🎉🎉🎉</p>
<p>Nous avons eu beaucoup de tests et de retours de la part de pionniers (un gros merci au
  <a href="https://framacolibri.org/t/fonctionnalite-live-retour-dutilisation/10070">Canard Réfractaire</a> pour leur
  aide), et nous sommes sûrs que le direct en pair-à-pair de Tube peut fonctionner avec des centaines de vues en
  simultané (mais ne venez pas nous demander que ça marche avec des milliers. Pas encore).
</p>
<p>Dans les différents tests que nous avons effectués, nous avons réussi à maintenir un
  décalage entre 30 secondes et 1 minute. À notre connaissance, l'utilisation du pair-à-pair dans la diffusion en direct
  va forcément induire un décalage incompressible entre les vidéastes et le public. En fin de compte, ce décalage
  dépendra de la charge sur le serveur (combien de flux en direct ont lieu en même temps), de sa puissance, du
  transcodage de la vidéo...</p>
<p>Nous encourageons les administrateurs à autoriser le transcodage en direct dans le
  cadre
  de la diffusion en continu. Même si cela nécessite une puissance de traitement et induit un certain délai, c'est
  vraiment essentiel pour faciliter l'expérience des vidéastes (qui peuvent utiliser les paramètres OBS de base) et pour
  le public (qui peut regarder dans leur résolution favorite).</p>
<p>Avec cette version 3, les admins d'instances auront la possibilité d'activer (ou non)
  la
  diffusion en direct, de configurer un nombre maximum de flux en cours et en attente (par compte mais aussi pour
  l'ensemble de l'instance). Iels pourront également fixer une durée maximale pour les directs.</p>
<figure><img loading="lazy" src="/img/news/release-3.0rc/fr/2020-05-21_Peertube-Livestream_by-David-Revoy_hires.jpg"
    alt=""></figure>
<h4>Deux façons de configurer votre direct</h4>
<p>Comme vous pouvez le voir <a
    href="https://tube.docs.imzqqq.top/use-create-upload-video?id=publish-a-live-in-tube-gt-v3">dans notre
    documentation sur <i>comment faire un live</i></a>, les vidéastes utilisant la version 3 de Tube auront besoin
  d'un logiciel pour créer le flux de leur direct (nous recommandons le logiciel libre <a
    href="https://obsproject.com">OBS</a>), et utiliser une clé RTMP.</p>
<p>Créer un nouveau direct sera comme téléverser une nouvelle vidéo. Le réglage par défaut
  vous donnera une URL (lien, adresse web) Tube, un conteneur de vidéo (avec description, vignette et tags...), et
  une clé RTMP pour chacun de vos flux. Ce réglage est utile si vous souhaitez diffuser plusieurs directs simultanément
  sur votre chaîne. Lorsque votre live est terminé, il sera remplacé par le replay (si l'administrateur de l'instance et
  le ou la vidéaste ont toutes deux activé ce paramètre).</p>
<p>Les vidéastes auront également la possibilité d'activer le "direct permanent". Cela
  fonctionnera à la manière de Twitch : le lien vers le direct permanent et votre conteneur vidéo correspondront à une
  seule clé RTMP. Vous pouvez commencer le direct et l'arrêter quand vous le souhaitez, le direct sera diffusé sur la
  même URL. Ce paramètre ne permet cependant pas de sauvegarder une vidéo pour la rediffuser.</p>
<h4>Il n'y a pas que le direct dans la vie
</h4>
<p>Cette v3 est
  livrée avec de nombreux changements et améliorations.</p>
<p>Le menu latéral a été complètement retravaillé, grâce au travail de design UX que nous
  avons fait avec Marie Cécile Godwin Paccard. Il est maintenant beaucoup plus facile d'interagir avec votre profil ou
  de distinguer les pages affichant ce qui se trouve dans votre bibliothèque de celles qui montrent les contenus dans la
  bulle de fédération de votre instance.</p>
<p>Les notifications ont été améliorées : maintenant, lorsqu'un compte a été mis en
  sourdine (par un utilisateur ou une administratrice), les notifications de ses actions sont supprimées, ce qui est
  très pratique lorsque quelqu'un a un pic d'activité et que vous ne voulez pas nettoyer vos notifications une par une
  ;).</p>
<p>Les administratrices et les modérateurs disposent, une fois de plus, de nouveaux outils
  utiles dans cette mise à jour. Il y a : une nouvelle page pour faciliter la modération des commentaires, la
  possibilité d'appliquer des actions par lot, l'option de supprimer tous les commentaires d'un compte, ou de voir les
  vidéos non listées téléchargées par un compte sur l'instance que vous modérez.</p>
<figure><img loading="lazy" src="/img/news/release-3.0rc/fr/tube-v3rc.jpg" alt=""></figure>
<h4>Attention : peinture fraîche</h4>
<p>Il y a encore beaucoup à dire sur cette v3, et sur les personnes qui ont contribué à sa
  réalisation. C'est une bonne chose, car nous en parlerons plus longuement en janvier, dans l'article de la sortie de
  la v3.</p>
<p>Nous voulions juste décrire brièvement ce que vous pouvez attendre de cette nouvelle
  version de Tube, et vous présenter notre progression avec cette version Release Candidate.</p>
<p>En attendant, si vous essayez de tester <a
    href="https://github.com/Chocobozzz/Tube/releases/tag/v3.0.0-rc.1">Peertube v3 RC</a> et son direct, n'hésitez
  pas à nous faire part de vos commentaires sur les issues du dépôt de code, ou <a
    href="https://framacolibri.org/c/tube/38">sur notre forum</a>.</p>
<p><span>Passez de bonnes vacances et prenez soin de vous,</span><br> Framasoft </p>
