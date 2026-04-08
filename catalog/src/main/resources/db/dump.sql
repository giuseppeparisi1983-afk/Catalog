-- MariaDB dump 10.19  Distrib 10.4.21-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: catalog
-- ------------------------------------------------------
-- Server version	10.4.21-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `audio_file`
--

DROP TABLE IF EXISTS `audio_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audio_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `filename` varchar(255) NOT NULL,
  `mime_type` varchar(255) DEFAULT NULL,
  `duration_seconds` int(11) DEFAULT NULL,
  `size_bytes` bigint(20) NOT NULL,
  `formato` enum('MP3','FLAC','WAV') NOT NULL,
  `cover_path` varchar(255) DEFAULT NULL,
  `genere` varchar(255) DEFAULT NULL,
  `autore` varchar(255) DEFAULT NULL,
  `album` varchar(255) DEFAULT NULL,
  `anno_pubblicazione` int(11) DEFAULT NULL,
  `cancelled` tinyint(1) NOT NULL DEFAULT 0,
  `preferito` tinyint(1) NOT NULL DEFAULT 0,
  `rating` int(11) DEFAULT NULL,
  `visualizzazioni` bigint(20) NOT NULL DEFAULT 0,
  `data_archiviazione` DATE DEFAULT (CURRENT_DATE),
  `last_view` DATETIME DEFAULT NULL,
  `backup` tinyint(1) NOT NULL DEFAULT 0,
  `note` varchar(255) DEFAULT NULL,
  `last_update` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chitarra`
--

DROP TABLE IF EXISTS `chitarra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chitarra` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_video` int(11) NOT NULL,
  `visto` tinyint(1) DEFAULT 0,
  `todo` tinyint(1) DEFAULT 0,
  `difficolta` varchar(255) DEFAULT NULL,
  `autore` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_video` (`id_video`),
  CONSTRAINT `chitarra_ibfk_1` FOREIGN KEY (`id_video`) REFERENCES `video` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chitarra`
--

LOCK TABLES `chitarra` WRITE;
/*!40000 ALTER TABLE `chitarra` DISABLE KEYS */;
INSERT INTO `chitarra` VALUES (1,1016,0,0,'intermedio','Tuccio Musumeci'),(2,1017,0,0,'base','Tuccio Musumeci');
/*!40000 ALTER TABLE `chitarra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `documenti`
--

DROP TABLE IF EXISTS `documenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `documenti` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL COMMENT 'Nome documento che il più delle volte coincide con il nome del file.',
  `path_file` varchar(255) DEFAULT NULL COMMENT 'Percorso completo sul PC',
  `dimensione` bigint(20) DEFAULT NULL COMMENT 'Dimensione espressa in MB',
  `data_archiviazione` DATE DEFAULT (CURRENT_DATE) COMMENT 'data di archiviazione del file',
  `last_update` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Ultima modifica',
  `autore` varchar(255) DEFAULT NULL COMMENT 'Autore del documento',
  `descrizione` varchar(255) DEFAULT NULL,
  `categoria` varchar(255) DEFAULT NULL,
  `lingua` varchar(255) DEFAULT NULL COMMENT 'Lingua del documento (es. IT, EN)',
  `versione` int(11) DEFAULT 1,
  `stato` varchar(255) DEFAULT NULL,
  `origine` varchar(255) DEFAULT NULL COMMENT 'Origine del file (es. scanner, download, email)',
  `preferito` tinyint(1) NOT NULL DEFAULT 0,
  `rating` int(11) DEFAULT NULL,
  `visualizzazioni` bigint(20) NOT NULL DEFAULT 0,
  `backup` tinyint(1) NOT NULL DEFAULT 0,
  `note` varchar(255) DEFAULT NULL COMMENT 'Annotazioni aggiuntive',
  `estensione` varchar(255) DEFAULT NULL COMMENT 'Tipo di file (pdf, docx, txt...)',
  `last_view` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documenti`
--

LOCK TABLES `documenti` WRITE;
/*!40000 ALTER TABLE `documenti` DISABLE KEYS */;
INSERT INTO `documenti` VALUES (1,'Fattura_2024_001.pdf','/docs/fatture/2024/Fattura_001.pdf',2,'2024-01-10 09:15:00','2024-01-10 09:15:00','Mario Rossi','Fattura gennaio','fattura','IT',1,'attivo','email',0,4,12,0,NULL,'pdf','2024-01-11 09:00:00.000000'),(2,'Report_Vendite_Q1.pdf','/docs/report/Report_Vendite_Q1.pdf',5,'2024-02-02 13:20:00','2024-02-02 13:20:00','Luca Bianchi','Report trimestrale vendite','report','IT',1,'attivo','download',1,5,45,1,'Analisi dettagliata','pdf','2024-02-05 16:00:00.000000'),(3,'Contratto_Cliente_Alpha.docx','/docs/contratti/Cliente_Alpha.docx',3,'2024-03-01 08:00:00','2024-03-01 08:00:00','Giuseppe Verdi','Contratto di fornitura','contratto','IT',2,'archiviato','scanner',0,3,8,0,NULL,'docx',NULL),(4,'Curriculum_MarcoRossi.pdf','/docs/cv/Marco_Rossi.pdf',1,'2024-01-15 10:00:00','2024-01-15 10:00:00','Marco Rossi','CV aggiornato','Curriculum','IT',1,'attivo','upload',0,4,22,0,NULL,'pdf','2024-01-20 10:00:00.000000'),(5,'Certificato_Corso_Java.pdf','/docs/certificati/Java.pdf',1,'2024-02-10 07:30:00','2024-02-10 07:30:00','Anna Neri','Certificazione Java','certificati','EN',1,'attivo','email',1,5,60,1,NULL,'pdf','2024-02-12 09:00:00.000000'),(6,'Guida_Installazione_Software.pdf','/docs/guide/installazione.pdf',4,'2024-01-22 12:00:00','2024-01-22 12:00:00','Tech Team','Guida tecnica','guida','IT',1,'attivo','download',0,4,33,0,NULL,'pdf',NULL),(7,'Articolo_AI_2024.txt','/docs/articoli/AI_2024.txt',1,'2024-03-05 16:00:00','2024-03-05 16:00:00','Redazione','Articolo sull’AI','articolo','IT',1,'attivo','email',0,3,15,0,NULL,'txt',NULL),(8,'Fattura_2024_002.pdf','/docs/fatture/2024/Fattura_002.pdf',3,'2024-01-12 08:30:00','2024-01-12 08:30:00','Mario Rossi','Fattura febbraio','fattura','IT',1,'attivo','email',0,4,18,0,NULL,'pdf','2024-01-13 10:00:00.000000'),(9,'Report_Sicurezza_2024.pdf','/docs/report/sicurezza.pdf',6,'2024-02-18 09:00:00','2024-02-18 09:00:00','Luca Bianchi','Report sicurezza annuale','report','IT',1,'archiviato','download',0,5,70,1,NULL,'pdf',NULL),(10,'Contratto_Cliente_Beta.docx','/docs/contratti/Cliente_Beta.docx',4,'2024-03-03 11:00:00','2024-03-03 11:00:00','Giuseppe Verdi','Contratto servizio','contratto','IT',1,'attivo','scanner',0,4,10,0,NULL,'docx',NULL),(11,'Fattura_2024_001_RossiSpa.pdf','/archivio/fatture/2024/Fattura_001_RossiSpa.pdf',3,'2024-01-05 08:12:00','2024-01-05 08:12:00','Ufficio Amministrazione','Fattura gennaio Rossi S.p.A.','fattura','IT',1,'attivo','email',0,4,18,0,NULL,'pdf','2024-01-06 10:00:00.000000'),(12,'Fattura_2024_002_BianchiSRL.pdf','/archivio/fatture/2024/Fattura_002_BianchiSRL.pdf',2,'2024-02-03 09:00:00','2024-02-03 09:00:00','Ufficio Amministrazione','Fattura febbraio Bianchi Srl','fattura','IT',1,'attivo','email',0,5,25,0,NULL,'pdf','2024-02-04 09:00:00.000000'),(13,'Fattura_2024_003_AlfaGroup.pdf','/archivio/fatture/2024/Fattura_003_AlfaGroup.pdf',4,'2024-03-04 10:30:00','2024-03-04 10:30:00','Ufficio Amministrazione','Fattura marzo Alfa Group','fattura','IT',1,'attivo','email',1,4,30,1,NULL,'pdf','2024-03-05 08:00:00.000000'),(14,'Fattura_2023_012_Storico.pdf','/archivio/fatture/2023/Fattura_012_Storico.pdf',3,'2023-12-10 13:00:00','2023-12-10 13:00:00','Ufficio Amministrazione','Ultima fattura 2023','fattura','IT',1,'archiviato','scanner',0,3,12,0,NULL,'pdf',NULL),(15,'Fattura_2022_Archivio_007.pdf','/archivio/fatture/2022/Fattura_007.pdf',2,'2022-07-15 07:00:00','2022-07-15 07:00:00','Ufficio Amministrazione','Fattura storica','fattura','IT',1,'archiviato','scanner',0,2,5,0,NULL,'pdf',NULL),(16,'Report_Vendite_Q1_2024.pdf','/report/2024/Report_Vendite_Q1.pdf',6,'2024-04-01 06:00:00','2024-04-01 06:00:00','Luca Bianchi','Analisi vendite Q1','report','IT',1,'attivo','download',1,5,80,1,NULL,'pdf','2024-04-02 09:00:00.000000'),(17,'Report_Vendite_Q2_2024.pdf','/report/2024/Report_Vendite_Q2.pdf',7,'2024-07-01 06:00:00','2024-07-01 06:00:00','Luca Bianchi','Analisi vendite Q2','report','IT',1,'attivo','download',0,4,65,1,NULL,'pdf',NULL),(18,'Report_Sicurezza_2023.pdf','/report/2023/Report_Sicurezza.pdf',5,'2023-11-20 09:00:00','2023-11-20 09:00:00','Team Sicurezza','Report annuale sicurezza','report','IT',1,'archiviato','email',0,5,120,1,NULL,'pdf',NULL),(19,'Report_IT_Infrastruttura.pdf','/report/it/infrastruttura.pdf',4,'2024-02-15 08:00:00','2024-02-15 08:00:00','Team IT','Stato infrastruttura IT','report','IT',1,'attivo','download',0,4,40,0,NULL,'pdf',NULL),(20,'Contratto_Cliente_Alpha_2024.docx','/contratti/2024/Cliente_Alpha.docx',3,'2024-01-20 09:00:00','2024-01-20 09:00:00','Giuseppe Verdi','Contratto fornitura Alpha','contratto','IT',2,'attivo','scanner',0,4,15,0,NULL,'docx',NULL),(21,'Contratto_Cliente_Beta_2023.docx','/contratti/2023/Cliente_Beta.docx',4,'2023-06-10 09:00:00','2023-06-10 09:00:00','Giuseppe Verdi','Contratto servizio Beta','contratto','IT',1,'archiviato','scanner',0,3,9,0,NULL,'docx',NULL),(22,'Contratto_Cliente_Gamma_2022.docx','/contratti/2022/Cliente_Gamma.docx',3,'2022-03-15 08:00:00','2022-03-15 08:00:00','Giuseppe Verdi','Contratto storico Gamma','contratto','IT',1,'archiviato','scanner',0,2,4,0,NULL,'docx',NULL),(23,'Contratto_Interno_Manutenzione.docx','/contratti/interni/manutenzione.docx',2,'2024-02-01 13:00:00','2024-02-01 13:00:00','Team IT','Contratto manutenzione interna','contratto','IT',1,'attivo','email',1,5,22,0,NULL,'docx',NULL),(24,'CV_Marco_Rossi.pdf','/cv/Marco_Rossi.pdf',1,'2024-01-12 08:00:00','2024-01-12 08:00:00','Marco Rossi','Curriculum aggiornato','Curriculum','IT',1,'attivo','upload',0,4,20,0,NULL,'pdf','2024-01-13 10:00:00.000000'),(25,'CV_Laura_Bianchi.pdf','/cv/Laura_Bianchi.pdf',1,'2024-02-10 09:00:00','2024-02-10 09:00:00','Laura Bianchi','Candidatura area marketing','Curriculum','IT',1,'attivo','upload',0,5,18,0,NULL,'pdf',NULL),(26,'CV_Andrea_Neri.pdf','/cv/Andrea_Neri.pdf',1,'2023-12-01 07:00:00','2023-12-01 07:00:00','Andrea Neri','Candidatura area IT','Curriculum','IT',1,'archiviato','email',0,3,10,0,NULL,'pdf',NULL),(27,'CV_Sara_Conti.pdf','/cv/Sara_Conti.pdf',1,'2024-03-01 08:00:00','2024-03-01 08:00:00','Sara Conti','Candidatura HR','Curriculum','IT',1,'attivo','upload',1,5,28,0,NULL,'pdf','2024-03-02 11:00:00.000000'),(28,'Certificato_Corso_Java_2024.pdf','/certificati/java_2024.pdf',1,'2024-02-20 07:00:00','2024-02-20 07:00:00','Anna Neri','Certificazione Java','certificati','EN',1,'eliminato','email',1,5,60,1,NULL,'pdf','2024-02-21 09:00:00.000000'),(29,'Certificato_Sicurezza_2023.pdf','/certificati/sicurezza_2023.pdf',1,'2023-09-10 08:00:00','2023-09-10 08:00:00','Luca Bianchi','Certificazione sicurezza','certificati','IT',1,'archiviato','email',0,4,25,0,NULL,'pdf',NULL),(30,'Guida_Installazione_Server.pdf','/guide/installazione_server.pdf',5,'2024-01-18 13:00:00','2024-01-18 13:00:00','Team IT','Guida tecnica server','guida','IT',1,'attivo','download',0,5,90,1,NULL,'pdf',NULL),(31,'Guida_Utilizzo_AppMobile.pdf','/guide/app_mobile.pdf',3,'2024-02-05 10:00:00','2024-02-05 10:00:00','Team UX','Guida app mobile','guida','IT',1,'attivo','download',0,4,55,0,NULL,'pdf',NULL),(32,'Articolo_AI_Tendenze2024.txt','/articoli/AI_Tendenze2024.txt',1,'2024-03-10 14:00:00','2024-03-10 14:00:00','Redazione Tech','Tendenze AI 2024','articolo','IT',1,'attivo','email',0,4,40,0,NULL,'txt',NULL),(33,'Articolo_CyberSecurity_2023.txt','/articoli/CyberSecurity_2023.txt',1,'2023-10-12 07:00:00','2023-10-12 07:00:00','Redazione Tech','Analisi cyber security','articolo','IT',1,'eliminato','email',0,3,22,0,NULL,'txt',NULL);
/*!40000 ALTER TABLE `documenti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `film`
--

DROP TABLE IF EXISTS `film`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `film` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `titolo` varchar(100) NOT NULL,
  `genere` varchar(50) DEFAULT NULL,
  `voto` decimal(2,1) DEFAULT NULL CHECK (`voto` >= 0.0 and `voto` <= 5.0),
  `regista` varchar(100) DEFAULT NULL,
  `protagonisti` longtext DEFAULT NULL,
  `anno_uscita` int(11) DEFAULT NULL,
  `durata` varchar(10) DEFAULT NULL,
  `trama` longtext DEFAULT NULL,
  `trailer` varchar(255) DEFAULT NULL,
  `path_file` varchar(255) DEFAULT NULL COMMENT 'Percorso completo sul PC',
  `preferito` tinyint(1) NOT NULL DEFAULT 0,
  `filename` varchar(255) NOT NULL,
  `formato` enum('MP4','MKV','AVI','MOV') NOT NULL,
  `size_bytes` bigint(20) NOT NULL,
  `cancelled` tinyint(1) NOT NULL DEFAULT 0,
  `visualizzazioni` bigint(20) NOT NULL DEFAULT 0,
  `data_archiviazione` DATE DEFAULT (CURRENT_DATE),
  `last_view` datetime DEFAULT NULL,
  `backup` tinyint(1) NOT NULL DEFAULT 0,
  `note` varchar(255) DEFAULT NULL,
  `last_update` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `film`
--

LOCK TABLES `film` WRITE;
/*!40000 ALTER TABLE `film` DISABLE KEYS */;
INSERT INTO `film` VALUES (1,'Il Padrino','Drammatico',4.9,'Francis Ford Coppola','Michael Corleone (Al Pacino), Vito Corleone (Marlon Brando)',1972,'175','La storia della famiglia mafiosa Corleone e del suo erede Michael.','https://www.youtube.com/watch?v=UaVTIH8mujA','C:/Videos/Movies/Il_Padrino.mp4',0,'Il_Padrino.mp4','MP4',1501234567,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(2,'Le Ali della Libertà','Drammatico',4.8,'Frank Darabont','Andy Dufresne (Tim Robbins), Ellis Boyd Redding (Morgan Freeman)',1994,'142','Un uomo ingiustamente condannato trova speranza e amicizia in prigione.','https://www.youtube.com/watch?v=6hB3S9bIaco','C:/Videos/Movies/Le_Ali_della_Libertà.mp4',0,'Le_Ali_della_Libertà.mp4','MP4',1502469134,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(3,'Il Cavaliere Oscuro','Azione, Fantasy',4.6,'Christopher Nolan','Bruce Wayne (Christian Bale), Joker (Heath Ledger)',2008,'152','Batman affronta il Joker, un criminale psicopatico che semina il caos a Gotham.','https://www.youtube.com/watch?v=EXeTwQWrcwY','C:/Videos/Movies/Il_Cavaliere_Oscuro.mp4',0,'Il_Cavaliere_Oscuro.mp4','AVI',1503703701,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(4,'Pulp Fiction','Drammatico, Thriller',4.5,'Quentin Tarantino','Vincent Vega (John Travolta), Jules Winnfield (Samuel L. Jackson)',1994,'154','Storie intrecciate di criminali, pugili e gangster a Los Angeles.','https://www.youtube.com/watch?v=s7EdQ4FqbhY','C:/Videos/Movies/Pulp_Fiction.mp4',0,'Pulp_Fiction.mp4','MKV',1504938268,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(5,'Schindler\'s List','Drammatico, Guerra',4.7,'Steven Spielberg','Oskar Schindler (Liam Neeson), Itzhak Stern (Ben Kingsley)',1993,'195','Un industriale tedesco salva centinaia di ebrei durante l\'Olocausto.','https://www.youtube.com/watch?v=gG22XNhtnoY','C:/Videos/Movies/Schindler\'s_List.mp4',0,'Schindler\'s_List.mp4','MP4',1506172835,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(6,'Il Signore degli Anelli: La Compagnia degli anelli','Fantasy, Avventura',4.4,'Peter Jackson','Frodo Baggins (Elijah Wood), Gandalf (Ian McKellen)',2001,'178','Un giovane hobbit intraprende un viaggio per distruggere un potente anello.','https://www.youtube.com/watch?v=V75dMMIW2B4','C:/Videos/Movies/Il_Signore_degli_Anelli:_La_Compagnia_degli_anelli.mp4',0,'Il_Signore_degli_Anelli:_La_Compagnia_degli_anelli.mp4','AVI',1507407402,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(7,'Fight Club','Drammatico',4.3,'David Fincher','Narratore (Edward Norton), Tyler Durden (Brad Pitt)',1999,'139','Un uomo insoddisfatto fonda un club segreto di combattimento.','https://www.youtube.com/watch?v=SUXWAEX2jlg','C:/Videos/Movies/Fight_Club.mp4',0,'Fight_Club.mp4','MP4',1508641969,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(8,'Inception','Fantascienza, Thriller',4.5,'Christopher Nolan','Dom Cobb (Leonardo DiCaprio), Arthur (Joseph Gordon-Levitt)',2010,'148','Un ladro entra nei sogni per impiantare idee nella mente delle persone.','https://www.youtube.com/watch?v=YoHD9XEInc0','C:/Videos/Movies/Inception.mp4',0,'Inception.mp4','MKV',1509876536,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(9,'Forrest Gump','Drammatico, Commedia',4.6,'Robert Zemeckis','Forrest Gump (Tom Hanks), Jenny Curran (Robin Wright)',1994,'142','La vita straordinaria di un uomo semplice che attraversa la storia americana.','https://www.youtube.com/watch?v=bLvqoHBptjg','C:/Videos/Movies/Forrest_Gump.mp4',0,'Forrest_Gump.mp4','AVI',1511111103,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(10,'Matrix','Fantascienza, Azione',4.5,'Lana e Lilly Wachowski','Neo (Keanu Reeves), Morpheus (Laurence Fishburne)',1999,'136','Un hacker scopre che la realtà è una simulazione controllata da macchine.','https://www.youtube.com/watch?v=vKQi3bBA1y8','C:/Videos/Movies/Matrix.mp4',0,'Matrix.mp4','MP4',1512345670,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(11,'Il marchese del Grillo','Commedia storica',4.6,'Mario Monicelli','Onofrio del Grillo (Alberto Sordi), Papa Pio VII (Paolo Stoppa)',1981,'135','Nella Roma papalina del 1809, il marchese Onofrio del Grillo si diverte a fare scherzi e beffe, sfidando le convenzioni sociali.','https://www.youtube.com/watch?v=0pjed6G--18','C:/Videos/Movies/Il_marchese_del_Grillo.mp4',0,'Il_marchese_del_Grillo.mp4','MP4',1513580237,1,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(12,'Benvenuti al Sud','Commedia',4.2,'Luca Miniero','Alberto Colombo (Claudio Bisio), Mattia Volpe (Alessandro Siani)',2010,'102','Un impiegato del Nord viene trasferito in Campania e scopre un mondo diverso da quello che immaginava.','https://www.youtube.com/watch?v=8kYkciD9VjU','C:/Videos/Movies/Benvenuti_al_Sud.mp4',0,'Benvenuti_al_Sud.mp4','MKV',1514814804,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(13,'Avatar','Fantascienza, Avventura',4.5,'James Cameron','Jake Sully (Sam Worthington), Neytiri (Zoe Saldana)',2009,'162','Un ex marine si unisce agli alieni Na’vi su Pandora e si trova coinvolto in una lotta per la sopravvivenza.','https://www.youtube.com/watch?v=5PSNL1qE6VY','C:/Videos/Movies/Avatar.mp4',0,'Avatar.mp4','MP4',1516049371,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(14,'Spider-Man','Azione, Supereroi',4.3,'Sam Raimi','Peter Parker (Tobey Maguire), Mary Jane Watson (Kirsten Dunst)',2002,'121','Un giovane acquisisce poteri da ragno e combatte il crimine mentre cerca di vivere una vita normale.','https://www.youtube.com/watch?v=t06RUxPbp_c','C:/Videos/Movies/Spider-Man.mp4',0,'Spider-Man.mp4','MP4',1517283938,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(15,'Avengers: Infinity War','Azione, Fantascienza',4.6,'Anthony e Joe Russo','Iron Man (Robert Downey Jr.), Thanos (Josh Brolin)',2018,'149','I supereroi si uniscono per fermare Thanos, che vuole distruggere metà dell’universo.','https://www.youtube.com/watch?v=6ZfuNTqbHE8','C:/Videos/Movies/Avengers:_Infinity_War.mp4',0,'Avengers:_Infinity_War.mp4','AVI',1518518505,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(16,'Titanic','Drammatico, Romantico',4.7,'James Cameron','Jack Dawson (Leonardo DiCaprio), Rose DeWitt Bukater (Kate Winslet)',1997,'195','Una storia d’amore tra due giovani di classi diverse sullo sfondo del naufragio del Titanic.','https://www.youtube.com/watch?v=kVrqfYjkTdQ','C:/Videos/Movies/Titanic.mp4',0,'Titanic.mp4','MKV',1519753072,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(17,'Ti amo in tutte le lingue del mondo','Commedia romantica',3.8,'Leonardo Pieraccioni','Gilberto (Leonardo Pieraccioni), Margherita (Marjo Berasategui)',2005,'95','Un professore riceve lettere d’amore da una sua giovane studentessa, creando equivoci e situazioni comiche.','https://www.youtube.com/watch?v=ZzqVYxQWZzI','C:/Videos/Movies/Ti_amo_in_tutte_le_lingue_del_mondo.mp4',0,'Ti_amo_in_tutte_le_lingue_del_mondo.mp4','MP4',1520987639,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(18,'Miseria e Nobiltà','Commedia',4.4,'Mario Mattoli','Felice Sciosciammocca (Totò), Luigino (Enzo Turco)',1954,'95','Un gruppo di poveri si finge nobile per aiutare un giovane innamorato a sposare una ragazza ricca.','https://www.youtube.com/watch?v=HqvZKXKzKZg','C:/Videos/Movies/Miseria_e_Nobiltà.mp4',0,'Miseria_e_Nobiltà.mp4','AVI',1522222206,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(19,'Lo chiamavano Trinità','Western, Commedia',4.5,'Enzo Barboni','Trinità (Terence Hill), Bambino (Bud Spencer)',1970,'106','Due fratelli pistoleri difendono dei pacifici mormoni da un gruppo di banditi.','https://www.youtube.com/watch?v=5z8fU5xG5uA','C:/Videos/Movies/Lo_chiamavano_Trinità.mp4',0,'Lo_chiamavano_Trinità.mp4','MP4',1523456773,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(20,'Non ci resta che piangere','Commedia, Fantastico',4.6,'Roberto Benigni e Massimo Troisi','Mario (Massimo Troisi), Saverio (Roberto Benigni)',1984,'113','Due amici si ritrovano misteriosamente nel 1492 e cercano di tornare nel presente.','https://www.youtube.com/watch?v=3gJ2X9b0Yqk','C:/Videos/Movies/Non_ci_resta_che_piangere.mp4',0,'Non_ci_resta_che_piangere.mp4','MKV',1524691340,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(21,'Febbre da cavallo','Commedia',4.3,'Steno','Mandrake (Gigi Proietti), Pomata (Enrico Montesano)',1976,'96','Tre amici appassionati di corse di cavalli cercano di arricchirsi con scommesse improbabili.','https://www.youtube.com/watch?v=KZJvHkZJY7g','C:/Videos/Movies/Febbre_da_cavallo.mp4',0,'Febbre_da_cavallo.mp4','AVI',1525925907,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(22,'La storia fantastica','Fantasy, Avventura',4.4,'Rob Reiner','Westley (Cary Elwes), Buttercup (Robin Wright)',1987,'98','Un giovane contadino affronta mille pericoli per salvare la sua amata principessa.','https://www.youtube.com/watch?v=VYgcrny2hRs','C:/Videos/Movies/La_storia_fantastica.mp4',0,'La_storia_fantastica.mp4','MP4',1527160474,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(23,'Il ladrone','Commedia',3.9,'Pasquale Festa Campanile','Caleb (Enrico Montesano), Gesù (Claudio Cassinelli)',1980,'100','Un ladro si finge profeta ai tempi di Gesù, ma finisce per incrociare il vero Messia.','https://www.youtube.com/watch?v=3XKZKXKXKX','C:/Videos/Movies/Il_ladrone.mp4',0,'Il_ladrone.mp4','MP4',1528395041,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(24,'Il sapore della vittoria','Drammatico, Sportivo',4.2,'Boaz Yakin','Coach Boone (Denzel Washington), Petey Jones (Donald Faison)',2000,'113','Un allenatore afroamericano guida una squadra di football in un periodo di tensioni razziali.','https://www.youtube.com/watch?v=35MvdHB5YyA','C:/Videos/Movies/Il_sapore_della_vittoria.mp4',0,'Il_sapore_della_vittoria.mp4','MKV',1529629608,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(25,'Star Wars: La vendetta dei Sith','Fantascienza, Azione',4.3,'George Lucas','Anakin Skywalker (Hayden Christensen), Obi-Wan Kenobi (Ewan McGregor)',2005,'140','La caduta di Anakin Skywalker e la nascita di Darth Vader.','https://www.youtube.com/watch?v=5UnjrG_N8hU','C:/Videos/Movies/Star_Wars:_La_vendetta_dei_Sith.mp4',0,'Star_Wars:_La_vendetta_dei_Sith.mp4','MP4',1530864175,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(26,'Star Trek Beyond','Fantascienza, Avventura',4.0,'Justin Lin','James T. Kirk (Chris Pine), Spock (Zachary Quinto)',2016,'122','L’equipaggio dell’Enterprise affronta una nuova minaccia in una zona inesplorata dello spazio.','https://www.youtube.com/watch?v=XRVD32rnzOw','C:/Videos/Movies/Star_Trek_Beyond.mp4',0,'Star_Trek_Beyond.mp4','MP4',1532098742,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(27,'Una vita difficile','Drammatico, Commedia',4.1,'Dino Risi','Silvio Magnozzi (Alberto Sordi), Elena (Lea Massari)',1961,'118','Un ex partigiano cerca di mantenere i suoi ideali in un’Italia che cambia.','https://www.youtube.com/watch?v=2XKZKXKXKX','C:/Videos/Movies/Una_vita_difficile.mp4',0,'Una_vita_difficile.mp4','AVI',1533333309,0,0,'2023-10-01 08:00:00',NULL,0,NULL,'2026-04-03 15:09:12'),(28,'Interstellar','Fantascienza',4.8,'Christopher Nolan','Matthew McConaughey, Anne Hathaway',2014,'169','Un gruppo di astronauti viaggia attraverso un wormhole alla ricerca di una nuova casa per l\'umanità.','https://www.youtube.com/watch?v=zSWdZVtXT7E','C:/Videos/Movies/Interstellar.mkv',1,'Interstellar.mkv','MKV',4500000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(29,'Il Gladiatore','Azione, Storico',4.7,'Ridley Scott','Russell Crowe, Joaquin Phoenix',2000,'155','Un generale romano tradito cerca vendetta contro il corrotto imperatore che ha ucciso la sua famiglia.','https://www.youtube.com/watch?v=P5ieIbInFpg','C:/Videos/Movies/Gladiatore.mp4',1,'Gladiatore.mp4','MP4',2800000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(30,'La Vita è Bella','Drammatico, Guerra',4.9,'Roberto Benigni','Roberto Benigni, Nicoletta Braschi',1997,'116','Un cameriere ebreo usa la sua immaginazione per proteggere il figlio dagli orrori di un campo di concentramento.','https://www.youtube.com/watch?v=8CTjcV8_V_A','C:/Videos/Movies/La_Vita_e_Bella.mp4',1,'La_Vita_e_Bella.mp4','MP4',1800000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(31,'The Wolf of Wall Street','Biografico, Commedia',4.5,'Martin Scorsese','Leonardo DiCaprio, Jonah Hill',2013,'180','L\'ascesa e la caduta di Jordan Belfort, un broker di New York dedito a eccessi e frodi.','https://www.youtube.com/watch?v=iszwuX1AK6A','C:/Videos/Movies/Wolf_Wall_Street.mkv',0,'Wolf_Wall_Street.mkv','MKV',3500000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(32,'Seven','Thriller, Noir',4.6,'David Fincher','Brad Pitt, Morgan Freeman',1995,'127','Due detective danno la caccia a un serial killer che usa i sette peccati capitali come movente.','https://www.youtube.com/watch?v=znmZoVkCjpI','C:/Videos/Movies/Seven.mp4',0,'Seven.mp4','MP4',2100000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(33,'Django Unchained','Western',4.5,'Quentin Tarantino','Jamie Foxx, Christoph Waltz',2012,'165','Uno schiavo liberato diventa un cacciatore di taglie per salvare sua moglie da un brutale proprietario di piantagione.','https://www.youtube.com/watch?v=0fUCuvNlOCg','C:/Videos/Movies/Django.mkv',0,'Django.mkv','MKV',3200000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(34,'Parasite','Thriller, Drammatico',4.6,'Bong Joon-ho','Song Kang-ho, Lee Sun-kyun',2019,'132','Una famiglia povera si insinua con l\'inganno nella vita di una ricca famiglia coreana.','https://www.youtube.com/watch?v=5xH0HfJHsaY','C:/Videos/Movies/Parasite.mp4',1,'Parasite.mp4','MP4',2400000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(35,'C\'era una volta il West','Western',4.7,'Sergio Leone','Henry Fonda, Claudia Cardinale',1968,'165','Una storia di vendetta e progresso durante la costruzione della ferrovia nel West.','https://www.youtube.com/watch?v=MNGQ1hUyx-k','C:/Videos/Movies/Once_West.mp4',0,'Once_West.mp4','MP4',2600000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(36,'Joker','Drammatico, Crime',4.4,'Todd Phillips','Joaquin Phoenix, Robert De Niro',2019,'122','Le origini del celebre villain di Gotham City in una chiave cruda e psicologica.','https://www.youtube.com/watch?v=zAGVQLHvwOY','C:/Videos/Movies/Joker.mkv',1,'Joker.mkv','MKV',2700000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(37,'Il Silenzio degli Innocenti','Thriller, Horror',4.6,'Jonathan Demme','Jodie Foster, Anthony Hopkins',1991,'118','Una giovane recluta dell\'FBI chiede aiuto al dottor Hannibal Lecter per catturare un serial killer.','https://www.youtube.com/watch?v=W6Mm8SbeRIw','C:/Videos/Movies/Silence_Lambs.mp4',0,'Silence_Lambs.mp4','MP4',1900000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(38,'Aliens - Scontro finale','Fantascienza, Azione',4.4,'James Cameron','Sigourney Weaver, Michael Biehn',1986,'137','Ellen Ripley torna sul pianeta LV-426 con una squadra di marine coloniali.','https://www.youtube.com/watch?v=z8X57K0Wnu0','C:/Videos/Movies/Aliens.mkv',0,'Aliens.mkv','MKV',3100000000,1,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(39,'Amadeus','Biografico, Drammatico',4.5,'Milos Forman','F. Murray Abraham, Tom Hulce',1984,'160','La vita di Wolfgang Amadeus Mozart raccontata dal suo rivale Antonio Salieri.','https://www.youtube.com/watch?v=r7kWQj9GSkA','C:/Videos/Movies/Amadeus.mp4',0,'Amadeus.mp4','MP4',2300000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(40,'Nuovo Cinema Paradiso','Drammatico',4.7,'Giuseppe Tornatore','Philippe Noiret, Salvatore Cascio',1988,'155','L\'amicizia tra un bambino e il proiezionista del cinema del paese.','https://www.youtube.com/watch?v=stLAmG_t8_8','C:/Videos/Movies/Cinema_Paradiso.mp4',1,'Cinema_Paradiso.mp4','MP4',2000000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(41,'Blade Runner 2049','Fantascienza',4.3,'Denis Villeneuve','Ryan Gosling, Harrison Ford',2017,'164','Un nuovo blade runner scopre un segreto che potrebbe far precipitare la società nel caos.','https://www.youtube.com/watch?v=gCcx85zbxz4','C:/Videos/Movies/BR2049.mkv',0,'BR2049.mkv','MKV',4800000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(42,'The Prestige','Drammatico, Mystery',4.5,'Christopher Nolan','Hugh Jackman, Christian Bale',2006,'130','Due maghi rivali a Londra ingaggiano una battaglia per creare l\'illusione suprema.','https://www.youtube.com/watch?v=ijXrqWwZ8Pw','C:/Videos/Movies/Prestige.mp4',1,'Prestige.mp4','MP4',2200000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(43,'Il Grande Lebowski','Commedia',4.2,'Joel Coen','Jeff Bridges, John Goodman',1998,'117','Un uomo pigro viene scambiato per un milionario e finisce in una serie di guai assurdi.','https://www.youtube.com/watch?v=it0uxWAnfLI','C:/Videos/Movies/Lebowski.mp4',0,'Lebowski.mp4','MP4',1850000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(44,'Gran Torino','Drammatico',4.5,'Clint Eastwood','Clint Eastwood, Christopher Carley',2008,'116','Un veterano della guerra di Corea burbero cerca di rimettere sulla retta via un vicino di casa adolescente.','https://www.youtube.com/watch?v=9ecW-z4VneU','C:/Videos/Movies/Gran_Torino.mp4',0,'Gran_Torino.mp4','MP4',1950000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(45,'V per Vendetta','Azione, Fantascienza',4.4,'James McTeigue','Natalie Portman, Hugo Weaving',2005,'132','In un futuro distopico, un misterioso rivoluzionario combatte contro un regime totalitario.','https://www.youtube.com/watch?v=lSA7mAHolAw','C:/Videos/Movies/V_Vendetta.mkv',0,'V_Vendetta.mkv','MKV',2600000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(46,'Arancia Meccanica','Drammatico, Crime',4.3,'Stanley Kubrick','Malcolm McDowell, Patrick Magee',1971,'136','Un giovane delinquente viene sottoposto a un trattamento sperimentale per curare la sua violenza.','https://www.youtube.com/watch?v=HI98v_0706U','C:/Videos/Movies/Arancia_Meccanica.mp4',0,'Arancia_Meccanica.mp4','MP4',2100000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(47,'The Departed','Thriller, Crime',4.5,'Martin Scorsese','Leonardo DiCaprio, Matt Damon',2006,'151','Un poliziotto sotto copertura e una talpa nella polizia si cercano a vicenda all\'interno della mafia irlandese.','https://www.youtube.com/watch?v=iojhqm0J79Y','C:/Videos/Movies/The_Departed.mp4',1,'The_Departed.mp4','MP4',2900000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(48,'Mad Max: Fury Road','Azione, Fantascienza',4.4,'George Miller','Tom Hardy, Charlize Theron',2015,'120','In un mondo post-apocalittico, una donna si ribella a un tiranno con l\'aiuto di un vagabondo.','https://www.youtube.com/watch?v=hEJnMQG9ev8','C:/Videos/Movies/Mad_Max.mkv',0,'Mad_Max.mkv','MKV',3800000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(49,'Il Favoloso Mondo di Amélie','Commedia, Romantico',4.3,'Jean-Pierre Jeunet','Audrey Tautou, Mathieu Kassovitz',2001,'122','Una giovane cameriera parigina decide di cambiare in meglio la vita delle persone intorno a lei.','https://www.youtube.com/watch?v=HUECQuPaCqc','C:/Videos/Movies/Amelie.mp4',0,'Amelie.mp4','MP4',1700000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL),(50,'Inglourious Basterds','Guerra, Azione',4.5,'Quentin Tarantino','Brad Pitt, Christoph Waltz',2009,'153','Nella Francia occupata dai nazisti, un gruppo di soldati ebrei americani pianifica l\'assassinio dei leader del Terzo Reich.','https://www.youtube.com/watch?v=KnrRy6kSFF0','C:/Videos/Movies/Bastardi_Senza_Gloria.mkv',1,'Bastardi_Senza_Gloria.mkv','MKV',3300000000,0,0,'2026-04-03 13:09:35',NULL,0,NULL,NULL);
/*!40000 ALTER TABLE `film` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image_file`
--

DROP TABLE IF EXISTS `image_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `filename` varchar(255) NOT NULL,
  `mime_type` varchar(255) DEFAULT NULL,
  `size_bytes` bigint(20) NOT NULL,
  `formato` enum('JPEG','RAW','TIFF','PNG') NOT NULL,
  `tipo_file` enum('Fotografia','Sfondo','Illustrazione') NOT NULL,
  `cancelled` tinyint(1) NOT NULL DEFAULT 0,
  `preferito` tinyint(1) NOT NULL DEFAULT 0,
  `rating` int(11) DEFAULT NULL,
  `visualizzazioni` bigint(20) NOT NULL DEFAULT 0,
  `data_archiviazione` DATE DEFAULT (CURRENT_DATE),
  `last_view` DATETIME DEFAULT NULL,
  `backup` tinyint(1) NOT NULL DEFAULT 0,
  `note` varchar(255) DEFAULT NULL,
  `last_update` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `path_file` varchar(255) DEFAULT NULL COMMENT 'Percorso completo sul PC',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_file`
--

LOCK TABLES `image_file` WRITE;
/*!40000 ALTER TABLE `image_file` DISABLE KEYS */;
INSERT INTO `image_file` VALUES (1,'Logo aziendale','Logo ufficiale','logo.png','image/png',24567,'PNG','Illustrazione',0,1,5,120,'2025-12-16 14:25:57',NULL,0,'Usato su web',NULL,NULL),(2,'Foto prodotto A','Shot in studio','productA.jpg','image/jpeg',152345,'JPEG','Fotografia',0,0,4,85,'2025-12-16 14:25:57',NULL,0,'Catalogo 2025',NULL,NULL),(3,'Banner natale','Promo natalizia','banner_natale.png','image/png',98345,'PNG','Sfondo',0,0,3,33,'2025-12-16 14:25:57',NULL,0,'Campagna Q4',NULL,NULL),(4,'Raw prototipo','Scatto raw','proto.cr2','image/raw',3256345,'RAW','Fotografia',0,0,5,12,'2025-12-16 14:25:57',NULL,0,'Per post-produzione',NULL,NULL),(5,'Poster evento','Locandina conferenza','poster_conf.tif','image/tiff',823456,'TIFF','Illustrazione',0,1,4,50,'2025-12-16 14:25:57',NULL,0,'Stampa HD',NULL,NULL),(6,'Tramonto sul mare','Scatto al tramonto sulla costa','tramonto_mare_001.jpg','image/jpeg',2456789,'JPEG','Fotografia',0,1,5,120,'2024-01-12 17:30:00','2024-02-01 21:10:00',1,'Ottima luce','2024-02-01 21:10:00','C:/Images/Fotografie/tramonto_mare_001.jpg'),(7,'Montagna innevata','Panorama invernale','montagna_innevata_002.raw','image/raw',12567890,'RAW','Fotografia',0,0,4,85,'2024-01-15 09:00:00','2024-02-03 09:20:00',1,NULL,'2024-02-03 09:20:00','C:/Images/Fotografie/montagna_innevata_002.raw'),(8,'Illustrazione futuristica','Concept art di città futuristica','city_future_01.png','image/png',345678,'PNG','Illustrazione',0,0,5,300,'2024-02-01 13:00:00','2024-02-10 11:00:00',0,NULL,'2024-02-10 11:00:00','C:/Images/Illustrazioni/city_future_01.png'),(9,'Sfondi astratti blu','Texture astratta blu','abstract_blue_01.tiff','image/tiff',567890,'TIFF','Sfondo',0,0,3,40,'2024-01-20 08:00:00','2024-02-05 16:00:00',0,NULL,'2024-02-05 16:00:00','C:/Images/Sfondi/abstract_blue_01.tiff'),(10,'Ritratto in studio','Ritratto professionale','ritratto_studio_01.jpg','image/jpeg',1678900,'JPEG','Fotografia',0,1,5,210,'2024-01-18 11:00:00','2024-02-02 10:00:00',1,'Cliente soddisfatto','2024-02-02 10:00:00','C:/Images/Fotografie/ritratto_studio_01.jpg'),(11,'Paesaggio collinare','Colline verdi in primavera','colline_primavera_01.jpg','image/jpeg',2345678,'JPEG','Fotografia',0,0,4,98,'2024-01-22 14:00:00','2024-02-06 17:00:00',1,NULL,'2024-02-06 17:00:00','C:/Images/Fotografie/colline_primavera_01.jpg'),(12,'Illustrazione fantasy','Dragone su montagna','dragon_mountain_01.png','image/png',456789,'PNG','Illustrazione',0,1,5,450,'2024-02-03 10:00:00','2024-02-11 13:00:00',0,'Molto dettagliata','2024-02-11 13:00:00','C:/Images/Illustrazioni/dragon_mountain_01.png'),(13,'Sfondo minimalista','Sfondo chiaro minimal','minimal_light_01.jpg','image/jpeg',345678,'JPEG','Sfondo',0,0,3,22,'2024-01-10 07:00:00','2024-02-01 09:00:00',0,NULL,'2024-02-01 09:00:00','C:/Images/Sfondi/minimal_light_01.jpg'),(14,'Foto macro insetto','Macro di una coccinella','macro_coccinella_01.raw','image/raw',9876543,'RAW','Fotografia',0,0,4,130,'2024-01-25 12:00:00','2024-02-07 14:00:00',1,NULL,'2024-02-07 14:00:00','C:/Images/Fotografie/macro_coccinella_01.raw'),(15,'Illustrazione cartoon','Personaggio cartoon originale','cartoon_char_01.png','image/png',234567,'PNG','Illustrazione',1,0,4,70,'2024-02-02 09:00:00','2024-02-09 12:00:00',0,NULL,'2026-03-30 15:25:55','C:/Images/Illustrazioni/cartoon_char_01.png'),(16,'Aurora boreale','Aurora in Islanda','aurora_01.jpg','image/jpeg',2789000,'JPEG','Fotografia',1,1,5,500,'2024-01-05 21:00:00','2024-02-12 20:00:00',1,'Scatto preferito','2026-03-30 15:25:39','C:/Images/Fotografie/aurora_01.jpg'),(17,'Sfondi geometrici','Pattern geometrico scuro','geometric_dark_01.tiff','image/tiff',678900,'TIFF','Sfondo',0,0,3,33,'2024-01-14 08:00:00','2024-02-04 11:00:00',0,NULL,'2024-02-04 11:00:00','C:/Images/Sfondi/geometric_dark_01.tiff'),(18,'Illustrazione spaziale','Astronauta nello spazio','astronaut_space_01.png','image/png',567890,'PNG','Illustrazione',0,1,5,380,'2024-02-04 13:00:00','2024-02-10 15:00:00',0,NULL,'2024-02-10 15:00:00','C:/Images/Illustrazioni/astronaut_space_01.png'),(19,'Ritratto naturale','Ritratto in luce naturale','ritratto_naturale_01.jpg','image/jpeg',1987654,'JPEG','Fotografia',0,0,4,140,'2024-01-19 10:00:00','2024-02-03 10:00:00',1,NULL,'2024-02-03 10:00:00','C:/Images/Fotografie/ritratto_naturale_01.jpg'),(20,'Paesaggio urbano','Skyline notturno','skyline_notte_01.jpg','image/jpeg',2567890,'JPEG','Fotografia',0,1,5,600,'2024-01-08 19:00:00','2024-02-12 22:00:00',1,NULL,'2024-02-12 22:00:00','C:/Images/Fotografie/skyline_notte_01.jpg'),(21,'Illustrazione steampunk','Macchina volante steampunk','steampunk_airship_01.png','image/png',467890,'PNG','Illustrazione',0,0,4,210,'2024-02-05 12:00:00','2024-02-11 14:00:00',0,NULL,'2024-02-11 14:00:00','C:/Images/Illustrazioni/steampunk_airship_01.png'),(22,'Sfondo rosso','Sfondo astratto rosso','abstract_red_01.jpg','image/jpeg',345678,'JPEG','Sfondo',0,0,3,18,'2024-01-11 07:00:00','2024-02-01 09:00:00',0,NULL,'2024-02-01 09:00:00','C:/Images/Sfondi/abstract_red_01.jpg'),(23,'Foto notturna città','Strada illuminata di notte','city_night_01.raw','image/raw',11234567,'RAW','Fotografia',0,0,4,190,'2024-01-26 22:00:00','2024-02-07 23:00:00',1,NULL,'2024-02-07 23:00:00','C:/Images/Fotografie/city_night_01.raw'),(24,'Illustrazione robot','Robot futuristico','robot_future_01.png','image/png',345678,'PNG','Illustrazione',0,1,5,420,'2024-02-06 09:00:00','2024-02-11 11:00:00',0,NULL,'2024-02-11 11:00:00','C:/Images/Illustrazioni/robot_future_01.png'),(25,'Sfondi verdi','Texture verde naturale','green_texture_01.tiff','image/tiff',567890,'TIFF','Sfondo',0,0,3,25,'2024-01-13 08:00:00','2024-02-04 10:00:00',0,NULL,'2024-02-04 10:00:00','C:/Images/Sfondi/green_texture_01.tiff'),(26,'Cascata nella foresta','Cascata immersa nel verde','cascata_forest_01.jpg','image/jpeg',2890000,'JPEG','Fotografia',0,0,4,160,'2024-01-17 10:00:00','2024-02-06 12:00:00',1,NULL,'2024-02-06 12:00:00','C:/Images/Fotografie/cascata_forest_01.jpg'),(27,'Illustrazione medievale','Castello medievale in stile fantasy','castle_fantasy_01.png','image/png',478900,'PNG','Illustrazione',0,1,5,390,'2024-02-05 14:00:00','2024-02-11 16:00:00',0,NULL,'2024-02-11 16:00:00','C:/Images/Illustrazioni/castle_fantasy_01.png'),(28,'Sfondo metallico','Texture metallica spazzolata','metal_texture_01.tiff','image/tiff',589000,'TIFF','Sfondo',0,0,3,28,'2024-01-12 08:00:00','2024-02-03 10:00:00',0,NULL,'2024-02-03 10:00:00','C:/Images/Sfondi/metal_texture_01.tiff'),(29,'Ritratto in bianco e nero','Ritratto artistico monocromatico','ritratto_bn_01.jpg','image/jpeg',1789000,'JPEG','Fotografia',0,1,5,250,'2024-01-21 13:00:00','2024-02-08 18:00:00',1,NULL,'2024-02-08 18:00:00','C:/Images/Fotografie/ritratto_bn_01.jpg'),(30,'Illustrazione cyberpunk','Strada neon futuristica','cyberpunk_street_01.png','image/png',512000,'PNG','Illustrazione',0,0,4,310,'2024-02-03 11:00:00','2024-02-10 13:00:00',0,NULL,'2024-02-10 13:00:00','C:/Images/Illustrazioni/cyberpunk_street_01.png'),(31,'Foto di un gatto','Gatto su un davanzale','cat_window_01.raw','image/raw',10456789,'RAW','Fotografia',0,0,4,180,'2024-01-27 09:00:00','2024-02-07 11:00:00',1,NULL,'2024-02-07 11:00:00','C:/Images/Fotografie/cat_window_01.raw'),(32,'Sfondo blu notte','Sfondo scuro con sfumature blu','blue_night_01.jpg','image/jpeg',345000,'JPEG','Sfondo',0,0,3,20,'2024-01-09 07:00:00','2024-02-01 09:00:00',0,NULL,'2024-02-01 09:00:00','C:/Images/Sfondi/blue_night_01.jpg'),(33,'Illustrazione manga','Personaggio manga originale','manga_char_01.png','image/png',267890,'PNG','Illustrazione',0,1,5,410,'2024-02-04 09:00:00','2024-02-11 12:00:00',0,NULL,'2024-02-11 12:00:00','C:/Images/Illustrazioni/manga_char_01.png'),(34,'Paesaggio desertico','Dune al tramonto','desert_dunes_01.jpg','image/jpeg',2345000,'JPEG','Fotografia',0,0,4,150,'2024-01-16 16:00:00','2024-02-05 18:00:00',1,NULL,'2024-02-05 18:00:00','C:/Images/Fotografie/desert_dunes_01.jpg'),(35,'Illustrazione vettoriale','Icona vettoriale moderna','vector_icon_01.png','image/png',145678,'PNG','Illustrazione',0,0,3,55,'2024-02-02 08:00:00','2024-02-09 10:00:00',0,NULL,'2024-02-09 10:00:00','C:/Images/Illustrazioni/vector_icon_01.png'),(36,'Foto di un cane','Cane in un prato','dog_field_01.raw','image/raw',9876000,'RAW','Fotografia',0,0,4,175,'2024-01-28 10:00:00','2024-02-07 12:00:00',1,'','2026-04-01 14:20:40','C:/Images/Fotografie/dog_field_01.raw'),(37,'Sfondo viola','Sfondo astratto viola','purple_abstract_01.jpg','image/jpeg',367890,'JPEG','Sfondo',0,0,3,30,'2024-01-11 07:00:00','2024-02-01 09:00:00',0,NULL,'2024-02-01 09:00:00','C:/Images/Sfondi/purple_abstract_01.jpg'),(38,'Illustrazione horror','Creatura oscura','dark_creature_01.png','image/png',478900,'PNG','Illustrazione',0,1,5,500,'2024-02-06 13:00:00','2024-02-11 15:00:00',0,NULL,'2024-02-11 15:00:00','C:/Images/Illustrazioni/dark_creature_01.png'),(39,'Paesaggio autunnale','Bosco in autunno','autumn_forest_01.jpg','image/jpeg',2567000,'JPEG','Fotografia',0,0,4,140,'2024-01-18 15:00:00','2024-02-06 17:00:00',1,NULL,'2024-02-06 17:00:00','C:/Images/Fotografie/autumn_forest_01.jpg'),(40,'Illustrazione spaziale 2','Pianeta alieno','alien_planet_01.png','image/png',389000,'PNG','Illustrazione',0,0,4,260,'2024-02-03 12:00:00','2024-02-10 14:00:00',0,NULL,'2024-02-10 14:00:00','C:/Images/Illustrazioni/alien_planet_01.png'),(41,'Sfondo dorato','Texture oro brillante','gold_texture_01.tiff','image/tiff',678900,'TIFF','Sfondo',0,0,3,22,'2024-01-14 08:00:00','2024-02-04 11:00:00',0,NULL,'2024-02-04 11:00:00','C:/Images/Sfondi/gold_texture_01.tiff'),(42,'Foto di un lago','Lago alpino al mattino','lake_morning_01.jpg','image/jpeg',2456000,'JPEG','Fotografia',0,1,5,300,'2024-01-20 06:00:00','2024-02-06 08:00:00',1,NULL,'2024-02-06 08:00:00','C:/Images/Fotografie/lake_morning_01.jpg'),(43,'Illustrazione minimal','Linee geometriche semplici','minimal_lines_01.png','image/png',123450,'PNG','Illustrazione',0,0,3,40,'2024-02-01 08:00:00','2024-02-08 10:00:00',0,NULL,'2024-02-08 10:00:00','C:/Images/Illustrazioni/minimal_lines_01.png'),(44,'Foto di un ponte','Ponte sospeso al tramonto','bridge_sunset_01.raw','image/raw',11567890,'RAW','Fotografia',0,0,4,210,'2024-01-29 17:00:00','2024-02-07 19:00:00',1,NULL,'2024-02-07 19:00:00','C:/Images/Fotografie/bridge_sunset_01.raw'),(45,'Sfondo acqua','Texture acqua in movimento','water_texture_01.jpg','image/jpeg',345600,'JPEG','Sfondo',0,0,3,26,'2024-01-10 07:00:00','2024-02-01 09:00:00',0,NULL,'2024-02-01 09:00:00','C:/Images/Sfondi/water_texture_01.jpg');
/*!40000 ALTER TABLE `image_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oggetto_tag`
--

DROP TABLE IF EXISTS `oggetto_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oggetto_tag` (
  `id_tag` bigint(20) NOT NULL,
  `id_oggetto` bigint(20) NOT NULL,
  PRIMARY KEY (`id_tag`,`id_oggetto`),
  KEY `FK7u4eob9xgqf9f2mqdsym9x6we` (`id_oggetto`),
  CONSTRAINT `oggetto_tag_ibfk_1` FOREIGN KEY (`id_tag`) REFERENCES `tag` (`id_tag`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oggetto_tag`
--

LOCK TABLES `oggetto_tag` WRITE;
/*!40000 ALTER TABLE `oggetto_tag` DISABLE KEYS */;
INSERT INTO `oggetto_tag` VALUES (30,1),(31,1),(32,5),(32,51),(33,5),(34,45),(35,36),(37,1),(37,2),(38,5),(39,10),(42,11),(42,40),(43,8),(43,28),(44,4),(44,33),(45,3),(45,28);
/*!40000 ALTER TABLE `oggetto_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag` (
  `id_tag` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome_tag` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_oggetto` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_oggetto` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_tag`),
  UNIQUE KEY `nome_tag` (`nome_tag`),
  KEY `idx_tag_tipo_oggetto` (`tipo_oggetto`),
  KEY `FKsdc5uol21le8b3s14fhnr2i2n` (`id_oggetto`),
  CONSTRAINT `FKsdc5uol21le8b3s14fhnr2i2n` FOREIGN KEY (`id_oggetto`) REFERENCES `audio_file` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` VALUES 
(30,'pubblicità','Audio',NULL),(31,'natale','Audio',NULL),(32,'estate','Audio',NULL),(33,'cinema','Audio',NULL),(34,'archimede','Audio',NULL),(35,'cane','Image',NULL),(36,'animale','Image',NULL),(37,'Capolavoro','film',NULL),(38,'Oscar','film',NULL),(39,'Cult','film',NULL),(40,'Must Watch','film',NULL),(41,'Noir','film',NULL),(42,'Italian Classic','film',NULL),(43,'Mind-bending','film',NULL),(44,'Tarantino','film',NULL),(45,'Nolan','film',NULL);
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video`
--

DROP TABLE IF EXISTS `video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `video` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `titolo` varchar(255) NOT NULL,
  `categoria` varchar(255) DEFAULT NULL,
  `percorso_file` varchar(255) DEFAULT NULL,
  `preferito` tinyint(1) DEFAULT 0,
  `rating` double DEFAULT NULL,
  `visualizzazioni` int(11) DEFAULT 0,
  `durata_min` int(11) DEFAULT NULL,
  `data_archiviazione` DATE DEFAULT (CURRENT_DATE),
  `last_view` DATETIME DEFAULT NULL,
  `last_update` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `backup` tinyint(1) DEFAULT 0,
  `note` varchar(255) DEFAULT NULL,
  `cancelled` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1018 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-03 16:51:43
