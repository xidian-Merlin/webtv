DROP TABLE IF EXISTS `user`;  
/*!40101 SET @saved_cs_client     = @@character_set_client */;  
/*!40101 SET character_set_client = utf8 */;  
CREATE TABLE `user` (  
  `id` int(11) NOT NULL AUTO_INCREMENT,  
  `username` varchar(32) NOT NULL COMMENT '用户名称',  
  `password` varchar(32) NOT NULL COMMENT '用户密码',
  `sex` char(1) DEFAULT NULL COMMENT '性别',  
  `address` varchar(256) DEFAULT NULL COMMENT '地址',
  'pic' varchar(256) NOT NULL COMMENT '用户头像地址',
  'live' BOOL NOT NULL COMMENT '是否在直播',
  PRIMARY KEY (`id`)  
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;