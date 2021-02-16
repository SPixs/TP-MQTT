Histoire
--------

	Concu à la fin du XXe siècle :-) par Andy Stanford-Clark (IBM) et Arlen Nipper (EuroTech),
	afin de surveiller un oléoduc dans le désert (bande passante faible et couteuse via un satellite).

	Avantage : leger, peu gourmand en ressources, idéal pour l'IoT.


A) Installation du broker Mosquitto
===================================

sudo apt-get update
sudo apt-get install mosquitto
sudo apt clean

On verifie si le service est lancé :
sudo /etc/init.d/mosquitto status
(ou sudo service mosquitto status)

(au 09/02/2021 Mosquitto version 1.6.9 for MQTT 3.1/3.1.1)

service up :
sudo /etc/init.d/mosquitto start

service down :
sudo /etc/init.d/mosquitto stop

Pour lancer mosquitto en ligne de commande et accéder aux logs console :

1) arreter le service :

sudo service mosquitto stop

2) lancer l'executable qui se situe dans le PATH (/usr/sbin/mosquitto)

mosquitto -v

	1612882583: mosquitto version 1.6.9 starting
	1612882583: Using default config.
	1612882583: Opening ipv4 listen socket on port 1883.
	1612882583: Opening ipv6 listen socket on port 1883.

Le broker ecoute sur le port TCP 1883

Le fichier de configuration par defaut se trouve sous :

/etc/mosquitto/mosquitto.conf

	# Place your local configuration in /etc/mosquitto/conf.d/
	#
	# A full description of the configuration file is at
	# /usr/share/doc/mosquitto/examples/mosquitto.conf.example

	pid_file /var/run/mosquitto.pid

	persistence true
	persistence_location /var/lib/mosquitto/

	log_dest file /var/log/mosquitto/mosquitto.log

	include_dir /etc/mosquitto/conf.d

Si le service doit utiliser une configuration specifique, copier le template dans
/etc/mosquitto/conf.d/ et editer le fichier.

Par exemple, pour mettre en place des connexions securisées, on va être amenés à utiliser
un fichier de credentials. Ajouter dans le fichier de conf :

	allow_anonymous false
	password_file /etc/mosquitto/mqttPassword


Il possible egalement d'invoquer l'exe avec une configuration specifique :

mosquitto -v -c mosquitto.conf

B) Test du broker avec un producer (publisher) et un consumer (subscriber)
==========================================================================

Installer le package additionnel :

sudo apt install mosquitto-clients 

Lancer le consumer MQTT de test sur le topic "ntico" :

mosquitto_sub -t ntico

On constate dans les logs console du serveur l'activité du client :

	1612884666: New connection from 127.0.0.1 on port 1883.
	1612884666: New client connected from 127.0.0.1 as mosq-9HaGvBcjbPSY4Nv1O7 (p2, c1, k60).
	1612884666: No will message specified.
	1612884666: Sending CONNACK to mosq-9HaGvBcjbPSY4Nv1O7 (0, 0)
	1612884666: Received SUBSCRIBE from mosq-9HaGvBcjbPSY4Nv1O7
	1612884666:     ntico (QoS 0)
	1612884666: mosq-9HaGvBcjbPSY4Nv1O7 0 ntico
	1612884666: Sending SUBACK to mosq-9HaGvBcjbPSY4Nv1O7
	1612884726: Received PINGREQ from mosq-9HaGvBcjbPSY4Nv1O7
	1612884726: Sending PINGRESP to mosq-9HaGvBcjbPSY4Nv1O7

Noter la notion de QoS (qualité de service) et la paire PINGREQ/PINGRESP

Lancer le producer MQTT de test sur le topic "ntico" :

mosquitto_pub -t ntico -m "NTI-CO²"

On peut constater l'activité de reception de message par le broker :

	1612887876: New connection from 127.0.0.1 on port 1883.
	1612887876: New client connected from 127.0.0.1 as mosq-VcVBohLWbXzsFGbb2I (p2, c1, k60).
	1612887876: No will message specified.
	1612887876: Sending CONNACK to mosq-VcVBohLWbXzsFGbb2I (0, 0)
	1612887876: Received PUBLISH from mosq-VcVBohLWbXzsFGbb2I (d0, q0, r0, m0, 'ntico', ... (8 bytes))
	1612887876: Sending PUBLISH to mosq-9HaGvBcjbPSY4Nv1O7 (d0, q0, r0, m0, 'ntico', ... (8 bytes))
	1612887876: Sending PUBLISH to mqtt-explorer-a8464b47 (d0, q0, r0, m0, 'ntico', ... (8 bytes))
	1612887876: Received DISCONNECT from mosq-VcVBohLWbXzsFGbb2I
	1612887876: Client mosq-VcVBohLWbXzsFGbb2I disconnected.
	
Et le subscriber affiche le message dans la console :

	NTI-CO²

On peut egalement poster une valeur numerique dans un autre sous-topic :

	 mosquitto_pub -t ntico/measure -m 125.7
	 
On constate que le subscriber n'affiche pas la donnée. En effet, il n'est abonné qu'aux message du topic
racine ntico, et ignore donc tous les autres topic.

Relancer le client avec un nouveau topic d'abonnement :

	mosquitto_sub -t ntico/#
	
Envoyer une nouvelle mesure dans le publisher :

	mosquitto_pub -t ntico/measure -m 125.7
	
Cette fois, le consommateur affiche bien la nouvelle valeur dans la console

Notes : 

(1) grace à un outil comme MQTT Explorer, il est deja possible d'afficher un graph elementaire de l'historique
de ces mesures (à faire pdt le TP).

(2) la charge utile (payload) d'un message MQTT est en binaire. Dans les exemples precedents les clients de test
convertissaient les données en chaine de charactères. Pour envoyer des données plus complexes, nous sommes amenés
à employer la serialisation (générique via par exemple Apache Avro (Kafka) ou manuelle avec un encodage propriétaire).

(3) l'authentication etant désactivée, n'importe quel robot ou client d'exploration peut lire les messages publiés.

(4) la connexion peut être en TCP classique (mqtt://) ou websocket (ws://), eventuellement cryptée (TLS)

	
C) Developpement d'un producer (publisher) full Java
====================================================

On utilisera le client du projet eclipse PAHO.

* 1 client producer et 1 client consummer d'une simple donnée numerique (integer)
* 1 client producer et 1 client consummer de données financières (CSV sur REST) serialisées en JSON


Annexe
======

Explorateur de messages MQTT : http://mqtt-explorer.com/
