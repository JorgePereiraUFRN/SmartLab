# --------------------------------------------------------
# Host:                         127.0.0.1
# Server version:               5.7.12-0ubuntu1
# Server OS:                    Linux
# HeidiSQL version:             6.0.0.3603
# Date/time:                    2016-07-05 11:54:20
# --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

# Dumping database structure for smartlab
CREATE DATABASE IF NOT EXISTS `smartlab` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `smartlab`;


# Dumping structure for table smartlab.Measurement
CREATE TABLE IF NOT EXISTS `Measurement` (
  `measurementId` bigint(20) NOT NULL AUTO_INCREMENT,
  `time` datetime DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `sensor_sensorId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`measurementId`),
  KEY `FKF75C839CF756909C` (`sensor_sensorId`),
  CONSTRAINT `FKF75C839CF756909C` FOREIGN KEY (`sensor_sensorId`) REFERENCES `Sensor` (`sensorId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

# Data exporting was unselected.


# Dumping structure for table smartlab.Sensor
CREATE TABLE IF NOT EXISTS `Sensor` (
  `sensorId` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `local` varchar(255) DEFAULT NULL,
  `sensorType` int(11) DEFAULT NULL,
  PRIMARY KEY (`sensorId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

# Data exporting was unselected.


# Dumping structure for table smartlab.Sensor_Measurement
CREATE TABLE IF NOT EXISTS `Sensor_Measurement` (
  `Sensor_sensorId` bigint(20) NOT NULL,
  `measurements_measurementId` bigint(20) NOT NULL,
  UNIQUE KEY `measurements_measurementId` (`measurements_measurementId`),
  KEY `FKD05B2AB7F756909C` (`Sensor_sensorId`),
  KEY `FKD05B2AB7A5A79CE3` (`measurements_measurementId`),
  CONSTRAINT `FKD05B2AB7A5A79CE3` FOREIGN KEY (`measurements_measurementId`) REFERENCES `Measurement` (`measurementId`),
  CONSTRAINT `FKD05B2AB7F756909C` FOREIGN KEY (`Sensor_sensorId`) REFERENCES `Sensor` (`sensorId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

# Data exporting was unselected.
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
