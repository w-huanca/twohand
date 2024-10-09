-- 创建数据库（用户表增加禁用属性）
CREATE DATABASE IF NOT EXISTS twohand2;

-- 使用数据库
USE twohand2;
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for board
-- ----------------------------
DROP TABLE IF EXISTS `board`;
CREATE TABLE `board` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `sname` varchar(255) DEFAULT NULL COMMENT '用户名',
  `email` varchar(255) DEFAULT NULL COMMENT '密码',
  `simage` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `btime` datetime DEFAULT NULL COMMENT '手机',
  `content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of board
-- ----------------------------
INSERT INTO `board` VALUES ('1', '张三', '12345678@qq.com', 'q6.jfif', '2024-08-14 10:53:51', '宿管阿姨，三楼厕所堵了');
INSERT INTO `board` VALUES ('3', '张三', '1234567@qq.com', 'q6.jfif', '2024-08-02 10:18:47', '哈哈哈');
INSERT INTO `board` VALUES ('4', '张三', '1234567@qq.com', 'q6.jfif', '2024-08-14 10:54:17', '哈哈哈');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `good_id` int(11) NOT NULL,
  `customer` varchar(255) DEFAULT NULL COMMENT '评论人',
  `cimage` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `comment_time` datetime DEFAULT NULL,
  `star` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES ('1', '1', '张三', 'q6.jfif', '好卖家，商品全新，服务态度佳，物流超快，推荐给大家。', '2023-11-09 18:30:26', '4');
INSERT INTO `comment` VALUES ('37', '1', '张三', 'q6.jfif', '宝贝很棒，物超所值，卖家热情，发货速度快，赞一个!', '2024-08-09 16:52:32', '3');
INSERT INTO `comment` VALUES ('38', '1', '张三', 'q6.jfif', '商品与描述一致，卖家耐心，解答疑问及时，很满意的一次购物。', '2024-05-14 22:52:53', '3');
INSERT INTO `comment` VALUES ('39', '2', '张三', 'q6.jfif', '包装完好，物流迅速，卖家诚信，购物体验非常好，感谢!', '2024-05-14 22:53:49', '2');
INSERT INTO `comment` VALUES ('40', '2', '李四', '76913696178700.png', '商品质量上乘，卖家服务周到，价格合理，下次还会再来。', '2024-06-13 16:04:34', '4');
INSERT INTO `comment` VALUES ('41', '2', '李四', '76913696178700.png', '卖家响应迅速，问题解决效率高，商品性价比高，值得信赖。', '2024-06-13 16:36:27', '3');
INSERT INTO `comment` VALUES ('42', '2', '李四', '76913696178700.png', '收到的商品很满意，卖家态度好，物流也给力，好评!', '2024-06-13 16:43:04', '5');
INSERT INTO `comment` VALUES ('44', '1', null, null, '很好', '2024-08-22 21:01:19', '5');
INSERT INTO `comment` VALUES ('45', '1', '张三', 'q6.jfif', 'qweqwe', '2024-08-22 21:01:51', '5');
INSERT INTO `comment` VALUES ('47', '3', '张三', 'q6.jfif', '23123', '2024-08-22 21:14:08', '3');
INSERT INTO `comment` VALUES ('51', '3', '李四', '76913696178700.png', '412', '2024-08-22 21:25:07', '4');
INSERT INTO `comment` VALUES ('52', '3', '李四', '76913696178700.png', '21312124', '2024-08-22 21:28:28', '3');

-- ----------------------------
-- Table structure for good
-- ----------------------------
DROP TABLE IF EXISTS `good`;
CREATE TABLE `good` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜品id',
  `gname` varchar(255) DEFAULT NULL COMMENT '菜品名称',
  `type` varchar(255) DEFAULT NULL COMMENT '所属店铺',
  `price` double(255,2) DEFAULT NULL COMMENT '单价',
  `pricen` double(255,2) DEFAULT NULL,
  `stock` int(11) DEFAULT NULL COMMENT '容量',
  `descr` varchar(255) DEFAULT NULL COMMENT '简介',
  `gimage` varchar(255) DEFAULT NULL COMMENT '图像',
  `sid` int(11) DEFAULT NULL,
  `star` int(11) NOT NULL DEFAULT '0',
  `discount` double(11,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of good
-- ----------------------------
INSERT INTO `good` VALUES ('1', '台灯', '生活用品', '23.00', '20.01', '2', '台灯', '150408146338800.png', '1', '3', '0.87');
INSERT INTO `good` VALUES ('2', '高数教材', '二手教材', '12.00', '32.00', '2', '高数教材，没怎么看过，非常新，诚心要的私我', '78258095438800.png', '2', '3', '0.32');
INSERT INTO `good` VALUES ('3', '四六级收音机', '电子产品', '10.00', '7.00', '1', '要毕业了，低价出售', '78483760446000.png', '3', '3', '0.67');
INSERT INTO `good` VALUES ('4', '有线罗技鼠标', '电子产品', '150.00', '133.00', '1', '换鼠标了，低价出售二手鼠标，用的时间也不长，性价比极高', '78794757029700.png', '2', '5', '0.89');
INSERT INTO `good` VALUES ('5', '大学英语教材', '二手教材', '10.00', '9.00', '0', '新视野大学英语教材，低价出售', '78950247348100.png', '3', '0', '0.88');
INSERT INTO `good` VALUES ('30', '雷柏机械键盘', '电子产品', '321.00', '279.00', '2', '知名外设厂商雷柏推出机械游戏机械键盘V500，这款机械键盘凭借较为强大性能和高性价比，得到了玩家的喜爱。如今，雷柏V500全面升级——雷柏V500S，为玩家提供一种全新的选择。雷柏V500S相较V500增加全键智能背光，并支持USB接口下全按键无冲。', '331026765897600.png', '1', '0', '0.87');
INSERT INTO `good` VALUES ('31', '饮水机', '生活用品', '23.00', '21.00', '1', '要毕业了，低价出售宿舍用品', '79136566576000.png', '1', '0', '0.91');
INSERT INTO `good` VALUES ('32', '二手苹果手机', '电子产品', '2000.00', '1780.00', '1', '低价出售二手苹果手机，可以先验货，想要的私我', '79482544313200.png', '2', '0', '0.89');
INSERT INTO `good` VALUES ('33', 'c语言教材', '二手教材', '5.00', '4.95', '1', '二手大学c语言程序设计，低价出售', '79791385210600.png', '1', '0', '0.99');
INSERT INTO `good` VALUES ('34', '蓝牙耳机', '电子产品', '123.00', '121.77', '1', '蓝牙耳机', '103184042232500.png', '1', '0', '0.99');

-- ----------------------------
-- Table structure for g_order
-- ----------------------------
DROP TABLE IF EXISTS `g_order`;
CREATE TABLE `g_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单编号',
  `sid` int(11) NOT NULL COMMENT '顾客id',
  `gid` int(11) NOT NULL COMMENT '菜品id',
  `order_time` datetime DEFAULT NULL COMMENT '下单日期',
  `total` double(255,2) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  `isorder` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `sid` (`sid`),
  KEY `gid` (`gid`),
  CONSTRAINT `g_order_ibfk_1` FOREIGN KEY (`sid`) REFERENCES `student` (`id`),
  CONSTRAINT `g_order_ibfk_2` FOREIGN KEY (`gid`) REFERENCES `good` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of g_order
-- ----------------------------
INSERT INTO `g_order` VALUES ('56', '1', '1', '2024-08-18 15:32:08', '23.00', '1', '0', '2');
INSERT INTO `g_order` VALUES ('58', '1', '2', '2024-08-18 15:58:41', '64.00', '2', '2', '1');
INSERT INTO `g_order` VALUES ('63', '1', '5', '2024-08-21 10:39:35', '18.00', '2', '0', '0');
INSERT INTO `g_order` VALUES ('65', '1', '4', null, '150.00', '1', '0', '2');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `nname` varchar(255) NOT NULL COMMENT '角色名',
  `content` varchar(255) NOT NULL COMMENT '内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES ('1', '关于毕业季设立个人二手物品交易市场的公告', '为了倡导节约、减少浪费、方便即将毕业学生处置个人物品，根据校园实际情况，拟于2024年6月1-15日在1960街区西侧道路（沁园30栋北侧、沁园29栋东侧至中苑南门）、西苑运动场北侧道路，设立两处毕业生闲置个人物品临时交易场所。如有需要个人物品交易的同学可前往上述场所，同时请文明、规范交易，维护校园秩序。');
INSERT INTO `notice` VALUES ('4', '关于举办毕业生二手物品交易活动的通知', '为推进校园文明建设，弘扬勤俭节约的传统美德，满足毕业生对个人闲置物品处置的需要，培养学生创业意识，同时，通过二手物品交易在物品传递中寄托毕业生对母校的留恋与不舍，学校拟开展毕业生二手物品交易活动。');

-- ----------------------------
-- Table structure for score_good
-- ----------------------------
DROP TABLE IF EXISTS `score_good`;
CREATE TABLE `score_good` (
  `user_id` int(11) DEFAULT NULL,
  `good_id` int(11) DEFAULT NULL,
  `score` int(255) DEFAULT NULL,
  `time` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of score_good
-- ----------------------------
INSERT INTO `score_good` VALUES ('1', '1', '5', null);
INSERT INTO `score_good` VALUES ('1', '2', '3', null);
INSERT INTO `score_good` VALUES ('2', '1', '4', null);
INSERT INTO `score_good` VALUES ('2', '2', '3', null);
INSERT INTO `score_good` VALUES ('2', '3', '5', null);
INSERT INTO `score_good` VALUES ('2', '4', '5', null);
INSERT INTO `score_good` VALUES ('2', '5', '5', null);
INSERT INTO `score_good` VALUES ('2', '30', '5', null);
INSERT INTO `score_good` VALUES ('4', '1', '4', null);
INSERT INTO `score_good` VALUES ('4', '2', '5', null);
INSERT INTO `score_good` VALUES ('4', '3', '5', null);
INSERT INTO `score_good` VALUES ('4', '4', '5', null);
INSERT INTO `score_good` VALUES ('4', '5', '5', null);
INSERT INTO `score_good` VALUES ('4', '30', '5', null);
INSERT INTO `score_good` VALUES ('3', '1', '4', null);
INSERT INTO `score_good` VALUES ('3', '2', '5', null);
INSERT INTO `score_good` VALUES ('3', '3', '2', null);
INSERT INTO `score_good` VALUES ('3', '4', '5', null);
INSERT INTO `score_good` VALUES ('3', '5', '3', null);
INSERT INTO `score_good` VALUES ('3', '30', '4', null);
INSERT INTO `score_good` VALUES ('1', '3', '3', '2024-08-21');
INSERT INTO `score_good` VALUES ('1', '2', '3', '2024-08-21');
INSERT INTO `score_good` VALUES ('1', '3', '3', '2024-08-22');
INSERT INTO `score_good` VALUES ('2', '3', '4', '2024-08-22');
INSERT INTO `score_good` VALUES ('2', '3', '3', '2024-08-22');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '学生学号',
  `sname` varchar(255) NOT NULL COMMENT '学生姓名',
  `password` varchar(255) NOT NULL DEFAULT '202cb962ac59075b964b07152d234b70' COMMENT '密码',
  `sex` varchar(255) DEFAULT NULL COMMENT '性别',
  `age` int(255) DEFAULT NULL COMMENT '年龄',
  `major` varchar(255) DEFAULT NULL COMMENT '所属专业',
  `college` varchar(255) DEFAULT NULL COMMENT '所属学院',
  `simage` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `disable` int(255) DEFAULT '0' COMMENT '是否禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('1', '张三', '202cb962ac59075b964b07152d234b70', '男', '33', '软件', '计算机', 'q6.jfif', '1234', '1234567@qq.com', '3号楼b145', 0);
INSERT INTO `student` VALUES ('2', '李四', '202cb962ac59075b964b07152d234b70', '男', '12', '软件', '计算机', '76913696178700.png', '1111111111', '1234567@qq.com', '5号楼234', 0);
INSERT INTO `student` VALUES ('3', '王五', '202cb962ac59075b964b07152d234b70', '男', '23', '管理科学', '管理学院', '76877272498600.png', '12345678910111', '31231232', '4号楼121', 0);
INSERT INTO `student` VALUES ('7', '大唐不夜城', '202cb962ac59075b964b07152d234b70', '男', '32', '213', '3213', '.png79970094841700', '12345678910111', '333212', '河北省邢台市邢台县a', 0);
INSERT INTO `student` VALUES ('8', '金字塔', '202cb962ac59075b964b07152d234b70', '男', '12', '管理科学', '管理学院', '.png80035963376300', '12345678910111', '234760595678@qq.com', '', 0);
INSERT INTO `student` VALUES ('9', 'xsxa', '202cb962ac59075b964b07152d234b70', '男', '132', '3123', '3123', '80166664594200.png', '3123', '3123', '3213', 0);
INSERT INTO `student` VALUES ('10', '张三eeee', '202cb962ac59075b964b07152d234b70', '男', '23', null, null, '105352820569700.png', '3123', '3213', null, 0);

-- ----------------------------
-- Table structure for type
-- ----------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `type` varchar(255) NOT NULL COMMENT '类型名称',
  `feature` varchar(255) NOT NULL COMMENT '类型特征',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of type
-- ----------------------------
INSERT INTO `type` VALUES ('1', '生活用品', '学校宿舍经常用到的一些生活用品');
INSERT INTO `type` VALUES ('2', '电子产品', '主要包括：手表、智能手机、电话、收音机、收录机、组合音箱、电脑、游戏机等');
INSERT INTO `type` VALUES ('3', '二手教材', '价格优惠的二手教材');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `email` varchar(255) NOT NULL COMMENT '邮箱',
  `phone` varchar(255) NOT NULL COMMENT '手机',
  `image` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '202cb962ac59075b964b07152d234b70', '234760595678@qq.com', '2222222222222222', 'q6.jfif');
