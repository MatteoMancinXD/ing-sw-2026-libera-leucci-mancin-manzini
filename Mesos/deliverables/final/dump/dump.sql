-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versione server:              8.4.9 - MySQL Community Server - GPL
-- S.O. server:                  Win64
-- HeidiSQL Versione:            12.5.0.6677
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dump della struttura del database mesos
DROP DATABASE IF EXISTS `mesos`
CREATE DATABASE IF NOT EXISTS `mesos` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `mesos`;

-- Dump della struttura di tabella mesos.game
CREATE TABLE IF NOT EXISTS `game` (
  `idGame` int NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `numPlayer` int NOT NULL,
  PRIMARY KEY (`idGame`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dump dei dati della tabella mesos.game: ~9 rows (circa)
INSERT INTO `game` (`idGame`, `date`, `numPlayer`) VALUES
	(1, '2026-05-18', 2),
	(2, '2026-06-11', 2),
	(3, '2026-06-11', 2),
	(4, '2026-06-11', 2),
	(5, '2026-06-11', 2),
	(6, '2026-06-14', 2),
	(7, '2026-06-14', 2),
	(8, '2026-06-14', 2),
	(9, '2026-06-20', 2);

-- Dump della struttura di tabella mesos.play
CREATE TABLE IF NOT EXISTS `play` (
  `nickname` varchar(32) NOT NULL,
  `idGame` int NOT NULL,
  `prestigePoints` int NOT NULL,
  PRIMARY KEY (`nickname`,`idGame`),
  KEY `fk_game_idx` (`idGame`),
  CONSTRAINT `fk_game` FOREIGN KEY (`idGame`) REFERENCES `game` (`idGame`) ON UPDATE CASCADE,
  CONSTRAINT `fk_user` FOREIGN KEY (`nickname`) REFERENCES `user` (`nickname`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dump dei dati della tabella mesos.play: ~18 rows (circa)
INSERT INTO `play` (`nickname`, `idGame`, `prestigePoints`) VALUES
	('Cesare', 6, 32),
	('Elisa', 6, -4),
	('P1', 1, -1),
	('P1', 2, 15),
	('P1', 3, -30),
	('P1', 4, -11),
	('P1', 5, -1),
	('P1', 9, 2),
	('P10', 8, 21),
	('P11', 8, -57),
	('P2', 1, 28),
	('P2', 2, -19),
	('P2', 5, -5),
	('P2', 9, 3),
	('P20', 7, 36),
	('P21', 7, -26),
	('P3', 4, -11),
	('P4', 3, 26);

-- Dump della struttura di tabella mesos.user
CREATE TABLE IF NOT EXISTS `user` (
  `nickname` varchar(32) NOT NULL,
  PRIMARY KEY (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dump dei dati della tabella mesos.user: ~10 rows (circa)
INSERT INTO `user` (`nickname`) VALUES
	('Cesare'),
	('Elisa'),
	('P1'),
	('P10'),
	('P11'),
	('P2'),
	('P20'),
	('P21'),
	('P3'),
	('P4');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
