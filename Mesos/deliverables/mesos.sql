-- Dump del database MESOS con dati fittizi per test
-- Include le correzioni architetturali (AUTO_INCREMENT e Foreign Keys corrette)

SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `nickname` varchar(32) NOT NULL,
  PRIMARY KEY (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `game`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `game`;
CREATE TABLE `game` (
  `idGame` int NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `numPlayer` int NOT NULL,
  PRIMARY KEY (`idGame`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `play`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `play`;
CREATE TABLE `play` (
  `nickname` varchar(32) NOT NULL,
  `idGame` int NOT NULL,
  `prestigePoints` int NOT NULL,
  PRIMARY KEY (`nickname`,`idGame`),
  KEY `fk_game_idx` (`idGame`),
  CONSTRAINT `fk_game` FOREIGN KEY (`idGame`) REFERENCES `game` (`idGame`) ON UPDATE CASCADE,
  CONSTRAINT `fk_user` FOREIGN KEY (`nickname`) REFERENCES `user` (`nickname`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- INSERIMENTO DATI FITTIZI (DUMMY DATA)
-- =====================================================

-- 1. Creazione degli Utenti
INSERT INTO `user` (`nickname`) VALUES 
('Matteo'),
('Andrea'),
('Giulia'),
('Sofia'),
('Luca'),
('Davide');

-- 2. Creazione delle Partite (Games)
-- Nota: idGame è AUTO_INCREMENT, ma qui lo forziamo per poter creare i collegamenti nella tabella play
INSERT INTO `game` (`idGame`, `date`, `numPlayer`) VALUES 
(1, '2026-05-10', 3),
(2, '2026-05-12', 2),
(3, '2026-05-15', 4),
(4, '2026-05-16', 3),
(5, '2026-05-18', 2);

-- 3. Inserimento dei punteggi (Play)
-- Partita 1 (3 giocatori): Vince Giulia
INSERT INTO `play` (`nickname`, `idGame`, `prestigePoints`) VALUES 
('Matteo', 1, 35),
('Andrea', 1, 42),
('Giulia', 1, 55);

-- Partita 2 (2 giocatori): Vince Sofia
INSERT INTO `play` (`nickname`, `idGame`, `prestigePoints`) VALUES 
('Sofia', 2, 60),
('Luca', 2, 45);

-- Partita 3 (4 giocatori): Vince Matteo
INSERT INTO `play` (`nickname`, `idGame`, `prestigePoints`) VALUES 
('Matteo', 3, 70),
('Andrea', 3, 50),
('Giulia', 3, 48),
('Davide', 3, 30);

-- Partita 4 (3 giocatori): Vince Sofia
INSERT INTO `play` (`nickname`, `idGame`, `prestigePoints`) VALUES 
('Sofia', 4, 65),
('Matteo', 4, 55),
('Davide', 4, 40);

-- Partita 5 (2 giocatori): Vince Andrea
INSERT INTO `play` (`nickname`, `idGame`, `prestigePoints`) VALUES 
('Andrea', 5, 80),
('Luca', 5, 75);

COMMIT;
