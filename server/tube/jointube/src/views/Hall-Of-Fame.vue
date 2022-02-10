<template>
  <main id="hall-of-fame">
    <div class="blocks">

      <div class="title-block">
        <h1 v-translate>Hall of Fame</h1>

        <div class="separator"></div>
      </div>

      <div class="block sponsors">
        <h2 v-translate>Sponsors</h2>

        <a href="https://qonfucius.com" target="_blank" rel="noopener noreferrer">
          <img :src="buildImgUrl('sponsors/qonfucius.png')" alt="Qonfucius" />
        </a>

        <a href="https://www.ipsolution.fr" target="_blank" rel="noopener noreferrer">
          <img :src="buildImgUrl('sponsors/ipsolution.png')" alt="IP Solution" />
        </a>
      </div>

      <div class="block">
        <h2 v-translate>Financial Contributors</h2>
        <ul>
          <li>0x010C</li>
          <li>3dsman</li>
          <li>3rw4n-G3D</li>
          <li>aallrd</li>
          <li>Adam-Odell</li>
          <li>adechambost</li>
          <li>adim</li>
          <li>adngdb</li>
          <li>Adrien-Horcholle</li>
          <li>Agence-Différente</li>
          <li>Al-Nimr</li>
          <li>Alain-Delgrange</li>
          <li>Alain-Girard</li>
          <li>Alain-MICHEL</li>
          <li>Alexandre-Badez</li>
          <li>Alexandre-Mercier</li>
          <li>Alexandre-Roux-2</li>
          <li>Alexis-Kauffmann</li>
          <li>alfajet</li>
          <li>Alias</li>
          <li>Aliocha-Lang</li>
          <li>André-Rabe</li>
          <li>Anne-PROTAS</li>
          <li>Antoine Derouin</li>
          <li>Antonin-Segault</li>
          <li>ariasuni</li>
          <li>Aris-Papathéodorou</li>
          <li>Arnaud-Risler</li>
          <li>Arnaud-Vigouroux</li>
          <li>Arnulf</li>
          <li>Arthur-Bellier</li>
          <li>Arthur-Charron</li>
          <li>Arthur.Ball</li>
          <li>Arvi-LEFEVRE</li>
          <li>athanael.fr</li>
          <li>Aurélien-Tamisier</li>
          <li>Avel-Musicavel</li>
          <li>axel-guegant</li>
          <li>Aymeric-Dlv</li>
          <li>baptiste-lemoine</li>
          <li>Baptiste-Rochez</li>
          <li>Bea-Schaack-2</li>
          <li>beaufils</li>
          <li>Benjamin-Baratta</li>
          <li>Benjamin-Roussel</li>
          <li>Benoîtdd</li>
          <li>Bernard-Legrand</li>
          <li>Bernard-Vauquelin</li>
          <li>bnjbvr</li>
          <li>bobstechsite</li>
          <li>Brice.Francois</li>
          <li>broz42</li>
          <li>Bruno-Gadaleta</li>
          <li>Bruno-VASTA</li>
          <li>Bumblebee</li>
          <li>Butchcassidy</li>
          <li>Cadiou-Christophe</li>
          <li>carrigns</li>
          <li>case</li>
          <li>Cathy-Barbet</li>
          <li>ccazin</li>
          <li>Cecile-Obernesser</li>
          <li>cgay</li>
          <li>champ contrechamp</li>
          <li>charles-jacquin</li>
          <li>Charlie-Duclut</li>
          <li>Chris-Doe</li>
          <li>Christel-Berthelot</li>
          <li>Christophe-Verhaege</li>
          <li>chud</li>
          <li>Claire-C</li>
          <li>Claude-POUGHEON</li>
          <li>Clément-Hubert</li>
          <li>Clément-Morelle</li>
          <li>clydeb</li>
          <li>Côme Chilliet</li>
          <li>Confederac.io</li>
          <li>Consulting-AZAPTEC</li>
          <li>Corentin3892</li>
          <li>CryoGen</li>
          <li>Cyril</li>
          <li>Cyril-Waechter</li>
          <li>Dams3132</li>
          <li>Daniel-Bartsch</li>
          <li>Daniel-Thul</li>
          <li>Danny-Joerger</li>
          <li>Darth Judge</li>
          <li>Dashcom</li>
          <li>David-BADOIL</li>
          <li>David-Dormoy</li>
          <li>David-Velasco</li>
          <li>David-Wagner</li>
          <li>de Folleville -Matthieu</li>
          <li>DeBugs</li>
          <li>Didier-Bove</li>
          <li>Diego-Crespo</li>
          <li>dino</li>
          <li>Doc Skellington</li>
          <li>Dominique-Brun</li>
          <li>dr4Ke</li>
          <li>DRogueRonin</li>
          <li>Ealhad</li>
          <li>Elric-Noel</li>
          <li>Elwan-Héry</li>
          <li>Emilie-Wietzke</li>
          <li>eparth</li>
          <li>Eric-Bouhana</li>
          <li>Ex-Serv</li>
          <li>Fabien.Abraini</li>
          <li>Fabien.Bonneval</li>
          <li>fabrice-simon</li>
          <li>farlistener</li>
          <li>Flopômpôm</li>
          <li>Florent-Deschamps</li>
          <li>Florent-Fayolle</li>
          <li>Florestan Fournier</li>
          <li>Florian Kohrt</li>
          <li>Florian-Siegenthaler</li>
          <li>fobrice</li>
          <li>FP45</li>
          <li>Francis.Moraud</li>
          <li>Francois-Goer</li>
          <li>François-Lemaire</li>
          <li>François-MORLET</li>
          <li>François-Schoubben</li>
          <li>francois.peyratout</li>
          <li>Frathom</li>
          <li>Frédéric GUÉLEN</li>
          <li>Frédéric-Blumstein</li>
          <li>Frédéric-Meurou</li>
          <li>Frederic-Reynaud</li>
          <li>freyja</li>
          <li>FugazziPL</li>
          <li>Gabriel-Devillers</li>
          <li>GardoToF</li>
          <li>Gaspard-Kemlin</li>
          <li>Gauvain "GovanifY" Roussel-Tarbouriech</li>
          <li>gdquest</li>
          <li>Geek Faëries</li>
          <li>Georges-Dutreix</li>
          <li>Gerrit-Großkopf</li>
          <li>GGBNM</li>
          <li>Gil-Felot</li>
          <li>Gilles-SACLIER</li>
          <li>GinGa</li>
          <li>ginkgopr</li>
          <li>glazzara</li>
          <li>Gof_</li>
          <li>Gonçalves-Daniel</li>
          <li>GRAP-Groupement Régional Alimentaire de Proximité</li>
          <li>Gregouw</li>
          <li>Grummfy</li>
          <li>guiaug</li>
          <li>Guillaume-Chaslot</li>
          <li>Guillaume-David</li>
          <li>gwlolos</li>
          <li>Harald-Eilertsen</li>
          <li>Harpocrate</li>
          <li>Hebus82</li>
          <li>Hellmut</li>
          <li>Henri-ROS</li>
          <li>hguilbert</li>
          <li>hoper</li>
          <li>Hoshin</li>
          <li>Hylm</li>
          <li>Ivan-Ogai</li>
          <li>Jacques-Roos</li>
          <li>James-Moore</li>
          <li>James-Valleroy</li>
          <li>Jan-Aagaard</li>
          <li>Jan-Keromnes</li>
          <li>jano31coa</li>
          <li>Jean CHARPENTIER</li>
          <li>jean claude-skowron</li>
          <li>Jean Dos</li>
          <li>jean-FISCHER</li>
          <li>Jean-François-PETITBON</li>
          <li>Jean-François-Tomasi</li>
          <li>Jean-Galland</li>
          <li>Jean-louis-Bergamo</li>
          <li>Jean-Martin Laval</li>
          <li>Jean-Paul-GIBERT</li>
          <li>Jean-Philippe-Eisenbarth</li>
          <li>Jean-Sébastien-Renaud</li>
          <li>Jean-Yves Kiger</li>
          <li>Jean-Yves-DUPARC</li>
          <li>jeansebastien</li>
          <li>Jérémie -Wach</li>
          <li>Jérôme-Avond</li>
          <li>Jerome-Bu</li>
          <li>Jery</li>
          <li>Jezza</li>
          <li>Jim-McDoniel</li>
          <li>jlcpuzzle</li>
          <li>Joe-Riche</li>
          <li>John-Devor</li>
          <li>John-Doe</li>
          <li>Jojo-Boulix</li>
          <li>Jonas-Aparicio</li>
          <li>Jonathan-Kohler</li>
          <li>Jos-van den Oever</li>
          <li>Jozef-Knaperek</li>
          <li>ju</li>
          <li>jubarbu</li>
          <li>Julie-Bultez</li>
          <li>Julien Loudet</li>
          <li>Julien-AILHAUD</li>
          <li>Julien-Aubin</li>
          <li>Julien-Cochennec</li>
          <li>jyb</li>
          <li>KalambakA</li>
          <li>Kanor</li>
          <li>kari-kimber</li>
          <li>Kdecherf</li>
          <li>Keplerpondorskell</li>
          <li>Kevin-Nguyen</li>
          <li>King-Of Peons</li>
          <li>Kioob</li>
          <li>kloh</li>
          <li>kokoklems</li>
          <li>Konstantin-Kovar</li>
          <li>Kriĉjo</li>
          <li>Kyâne-PICHOU</li>
          <li>L'elfe-Sylvain</li>
          <li>Lara-Dufour</li>
          <li>lareinedeselfes</li>
          <li>Laurent-HEINTZ</li>
          <li>Laurent-PICQUENOT</li>
          <li>ldubost</li>
          <li>Legrave</li>
          <li>Leyokki-Tk</li>
          <li>LibreEnFete-en Tregor</li>
          <li>LilO. Moino</li>
          <li>Liloumuloup</li>
          <li>Linuxine-T</li>
          <li>Louis-Roche</li>
          <li>Lukas-Steiblys</li>
          <li>Mad Sugar</li>
          <li>mahen</li>
          <li>Manuel-Vazquez</li>
          <li>Manumerique</li>
          <li>Marc-BESSIERES</li>
          <li>Marc-DUFOURNET</li>
          <li>Marc-GASSER</li>
          <li>Marc-Honnorat</li>
          <li>Marco-Heisig</li>
          <li>Marie-PACHECO</li>
          <li>Marius-Lemonnier</li>
          <li>Mark-O'Donovan</li>
          <li>mart1n</li>
          <li>Mathieu-Cornic</li>
          <li>matt.faure</li>
          <li>Mattéo-Delabre</li>
          <li>Matthias-Devlamynck</li>
          <li>Matthieu-Bollot</li>
          <li>Matthieu-Dupont de Dinechin</li>
          <li>matthieublanco</li>
          <li>Max-PENY</li>
          <li>Maxime-de WYROW</li>
          <li>Maxime-Forest</li>
          <li>Maxime-Mangel</li>
          <li>Mayeul-Guiraud</li>
          <li>mheiber</li>
          <li>Michael-Koppmann</li>
          <li>Michael-Loew</li>
          <li>Michael-Q. Bid</li>
          <li>Michel-POUSSIER</li>
          <li>Mickaël-Gauvin</li>
          <li>MicMP3Man</li>
          <li>mjhvc</li>
          <li>Moutmout</li>
          <li>mphdp</li>
          <li>mylainos</li>
          <li>nayya</li>
          <li>Neal-Wilson</li>
          <li>neodarz-neodarz</li>
          <li>NepsKi</li>
          <li>NHenry</li>
          <li>nicolas-k</li>
          <li>NicolasCARPi</li>
          <li>nicolaslegland</li>
          <li>niconil</li>
          <li>Niles</li>
          <li>Norbert</li>
          <li>Obrow</li>
          <li>Okki</li>
          <li>Olivier-Calzi</li>
          <li>Olivier-Ganneval</li>
          <li>olivier-pierret</li>
          <li>p3n15634n7</li>
          <li>Pas De-Panique</li>
          <li>Patrice-Jabeneau</li>
          <li>Patrick-MERCIER</li>
          <li>Paul-Härle</li>
          <li>pbramy</li>
          <li>Philippe-Cabaud</li>
          <li>Philippe-Lallemant</li>
          <li>Philippe-Le Van</li>
          <li>philippe-lhardy</li>
          <li>PhilOGM</li>
          <li>Pierre 'catwell' Chapuis</li>
          <li>Pierre-Antoine-Champin</li>
          <li>Pierre-Equoy</li>
          <li>Pierre-Marijon</li>
          <li>Pierre-petch</li>
          <li>Pierrick-Couturier</li>
          <li>PME2050</li>
          <li>pmiossec</li>
          <li>PtrckVllnv</li>
          <li>queertube</li>
          <li>ra-mon</li>
          <li>Radhwan-Ben Madhkour</li>
          <li>Raphaël-Grolimund</li>
          <li>Raven</li>
          <li>Raymond-Lutz</li>
          <li>Razael</li>
          <li>Rebecca-Breu</li>
          <li>Rémi-Herrmann</li>
          <li>Remix-the commons</li>
          <li>rgggn</li>
          <li>Rivinbeg</li>
          <li>Romain Théry-Hermain</li>
          <li>Romain-Ortiz</li>
          <li>RomainVENNE</li>
          <li>Romuald-EYRAUD</li>
          <li>royhome</li>
          <li>Samh</li>
          <li>Sascha-Brendel</li>
          <li>Sebastiaan-Glazenborg</li>
          <li>Sebastian-Hugentobler</li>
          <li>Sébastien Adam</li>
          <li>Septie</li>
          <li>shlagevuk-shlagevuk</li>
          <li>Simon-Hemery</li>
          <li>Simon-Larcher</li>
          <li>sissssou</li>
          <li>skarab</li>
          <li>Skurious</li>
          <li>skynebula</li>
          <li>Sohga-Sohga</li>
          <li>Solène-Rapenne</li>
          <li>Spiderweak</li>
          <li>Stanislas-Michalak</li>
          <li>starmatt</li>
          <li>Stéphane-Girardon</li>
          <li>Stéphanie-Baltus</li>
          <li>Stev-3d</li>
          <li>Stoori</li>
          <li>SuckyStrike</li>
          <li>Sufflope</li>
          <li>SundownDEV</li>
          <li>Syk</li>
          <li>Syluban</li>
          <li>Sylv1c</li>
          <li>Sylvain P</li>
          <li>Sylvain_M</li>
          <li>Sylvain-Cazaux</li>
          <li>Sylvain-GLAIZE</li>
          <li>Sylvestre Ledru</li>
          <li>sylvie-boutet</li>
          <li>Sylvie-TORRES</li>
          <li>Tarulien</li>
          <li>Taunya-Debolt</li>
          <li>Thatoo</li>
          <li>Thibault-Vlieghe</li>
          <li>Thierry-Fenasse</li>
          <li>Thomas-Aurel</li>
          <li>thomas-constans</li>
          <li>thomassin-loucas</li>
          <li>Tim-Albers</li>
          <li>TkPx</li>
          <li>tnntwister</li>
          <li>toto-leroidelasaucisse</li>
          <li>ToumToum</li>
          <li>trigrou</li>
          <li>Tryph</li>
          <li>Un Sur Quatre</li>
          <li>Valerio-Paladino</li>
          <li>Valeryan_24</li>
          <li>Vegattitude</li>
          <li>Vero-Pajot</li>
          <li>Victor -Hery</li>
          <li>Vincent-Corrèze</li>
          <li>VINCENT-PEYRET</li>
          <li>vmorel</li>
          <li>Walter-van Holst</li>
          <li>Watsdesign</li>
          <li>win100</li>
          <li>Xavier ALT</li>
          <li>Xavier-Chantry</li>
          <li>Xavier-Godard</li>
          <li>XoD</li>
          <li>Yann-Delaunoy</li>
          <li>yanselmetti</li>
          <li>Yves-Caniou</li>
          <li>ZeBlackPearl</li>
          <li>ZeGreg</li>
        </ul>
      </div>

      <div class="block">
        <div class="subtitle" v-translate>Contributors</div>
        <ul>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/Chocobozzz">Chocobozzz</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/rigelk">rigelk</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/Nutomic">Nutomic</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/gegeweb">gegeweb</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/joshmorel">joshmorel</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/Jorropo">Jorropo</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/buoyantair">buoyantair</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/bnjbvr">bnjbvr</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/frankstrater">frankstrater</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/jankeromnes">jankeromnes</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/lucas-dclrcq">lucas-dclrcq</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/JohnXLivingston">JohnXLivingston</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/DavidLibeau">DavidLibeau</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/fflorent">fflorent</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/kaiyou">kaiyou</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/ldidry">ldidry</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/McFlat">McFlat</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/DimitriGilbert">DimitriGilbert</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/floSoX">floSoX</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/Green-Star">Green-Star</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/NassimBounouas">NassimBounouas</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/thomaskuntzz">thomaskuntzz</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/rezonant">rezonant</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/Wirebrass">Wirebrass</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/clementbrizard">clementbrizard</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/LecygneNoir">LecygneNoir</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/okhin">okhin</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/daftaupe">daftaupe</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/tcitworld">tcitworld</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/dedesite">dedesite</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/Nautigsam">Nautigsam</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/scanlime">scanlime</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/am97">am97</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/dadall">dadall</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/jonathanraes">jonathanraes</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/yohanboniface">yohanboniface</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/anoadragon453">anoadragon453</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/auberanger">auberanger</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/darnuria">darnuria</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/rhaamo">rhaamo</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/mrflos">mrflos</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/Yetangitu">Yetangitu</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/jocelynj">jocelynj</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/lucaspontoexe">lucaspontoexe</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/flyingrub">flyingrub</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/valvin1">valvin1</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/taziden">taziden</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/sticmac">sticmac</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/barbeque">barbeque</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/luzpaz">luzpaz</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/acid-chicken">acid-chicken</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/louistio">louistio</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/qsypoq">qsypoq</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/daker">daker</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/xyproto">xyproto</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/NaPs">NaPs</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/Anton-Latukha">Anton-Latukha</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/noplanman">noplanman</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/austinheap">austinheap</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/BO41">BO41</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/benabbottnz">benabbottnz</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/ewft">ewft</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/bradsk88">bradsk88</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/Ealhad">Ealhad</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/DeeJayBro">DeeJayBro</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/dirkkelly">dirkkelly</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/Edznux">Edznux</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/ebrehault">ebrehault</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/DatBewar">DatBewar</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/ReK2Fernandez">ReK2Fernandez</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/grizio">grizio</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/Glandos">Glandos</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/lanodan">lanodan</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/HesioZ">HesioZ</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/jacen92">jacen92</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/realityfabric">realityfabric</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/jlebras">jlebras</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/alcalyn">alcalyn</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/mkody">mkody</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/pichouk">pichouk</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/LeoMouyna">LeoMouyna</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/LiPeK">LiPeK</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/Findus23">Findus23</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/zapashcanon">zapashcanon</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/mart-e">mart-e</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/0mp">0mp</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/magopian">magopian</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/mkoppmann">mkoppmann</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/1000i100">1000i100</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/roipoussiere">roipoussiere</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/zallek">zallek</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/zeograd">zeograd</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/gbip">gbip</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/PhieF">PhieF</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/pdurbin">pdurbin</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/quentinDupont">quentinDupont</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/Quenty31">Quenty31</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/sundowndev">sundowndev</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/robinkooli">robinkooli</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/sesn">sesn</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/ALSai">ALSai</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/Simounet">Simounet</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/sschueller">sschueller</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/TrashMacNugget">TrashMacNugget</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/FrozenDroid">FrozenDroid</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/fallen">fallen</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/melongbob">melongbob</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/anmol26s">anmol26s</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/imbsky">imbsky</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/ctlaltdefeat">ctlaltdefeat</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/jomo">jomo</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/libertysoft3">libertysoft3</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/lsde">lsde</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/brain-zhang">brain-zhang</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/norrist">norrist</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/osauzet">osauzet</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/SansPseudoFix">SansPseudoFix</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/tomamplius">tomamplius</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/tuxayo">tuxayo</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/victor-long">victor-long</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/zacharystenger">zacharystenger</a></li>
          <li><a target="_blank" rel="noopener noreferrer" href="https://github.com/ewasion">ewasion</a></li>
        </ul>
      </div>

    </div>
  </main>
</template>

<style lang="scss">
  @import '../scss/_variables.scss';
  @import '../scss/_mixins.scss';

  #hall-of-fame {
    .blocks {
      margin: 60px auto;
    }

    h2 {
      font-size: 24px;
      white-space: nowrap;
      margin: 100px 0 10px 0;
    }

    .sponsors {
      a {
        @include disable-default-a-behaviour;

        display: inline-block;
        margin-right: 10px;

        img {
          width: 270px;
          height: 135px;
        }
      }
    }

    ul {
      padding: 0;

      li {
        display: inline-block;
        padding: 0 5px;

        &:before {
          content: "\25B6";
          margin-right: 12px;
        }

        &:nth-of-type(3n):before {
          color: #f1680d
        }

        &:nth-of-type(3n+1):before {
          color: #211f20
        }

        &:nth-of-type(3n+2):before {
          color: #737373
        }

        a {
          color: #000;
        }
      }
    }

    @media screen and (max-width: $small-screen) {
      .sponsors a img {
        width: 100%;
        height: auto;
      }

      ul li {
        display: block;
      }
    }
  }
</style>

<script>
  export default {
    metaInfo: function () {
      return {
        title: this.$gettext('Hall of Fame')
      }
    }
  }
</script>
