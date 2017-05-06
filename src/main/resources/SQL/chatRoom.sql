DROP TABLE IF EXISTS `chatRoom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chatRoom` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `anchorname` varchar(32) NOT NULL COMMENT '用户名称',
  `message` varchar(1024) DEFAULT NULL COMMENT '用户消息',
  `sender` varchar(32)  DEFAULT NULL COMMENT '发送方名称',
  `sendtime` TIMESTAMP NOT NULL DEFAULT  CURRENT_TIMESTAMP COMMENT '时间戳',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;