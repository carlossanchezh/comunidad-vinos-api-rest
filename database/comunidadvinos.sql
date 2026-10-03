-- MySQL dump 10.13  Distrib 9.6.0, for Win64 (x86_64)
--
-- Host: localhost    Database: comunidadvinos
-- ------------------------------------------------------
-- Server version	9.6.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `comunidadvinos`
--

/*!40000 DROP DATABASE IF EXISTS `comunidadvinos`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `comunidadvinos` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `comunidadvinos`;

--
-- Table structure for table `seguimientos`
--

DROP TABLE IF EXISTS `seguimientos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seguimientos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `seguidor_id` int NOT NULL,
  `seguido_id` int NOT NULL,
  `fecha_solicitud` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_seguimiento` (`seguidor_id`,`seguido_id`),
  KEY `idx_seguidor_id` (`seguidor_id`),
  KEY `idx_seguido_id` (`seguido_id`),
  CONSTRAINT `fk_seguimientos_seguido` FOREIGN KEY (`seguido_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_seguimientos_seguidor` FOREIGN KEY (`seguidor_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seguimientos`
--

LOCK TABLES `seguimientos` WRITE;
/*!40000 ALTER TABLE `seguimientos` DISABLE KEYS */;
INSERT INTO `seguimientos` VALUES (1,1,2,'2022-03-15'),(2,1,3,'2023-02-10'),(3,2,1,'2022-03-15'),(4,2,4,'2023-05-20'),(5,3,5,'2024-01-01'),(6,4,2,'2023-05-20'),(7,5,3,'2024-01-01'),(8,6,7,'2024-06-15'),(9,7,6,'2024-06-15'),(10,8,1,'2026-04-10'),(11,9,8,'2026-04-11');
/*!40000 ALTER TABLE `seguimientos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_vino`
--

DROP TABLE IF EXISTS `usuario_vino`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario_vino` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `vino_id` int NOT NULL,
  `puntuacion` int DEFAULT NULL,
  `fecha_anadido` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_usuario_id` (`usuario_id`),
  KEY `idx_vino_id` (`vino_id`),
  CONSTRAINT `fk_usuario_vino_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_usuario_vino_vino` FOREIGN KEY (`vino_id`) REFERENCES `vinos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `usuario_vino_chk_1` CHECK (((`puntuacion` >= 0) and (`puntuacion` <= 10)))
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_vino`
--

LOCK TABLES `usuario_vino` WRITE;
/*!40000 ALTER TABLE `usuario_vino` DISABLE KEYS */;
INSERT INTO `usuario_vino` VALUES (2,1,2,8,'2023-04-12'),(3,1,13,10,'2024-06-17'),(4,2,1,8,'2023-09-30'),(5,2,6,7,'2024-06-17'),(6,2,7,9,'2025-01-22'),(7,2,15,7,'2026-02-14'),(8,3,3,9,'2023-06-28'),(9,3,5,8,'2024-03-03'),(10,3,12,9,'2025-07-21'),(11,4,4,7,'2024-06-30'),(12,4,8,8,'2022-09-15'),(13,4,14,8,'2025-02-18'),(14,5,9,9,'2023-04-03'),(15,5,10,8,'2023-11-30'),(16,5,18,7,'2024-07-25'),(17,6,11,9,'2025-01-22'),(18,6,16,8,'2025-03-10'),(19,6,17,8,'2023-08-15'),(20,7,19,7,'2025-03-10'),(21,7,20,8,'2025-11-11'),(22,7,21,8,'2025-12-05'),(23,1,1,10,'2026-04-10'),(24,1,14,6,'2026-04-10');
/*!40000 ALTER TABLE `usuario_vino` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `fecha_nacimiento` date NOT NULL,
  `correo` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `correo` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Juan Pérez','1990-05-15','juan.perez@gmail.com'),(2,'Carlos Martínez','1985-07-22','carlos.martinez@gmail.com'),(3,'María García','1992-11-03','maria.garcia@gmail.com'),(4,'Ana López','1988-09-18','ana.lopez@gmail.com'),(5,'Pedro Gimenez','1990-05-15','pedro.gimenez@email.com'),(6,'Laura Gómez','1995-03-10','laura.gomez@gmail.com'),(7,'David Ruiz','1993-12-01','david.ruiz@gmail.com'),(8,'Ana García','1990-05-15','ana.garcia@email.com'),(9,'Laura García','1992-08-20','laura.garcia@email.com');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uvas`
--

DROP TABLE IF EXISTS `uvas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uvas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uvas`
--

LOCK TABLES `uvas` WRITE;
/*!40000 ALTER TABLE `uvas` DISABLE KEYS */;
INSERT INTO `uvas` VALUES (1,'Tempranillo','Uva tinta principal de Rioja y Ribera del Duero'),(2,'Garnacha','Uva tinta muy versátil, originaria de Aragón'),(3,'Albariño','Uva blanca emblemática de Rías Baixas'),(4,'Cava','Uva utilizada para espumosos'),(5,'Verdejo','Uva blanca principal de Rueda');
/*!40000 ALTER TABLE `uvas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vino_uva`
--

DROP TABLE IF EXISTS `vino_uva`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vino_uva` (
  `id` int NOT NULL AUTO_INCREMENT,
  `vino_id` int NOT NULL,
  `uva_id` int NOT NULL,
  `porcentaje` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_vino_uva` (`vino_id`,`uva_id`),
  KEY `fk_vino_uva_uva` (`uva_id`),
  CONSTRAINT `fk_vino_uva_uva` FOREIGN KEY (`uva_id`) REFERENCES `uvas` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_vino_uva_vino` FOREIGN KEY (`vino_id`) REFERENCES `vinos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `vino_uva_chk_1` CHECK (((`porcentaje` >= 1) and (`porcentaje` <= 100)))
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vino_uva`
--

LOCK TABLES `vino_uva` WRITE;
/*!40000 ALTER TABLE `vino_uva` DISABLE KEYS */;
INSERT INTO `vino_uva` VALUES (1,1,1,85),(2,2,1,90),(3,6,1,80),(4,7,1,70),(5,13,1,95),(6,15,1,75),(7,7,2,30),(8,10,2,60),(9,3,3,100),(10,12,3,100),(11,4,4,100),(12,14,4,100),(13,20,4,100),(14,19,5,100),(15,21,5,100);
/*!40000 ALTER TABLE `vino_uva` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vinos`
--

DROP TABLE IF EXISTS `vinos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vinos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `bodega` varchar(255) NOT NULL,
  `anada` int NOT NULL,
  `origen` varchar(255) NOT NULL,
  `tipo` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vinos`
--

LOCK TABLES `vinos` WRITE;
/*!40000 ALTER TABLE `vinos` DISABLE KEYS */;
INSERT INTO `vinos` VALUES (1,'Imperial Reserva','Bodegas CVNE',2018,'Rioja','Tinto'),(2,'Pesquera Crianza','Bodegas Alejandro Fernández',2019,'Ribera del Duero','Tinto'),(3,'Zárate Albariño','Bodegas Zárate',2022,'Rías Baixas','Blanco'),(4,'Gran Reserva Brut','Codorníu',2019,'Cava','Espumoso'),(5,'Viña Gravonia','Bodegas López de Heredia',2016,'Rioja','Blanco'),(6,'Protos Roble','Protos',2021,'Ribera del Duero','Tinto'),(7,'Clos Martinet','Martinet',2017,'Priorat','Tinto'),(8,'Prado Rey Rosado','Bodegas Prado Rey',2023,'Ribera del Duero','Rosado'),(9,'Tío Pepe','González Byass',2020,'Jerez','Generoso'),(10,'Pétalos','Descendientes de J. Palacios',2021,'Bierzo','Tinto'),(11,'Numanthia','Numanthia',2018,'Toro','Tinto'),(12,'Pazo de Señorans','Pazo de Señorans',2023,'Rías Baixas','Blanco'),(13,'Vega Sicilia Único','Vega Sicilia',2014,'Ribera del Duero','Tinto'),(14,'Jaume Giró Giró','Jaume Giró i Giró',2020,'Cava','Espumoso'),(15,'Enate Reserva','Bodegas Enate',2017,'Somontano','Tinto'),(16,'Mencía Joven','Bodegas Godelia',2022,'Bierzo','Tinto'),(17,'Fino La Ina','Bodegas La Ina',2019,'Jerez','Generoso'),(18,'Málaga Virgen','Bodegas Málaga Virgen',2018,'Málaga','Dulce'),(19,'Menade Sauvignon','Bodegas Menade',2023,'Rueda','Blanco'),(20,'Anna de Codorníu Rosado','Codorníu',2021,'Cava','Espumoso'),(21,'Godello Valdeorras','Bodegas Valdeorras',2023,'Valdeorras','Blanco');
/*!40000 ALTER TABLE `vinos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'comunidadvinos'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-03 17:06:04
