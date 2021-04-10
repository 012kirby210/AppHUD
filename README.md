# AppHUD
Affichage tête-haute.

L'apk s'installe en mode debug sur android.  

## Configuration :
La configuration s'effectue comme dans toute app android en se servant des gimmick material design.  

On peut définir la police utilisée, le système métrique, et la couleur du texte.

## Comment ça marche :
L'écran est inversé pour permettre au téléphone d'être mis sur le tableau de bord de la voiture. Le reflet sur le pare-brise indique la vitesse.  
La vitesse est calculée en se servant de la puce GPS de l'appareil.    

:exclamation: ça fonctionne mieux avec peu de luminosité extérieure.  

## Options :

Le système embarque un système de prise de POI.  
:warning: il faut activer le paramétrage voix sur votre appareil mobile pour l'option fonctionne.  

L'app est réglée pour intercepter le "ok google". Une fois le "ok google" dit, en disant "checkpoint", une trace au format GPX est déposé dans un fichier 
accessible dans le répertoire de l'app.  
L'ensemble des points GPX ainsi enregistrés formeront un graphe GPX exploitable avec des applications compatibles, telles que josm :  
[ https://www.lwjgl.org/ ](https://www.lwjgl.org/)  

:grey-question: La donnée vocale est capturée par le mobile, puis envoyée aux serveurs de google pour traitement, et revient pour analyse. 
Il convient donc d'avoir du réseau et du forfait pour se servir de l'option.  

Un son de "checkpoint" doit se faire entendre si le point est enregistré.
