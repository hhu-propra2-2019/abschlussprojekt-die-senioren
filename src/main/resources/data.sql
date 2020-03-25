-- MySQL dump 10.13  Distrib 8.0.19, for macos10.15 (x86_64)
--
-- Host: 127.0.0.1    Database: gruppen
-- ------------------------------------------------------
-- Server version	8.0.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `events`
--

LOCK TABLES `events` WRITE;
/*!40000 ALTER TABLE `events` DISABLE KEYS */;
INSERT INTO events (id, user_name, group_id, event_time_stamp, event_type, payload)
VALUES (
           NULL,
           'userX',
           '6ec87136-6e76-11ea-bc55-0242ac130003',
           '2020-03-02 13:22:14',
           'UserCreationEvent',
           '{"username": "userX"}');

INSERT INTO events (id, user_name, group_id, event_time_stamp, event_type, payload)
VALUES (
           NULL,
           'userX',
           '6ec87136-6e76-11ea-bc55-0242ac130003',
           '2020-03-02 13:22:14',
           'GroupCreationEvent',
           '{"groupId": "6ec87136-6e76-11ea-bc55-0242ac130003","groupName": "Testgruppe1","groupCreator": "userX","groupDescription": "Lalalala","groupType": "PUBLIC"}');


INSERT INTO events (id, user_name, group_id, event_time_stamp, event_type, payload)
VALUES (
           NULL,
           'userRST',
           '6ec87136-6e76-11ea-bc55-0242ac130003',
           '2020-03-02 13:22:14',
           'UserCreationEvent',
           '{"username": "userRST"}');


INSERT INTO events (id, user_name, group_id, event_time_stamp, event_type, payload)
VALUES (
           NULL,
           'userRST',
           '6ec87136-6e76-11ea-bc55-0242ac130003',
           '2020-03-02 13:24:14',
           'MembershipAssignmentEvent','{"groupId": "6ec87136-6e76-11ea-bc55-0242ac130003","userName": "userRST","membershipType": "VIEWER"}');


INSERT INTO events (id, user_name, group_id, event_time_stamp, event_type, payload)
VALUES (
           NULL,
           'userVWX',
           '6ec87136-6e76-11ea-bc55-0242ac130003',
           '2020-03-02 13:22:14',
           'UserCreationEvent',
           '{"username": "userVWX"}');


INSERT INTO events (id, user_name, group_id, event_time_stamp, event_type, payload)
VALUES (
           NULL,
           'userVWX',
           'cc5b6402-6e76-11ea-bc55-0242ac130003',
           '2020-03-02 13:22:14',
           'GroupCreationEvent',
           '{"groupId": "cc5b6402-6e76-11ea-bc55-0242ac130003","groupName": "Testgruppe2","groupCreator": "userX","groupDescription": "Lalelu Nur Der Mann Im Mond","groupType": "RESTRICTED"}');


INSERT INTO events (id, user_name, group_id, event_time_stamp, event_type, payload)
VALUES (
           NULL,
           'userX',
           '6ec87136-6e76-11ea-bc55-0242ac130003',
           '2020-03-02 19:22:14',
           'GroupDeletionEvent',
           '{"groupId": "6ec87136-6e76-11ea-bc55-0242ac130003","deletedByUser": "userX"}');


INSERT INTO events (id, user_name, group_id, event_time_stamp, event_type, payload)
VALUES (
           NULL,
           'userVWX',
           'cc5b6402-6e76-11ea-bc55-0242ac130003',
           '2020-03-02 15:22:14',
           'GroupPropertyUpdateEvent',
           '{"groupId": "cc5b6402-6e76-11ea-bc55-0242ac130003","groupName": "Testgruppe2","updatedBy": "userVWX","description": "Lalelu Nur der Mann Im Mond Schaut Zu","groupType": "RESTRICTED"}');


INSERT INTO events (id, user_name, group_id, event_time_stamp, event_type, payload)
VALUES (
           NULL,
           'userABC',
           '6ec87136-6e76-11ea-bc55-0242ac130003',
           '2020-03-02 13:22:14',
           'UserCreationEvent',
           '{"username": "userABC"}');

INSERT INTO events (id, user_name, group_id, event_time_stamp, event_type, payload)
VALUES (
           NULL,
           'userABC',
           'cc5b6402-6e76-11ea-bc55-0242ac130003',
           '2020-03-02 15:22:14',
           'MembershipRequestEvent',
           '{"groupId": "cc5b6402-6e76-11ea-bc55-0242ac130003","userName": "userABC","membershipType": "VIEWER"}');


INSERT INTO events (id, user_name, group_id, event_time_stamp, event_type, payload)
VALUES (
           NULL,
           'userDEF',
           '6ec87136-6e76-11ea-bc55-0242ac130003',
           '2020-03-02 13:22:14',
           'UserCreationEvent',
           '{"username": "userDEF"}');


INSERT INTO events (id, user_name, group_id, event_time_stamp, event_type, payload)
VALUES (
           NULL,
           'userDEF',
           'cc5b6402-6e76-11ea-bc55-0242ac130003',
           '2020-03-02 16:22:14',
           'MembershipRequestEvent',
           '{"groupId": "cc5b6402-6e76-11ea-bc55-0242ac130003","userName": "userDEF","membershipType": "ADMIN"}');


INSERT INTO events (id, user_name, group_id, event_time_stamp, event_type, payload)
VALUES (
           NULL,
           'userABC',
           'cc5b6402-6e76-11ea-bc55-0242ac130003',
           '2020-03-02 20:22:14',
           'MembershipAcceptanceEvent',
           '{"groupId": "cc5b6402-6e76-11ea-bc55-0242ac130003","userName": "userABC"}');

INSERT INTO events (id, user_name, group_id, event_time_stamp, event_type, payload)
VALUES (
           NULL,
           'userABC',
           'cc5b6402-6e76-11ea-bc55-0242ac130003',
           '2020-03-02 14:22:14',
           'MembershipAssignmentEvent',
           '{"groupId": "cc5b6402-6e76-11ea-bc55-0242ac130003","userName": "userABC","membershipType": "VIEWER"}');

INSERT INTO events (id, user_name, group_id, event_time_stamp, event_type, payload)
VALUES (
           NULL,
           'userVWX',
           'cc5b6402-6e76-11ea-bc55-0242ac130003',
           '2020-03-02 20:22:14',
           'MembershipUpdateEvent',
           '{"groupId": "cc5b6402-6e76-11ea-bc55-0242ac130003","userName": "userABC","updatedBy": "userVWX","updatedTo": "ADMIN"}');


INSERT INTO events (id, user_name, group_id, event_time_stamp, event_type, payload)
VALUES (
           NULL,
           'userDEF',
           'cc5b6402-6e76-11ea-bc55-0242ac130003',
           '2020-03-02 21:22:14',
           'MembershipRejectionEvent',
           '{"groupId": "cc5b6402-6e76-11ea-bc55-0242ac130003","userName": "userDEF"}');


INSERT INTO events (id, user_name, group_id, event_time_stamp, event_type, payload)
VALUES (
           NULL,
           'userVWX',
           'cc5b6402-6e76-11ea-bc55-0242ac130003',
           '2020-03-02 21:22:14',
           'MemberDeletionEvent',
           '{"groupId": "cc5b6402-6e76-11ea-bc55-0242ac130003","removedMemberId": "4ef639d6-6e78-11ea-bc55-0242ac130003","removedByMemberId": "7ae383be-6e78-11ea-bc55-0242ac130003"}');

INSERT INTO events (id, user_name, group_id, event_time_stamp, event_type, payload)
VALUES (
           NULL,
           'userVWX',
           'cc5b6402-6e76-11ea-bc55-0242ac130003',
           '2020-03-02 22:22:14',
           'MemberResignmentEvent',
           '{"groupId": "cc5b6402-6e76-11ea-bc55-0242ac130003","leavingUserName": "userVWX"}');
/*!40000 ALTER TABLE `events` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-22 12:11:01