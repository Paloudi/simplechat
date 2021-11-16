# Cas pratique I : Intégration continue d’un serveur de chat grâce à l’IDE IntelliJ  

Nom :
Prénom :

Nom :
Prénom :

## Présentation du projet

Vous venez de récupérer le code source d’un projet Java dont l’objectif était la création d’un serveur de chat. Malheureusement, ce projet a été abandonné car il ne fonctionnait pas correctement.

L’objectif de ce TP est travailler en binôme afin de finir ce projet et de le valider en s’aidant des principes de l’intégration continue.

## Résultat attendu

Il est attendu un rendu par binôme comprenant :
- le code source du projet
- la configuration des outils utilisés dans l’environnement de travail (projet IntelliJ, répertoire Git, ...)
- le rapport du projet

## Déroulement du TP

### Préparation

* Installer l’éditeur IntelliJ : https://www.jetbrains.com/fr-fr/idea/
* et récupérer les sources du projet SimpleChat : https://git.sr.ht/~toma/simplechat
Lire le fichier `README.md` à la racine du projet pour se familiariser avec celui-ci.

### Le projet

Dans l’IDE IntelliJ, créer un nouveau projet Java à partir des sources du projet SimpleChat.
Il est possible d’intégrer le fichier de compilation `build.gradle` fourni pour simplifier la récupération des dépendances.

Le projet a été récupéré au stade de son abandon : il est tout à fait possible que celui-ci ne compile pas et/ou que certains tests ne passent pas !
On corrigera ces éventuelles erreurs plus tard dans le TP.
Les dépendances externes et les fichiers de compilation `build.gradle` ont cependant été vérifiés et sont de qualité suffisante pour réaliser le TP (seul le code Java est à changer).

### Le gestionnaire de versions

Le gestionnaire de versions a 2 objectifs principaux dans une intégration continue :
* pouvoir partager les modifications de code entre les développeurs du projet de manière sûre
* avoir une référence de code stable pour tester la qualité
  
En intégration continue, il est important d’intégrer le plus régulièrement possible ses modifications (dans un commit) afin de limiter les changements à tester / valider.

* Utiliser le gestionnaire de versions git pour intégrer et partager les modifications du projet au sein du binôme. On pourra utiliser une version hébergée (Github, Bitbucket, Gitlab, ...)
* Configurer IntelliJ pour associer le versionning Git au projet.

### Les tests unitaires et de comportement

La métrique principale dans un pipeline d’intégration continue est souvent le rapport de test.
Celui-ci permet de valider les modifications d’un commit et d’éviter les régressions.

Le projet utilise 2 frameworks de tests :
* Junit 4 pour les tests unitaires (exemple : `MessageTest`)
* Spock (http://spockframework.org/) pour les tests de comportement (exemple : `ChatServerMessageSpec`) 

Il est très compliqué de définir une couverture de test nécessaire et suffisante, car cela dépend énormement du code à tester : on privilégiera donc toujours la qualité du test sur les statistiques de sa couverture de code.
Les classes ayant des dépendances externes sont en général difficiles à tester (ex : on pourra omettre la classe `ChatServerService` qui sera plus facile à tester dans un serveur d’intégration avec toutes les dépéndances du projet).
Au contraire, les classes contenant majoritairement du code métier sont les plus critiques : on essaiera par exemple de tester au moins 75 % des lignes de code présentes dans le package model.

* On activera la couverture de test dans l’IDE afin de vérifier que les classes sont bien testées 
* On ajoutera les tests unitaires et/ou de comportement nécessaires.
* Si besoin, on modifiera le code source de l’application pour corriger les bugs trouvés.
* On pourra aussi lancer l’application et réaliser des tests de fonctionalité à la main.

> Scénarios de test réalisés à la main :

### La qualité du code

La qualité du code ne se limite pas aux tests !
De nombreuses métriques peuvent être mises en place pour limiter les bugs et faciliter la maintenance du code.
Par exemple, pour simplifier la compréhension du code on limitera la profondeur d'heritage dans les classes Java du projet à 2.

* Utiliser les outils d’analyse de l’IDE pour détecter d’éventuels problèmes dans l’application et améliorer la qualité de celle-ci.
* En plus de la profondeur d’héritage, on choisira quelles analyses paraissent pertinentes (on pourra laisser de côté les problèmes liés aux boucles et aux threads qui peuvent demander un refactoring conséquent). Par exemple : < 15 lignes de code dupliquées, ...

> Liste des analyses utilisées dans l’IDE et modifications de code apportées pour améliorer la qualité :


### Un pipeline intégré

Les outils précédents, bien que très efficaces, nous ont obligé à configurer notre IDE de manière adéquate.
Cela peut poser problème lorsque le projet devient conséquent, lors de l’arrivée d’un nouveau membre dans l’équipe, ou en cas de changement d’IDE : les critères de qualité risquent de diverger au sein de l’équipe.
Pour éviter cela, on peut essayer de partager au maximum ces critères de 2 manières :
* en intégrant les outils de vérification de la qualité dans les sources du projet
* en déployant un serveur d’intégration dédié (prochain cours)
Ces 2 méthodes ne sont pas incompatibles.

* En utilisant des plugins Gradle, intégrer les différent critères qualité directement dans la phase de build du projet. On pourra regarder les plugins `pmd`, `findbugs`, `checkstyle` et `jacoco`.
La commande `gradle build` devra donc automatiquement vérifier la qualité du projet.
  
Le plugin `build-dashboard` déjà configuré permet de centraliser l’accès aux rapports après le build dans le fichier `build/reports/buildDashboard/index.html`
