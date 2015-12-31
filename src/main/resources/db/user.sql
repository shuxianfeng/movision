/**
 * 用户表
 */
CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `name` varchar(256) DEFAULT NULL COMMENT '姓名',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `password` varchar(256) DEFAULT NULL COMMENT '密码',
  `salt` varchar(256) DEFAULT NULL COMMENT '加密串',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态 0:无效 1:有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- insert
INSERT INTO `user` (`id`, `name`, `mobile`, `password`, `salt`, `status`)
VALUES
  (1, '小志', '18652093798', '83d784512851260234933a426df30b6a', 'aaa0af3b28f8376cb7263a6e2cefdefe', '1'),
  (2, '用户一', '17700000001', '83d784512851260234933a426df30b6a', 'aaa0af3b28f8376cb7263a6e2cefdefe', '0'),
  (3, '用户二', '17700000002', '83d784512851260234933a426df30b6a', 'aaa0af3b28f8376cb7263a6e2cefdefe', '1'),
  (4, '用户三', '17700000003', '83d784512851260234933a426df30b6a', 'aaa0af3b28f8376cb7263a6e2cefdefe', '1'),
  (5, '用户四', '17700000004', '83d784512851260234933a426df30b6a', 'aaa0af3b28f8376cb7263a6e2cefdefe', '1'),
  (6, '用户五', '17700000005', '83d784512851260234933a426df30b6a', 'aaa0af3b28f8376cb7263a6e2cefdefe', '0'),
  (7, '用户六', '17700000006', '83d784512851260234933a426df30b6a', 'aaa0af3b28f8376cb7263a6e2cefdefe', '0');

