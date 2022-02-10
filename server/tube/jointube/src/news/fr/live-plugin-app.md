---
id: live-plugin-app
title: Nouveaux outils externes pour les Live Tube!
date: November 4, 2021
---

Un Tube pimpé avec des logiciels tiers, c'est possible ! Framasoft a financé et accompagné deux développements externes, pour apporter des fonctionnalités intéressantes aux vidéos en direct. Présentation de ces deux nouveaux outils, qui vont apporter du nouveau à vos live.

### Tube Live App : être en direct depuis son smartphone

ℹ️ *Cette application s'adresse aux vidéastes qui souhaitent transmettre en direct depuis un mobile.*

#### Pourquoi Tube Live App ?

Tube c'est la solution libre pour décentraliser ses vidéos et les partager, mais... et si on pouvait diffuser en direct avec son téléphone portable, que ce soit une conférence, un concert ou une manif ? Faire du direct sur une plateforme indépendante, en passant par une application simple à utiliser, ça n'existait pas.

C'est donc une nouvelle opportunité pour Tube, qui ne proposait jusqu'alors que du direct depuis un ordinateur ([https://framablog.org/2021/01/07/tube-v3-ca-part-en-live/](https://framablog.org/2021/01/07/tube-v3-ca-part-en-live/)) ce qui est quand même moins pratique quand on est au milieu de la foule !

![](/img/news/live-plugin-app/fr/meme-sans-tube-live-app.jpg)

C'est un constat chez Framasoft : nous n'avions pas les capacités en interne pour développer une application Android. Nous avons donc cherché en externe et contacté Schoumi, contributeur à [Exodus Privacy](https://exodus-privacy.eu.org/fr/), qui a accepté le projet. Après des premiers échanges en avril 2020, c'est en mai 2021 (le covid nous a un poil ralentis) que l'application Tube Live a commencé à être développée. Après quelques améliorations, nous souhaitons maintenant vous montrer l'outil.


#### Comment faire un live depuis son smartphone ?

Première étape : vous devez avoir un compte sur une instance Tube qui autorise le live (voir les conditions d'utilisation de l'instance). Téléchargez ensuite l'application Tube Live, disponible sur le [Play Store de Google](https://play.google.com/store/apps/details?id=fr.mobdev.peertubelive) et sur le store d'apps libres [F-Droid](https://f-droid.org/fr/packages/fr.mobdev.peertubelive/) (Attention : cette application est uniquement disponible pour les mobiles sous Android).

Nous conseillons ensuite de réaliser la configuration de l'application avant de faire un live (au risque de perdre un peu de temps à choisir les réglages - on préfère prévenir !) :

  * Ajoutez l'adresse web de votre instance et les identifiants de votre compte sur l'interface
  * Cliquez sur le *"+"* en haut à droite
  * Configurez les paramètres du live (Nom de la vidéo, chaîne, visibilité, résolution, rediffusion post live, etc.)
  * Pour passer en live, il vous suffit maintenant d'appuyer sur le gros bouton noir
  * C'est le même bouton qui vous permet de couper le live
  * Si vous avez choisi l'option *"Publier une rediffusion automatiquement à la fin du direct"*, soyez patient⋅e pour la publication sur votre chaîne : le délai est variable selon la durée du live, la qualité choisie ou encore la puissance du serveur qui héberge votre instance

![](/img/news/live-plugin-app/fr/Capture-ecran-PT-Live-App@2x.jpg)


#### Tube Live App a besoin de vous !

Il y a tout même quelques limites importantes à soulever. Tout d'abord, nous n'avons fait que très peu de tests sur l'application. Bref la peinture est fraîche, comme on dit, donc des bugs pourraient survenir.

Ensuite, l'application n'est pas développée par Framasoft. Son évolution et son amélioration dépendent donc entièrement... de vous ! Vous voulez contribuer à l'amélioration de l'application ? Voilà comment participer :

   * Code de l'application (pour les plus techniques d'entre vous) : [https://codeberg.org/Schoumi/PeerTubeLive](https://codeberg.org/Schoumi/PeerTubeLive)
   * Aider à la traduction (accessible même sans savoir coder !) : [https://hosted.weblate.org/projects/tube-live/app/](https://hosted.weblate.org/projects/tube-live/app/)
   * Soutenir Schoumi, le développeur de Tube Live : [https://en.liberapay.com/Schoumi/](https://en.liberapay.com/Schoumi/)


### Tube Live Chat : donner la possibilité à son instance de tchatter pendant les *live*

ℹ️ *Tube Live Chat est un plugin destiné aux administrateur⋅ices d'une instance Tube.*

#### Pourquoi PeerTubeLive Chat ?

Et surtout, pourquoi n'avons-nous pas profité du développement de Tube Live (initial, sur ordinateur) pour ajouter du tchat ? Et bien c'était un choix 100% assumé de se concentrer uniquement sur le live pour commencer (moins complexe et prioritaire).

Nous avons cependant vite remarqué un développeur travaillant sur la fonctionnalité d'un tchat (tout de même bien pratique !) : [John Livingston](http://john-livingston.fr/). Nous lui avons donc proposé de co-financer l'amélioration de son code afin de le rendre plus facile à utiliser pour le grand public.

Le projet est lancé en avril 2021. Il était essentiel que le plugin communique correctement avec le code de Tube, ce qui impliquait d'améliorer l'API des plugins de Tube. Après de nombreux allers-retours entre Chocobozzz (développeur de Tube) et John, le plugin est fin prêt !

#### Comment on l'installe ?

Il est nécessaire d'être administrateur⋅ice d'une instance Tube pour installer le plugin sur son serveur, et de suivre ces étapes :

   * Installation du serveur de messagerie [Prosody](https://prosody.im/) (version 0.11.9  ou suivantes). Vous pouvez vous aider de [la documentation](https://github.com/JohnXLivingston/tube-plugin-livechat/blob/main/documentation/prosody.md)
   * Installation du plugin *"livechat"* via l'interface d'administration de Tube
   * Choisir dans la configuration *"Prosody server controlled by Tube"*

Le tchat s'affichera maintenant lors des vidéos diffusées en live.

#### Comment ça s'utilise ?

Une fois le plugin installé sur l'instance, le public aura ainsi la possibilité de tchatter pendant les vidéos en direct qu'il suivra. Cependant cela sera possible uniquement depuis un ordinateur.

Si vous êtes connecté·e à votre compte Tube, vous serez directement reconnu·e par le plugin. Si vous n'êtes pas connecté·e, il vous suffira d'entrer un pseudo.

![](/img/news/live-plugin-app/fr/PT-Live-Chat-Interface@2x.jpg)

Vous aurez ensuite directement accès au tchat et ses fonctionnalités. La modération pourra être confiée à une ou plusieurs personnes.

![](/img/news/live-plugin-app/fr/Zoom-Fonctionnalites@2x.jpg)

#### Ça peut toujours s'améliorer !

Le plugin étant tout jeune, nous notons quelques points de vigilance :

   * Son installation peut être fastidieuse, voire capricieuse, selon certaines versions du logiciel
   * Nous n'avons pour le moment que très peu testé l'outil
   * L'interface peut-être améliorée afin d'être plus intuitive

À l'heure où nous écrivons cette news, nous apprenons que John Livingston  vient de recevoir un nouveau mécénat de 4000 € de la part de la société [Code Lutin](https://www.codelutin.com/) afin de poursuivre le travail sur Tube Live Chat. C'est une très bonne nouvelle pour ce plugin très utile et qu'on a hâte de voir évoluer.

Si vous aussi vous voulez soutenir ce développement, cela se passe ici : [https://liberapay.com/JohnLivingston/](https://liberapay.com/JohnLivingston/). Et pour contribuer au code, c'est là : [https://github.com/JohnXLivingston/tube-plugin-livechat](https://github.com/JohnXLivingston/tube-plugin-livechat).

#### Contribuer aux contributions

Ces deux outils apportent une belle valeur ajoutée à Tube et à nos libertés de diffusion. Leur amélioration dépend maintenant des contributions qui leur seront apportées.

Nous constatons de plus en plus la richesse  de l'écosystème Tube : [clients](https://tube.docs.dingshunyu.top/use-third-party-application), [plugins](https://joinpeertube.org/plugins-selection)... De nombreuses contributions externes viennent offrir de nouvelles options et de nouvelles libertés à la communauté : merci mille fois !

Si nous avons pu financer et accompagner ces deux développements externes, c'est en partie grâce à la bourse de NLnet dont nous vous parlions en annonçant le chemin vers la [v4 de Tube](https://joinpeertube.org/news#roadmap-v4), et en partie grâce à vos dons qui financent l'ensemble des actions de Framasoft [https://soutenir.framasoft.org](https://soutenir.framasoft.org).

Encore merci de votre confiance !
