export default {
  data: function () {
    return {
      pluginSelectionsFR: [
        {
          title: 'glavliiit',
          subtitle: 'Un plugin pour améliorer la modération',
          preview: 'glavliiit.png',
          url: 'https://gitlab.com/grin/tube-plugin-glavliiit',
          description: 'Une fois installé, glavliiit permet aux administrateur⋅ices de bloquer l\'inscription sur leur instance de certaines adresses mails en permettant de lister des mails, des noms de domaine ou des noms d\'utilisateur⋅ices à bloquer. Ce plugin permet aussi de bloquer les vidéos contenant certains mots ou expressions. Vraiment pratique !'
        },
        {
          title: 'upload-limits',
          subtitle: 'Un plugin pour alerter sur les limites de téléchargement',
          preview: 'upload-limits.png',
          url: 'https://github.com/kimsible/tube-plugin-upload-limits',
          description: 'Ce plugin permet d\'afficher une alerte lorsqu\'un⋅e utilisateur⋅ice télécharge un contenu qui ne respecte pas les limites de téléchargement (taille du fichier, débit vidéo, débit audio) préalablement définies par l\'instance. Le téléchargement est alors bloqué.'
        },
        {
          title: 'video-annotation',
          subtitle: 'Un plugin pour ajouter des annotations sur une vidéo',
          preview: 'video-annotation.png',
          url: 'https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-video-annotation',
          description: 'Ce plugin d\'annotations permet d\'afficher des informations à un moment donné d\'une vidéo. Pour cela, il suffit d\'aller, au moment de compléter les informations d\'une vidéo uploadée, sur l\'onglet Paramètres du plugin et d\'y ajouter les annotations et leur time-code. Par défaut, le placement des annotations est en haut à droite du lecteur, mais il est possible de modifier cela.'
        },
        {
          title: 'chapters',
          subtitle: 'Un plugin pour ajouter des chapitres à une vidéo',
          preview: 'chapters.png',
          url: 'https://github.com/samlich/tube-plugin-chapters',
          description: 'Ce plugin permet aux vidéastes d\'ajouter un système de chapitrage à leurs vidéos. Ainsi, les spectateur⋅ices ne sont pas obligés de regarder une vidéo dans son intégralité mais peuvent choisir de n\'en visionner que certaines parties. L\'ajout de chapitres se fait très simplement en ajoutant à la vidéo uploadée les noms et time-code de chaque chapitre. Un bouton supplémentaire apparaît alors dans le lecteur.'
        }
      ]
    }
  }
}
