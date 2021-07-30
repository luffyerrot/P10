# Packaging de l'application :  

Après avoir récupérer l'application web, l'API REST et le batch vous n'avez plus qu'à packager les projets.  
Pour cela ouvrer un invite de commandes à la racine d'un des projets et taper : `mvn package`.

# Exécution des projets :

Une fois cela fait pour les trois projets vous pouvez éxécuter la commande : `mvn spring-boot:run` à la racine de chaque projet
dans l'invite de commande.

# Paramétrage de la BDD :

Pour la base de données vous pouvez utiliser les fichiers .SQL dans le fichier nommé __sql__.  
L'un d'eux permet de créer la BDD et les différentes tables et l'autre permet d'insérer les variables.
Le chemin d'accès à la base de données :  
* Hostname = __localhost__
* Port = __3306__
* Username = __root__
* Password = __root__

## Informations de connexion :
	
* Profil Utilisateur : mail - *user@gmail.com* | mot de passe - *user*
* Profil Administrateur : mail - *admin@gmail.com* | mot de passe - *admin*

# Les requêtes Postman :

Dans le dossier back vous trouverez le fichier nommé __postman__, dans lequel vous aurez la collection des requêtes postman ainsi que les tests de l'API Rest.

# Informations utiles :

* *Gestionnaire de projets* - __Maven__ | version - __4.0.0__
* *Frameworks*
	* __Spring Boot__ | version - __2.3.1.RELEASE__
	* __Hibernate__
	* __Thymeleaf__ | version - __3.0.4.RELEASE__
	* __Json__ | version - __20200518__
	* __Spring Boot Mail__ | version - __2.1.9.RELEASE__
	* __Spring Boot Batch__ | version - __2.1.9.RELEASE__
* *java* - version - __1.8__
